package edu.cs3500.spreadsheets.model.visitor;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.function.AbstractCellFunction;
import edu.cs3500.spreadsheets.model.formula.reference.AbstractCellReference;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.EmptyCellValue;
import edu.cs3500.spreadsheets.model.formula.value.EvaluationError;

/**
 * A visitor that evaluates a formula.
 */
public class EvaluateFormula implements FormulaVisitor {

  private Graph<Coord, Cell> cells;

  /**
   * Constructor that takes in a graph of cells.
   * @param cells a graph of cells
   */
  public EvaluateFormula(Graph<Coord, Cell> cells) {
    this.cells = cells;
  }

  @Override
  public CellValue<?> visit(AbstractCellFunction function) {
    return function.compute(this);
  }

  @Override
  public CellValue<?> visit(AbstractCellReference reference) {
    if (reference.isMultiRef()) {
      return new EvaluationError();
    } else {
      Coord coord = Coord.strToCoord(reference.getCellRef());
      if (this.cells.contains(coord)) {
        return this.cells.get(coord).getCellValue();
      } else {
        return new EmptyCellValue();
      }
    }
  }

  @Override
  public CellValue<?> visit(CellValue<?> cellValue) {
    return cellValue;
  }
}
