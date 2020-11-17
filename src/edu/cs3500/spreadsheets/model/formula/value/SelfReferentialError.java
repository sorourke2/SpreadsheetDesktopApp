package edu.cs3500.spreadsheets.model.formula.value;

/**
 * An error regarding self referencing.
 */
public class SelfReferentialError extends AbstractCellValueError {

  @Override
  public String getValue() {
    return "SELF REF";
  }
}
