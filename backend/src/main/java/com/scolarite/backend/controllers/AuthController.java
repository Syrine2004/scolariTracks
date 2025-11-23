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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@Tag(name = "Authentification", description = "API pour l'authentification et l'inscription des utilisateurs")
public class AuthController {

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

    @Operation(
        summary = "Connexion utilisateur",
        description = "Authentifie un utilisateur et retourne un token JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Connexion réussie"),
        @ApiResponse(responseCode = "401", description = "Identifiants incorrects", content = @Content)
    })
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AppUser loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        String token = generateToken(loginRequest.getUsername());

        // On récupère l'utilisateur pour envoyer son rôle au front
        AppUser user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        return response;
    }

    @Operation(
        summary = "Inscription utilisateur",
        description = "Crée un nouveau compte utilisateur avec le rôle USER par défaut"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inscription réussie"),
        @ApiResponse(responseCode = "400", description = "Nom d'utilisateur déjà pris", content = @Content)
    })
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody AppUser user) {
        Map<String, String> response = new HashMap<>();

        // Vérifier si le nom d'utilisateur est déjà pris
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            response.put("message", "Erreur : Ce nom d'utilisateur existe déjà !");
            return response;
        }

        // Création du nouvel utilisateur
        AppUser newUser = new AppUser();
        newUser.setUsername(user.getUsername());
        // IMPORTANT : On crypte le mot de passe !
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole("USER"); // Rôle par défaut

        userRepository.save(newUser);

        response.put("message", "Inscription réussie !");
        return response;
    }

    // Méthode utilitaire pour créer le JWT
    private String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}