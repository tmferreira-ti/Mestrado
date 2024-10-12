package com.markingcarlos.trabalho_mestrado.Repository;

import com.markingcarlos.trabalho_mestrado.Model.MoradorModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface MoradorRepository extends JpaRepository<MoradorModel, Integer> {

    MoradorModel findByCPFMorador(BigInteger cpf);
}
