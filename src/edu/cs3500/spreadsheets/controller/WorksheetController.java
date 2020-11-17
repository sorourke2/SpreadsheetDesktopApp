package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.WorksheetInterface;
import edu.cs3500.spreadsheets.view.WorksheetView;
import edu.cs3500.spreadsheets.view.WorksheetViewInterface;

/**
 * A worksheet that mediates actions between user interactions with the view and updates to the
 * model.
 */
public class WorksheetController implements Features, WorksheetControllerInterface {

  private WorksheetInterface worksheet;
  private WorksheetViewInterface view;

  @Override
  public void editCellContents(int column, int row, String userString) {
    if (worksheet != null) {
      this.worksheet.setCell(column, row, userString);
      this.view.refresh(this.worksheet.getFilledCells());
    }
  }

  @Override
  public void cellContentsDeleted(int column, int row) {
    if (worksheet != null) {
      this.worksheet.clearCell(column, row);
      this.view.refresh(this.worksheet.getFilledCells());
    }
  }

  @Override
  public void go(WorksheetInterface worksheet, WorksheetViewInterface view) {
    this.worksheet = worksheet;
    this.view = view;
    this.view.addFeatures(this);
  }
}
