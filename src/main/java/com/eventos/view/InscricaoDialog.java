package com.eventos.view;

import com.eventos.dao.EventoDAO;
import com.eventos.dao.ParticipanteDAO;
import com.eventos.model.Evento;
import com.eventos.model.Participante;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class InscricaoDialog extends JDialog {
  private ParticipanteDAO participanteDAO;
  private EventoDAO eventoDAO;
  private JComboBox<Participante> participanteComboBox;
  private JComboBox<Evento> eventoComboBox;
  private boolean confirmed = false;

  // Construtor para inscrever qualquer participante
  public InscricaoDialog(JFrame parent) {
    super(parent, "Inscrição em Evento", true);
    initComponents();
  }

  // Construtor para inscrever um participante específico
  public InscricaoDialog(JFrame parent, Participante participante) {
    super(parent, "Inscrição em Evento para " + participante.getNome(), true);
    initComponents();

    // Seleciona o participante no combo box
    for (int i = 0; i < participanteComboBox.getItemCount(); i++) {
      Participante p = participanteComboBox.getItemAt(i);
      if (p.getId() == participante.getId()) {
        participanteComboBox.setSelectedIndex(i);
        break;
      }
    }

    // Desabilita a seleção de participante já que foi passado como parâmetro
    participanteComboBox.setEnabled(false);
  }

  private void initComponents() {
    setSize(400, 200);
    setLocationRelativeTo(getOwner());
    setLayout(new BorderLayout());

    participanteDAO = new ParticipanteDAO();
    eventoDAO = new EventoDAO();

    // Painel principal
    JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Componentes para seleção de participante
    JLabel participanteLabel = new JLabel("Participante:");
    participanteComboBox = new JComboBox<>();
    carregarParticipantes();

    // Componentes para seleção de evento
    JLabel eventoLabel = new JLabel("Evento:");
    eventoComboBox = new JComboBox<>();
    carregarEventos();

    // Adiciona componentes ao painel principal
    mainPanel.add(participanteLabel);
    mainPanel.add(participanteComboBox);
    mainPanel.add(eventoLabel);
    mainPanel.add(eventoComboBox);

    // Painel de botões
    JPanel buttonPanel = new JPanel();
    JButton confirmButton = new JButton("Inscrever");
    JButton cancelButton = new JButton("Cancelar");

    buttonPanel.add(confirmButton);
    buttonPanel.add(cancelButton);

    // Adiciona painéis ao diálogo
    add(mainPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    // Adiciona ações aos botões
    confirmButton.addActionListener(e -> inscrever());
    cancelButton.addActionListener(e -> dispose());
  }

  private void carregarParticipantes() {
    try {
      List<Participante> participantes = participanteDAO.findAll();
      for (Participante p : participantes) {
        participanteComboBox.addItem(p);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao carregar participantes: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void carregarEventos() {
    try {
      List<Evento> eventos = eventoDAO.findAll();
      for (Evento e : eventos) {
        eventoComboBox.addItem(e);
      }
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao carregar eventos: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void inscrever() {
    try {
      Participante participante = (Participante) participanteComboBox.getSelectedItem();
      Evento evento = (Evento) eventoComboBox.getSelectedItem();

      if (participante == null || evento == null) {
        JOptionPane.showMessageDialog(this,
            "Selecione um participante e um evento para inscrição",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      // Verifica se há capacidade disponível
      if (!eventoDAO.hasCapacidadeDisponivel(evento.getId())) {
        JOptionPane.showMessageDialog(this,
            "Evento com capacidade máxima atingida",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      // Verifica se o participante já está inscrito
      if (participanteDAO.estaInscrito(participante.getId(), evento.getId())) {
        JOptionPane.showMessageDialog(this,
            "Participante já está inscrito neste evento",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      // Realiza a inscrição
      boolean sucesso = participanteDAO.inscreverEmEvento(participante.getId(), evento.getId());

      if (sucesso) {
        confirmed = true;
        JOptionPane.showMessageDialog(this,
            "Inscrição realizada com sucesso!",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
      } else {
        JOptionPane.showMessageDialog(this,
            "Não foi possível realizar a inscrição",
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }

    } catch (SQLException e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao realizar inscrição: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public boolean isConfirmed() {
    return confirmed;
  }
}