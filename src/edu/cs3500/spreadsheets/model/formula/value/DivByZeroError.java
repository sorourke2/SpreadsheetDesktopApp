package edu.cs3500.spreadsheets.model.formula.value;

/**
 * A divide by zero error.
 */
public class DivByZeroError extends AbstractCellValueError {

  @Override
  public String getValue() {
    return "DIV BY 0";
  }
}
