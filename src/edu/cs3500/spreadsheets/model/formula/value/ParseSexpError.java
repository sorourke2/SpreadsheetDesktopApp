package edu.cs3500.spreadsheets.model.formula.value;

/**
 * An error with the formula.
 */
public class ParseSexpError extends AbstractCellValueError {

  @Override
  public String getValue() {
    return "INV FORMULA";
  }
}
