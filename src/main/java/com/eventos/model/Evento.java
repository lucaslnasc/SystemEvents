package com.eventos.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Evento {

  private int id;
  private String nome;
  private String descricao;
  private LocalDate data;
  private String local;
  private int capacidade;
  private List<Palestrante> palestrantes;

  public Evento() {
    this.palestrantes = new ArrayList<>();
  }

  public Evento(String nome, String descricao, LocalDate data, String local, int capacidade) {
    this.nome = nome;
    this.descricao = descricao;
    this.data = data;
    this.local = local;
    this.capacidade = capacidade;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public String getLocal() {
    return local;
  }

  public void setLocal(String local) {
    this.local = local;
  }

  public int getCapacidade() {
    return capacidade;
  }

  public void setCapacidade(int capacidade) {
    this.capacidade = capacidade;
  }

  public List<Palestrante> getPalestrantes() {
    return palestrantes;
  }

  public void setPalestrantes(List<Palestrante> palestrantes) {
    this.palestrantes = palestrantes;
  }

  @Override
  public String toString() {
    return nome + " - " + descricao + " - " + " - " + local;
  }
}
