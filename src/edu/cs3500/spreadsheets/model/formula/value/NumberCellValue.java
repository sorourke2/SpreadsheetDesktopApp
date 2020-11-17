package edu.cs3500.spreadsheets.model.formula.value;

/**
 * A number cell value.
 */
public class NumberCellValue extends PrimitiveCellValue<Double> {

  /**
   * Constructor which takes in the number value for this cell value.
   * @param value the numerical value that this cell value takes on
   */
  public NumberCellValue(double value) {
    super(value);
  }

  @Override
  public boolean isNumber() {
    return true;
  }
}
