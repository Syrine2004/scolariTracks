package com.scolarite.backend.controllers;

import com.scolarite.backend.entities.Classe;
import com.scolarite.backend.services.ClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin("*")
public class ClasseController {

    @Autowired
    private ClasseService classeService;

    @GetMapping
    public List<Classe> getAll() {
        return classeService.getAllClasses();
    }

    @PostMapping
    public Classe create(@RequestBody Classe classe) {
        return classeService.createClasse(classe);
    }

    @PutMapping("/{id}")
    public Classe update(@PathVariable Long id, @RequestBody Classe classe) {
        return classeService.updateClasse(id, classe);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        classeService.deleteClasse(id);
    }
}