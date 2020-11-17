package edu.cs3500.spreadsheets.view;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * A table model that allows "paging" of data. Access to other pages of data is given
 * by the paging around the table.
 */
public interface PagingInterface {

  /**
   * Moves data down by one page.
   */
  void pageDown();

  /**
   * Moves the data up by one page.
   */
  void pageUp();

  /**
   * Moves the data left by one page.
   */
  void pageLeft();

  /**
   * Moves the data right by one page.
   */
  void pageRight();

  /**
   * Returns the page the data is on.
   */
  int getRowPageOffset();

  /**
   * Returns the page the data is on.
   */
  int getColumnPageOffset();

  /**
   * Returns the number of rows in each page.
   */
  int getRowPageSize();

  /**
   * Returns the number of columns in each page.
   */
  int getColumnPageSize();

  /**
   * Creates a custom scroll pane using the table.
   * @param jTable the table with data
   * @return a custom scroll pane used for paging
   */
  JScrollPane createPagingScrollPane(JTable jTable);
}
