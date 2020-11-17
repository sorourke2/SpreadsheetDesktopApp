package edu.cs3500.spreadsheets.model.value;

/**
 * An error value.
 */
public class ErrorValue extends AbstractValue<String> {

  public ErrorValue(String value) {
    super(value);
  }
}
