package com.markingcarlos.trabalho_mestrado.Repository;

import com.markingcarlos.trabalho_mestrado.Model.ReservaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaModel, Integer> {

    List<ReservaModel> findBydataInicio(LocalDate dataInicio);

    List<ReservaModel> findBydataFim(LocalDate dataInicio);

    @Query("SELECT o.NomeMorador, r.id, r.dataInicio, r.dataFim, a.nome, r.Status FROM ReservaModel r " +
            "JOIN r.AreaReservada a " +
            "JOIN r.TitularReserva o WHERE r.Status = :status")
    List<Object[]> findByInfo( String status);

    @Query("SELECT o.NomeMorador, r.id, r.dataInicio, r.dataFim, a.nome, r.Status FROM ReservaModel r " +
            "JOIN r.AreaReservada a " +
            "JOIN r.TitularReserva o WHERE o.Email = :email")
    List<Object[]> findByReservaPorEmail(String email);


}
