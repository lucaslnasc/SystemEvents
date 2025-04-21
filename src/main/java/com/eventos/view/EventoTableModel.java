package com.eventos.view;

import javax.swing.table.AbstractTableModel;

import com.eventos.model.Evento;

import java.util.ArrayList;
import java.util.List;

public class EventoTableModel extends AbstractTableModel {
  private List<Evento> eventos = new ArrayList<>();
  private String[] colunas = { "Nome", "Data", "Local", "Capacidade" };

  @Override
  public int getRowCount() {
    return eventos.size();
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
    Evento evento = eventos.get(rowIndex);

    switch (columnIndex) {
      case 0:
        return evento.getNome();
      case 1:
        return evento.getData();
      case 2:
        return evento.getLocal();
      case 3:
        return evento.getCapacidade();
      default:
        return null;
    }
  }

  public void setEventos(List<Evento> eventos) {
    this.eventos = new ArrayList<>(eventos);
    fireTableDataChanged();
  }

  public Evento getEventoAt(int row) {
    return eventos.get(row);
  }
}