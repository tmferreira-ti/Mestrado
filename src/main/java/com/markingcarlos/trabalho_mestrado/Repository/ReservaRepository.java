package com.markingcarlos.trabalho_mestrado.Repository;

import com.markingcarlos.trabalho_mestrado.Model.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaModel, Integer> {

    List<ReservaModel> findBydataInicio(LocalDate dataInicio);
    List<ReservaModel> findBydataFim(LocalDate dataInicio);
}
