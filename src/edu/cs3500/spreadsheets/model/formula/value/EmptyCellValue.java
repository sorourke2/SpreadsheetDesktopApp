package edu.cs3500.spreadsheets.model.formula.value;

/**
 * The cell value of a cell with no contents.
 */
public class EmptyCellValue implements CellValue<String> {

  @Override
  public String getValue() {
    return "";
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
  public boolean equals(Object obj) {
    return obj instanceof EmptyCellValue;
  }

  @Override
  public int hashCode() {
    return 1;
  }
}
