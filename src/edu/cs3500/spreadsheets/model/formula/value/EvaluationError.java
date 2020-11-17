package edu.cs3500.spreadsheets.model.formula.value;

/**
 * An error with evaluating the formula.
 */
public class EvaluationError extends AbstractCellValueError {

  @Override
  public String getValue() {
    return "EVAL ERROR";
  }
}
