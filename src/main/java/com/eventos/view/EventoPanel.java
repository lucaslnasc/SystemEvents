package com.eventos.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.eventos.dao.EventoDAO;
import com.eventos.model.Evento;
import com.eventos.model.Palestrante;

public class EventoPanel extends JPanel {
  private JTable eventoTable;
  private EventoTableModel tableModel;
  private EventoDAO eventoDAO;
  private JTextField searchField;

  public EventoPanel() {
    setLayout(new BorderLayout());
    eventoDAO = new EventoDAO();

    // Modelo da tabela
    tableModel = new EventoTableModel();
    eventoTable = new JTable(tableModel);

    // Painel de pesquisa
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    searchField = new JTextField(20);
    JButton searchButton = new JButton("Pesquisar");
    JButton clearButton = new JButton("Limpar");

    searchPanel.add(new JLabel("Nome do evento:"));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);
    searchPanel.add(clearButton);

    // Painel de botões
    JPanel buttonPanel = new JPanel();
    JButton addButton = new JButton("Adicionar");
    JButton editButton = new JButton("Editar");
    JButton deleteButton = new JButton("Excluir");
    JButton viewSpeakersButton = new JButton("Ver Palestrantes");
    
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);
    buttonPanel.add(viewSpeakersButton);
    
    // Adiciona componentes ao painel
    add(searchPanel, BorderLayout.NORTH);
    add(new JScrollPane(eventoTable), BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
    
    // Carrega dados
    carregarEventos();
    
    // Configura listeners
    searchButton.addActionListener(e -> pesquisarEventos());
    clearButton.addActionListener(e -> limparPesquisa());
    addButton.addActionListener(e -> adicionarEvento());
    editButton.addActionListener(e -> editarEvento());
    deleteButton.addActionListener(e -> excluirEvento());
    viewSpeakersButton.addActionListener(e -> exibirPalestrantes());
    
    // Adiciona funcionalidade de pesquisa ao pressionar Enter no campo de texto
    searchField.addActionListener(e -> pesquisarEventos());
  }

  private void carregarEventos() {
    try {
      List<Evento> eventos = eventoDAO.findAll();
      tableModel.setEventos(eventos);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao carregar eventos: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void pesquisarEventos() {
    String termoPesquisa = searchField.getText().trim();

    try {
      List<Evento> resultados;

      if (termoPesquisa.isEmpty()) {
        resultados = eventoDAO.findAll();
      } else {
        resultados = eventoDAO.findByNome(termoPesquisa);
      }

      tableModel.setEventos(resultados);

      if (resultados.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Nenhum evento encontrado com o termo: " + termoPesquisa,
            "Informação",
            JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao pesquisar eventos: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void limparPesquisa() {
    searchField.setText("");
    carregarEventos();
  }

  private void adicionarEvento() {
    EventoDialog dialog = new EventoDialog((JFrame) SwingUtilities.getWindowAncestor(this));
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      try {
        eventoDAO.create(dialog.getEvento());
        carregarEventos();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao salvar evento: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void editarEvento() {
    int selectedRow = eventoTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Selecione um evento para editar",
          "Aviso",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    Evento evento = tableModel.getEventoAt(selectedRow);
    EventoDialog dialog = new EventoDialog((JFrame) SwingUtilities.getWindowAncestor(this), evento);
    dialog.setVisible(true);

    if (dialog.isConfirmed()) {
      try {
        eventoDAO.update(dialog.getEvento());
        carregarEventos();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao atualizar evento: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void excluirEvento() {
    int selectedRow = eventoTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this,
          "Selecione um evento para excluir",
          "Aviso",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
        "Tem certeza que deseja excluir este evento?",
        "Confirmação",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      try {
        Evento evento = tableModel.getEventoAt(selectedRow);
        eventoDAO.delete(evento.getId());
        carregarEventos();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao excluir evento: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void exibirPalestrantes() {
    int selectedRow = eventoTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
            "Selecione um evento para ver os palestrantes",
            "Aviso",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        Evento evento = tableModel.getEventoAt(selectedRow);

        // Obter os palestrantes associados ao evento
        List<Palestrante> palestrantes = eventoDAO.findPalestrantesByEventoId(evento.getId());

        if (palestrantes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nenhum palestrante associado a este evento.",
                "Informação",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Construir a mensagem com os nomes dos palestrantes
        StringBuilder mensagem = new StringBuilder("Palestrantes do evento \"" + evento.getNome() + "\":\n");
        for (Palestrante palestrante : palestrantes) {
            mensagem.append("- ").append(palestrante.getNome()).append("\n");
        }

        // Exibir a mensagem
        JOptionPane.showMessageDialog(this,
            mensagem.toString(),
            "Palestrantes",
            JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Erro ao carregar palestrantes: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
    }
  }
}