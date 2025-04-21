package com.eventos.view;

import javax.swing.table.AbstractTableModel;

import com.eventos.model.Participante;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteTableModel extends AbstractTableModel {
  private List<Participante> participantes = new ArrayList<>();
  private String[] colunas = { "Nome", "Email", "CPF" };

  @Override
  public int getRowCount() {
    return participantes.size();
  }

  @Override
  public int getColumnCount() {
    return colunas.length;
  }

  @Override
  public String getColumnName(int column) {
    return colunas[column];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Participante participante = participantes.get(rowIndex);

    switch (columnIndex) {
      case 0:
        return participante.getNome();
      case 1:
        return participante.getEmail();
      case 2:
        return participante.getCpf();
      default:
        return null;
    }
  }

  public void setParticipantes(List<Participante> participantes) {
    this.participantes = new ArrayList<>(participantes);
    fireTableDataChanged();
  }

  public Participante getParticipanteAt(int row) {
    return participantes.get(row);
  }
}