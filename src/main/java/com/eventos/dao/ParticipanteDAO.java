package com.eventos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.eventos.model.Participante;

public class ParticipanteDAO {

  public void create(Participante participante) throws SQLException {
    String sql = "INSERT INTO participante (nome, email, cpf) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setString(1, participante.getNome());
      stmt.setString(2, participante.getEmail());
      stmt.setString(3, participante.getCpf());
      stmt.executeUpdate();

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          participante.setId(generatedKeys.getInt(1));
        }
      }
    }
  }

  public void update(Participante participante) throws SQLException {
    String sql = "UPDATE participante SET nome = ?, email = ?, cpf = ? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, participante.getNome());
      stmt.setString(2, participante.getEmail());
      stmt.setString(3, participante.getCpf());
      stmt.setInt(4, participante.getId());
      stmt.executeUpdate();
    }
  }

  public void delete(int id) throws SQLException {
    String sql = "DELETE FROM participante WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      stmt.executeUpdate();
    }
  }

  public List<Participante> findAll() throws SQLException {
    List<Participante> lista = new ArrayList<>();
    String sql = "SELECT * FROM participante";

    try (Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        Participante p = new Participante();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setEmail(rs.getString("email"));
        p.setCpf(rs.getString("cpf"));
        lista.add(p);
      }
    }
    return lista;
  }

  public boolean inscreverEmEvento(int participanteId, int eventoId) throws SQLException {
    String sql = "INSERT INTO inscricao (evento_id, participante_id) VALUES (?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, eventoId);
      stmt.setInt(2, participanteId);
      stmt.executeUpdate();
      return true;
    } catch (SQLException e) {
      if (e.getSQLState().equals("23505")) { // Código para violação de chave única
        return false;
      }
      throw e;
    }
  }

  public boolean cancelarInscricao(int participanteId, int eventoId) throws SQLException {
    String sql = "DELETE FROM inscricao WHERE evento_id = ? AND participante_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, eventoId);
      stmt.setInt(2, participanteId);
      return stmt.executeUpdate() > 0;
    }
  }

  public boolean estaInscrito(int participanteId, int eventoId) throws SQLException {
    String sql = "SELECT COUNT(*) FROM inscricao WHERE evento_id = ? AND participante_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, eventoId);
      stmt.setInt(2, participanteId);
      ResultSet rs = stmt.executeQuery();
      return rs.next() && rs.getInt(1) > 0;
    }
  }

  public List<Integer> findEventosInscritos(int participanteId) throws SQLException {
    List<Integer> eventos = new ArrayList<>();
    String sql = "SELECT evento_id FROM inscricao WHERE participante_id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, participanteId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        eventos.add(rs.getInt("evento_id"));
      }
    }
    return eventos;
  }
}