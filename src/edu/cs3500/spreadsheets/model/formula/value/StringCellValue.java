package edu.cs3500.spreadsheets.model.formula.value;

/**
 * A string cell value.
 */
public class StringCellValue extends PrimitiveCellValue<String> {

  /**
   * Constructor which takes in the string value for this cell value.
   * @param value the string value that this cell value takes on
   */
  public StringCellValue(String value) {
    super(value);
  }

  @Override
  public boolean isString() {
    return true;
  }
}
