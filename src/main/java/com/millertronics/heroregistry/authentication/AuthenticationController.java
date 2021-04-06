package com.millertronics.heroregistry.authentication;

import com.millertronics.heroregistry.authentication.dto.AuthenticationRequestDto;
import com.millertronics.heroregistry.security.CustomUserDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/authenticate")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequestDto authenticationRequest) throws Exception {
        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch(BadCredentialsException e) {
            throw new Exception("Invalid credentials");
        }

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final long currentTimestamp = System.currentTimeMillis();
        final String jwt = Jwts.builder().setClaims(new HashMap<String, Object>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(currentTimestamp))
                .setExpiration(new Date(currentTimestamp + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, "s3cret!").compact();

        return ResponseEntity.ok(jwt);
    }

}
