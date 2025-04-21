package com.eventos.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.eventos.dao.EventoDAO;
import com.eventos.dao.PalestranteDAO;
import com.eventos.model.Evento;
import com.eventos.model.Palestrante;

public class PalestrantePanel extends JPanel {
  private JTable palestranteTable;
  private PalestranteTableModel tableModel;
  private PalestranteDAO palestranteDAO;

  public PalestrantePanel() {
    setLayout(new BorderLayout());
    palestranteDAO = new PalestranteDAO();

    // Modelo da tabela
    tableModel = new PalestranteTableModel();
    palestranteTable = new JTable(tableModel);

    // Painel de botões
    JPanel buttonPanel = new JPanel();
    JButton addButton = new JButton("Adicionar");
    JButton editButton = new JButton("Editar");
    JButton deleteButton = new JButton("Excluir");
    JButton inscreverEmEventoButton = new JButton("Inscrever em Evento"); // Novo botão

    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(inscreverEmEventoButton); // Adiciona o botão ao painel

    // Adiciona componentes ao painel
    add(new JScrollPane(palestranteTable), BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    // Carrega dados
    carregarPalestrantes();

    // Configura listeners
    addButton.addActionListener(e -> adicionarPalestrante());
    editButton.addActionListener(e -> editarPalestrante());
    deleteButton.addActionListener(e -> excluirPalestrante());
    inscreverEmEventoButton.addActionListener(e -> inscreverEmEvento()); // Listener do novo botão
  }

  private void carregarPalestrantes() {
    try {
      List<Palestrante> palestrantes = palestranteDAO.findAll();
      tableModel.setPalestrantes(palestrantes);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao carregar palestrantes: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void adicionarPalestrante() {
    PalestranteDialog dialog = new PalestranteDialog((JFrame) SwingUtilities.getWindowAncestor(this));
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      try {
        palestranteDAO.create(dialog.getPalestrante());
        carregarPalestrantes();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao salvar palestrante: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void editarPalestrante() {
    int selectedRow = palestranteTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Selecione um palestrante para editar",
          "Aviso",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    Palestrante palestrante = tableModel.getPalestranteAt(selectedRow);
    PalestranteDialog dialog = new PalestranteDialog((JFrame) SwingUtilities.getWindowAncestor(this), palestrante);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      try {
        palestranteDAO.update(dialog.getPalestrante());
        carregarPalestrantes();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao atualizar palestrante: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void excluirPalestrante() {
    int selectedRow = palestranteTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Selecione um palestrante para excluir",
          "Aviso",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
        "Tem certeza que deseja excluir este palestrante?",
        "Confirmação",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        Palestrante palestrante = tableModel.getPalestranteAt(selectedRow);
        palestranteDAO.delete(palestrante.getId());
        carregarPalestrantes();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao excluir palestrante: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void inscreverEmEvento() { // Novo método
    int selectedRow = palestranteTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Selecione um palestrante para inscrever em um evento",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Solicitar ao usuário que selecione um evento
    String eventoIdStr = JOptionPane.showInputDialog(this,
        "Digite o ID do evento para inscrever o palestrante:",
        "Inscrição em Evento",
        JOptionPane.QUESTION_MESSAGE);

    if (eventoIdStr == null || eventoIdStr.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Nenhum evento foi selecionado.",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        int eventoId = Integer.parseInt(eventoIdStr);

        // Obter o evento pelo ID (simulação, ajuste conforme necessário)
        Evento evento = new EventoDAO().findById(eventoId);
        if (evento == null) {
            JOptionPane.showMessageDialog(this,
                "Evento não encontrado.",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Abrir o diálogo de inscrição
        PalestranteInscricaoDialog dialog = new PalestranteInscricaoDialog((JFrame) SwingUtilities.getWindowAncestor(this), evento);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            JOptionPane.showMessageDialog(this,
                "Palestrante inscrito no evento com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "ID do evento inválido.",
            "Erro",
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao inscrever palestrante no evento: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
    }
  }
}