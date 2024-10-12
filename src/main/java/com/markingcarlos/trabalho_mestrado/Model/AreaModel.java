package com.markingcarlos.trabalho_mestrado.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Table(name = "Area")
public class AreaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArea;

    private String nome;
    private String descricao;
    private int tamanhoMetroQuadrado;
    private boolean utilizavel;
    private String localizacao;
    private boolean isAlugavel;

    public AreaModel(){}





}
