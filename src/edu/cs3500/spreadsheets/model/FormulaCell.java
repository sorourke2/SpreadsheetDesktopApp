package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.CellValueError;
import edu.cs3500.spreadsheets.model.formula.value.EmptyCellValue;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;

/**
 * A cell with a formula.
 */
public class FormulaCell implements Cell {

  private final String userString;
  private final Formula formula;

  private CellValue<?> cellVal;

  /**
   * Constructor that takes in the string the user typed in and a formula.
   *
   * @param userString the string the user typed in
   * @param formula a formula
   */
  FormulaCell(String userString, Formula formula) {
    this.userString = userString;
    this.formula = formula;
    this.cellVal = new EmptyCellValue();
  }

  @Override
  public CellValue<?> evaluate() {
    throw new UnsupportedOperationException();
  }

  @Override
  public CellValue<?> evaluate(Graph<Coord, Cell> graph) {
    this.cellVal = this.formula.accept(new EvaluateFormula(graph));
    return this.cellVal;
  }

  @Override
  public CellValue<?> getCellValue() {
    return this.cellVal;
  }

  @Override
  public String getUserString() {
    return this.userString;
  }

  @Override
  public boolean isFormula() {
    return true;
  }

  @Override
  public void setError(CellValueError error) {
    this.cellVal = error;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof FormulaCell
        && this.userString.equals(((FormulaCell) obj).userString)
        && this.formula.equals(((FormulaCell) obj).formula)
        && ((this.cellVal == null && ((FormulaCell) obj).cellVal == null)
        || (this.cellVal != null && ((FormulaCell) obj).cellVal != null
        && this.cellVal.equals(((FormulaCell) obj).cellVal)));
  }

  @Override
  public int hashCode() {
    return this.userString.hashCode()
        + this.formula.hashCode()
        + (this.cellVal == null ? 0 : this.cellVal.hashCode());
  }

  @Override
  public String toString() {
    return this.formula.toString();
  }
}
