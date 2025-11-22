package com.scolarite.backend;

import com.scolarite.backend.entities.AppUser;
import com.scolarite.backend.entities.Classe;
import com.scolarite.backend.repositories.AppUserRepository;
import com.scolarite.backend.repositories.ClasseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(AppUserRepository appUserRepository,
                            ClasseRepository classeRepository,
                            PasswordEncoder passwordEncoder) {
        return args -> {

            // 1. Création de l'ADMIN
            if (appUserRepository.findByUsername("syrine").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("syrine");
                admin.setPassword(passwordEncoder.encode("soap"));
                // GARANTIE MAJUSCULE POUR LA SÉCURITÉ : "ADMIN"
                admin.setRole("ADMIN".toUpperCase());
                appUserRepository.save(admin);
                System.out.println("✅ ADMIN CRÉÉ AVEC RÔLE: " + admin.getRole());
            }

            // 2. Création des CLASSES
            // On vérifie le nom de la première classe pour s'assurer qu'elles n'existent pas
            if (classeRepository.findByNom("RSI").isEmpty()) {
                classeRepository.save(new Classe(null, "RSI", null));
                classeRepository.save(new Classe(null, "MDW", null));
                classeRepository.save(new Classe(null, "DSI", null));
                classeRepository.save(new Classe(null, "SEM", null));
                System.out.println("✅ CLASSES CRÉÉES (RSI, MDW, DSI, SEM)");
            }
        };
    }
}