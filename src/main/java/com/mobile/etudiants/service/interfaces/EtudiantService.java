package com.mobile.etudiants.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.mobile.etudiants.model.Etudiant;

public interface EtudiantService {

     Etudiant create(Etudiant etudiant);


    Optional<Etudiant> getEtudiantById(Integer id);

   
    List<Etudiant> getAll();


    Etudiant update(Integer id, Etudiant etudiant);


    void delete(Integer id);
    

    
}
