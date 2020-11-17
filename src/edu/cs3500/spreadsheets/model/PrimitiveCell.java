package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.CellValueError;

/**
 * Represents a cell with a primitive type. All cells that evaluate properly should look like
 * primitive cells.
 */
public class PrimitiveCell implements Cell {

  private final String userString;
  private final CellValue cellValue;

  /**
   * A constructor that takes in a the string the user typed and the cell value.
   * @param str the string the user typed in
   * @param cellVal the cell value
   */
  PrimitiveCell(String str, CellValue cellVal) {
    this.userString = str;
    this.cellValue = cellVal;
  }

  @Override
  public CellValue<?> evaluate() {
    return this.cellValue;
  }

  @Override
  public CellValue<?> evaluate(Graph<Coord, Cell> graph) {
    return this.cellValue;
  }

  @Override
  public CellValue<?> getCellValue() {
    return this.cellValue;
  }

  @Override
  public String getUserString() {
    return this.userString;
  }

  @Override
  public boolean isFormula() {
    return false;
  }

  @Override
  public void setError(CellValueError error) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof PrimitiveCell
        && this.userString.equals(((PrimitiveCell) obj).userString)
        && this.cellValue.equals(((PrimitiveCell) obj).cellValue);
  }

  @Override
  public int hashCode() {
    return this.userString.hashCode()
        + this.cellValue.hashCode();
  }

  @Override
  public String toString() {
    return this.cellValue.toString();
  }
}
