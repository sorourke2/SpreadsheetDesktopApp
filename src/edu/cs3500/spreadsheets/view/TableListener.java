package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A listener that listens to when certain are done on the table by the user.
 */
public interface TableListener {

  /**
   * The action to be performed if the cell at the row and column is selected.
   * @param row the row of the cell selected where 0 indicates no cell is selected
   * @param column the column of the cell selected where 0 indicates no cell is selected
   */
  void cellSelected(int row, int column);

  /**
   * The action to be performed if the contents in the cell at the row and column are deleted.
   * @param row the row of the cell
   * @param column the column of the cell
   */
  void cellContentsDeleted(int row, int column);
}
