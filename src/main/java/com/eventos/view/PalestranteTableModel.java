package com.eventos.view;

import javax.swing.table.AbstractTableModel;

import com.eventos.model.Palestrante;

import java.util.ArrayList;
import java.util.List;

public class PalestranteTableModel extends AbstractTableModel {
  private List<Palestrante> palestrantes = new ArrayList<>();
  private String[] colunas = { "Nome", "Área de Atuação", "Currículo" };

  @Override
  public int getRowCount() {
    return palestrantes.size();
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
    Palestrante palestrante = palestrantes.get(rowIndex);

    switch (columnIndex) {
      case 0:
        return palestrante.getNome();
      case 1:
        return palestrante.getAreaAtuacao();
      case 2:
        return palestrante.getCurriculo();
      default:
        return null;
    }
  }

  public void setPalestrantes(List<Palestrante> palestrantes) {
    this.palestrantes = new ArrayList<>(palestrantes);
    fireTableDataChanged();
  }

  public Palestrante getPalestranteAt(int row) {
    return palestrantes.get(row);
  }
}