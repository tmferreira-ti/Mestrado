package com.markingcarlos.trabalho_mestrado.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Reserva")
public class ReservaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private boolean ativa;

    @Temporal(TemporalType.DATE)
    private LocalDate dataInicio;

    @Temporal(TemporalType.DATE)
    private LocalDate dataFim;

    private String descricao;

    private String Status;

    @ManyToOne
    @JoinColumn(name = "idMorador")
    private MoradorModel TitularReserva;

    @ManyToOne
    @JoinColumn(name = "idArea" )
    private AreaModel AreaReservada;


}
