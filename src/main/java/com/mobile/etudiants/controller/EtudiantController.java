package com.mobile.etudiants.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.mobile.etudiants.model.Etudiant;
import com.mobile.etudiants.service.interfaces.EtudiantService;
import com.mobile.etudiants.service.imple.FileStorageService;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "*")
public class EtudiantController {
    @Autowired
    private EtudiantService etudiantService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Etudiant> createEtudiant(
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("ville") String ville,
            @RequestParam("sexe") String sexe,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        Etudiant etudiant = new Etudiant();
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setVille(ville);
        etudiant.setSexe(sexe);
        
        if (image != null && !image.isEmpty()) {
            String fileName = fileStorageService.storeFile(image);
            etudiant.setImageUrl(fileName);
        }
        
        Etudiant newEtudiant = etudiantService.create(etudiant);
        return new ResponseEntity<>(newEtudiant, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Etudiant> updateEtudiant(
            @PathVariable Integer id,
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("ville") String ville,
            @RequestParam("sexe") String sexe,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Optional<Etudiant> existingEtudiantOpt = etudiantService.getEtudiantById(id);
            if (!existingEtudiantOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Etudiant existingEtudiant = existingEtudiantOpt.get();
            existingEtudiant.setNom(nom);
            existingEtudiant.setPrenom(prenom);
            existingEtudiant.setVille(ville);
            existingEtudiant.setSexe(sexe);

            if (image != null && !image.isEmpty()) {
                // Delete old image if exists
                if (existingEtudiant.getImageUrl() != null) {
                    fileStorageService.deleteFile(existingEtudiant.getImageUrl());
                }
                // Store new image
                String fileName = fileStorageService.storeFile(image);
                existingEtudiant.setImageUrl(fileName);
            }
            
            Etudiant updatedEtudiant = etudiantService.update(id, existingEtudiant);
            return new ResponseEntity<>(updatedEtudiant, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Integer id) {
        Optional<Etudiant> etudiant = etudiantService.getEtudiantById(id);
        return etudiant.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAll();
        return new ResponseEntity<>(etudiants, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Integer id) {
        try {
            Optional<Etudiant> etudiant = etudiantService.getEtudiantById(id);
            if (etudiant.isPresent() && etudiant.get().getImageUrl() != null) {
                fileStorageService.deleteFile(etudiant.get().getImageUrl());
            }
            etudiantService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}