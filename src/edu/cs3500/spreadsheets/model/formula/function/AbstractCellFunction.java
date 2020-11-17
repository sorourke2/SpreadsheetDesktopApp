package edu.cs3500.spreadsheets.model.formula.function;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.visitor.FormulaVisitor;

/**
 * Represents a cell function. Methods that indicate which type this formula is are implemented as
 * well as the method for accepting a visitor.
 */
public abstract class AbstractCellFunction implements Formula {

  @Override
  public CellValue<?> accept(FormulaVisitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public String getCellRef() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isMultiRef() {
    return false;
  }

  @Override
  public boolean isFunction() {
    return true;
  }

  @Override
  public boolean isReference() {
    return false;
  }

  @Override
  public boolean isCellValue() {
    return false;
  }
}
