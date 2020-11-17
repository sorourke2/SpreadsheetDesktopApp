package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.WorksheetInterface;
import edu.cs3500.spreadsheets.view.WorksheetViewInterface;

/**
 * A controller for a worksheet. Mediates between user interactions with the view and changes to the
 * model.
 */
public interface WorksheetControllerInterface {

  /**
   * Starts controlling the worksheet with the given view.
   *
   * @param worksheet the worksheet
   * @param view the view
   */
  void go(WorksheetInterface worksheet, WorksheetViewInterface view);
}
