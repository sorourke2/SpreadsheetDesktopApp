package edu.cs3500.spreadsheets.model.formula.value;

/**
 * An error value of a cell. Provides universal implementations of methods that assert which type of
 * value this cell value is.
 */
public abstract class AbstractCellValueError implements CellValueError {

  @Override
  public boolean isError() {
    return true;
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
  public boolean equals(Object obj) {
    return obj instanceof AbstractCellValueError
        && this.getClass().toString().equals(obj.getClass().toString());
  }

  @Override
  public int hashCode() {
    return this.getClass().toString().hashCode();
  }
}
