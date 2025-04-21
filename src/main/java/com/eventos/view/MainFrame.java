package com.eventos.view;

import javax.swing.*;

public class MainFrame extends JFrame {
  public MainFrame() {
    setTitle("Sistema de Gerenciamento de Eventos");
    setSize(1000, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    initUI();
  }

  private void initUI() {
    JTabbedPane tabbedPane = new JTabbedPane();

    // Adiciona abas para cada módulo do sistema
    tabbedPane.addTab("Eventos", new EventoPanel());
    tabbedPane.addTab("Palestrantes", new PalestrantePanel());
    tabbedPane.addTab("Participantes", new ParticipantePanel());

    add(tabbedPane);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      MainFrame frame = new MainFrame();
      frame.setVisible(true);
    });
  }
}