package com.fipe.domain.entities;

import java.time.LocalDateTime;

public class Veiculo {
    private Long id;
    private String codigo;
    private String marca;
    private String modelo;
    private String observacoes;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public Veiculo() {
        this.criadoEm = LocalDateTime.now();
    }

    public Veiculo(Long id, String codigo, String marca, String modelo) {
        this();
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
    }

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
