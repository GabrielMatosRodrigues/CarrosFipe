package com.fipe.domain.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade Veiculo - representa um veículo no banco de dados
 * Similar a uma classe com [Table] no Entity Framework
 */
@Entity
@Table(name = "veiculos")
public class Veiculo extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 200)
    private String modelo;

    @Column(length = 500)
    private String observacoes;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // Construtor padrão (obrigatório para JPA)
    public Veiculo() {
        this.criadoEm = LocalDateTime.now();
    }

    // Construtor com parâmetros
    public Veiculo(String codigo, String marca, String modelo) {
        this();
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
    }

    // Getters e Setters (como Properties em C#)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
        this.atualizadoEm = LocalDateTime.now();
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
        this.atualizadoEm = LocalDateTime.now();
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void atualizar(String modelo, String observacoes) {
        this.modelo = modelo;
        this.observacoes = observacoes;
        this.atualizadoEm = LocalDateTime.now();
    }
}