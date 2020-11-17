package edu.cs3500.spreadsheets.model.formula.reference;

/**
 * A reference to a single cell.
 */
public class SingleReference extends AbstractCellReference {

  private final String cell;

  /**
   * Constructor taking string representing the coordinate of the cell.
   *
   * @param cell the cell
   */
  public SingleReference(String cell) {
    this.cell = cell;
  }

  @Override
  public String getCellRef() {
    return this.cell;
  }

  @Override
  public boolean isMultiRef() {
    return false;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof SingleReference && this.cell.equals(((SingleReference) obj).cell);
  }

  @Override
  public int hashCode() {
    return this.cell.hashCode();
  }

  @Override
  public String toString() {
    return this.cell;
  }
}
