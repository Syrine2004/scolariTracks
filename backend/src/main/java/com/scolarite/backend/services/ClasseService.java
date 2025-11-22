package com.scolarite.backend.services;

import com.scolarite.backend.entities.Classe;
import java.util.List;

public interface ClasseService {
    List<Classe> getAllClasses();
    Classe createClasse(Classe classe);
    Classe updateClasse(Long id, Classe classe);
    void deleteClasse(Long id);
}