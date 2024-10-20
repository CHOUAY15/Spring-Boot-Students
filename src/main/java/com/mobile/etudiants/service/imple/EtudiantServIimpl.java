package com.mobile.etudiants.service.imple;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mobile.etudiants.model.Etudiant;
import com.mobile.etudiants.repository.EtudiantRepo;
import com.mobile.etudiants.service.interfaces.EtudiantService;

@Service
public class EtudiantServIimpl implements EtudiantService {
    @Autowired
    private EtudiantRepo etudiantRepository;

    @Override
    public Etudiant create(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    @Override
    public Optional<Etudiant> getEtudiantById(Integer id) {
        return etudiantRepository.findById(id);
    }

    @Override
    public List<Etudiant> getAll() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant update(Integer id, Etudiant etudiant) {
        Optional<Etudiant> optionalEtudiant = etudiantRepository.findById(id);
        if (optionalEtudiant.isPresent()) {
            Etudiant etudiantExist = optionalEtudiant.get();
            etudiantExist.setNom(etudiant.getNom());
            etudiantExist.setPrenom(etudiant.getPrenom());
            etudiantExist.setVille(etudiant.getVille());
            etudiantExist.setSexe(etudiant.getSexe());
            etudiantExist.setImageUrl(etudiant.getImageUrl());
            return etudiantRepository.save(etudiantExist);
        } else {
            throw new RuntimeException("Étudiant non trouvé pour l'ID : " + id);
        }
    }

    @Override
    public void delete(Integer id) {
        if (etudiantRepository.existsById(id)) {
            etudiantRepository.deleteById(id);
        } else {
            throw new RuntimeException("Étudiant non trouvé pour l'ID : " + id);
        }
    }
}