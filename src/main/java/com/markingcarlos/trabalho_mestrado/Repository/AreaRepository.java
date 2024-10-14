package com.markingcarlos.trabalho_mestrado.Repository;

import com.markingcarlos.trabalho_mestrado.Model.AreaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AreaRepository extends JpaRepository<AreaModel, Integer> {
    @Query("select a.idArea, a.nome from AreaModel a where a.isAlugavel = true")
    List<Object[]> findAreaAlugavel();
}
