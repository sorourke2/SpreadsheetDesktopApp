package edu.cs3500.spreadsheets.controller;

/**
 * An interface describing the features that a gui should be able to provide.
 */
public interface Features {

  /**
   * The event of editing cell contents.
   *
   * @param column the column
   * @param row the row
   * @param userString the string the user inserted into the cell
   */
  void editCellContents(int column, int row, String userString);

  /**
   * The event of deleting the cells contents.
   * @param column the column of the cell
   * @param row the row of the cell
   */
  void cellContentsDeleted(int column, int row);
}
