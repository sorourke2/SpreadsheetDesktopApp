package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.WorksheetInterface;
import edu.cs3500.spreadsheets.view.MockTableListener;
import edu.cs3500.spreadsheets.view.WorksheetViewInterface;
import java.io.IOException;

/**
 * Controller that records which methods are being called.
 */
public class MockController implements Features, WorksheetControllerInterface {

  private Appendable appendable;

  public MockController(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public void editCellContents(int column, int row, String userString) {
    try {
      appendable.append(String.format("editCellContents(%d, %d, %s)\n", column, row, userString));
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }

  @Override
  public void cellContentsDeleted(int column, int row) {
    try {
      appendable.append(String.format("cellContentsDeleted(%d, %d)\n", column, row));
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }

  @Override
  public void go(WorksheetInterface worksheet, WorksheetViewInterface view) {
    try {
      appendable.append("go(..., ...)\n");
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }
}
