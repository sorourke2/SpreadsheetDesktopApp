package edu.cs3500.spreadsheets.model.formula.reference;

/**
 * A reference to a range of cells.
 */
public class MultiReference extends AbstractCellReference {

  private final String cells;

  /**
   * Constructor taking string representing the coordinate of the range of cells.
   *
   * @param cells the range of cells
   */
  public MultiReference(String cells) {
    this.cells = cells;
  }

  @Override
  public String getCellRef() {
    return this.cells;
  }

  @Override
  public boolean isMultiRef() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof MultiReference && this.cells.equals(((MultiReference) obj).cells);
  }

  @Override
  public int hashCode() {
    return this.cells.hashCode();
  }
}
