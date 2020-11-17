package edu.cs3500.spreadsheets.view;

import java.io.IOException;

/**
 * Table listener that records which methods are being called.
 */
public class MockTableListener implements TableListener {

  private Appendable appendable;

  public MockTableListener(Appendable appendable) {
    this.appendable = appendable;
  }

  @Override
  public void cellSelected(int row, int column) {
    try {
      this.appendable.append(String.format("cellSelected(%d, %d)\n", row, column));
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }

  @Override
  public void cellContentsDeleted(int row, int column) {
    try {
      this.appendable.append(String.format("cellContentsDeleted(%d, %d)\n", row, column));
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }
}
