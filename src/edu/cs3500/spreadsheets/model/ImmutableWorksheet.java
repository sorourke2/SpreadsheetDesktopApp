package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.value.Value;
import java.util.Set;

/**
 * A wrapper for a worksheet that only allows access to the immutable methods of said worksheet.
 */
public class ImmutableWorksheet implements ImmutableWorksheetInterface {

  private WorksheetInterface worksheet;

  public ImmutableWorksheet(WorksheetInterface worksheet) {
    this.worksheet = worksheet;
  }

  @Override
  public String getUserString(int column, int row) {
    return this.worksheet.getUserString(column, row);
  }

  @Override
  public Value<?> getCellValue(int column, int row) {
    return this.worksheet.getCellValue(column, row);
  }

  @Override
  public boolean isCellError(int column, int row) {
    return this.worksheet.isCellError(column, row);
  }

  @Override
  public int getRows() {
    return this.worksheet.getRows();
  }

  @Override
  public int getColumns() {
    return this.worksheet.getColumns();
  }

  @Override
  public Set<Coord> getFilledCells() {
    return this.worksheet.getFilledCells();
  }
}
