package com.eventos.view;

import com.eventos.dao.ParticipanteDAO;
import com.eventos.model.Participante;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ParticipantePanel extends JPanel {
  private JTable participanteTable;
  private ParticipanteTableModel tableModel;
  private ParticipanteDAO participanteDAO;

  public ParticipantePanel() {
    setLayout(new BorderLayout());
    participanteDAO = new ParticipanteDAO();

    // Modelo da tabela
    tableModel = new ParticipanteTableModel();
    participanteTable = new JTable(tableModel);

    // Painel de botões
    JPanel buttonPanel = new JPanel();
    JButton addButton = new JButton("Adicionar");
    JButton editButton = new JButton("Editar");
    JButton deleteButton = new JButton("Excluir");
    JButton inscreverButton = new JButton("Inscrever em Evento"); // Novo botão

    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(inscreverButton); // Adiciona o novo botão

    // Adiciona componentes ao painel
    add(new JScrollPane(participanteTable), BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    // Carrega dados
    carregarParticipantes();

    // Configura listeners
    addButton.addActionListener(e -> adicionarParticipante());
    editButton.addActionListener(e -> editarParticipante());
    deleteButton.addActionListener(e -> excluirParticipante());
    inscreverButton.addActionListener(e -> inscreverEmEvento()); // Novo listener
  }

  private void carregarParticipantes() {
    try {
      List<Participante> participantes = participanteDAO.findAll();
      tableModel.setParticipantes(participantes);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao carregar participantes: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void adicionarParticipante() {
    ParticipanteDialog dialog = new ParticipanteDialog((JFrame) SwingUtilities.getWindowAncestor(this));
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      try {
        participanteDAO.create(dialog.getParticipante());
        carregarParticipantes();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao salvar participante: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void editarParticipante() {
    int selectedRow = participanteTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Selecione um participante para editar",
          "Aviso",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    Participante participante = tableModel.getParticipanteAt(selectedRow);
    ParticipanteDialog dialog = new ParticipanteDialog((JFrame) SwingUtilities.getWindowAncestor(this), participante);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      try {
        participanteDAO.update(dialog.getParticipante());
        carregarParticipantes();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao atualizar participante: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void excluirParticipante() {
    int selectedRow = participanteTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Selecione um participante para excluir",
          "Aviso",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
        "Tem certeza que deseja excluir este participante?",
        "Confirmação",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        Participante participante = tableModel.getParticipanteAt(selectedRow);
        participanteDAO.delete(participante.getId());
        carregarParticipantes();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao excluir participante: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // Novo método para inscrever um participante em um evento
  private void inscreverEmEvento() {
    int selectedRow = participanteTable.getSelectedRow();

    // Se um participante está selecionado, usamos o construtor que recebe o
    // participante
    if (selectedRow != -1) {
      Participante participante = tableModel.getParticipanteAt(selectedRow);
      InscricaoDialog dialog = new InscricaoDialog(
          (JFrame) SwingUtilities.getWindowAncestor(this),
          participante);
      dialog.setVisible(true);
    } else {
      // Se nenhum participante estiver selecionado, usamos o construtor padrão
      // que permite selecionar um participante na lista
      InscricaoDialog dialog = new InscricaoDialog((JFrame) SwingUtilities.getWindowAncestor(this));
      dialog.setVisible(true);
    }
  }
}