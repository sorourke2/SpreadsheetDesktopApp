package edu.cs3500.spreadsheets.model.formula.value;

import edu.cs3500.spreadsheets.model.visitor.FormulaVisitor;

/**
 * Provides defaults to many functions that are the same across the classes.
 */
public abstract class AbstractCellValue<T> implements CellValue<T> {

  @Override
  public CellValue<?> accept(FormulaVisitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean isNumber() {
    return false;
  }

  @Override
  public boolean isString() {
    return false;
  }

  @Override
  public boolean isBoolean() {
    return false;
  }

  @Override
  public boolean isError() {
    return false;
  }

  @Override
  public boolean isMultiRef() {
    return false;
  }
}
