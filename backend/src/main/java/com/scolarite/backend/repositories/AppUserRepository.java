package com.scolarite.backend.repositories;

import com.scolarite.backend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    // Cette méthode magique permet de trouver un utilisateur par son nom
    Optional<AppUser> findByUsername(String username);
}