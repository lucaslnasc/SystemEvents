package com.eventos.dao;

import java.sql.*;
import java.util.*;

import com.eventos.model.Palestrante;

public class PalestranteDAO {

  public void create(Palestrante palestrante) throws SQLException {
    String sql = "INSERT INTO palestrante (nome, curriculo, area_atuacao) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setString(1, palestrante.getNome());
      stmt.setString(2, palestrante.getCurriculo());
      stmt.setString(3, palestrante.getAreaAtuacao());

      int affectedRows = stmt.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Falha ao criar palestrante, nenhuma linha afetada.");
      }

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          palestrante.setId(generatedKeys.getInt(1));
        }
      }
    }
  }

  public List<Palestrante> findAll() throws SQLException {
    String sql = "SELECT * FROM palestrante";
    List<Palestrante> lista = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Palestrante p = new Palestrante();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setCurriculo(rs.getString("curriculo"));
        p.setAreaAtuacao(rs.getString("area_atuacao"));
        lista.add(p);
      }
    }
    return lista;
  }

  public void update(Palestrante palestrante) throws SQLException {
    String sql = "UPDATE palestrante SET nome=?, curriculo=?, area_atuacao=? WHERE id=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, palestrante.getNome());
      stmt.setString(2, palestrante.getCurriculo());
      stmt.setString(3, palestrante.getAreaAtuacao());
      stmt.setInt(4, palestrante.getId());
      stmt.executeUpdate();
    }
  }

  public void delete(int id) throws SQLException {
    String sql = "DELETE FROM palestrante WHERE id=?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  public void registerSpeakerToEvent(int eventId, int speakerId) throws SQLException {
    String sql = "INSERT INTO evento_palestrante (evento_id, palestrante_id) VALUES (?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, eventId);
      stmt.setInt(2, speakerId);
      stmt.executeUpdate();
    }
  }
}