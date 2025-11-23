package com.scolarite.backend.services;

import com.scolarite.backend.entities.Classe;
import com.scolarite.backend.repositories.ClasseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClasseServiceImpl implements ClasseService {

    @Autowired
    private ClasseRepository classeRepository;

    @Override
    public List<Classe> getAllClasses() {
        return classeRepository.findAll();
    }

    @Override
    public Classe createClasse(Classe classe) {
        return classeRepository.save(classe);
    }

    @Override
    public Classe updateClasse(Long id, Classe classeInfo) {
        return classeRepository.findById(id).map(classe -> {
            classe.setNom(classeInfo.getNom());
            return classeRepository.save(classe);
        }).orElse(null);
    }

    @Override
    public boolean deleteClasse(Long id) {
        if (classeRepository.existsById(id)) {
            classeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}