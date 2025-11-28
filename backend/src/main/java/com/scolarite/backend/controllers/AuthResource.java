package com.scolarite.backend.controllers;

import com.scolarite.backend.entities.AppUser;
import com.scolarite.backend.repositories.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Authentification", description = "Gestion de l'authentification")
@Component
public class AuthResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${scolarite.app.jwtSecret}")
    private String jwtSecret;

    @Value("${scolarite.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Operation(summary = "Connexion")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants incorrects", content = @Content)
    })
    @POST
    @Path("/login")
    public Response login(AppUser loginRequest) {
        try {
            if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Username and password are required");
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            String token = generateToken(authentication.getName());

            AppUser user = userRepository.findByUsername(authentication.getName()).orElseThrow();

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.getRole());
            return Response.ok(response).build();
        } catch (org.springframework.security.core.AuthenticationException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid username or password");
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred during login");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    @Operation(summary = "Inscription")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inscription réussie"),
            @ApiResponse(responseCode = "400", description = "Nom d'utilisateur déjà pris", content = @Content)
    })
    @POST
    @Path("/register")
    public Response register(AppUser user) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            response.put("message", "Erreur : Ce nom d'utilisateur existe déjà !");
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole("USER");

        userRepository.save(newUser);

        response.put("message", "Inscription réussie !");
        return Response.ok(response).build();
    }

    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        // Generate a key from the secret string, ensuring minimum length for HS256 (256
        // bits = 32 bytes)
        byte[] keyBytes;
        try {
            // Try to decode as Base64 first
            keyBytes = Decoders.BASE64.decode(jwtSecret);
        } catch (IllegalArgumentException e) {
            // If not Base64, use the string directly and pad/truncate to 32 bytes
            byte[] secretBytes = jwtSecret.getBytes();
            keyBytes = new byte[32];
            System.arraycopy(secretBytes, 0, keyBytes, 0, Math.min(secretBytes.length, 32));
            // If shorter than 32 bytes, pad with zeros (or repeat the secret)
            if (secretBytes.length < 32) {
                for (int i = secretBytes.length; i < 32; i++) {
                    keyBytes[i] = secretBytes[i % secretBytes.length];
                }
            }
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
