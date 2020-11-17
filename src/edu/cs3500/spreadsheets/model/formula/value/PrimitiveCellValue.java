package edu.cs3500.spreadsheets.model.formula.value;

/**
 * A primitive cell value. In other words the possible outputs of formulas.
 */
public abstract class PrimitiveCellValue<T> extends AbstractCellValue<T> {

  private final T value;

  public PrimitiveCellValue(T value) {
    this.value = value;
  }

  @Override
  public T getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof PrimitiveCellValue
        && this.value.equals(((PrimitiveCellValue) obj).value);
  }

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public String toString() {
    return this.value.toString();
  }
}
