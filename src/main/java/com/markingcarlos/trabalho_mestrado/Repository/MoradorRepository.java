package com.markingcarlos.trabalho_mestrado.Repository;

import com.markingcarlos.trabalho_mestrado.Model.MoradorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface MoradorRepository extends JpaRepository<MoradorModel, Integer> {

    MoradorModel findByCPFMorador(BigInteger cpf);
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM MoradorModel m WHERE m.Email = :email")
    boolean existsByEmail( String email);
    @Query("SELECT m FROM MoradorModel m WHERE m.Email = :email")
    MoradorModel findByEmail( String email);
}
