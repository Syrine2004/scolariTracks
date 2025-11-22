package com.scolarite.backend.controllers;

import com.scolarite.backend.entities.AppUser;
import com.scolarite.backend.repositories.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

    // 1. Endpoint de Login (Connexion)
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

    // 2. Endpoint de Register (Inscription) - AJOUTÉ ICI
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