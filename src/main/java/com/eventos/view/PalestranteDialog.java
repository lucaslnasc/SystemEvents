package com.eventos.view;

import javax.swing.*;

import com.eventos.model.Palestrante;

import java.awt.*;

public class PalestranteDialog extends JDialog {
  private boolean confirmed = false;
  private Palestrante palestrante;

  private JTextField nomeField;
  private JTextArea curriculoArea;
  private JTextField areaAtuacaoField;

  public PalestranteDialog(JFrame parent) {
    this(parent, null);
  }

  public PalestranteDialog(JFrame parent, Palestrante palestrante) {
    super(parent, palestrante == null ? "Novo Palestrante" : "Editar Palestrante", true);
    this.palestrante = palestrante == null ? new Palestrante() : palestrante;

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
    if (palestrante.getNome() != null)
      nomeField.setText(palestrante.getNome());
    formPanel.add(nomeField);

    // Currículo
    formPanel.add(new JLabel("Currículo:"));
    curriculoArea = new JTextArea(5, 20);
    if (palestrante.getCurriculo() != null)
      curriculoArea.setText(palestrante.getCurriculo());
    formPanel.add(new JScrollPane(curriculoArea));

    // Área de Atuação
    formPanel.add(new JLabel("Área de Atuação:"));
    areaAtuacaoField = new JTextField(20);
    if (palestrante.getAreaAtuacao() != null)
      areaAtuacaoField.setText(palestrante.getAreaAtuacao());
    formPanel.add(areaAtuacaoField);

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
          "O nome do palestrante é obrigatório",
          "Erro",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }

    palestrante.setNome(nomeField.getText());
    palestrante.setCurriculo(curriculoArea.getText());
    palestrante.setAreaAtuacao(areaAtuacaoField.getText());
    return true;
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public Palestrante getPalestrante() {
    return palestrante;
  }
}