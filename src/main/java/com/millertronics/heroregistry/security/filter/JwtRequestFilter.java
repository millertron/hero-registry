package com.millertronics.heroregistry.security.filter;

import com.millertronics.heroregistry.security.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtRequestFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public JwtRequestFilter(AuthenticationManager authenticationManager,
                            UserDetailsService userDetailsService,
                            JwtService jwtService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username;
        String jwt;

        final String bearerPrefix = "Bearer ";
        if (authorizationHeader != null && authorizationHeader.startsWith(bearerPrefix)) {
            jwt = authorizationHeader.substring(bearerPrefix.length());
            //TODO: move JWT processing code out of here
            final Claims claims = Jwts.parser()
                    .setSigningKey("s3cret!")
                    .parseClaimsJws(jwt)
                    .getBody();
            username = claims.getSubject();

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Date tokenExpirationDate = claims.getExpiration();
                final boolean validToken = userDetails != null && tokenExpirationDate.after(new Date());

                if (validToken) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
