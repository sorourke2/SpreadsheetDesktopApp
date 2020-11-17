package edu.cs3500.spreadsheets.model.formula;

import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import edu.cs3500.spreadsheets.model.visitor.FormulaVisitor;

/**
 * A formula of a cell in a spreadsheet. Any text inserted into a cell can be represented as some
 * formula.
 */
public interface Formula {

  /**
   * Accepts a visitor that returns a cell value.
   *
   * @param visitor the visitor
   * @return a cell value
   */
  CellValue<?> accept(FormulaVisitor visitor);

  /**
   * Computes the cell value of this formula.
   *
   * @param visitor the visitor that computes the formula
   * @return a cell value
   */
  CellValue<?> compute(EvaluateFormula visitor);

  /**
   * Return the cells that this formula references as a string.
   *
   * @return the cells that this formula references
   */
  String getCellRef();

  /**
   * Indicates if this formula is a multi-cell reference.
   *
   * @return true if this formula is a multi-cell reference
   */
  boolean isMultiRef();

  /**
   * Indicates if this formula is a function.
   *
   * @return true if this formula is a function
   */
  boolean isFunction();

  /**
   * Indicates if this formula is a reference.
   *
   * @return true if this formula is reference
   */
  boolean isReference();

  /**
   * Indicates if this formula is a cell value.
   *
   * @return true if this formula is a cell value
   */
  boolean isCellValue();
}
