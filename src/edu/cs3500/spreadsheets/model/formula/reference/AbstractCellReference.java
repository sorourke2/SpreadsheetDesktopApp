package edu.cs3500.spreadsheets.model.formula.reference;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import edu.cs3500.spreadsheets.model.visitor.FormulaVisitor;

/**
 * Represents a cell reference. Methods that indicate which type this formula is are implemented as
 * well as the method for accepting a visitor.
 */
public abstract class AbstractCellReference implements Formula {

  @Override
  public CellValue<?> accept(FormulaVisitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public CellValue<?> compute(EvaluateFormula visitor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isMultiRef() {
    return false;
  }

  @Override
  public boolean isFunction() {
    return false;
  }

  @Override
  public boolean isReference() {
    return true;
  }

  @Override
  public boolean isCellValue() {
    return false;
  }
}
