package com.eventos.view;

import javax.swing.*;

import com.eventos.model.Participante;

import java.awt.*;

public class ParticipanteDialog extends JDialog {
  private boolean confirmed = false;
  private Participante participante;

  private JTextField nomeField;
  private JTextField emailField;
  private JTextField cpfField;

  public ParticipanteDialog(JFrame parent) {
    this(parent, null);
  }

  public ParticipanteDialog(JFrame parent, Participante participante) {
    super(parent, participante == null ? "Novo Participante" : "Editar Participante", true);
    this.participante = participante == null ? new Participante() : participante;

    initComponents();
    pack();
    setLocationRelativeTo(parent);
  }

  private void initComponents() {
    setLayout(new BorderLayout());

    JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));

    // Nome
    formPanel.add(new JLabel("Nome:"));
    nomeField = new JTextField(20);
    if (participante.getNome() != null)
      nomeField.setText(participante.getNome());
    formPanel.add(nomeField);

    // Email
    formPanel.add(new JLabel("Email:"));
    emailField = new JTextField(20);
    if (participante.getEmail() != null)
      emailField.setText(participante.getEmail());
    formPanel.add(emailField);

    // CPF
    formPanel.add(new JLabel("CPF:"));
    cpfField = new JTextField(20);
    if (participante.getCpf() != null)
      cpfField.setText(participante.getCpf());
    formPanel.add(cpfField);

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
    if (nomeField.getText().trim().isEmpty()) {
      JOptionPane.showMessageDialog(this,
          "O nome do participante é obrigatório",
          "Erro",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    if (emailField.getText().trim().isEmpty() || !emailField.getText().contains("@")) {
      JOptionPane.showMessageDialog(this,
          "Email inválido",
          "Erro",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    if (cpfField.getText().trim().isEmpty() || cpfField.getText().length() != 11) {
      JOptionPane.showMessageDialog(this,
          "CPF inválido (deve conter 11 dígitos)",
          "Erro",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    participante.setNome(nomeField.getText());
    participante.setEmail(emailField.getText());
    participante.setCpf(cpfField.getText());
    return true;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public Participante getParticipante() {
    return participante;
  }
}