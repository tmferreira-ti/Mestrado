package com.markingcarlos.trabalho_mestrado.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Getter
@Setter
@Table(name = "Morador")
public class MoradorModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMorador;
    private BigInteger CPFMorador;
    private String NomeMorador;
    private String Email;
    private String Telefone;
    private String Endereco;
    private String TipoMorador;

}
