package com.eventos.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.eventos.dao.PalestranteDAO;
import com.eventos.model.Evento;
import com.eventos.model.Palestrante;

public class PalestranteInscricaoDialog extends JDialog {
  private JList<String> palestranteList;
  private DefaultListModel<String> listModel;
  private PalestranteDAO palestranteDAO;
  private Evento evento;
  private boolean confirmed;

  public PalestranteInscricaoDialog(JFrame parent, Evento evento) {
    super(parent, "Inscrever Palestrantes", true);
    this.evento = evento;
    this.palestranteDAO = new PalestranteDAO();
    this.listModel = new DefaultListModel<>();
    this.palestranteList = new JList<>(listModel);

    setLayout(new BorderLayout());
    setSize(400, 300);
    setLocationRelativeTo(parent);

    loadSpeakers();

    add(new JScrollPane(palestranteList), BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    JButton confirmButton = new JButton("Confirmar");
    JButton cancelButton = new JButton("Cancelar");

    buttonPanel.add(confirmButton);
    buttonPanel.add(cancelButton);

    add(buttonPanel, BorderLayout.SOUTH);

    confirmButton.addActionListener(e -> confirm());
    cancelButton.addActionListener(e -> cancel());
  }

  private void loadSpeakers() {
    try {
      List<Palestrante> palestrantes = palestranteDAO.findAll();
      listModel.clear();
      for (Palestrante palestrante : palestrantes) {
        listModel.addElement(palestrante.getNome());
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao carregar palestrantes: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void confirm() {
    int[] selectedIndices = palestranteList.getSelectedIndices();
    if (selectedIndices.length == 0) {
      JOptionPane.showMessageDialog(this,
          "Selecione pelo menos um palestrante para inscrever",
          "Aviso",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      // Inicializa a lista de palestrantes, se necessário
      if (evento.getPalestrantes() == null) {
        evento.setPalestrantes(new ArrayList<>());
      }

      List<Palestrante> palestrantes = palestranteDAO.findAll();
      boolean palestranteAdicionado = false; // Flag para verificar se algum palestrante foi adicionado

      for (int index : selectedIndices) {
        Palestrante palestrante = palestrantes.get(index);

        // Verifica se o palestrante já está associado ao evento
        if (evento.getPalestrantes().contains(palestrante)) {
          JOptionPane.showMessageDialog(this,
              "O palestrante \"" + palestrante.getNome() + "\" já está inscrito no evento.",
              "Aviso",
              JOptionPane.WARNING_MESSAGE);
          continue; // Pula para o próximo palestrante
        }

        // Validação de capacidade do evento
        if (evento.getPalestrantes().size() >= evento.getCapacidade()) {
          JOptionPane.showMessageDialog(this,
              "O evento atingiu sua capacidade máxima.",
              "Aviso",
              JOptionPane.WARNING_MESSAGE);
          return;
        }

        // Adiciona o palestrante ao evento na memória
        evento.getPalestrantes().add(palestrante);

        // Persiste a relação no banco de dados
        palestranteDAO.registerSpeakerToEvent(evento.getId(), palestrante.getId());
        palestranteAdicionado = true; // Marca que pelo menos um palestrante foi adicionado
      }

      if (palestranteAdicionado) {
        JOptionPane.showMessageDialog(this,
            "Palestrantes inscritos com sucesso!",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE);
      }

      confirmed = true;
      dispose();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Erro ao inscrever palestrantes: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void cancel() {
    confirmed = false;
    dispose();
  }

  public boolean isConfirmed() {
    return confirmed;
  }
}