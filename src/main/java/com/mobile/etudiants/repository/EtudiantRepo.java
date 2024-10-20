package com.mobile.etudiants.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobile.etudiants.model.Etudiant;

public interface EtudiantRepo  extends JpaRepository<Etudiant,Integer>{

    
}
