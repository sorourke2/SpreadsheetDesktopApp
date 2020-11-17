package edu.cs3500.spreadsheets.model.formula.value;

/**
 * A boolean cell value.
 */
public class BooleanCellValue extends PrimitiveCellValue<Boolean> {

  /**
   * Constructor which takes in the boolean value for this cell value.
   * @param value the boolean value that this cell value takes on
   */
  public BooleanCellValue(Boolean value) {
    super(value);
  }

  @Override
  public boolean isBoolean() {
    return true;
  }
}
