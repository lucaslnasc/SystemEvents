package com.eventos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.eventos.model.Evento;
import com.eventos.model.Palestrante;

public class EventoDAO {

  // Método para criar um novo evento no banco de dados
  public void create(Evento evento) throws SQLException {
    String sql = "INSERT INTO evento (nome, descricao, data, local, capacidade) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      stmt.setString(1, evento.getNome());
      stmt.setString(2, evento.getDescricao());
      stmt.setDate(3, java.sql.Date.valueOf(evento.getData()));
      stmt.setString(4, evento.getLocal());
      stmt.setInt(5, evento.getCapacidade());

      int affectedRows = stmt.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("Falha ao criar evento, nenhuma linha afetada.");
      }

      try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          evento.setId(generatedKeys.getInt(1));
        } else {
          throw new SQLException("Falha ao criar evento, nenhum ID obtido.");
        }
      }
    }
  }

  // Método READ para buscar um evento pelo ID
  public Evento findById(int id) throws SQLException {
    String sql = "SELECT * FROM evento WHERE id = ?";
    Evento evento = null;

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          evento = new Evento();
          evento.setId(rs.getInt("id"));
          evento.setNome(rs.getString("nome"));
          evento.setDescricao(rs.getString("descricao"));
          evento.setData(rs.getDate("data").toLocalDate());
          evento.setLocal(rs.getString("local"));
          evento.setCapacidade(rs.getInt("capacidade"));
        }
      }
    }
    return evento;
  }

  // Método READ para buscar todos os eventos
  public List<Evento> findAll() throws SQLException {
    String sql = "SELECT * FROM evento";
    List<Evento> eventos = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Evento evento = new Evento();
        evento.setId(rs.getInt("id"));
        evento.setNome(rs.getString("nome"));
        evento.setDescricao(rs.getString("descricao"));
        evento.setData(rs.getDate("data").toLocalDate());
        evento.setLocal(rs.getString("local"));
        evento.setCapacidade(rs.getInt("capacidade"));
        eventos.add(evento);
      }
    }
    return eventos;
  }

  // Método UPDATE
  public boolean update(Evento evento) throws SQLException {
    String sql = "UPDATE evento SET nome = ?, descricao = ?, data = ?, local = ?, capacidade = ? WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, evento.getNome());
      stmt.setString(2, evento.getDescricao());
      stmt.setDate(3, java.sql.Date.valueOf(evento.getData()));
      stmt.setString(4, evento.getLocal());
      stmt.setInt(5, evento.getCapacidade());
      stmt.setInt(6, evento.getId());

      return stmt.executeUpdate() > 0;
    }
  }

  // Método DELETE
  public boolean delete(int id) throws SQLException {
    String sql = "DELETE FROM evento WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, id);
      return stmt.executeUpdate() > 0;
    }
  }

  // Método para buscar eventos por nome (filtro)
  public List<Evento> findByNome(String nome) throws SQLException {
    String sql = "SELECT * FROM evento WHERE nome LIKE ? ORDER BY data";
    List<Evento> eventos = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, "%" + nome + "%");

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Evento evento = new Evento();
          evento.setId(rs.getInt("id"));
          evento.setNome(rs.getString("nome"));
          evento.setDescricao(rs.getString("descricao"));
          evento.setData(rs.getDate("data").toLocalDate());
          evento.setLocal(rs.getString("local"));
          evento.setCapacidade(rs.getInt("capacidade"));

          eventos.add(evento);
        }
      }
    }

    return eventos;
  }

  // Método para verificar disponibilidade de capacidade
  public boolean hasCapacidadeDisponivel(int eventoId) throws SQLException {
    String sql = "SELECT e.capacidade, COUNT(i.id) as inscritos " +
        "FROM evento e LEFT JOIN inscricao i ON e.id = i.evento_id " +
        "WHERE e.id = ? GROUP BY e.capacidade";

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setInt(1, eventoId);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          int capacidade = rs.getInt("capacidade");
          int inscritos = rs.getInt("inscritos");
          return inscritos < capacidade;
        }
        return false;
      }
    }
  }

  public List<Palestrante> findPalestrantesByEventoId(int eventoId) throws SQLException {
    String sql = "SELECT p.id, p.nome, p.curriculo, p.area_atuacao " +
        "FROM palestrante p " +
        "JOIN evento_palestrante ep ON p.id = ep.palestrante_id " +
        "WHERE ep.evento_id = ?";
    List<Palestrante> palestrantes = new ArrayList<>();

    try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, eventoId);
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Palestrante palestrante = new Palestrante();
          palestrante.setId(rs.getInt("id"));
          palestrante.setNome(rs.getString("nome"));
          palestrante.setCurriculo(rs.getString("curriculo"));
          palestrante.setAreaAtuacao(rs.getString("area_atuacao"));
          palestrantes.add(palestrante);
        }
      }
    }

    return palestrantes;
  }
}