package edu.cs3500.spreadsheets.model.visitor;

import edu.cs3500.spreadsheets.model.formula.function.AbstractCellFunction;
import edu.cs3500.spreadsheets.model.formula.reference.AbstractCellReference;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;

/**
 * An abstracted function object for processing any {@code Formula} into a {@code CellValue}.
 */
public interface FormulaVisitor {

  /**
   * Visits an {@code AbstractCellFunction}.
   * @param function the function to visit
   * @return a cell value
   */
  CellValue<?> visit(AbstractCellFunction function);

  /**
   * Visits an {@code AbstractCellReference}.
   * @param reference the cell reference
   * @return a cell value
   */
  CellValue<?> visit(AbstractCellReference reference);

  /**
   * Visits an {@code CellValue<?>}.
   * @param cellValue the cell value
   * @return a cell value
   */
  CellValue<?> visit(CellValue<?> cellValue);
}
