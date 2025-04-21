package com.eventos.view;

import javax.swing.*;

import com.eventos.model.Evento;

import java.awt.*;
import java.time.LocalDate;

public class EventoDialog extends JDialog {
  private boolean confirmed = false;
  private Evento evento;

  private JTextField nomeField;
  private JTextArea descricaoArea;
  private JTextField dataField;
  private JTextField localField;
  private JSpinner capacidadeSpinner;

  public EventoDialog(JFrame parent) {
    this(parent, null);
  }

  public EventoDialog(JFrame parent, Evento evento) {
    super(parent, evento == null ? "Novo Evento" : "Editar Evento", true);
    this.evento = evento == null ? new Evento() : evento;

    initComponents();
    pack();
    setLocationRelativeTo(parent);
  }

  private void initComponents() {
    setLayout(new BorderLayout());

    JPanel formPanel = new JPanel(new GridLayout(5, 2, 5, 5));

    // Nome
    formPanel.add(new JLabel("Nome:"));
    nomeField = new JTextField(20);
    if (evento.getNome() != null)
      nomeField.setText(evento.getNome());
    formPanel.add(nomeField);

    // Descrição
    formPanel.add(new JLabel("Descrição:"));
    descricaoArea = new JTextArea(3, 20);
    if (evento.getDescricao() != null)
      descricaoArea.setText(evento.getDescricao());
    formPanel.add(new JScrollPane(descricaoArea));

    // Data
    formPanel.add(new JLabel("Data (AAAA-MM-DD):"));
    dataField = new JTextField(10);
    if (evento.getData() != null)
      dataField.setText(evento.getData().toString());
    formPanel.add(dataField);

    // Local
    formPanel.add(new JLabel("Local:"));
    localField = new JTextField(20);
    if (evento.getLocal() != null)
      localField.setText(evento.getLocal());
    formPanel.add(localField);

    // Capacidade
    formPanel.add(new JLabel("Capacidade:"));
    capacidadeSpinner = new JSpinner(new SpinnerNumberModel(
        evento.getCapacidade() > 0 ? evento.getCapacidade() : 50, 1, 1000, 1));
    formPanel.add(capacidadeSpinner);

    add(formPanel, BorderLayout.CENTER);

    // Botões
    JPanel buttonPanel = new JPanel();
    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Cancelar");

    okButton.addActionListener(e -> {
      if (validarCampos()) {
        confirmed = true;
        dispose();
      }
    });

    cancelButton.addActionListener(e -> dispose());

    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private boolean validarCampos() {
    try {
      evento.setNome(nomeField.getText());
      evento.setDescricao(descricaoArea.getText());
      evento.setData(LocalDate.parse(dataField.getText()));
      evento.setLocal(localField.getText());
      evento.setCapacidade((Integer) capacidadeSpinner.getValue());
      return true;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Erro nos dados: " + e.getMessage(),
          "Erro",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public Evento getEvento() {
    return evento;
  }
}