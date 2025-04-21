package com.eventos.model;

public class Palestrante {

  private int id;
  private String nome;
  private String curriculo;
  private String areaAtuacao;

  public Palestrante() {
  }

  public Palestrante(String nome, String curriculo, String areaAtuacao) {
    this.nome = nome;
    this.curriculo = curriculo;
    this.areaAtuacao = areaAtuacao;
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

  public String getCurriculo() {
    return curriculo;
  }

  public void setCurriculo(String curriculo) {
    this.curriculo = curriculo;
  }

  public String getAreaAtuacao() {
    return areaAtuacao;
  }

  public void setAreaAtuacao(String areaAtuacao) {
    this.areaAtuacao = areaAtuacao;
  }

  @Override
  public String toString() {
    return nome + " - " + curriculo + " - " + areaAtuacao;
  }
}
