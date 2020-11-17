package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.CellValueError;

/**
 * Represents a cell.
 */
public interface Cell {

  /**
   * Evaluates the value of the cell by recalculating its value.
   *
   * @return the value of the cell
   */
  CellValue<?> evaluate();

  /**
   * Evaluates the value of the cell by recalculating its value.
   *
   * @param graph the graph with information about all the cells
   * @return the evaluated cell value
   */
  public CellValue<?> evaluate(Graph<Coord, Cell> graph);

  /**
   * Return the value of the cell.
   *
   * @return the value of the cell
   */
  CellValue<?> getCellValue();

  /**
   * Returns the string the user inputted into the cell.
   *
   * @return the string the user inputted
   */
  String getUserString();

  /**
   * Indicates if this cell contains a formula.
   *
   * @return true if this cell contains a formula
   */
  boolean isFormula();

  /**
   * Sets the value of this cell to the given error.
   * @param error the error
   */
  void setError(CellValueError error);
}
