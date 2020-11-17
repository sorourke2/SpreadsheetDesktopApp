package edu.cs3500.spreadsheets.model.builder;

import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFactory;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

/**
 * Builds a {@code Worksheet}.
 */
public class WorksheetBuilder implements WorksheetReader.WorksheetBuilder<Worksheet> {

  private int numRows;
  private int numCols;
  private ReferentialGraph<Coord, Cell> graph;

  /**
   * Default constructor that sets initial number of columns and rows to 1000, and creates an empty
   * graph of cells.
   */
  public WorksheetBuilder() {
    this.numRows = (int) Math.pow(2, 32);
    this.numCols = (int) Math.pow(2, 32);
    this.graph = new ReferentialGraph<>();
  }

  @Override
  public WorksheetBuilder createCell(int col, int row, String contents) {
    if (contents == null) {
      throw new IllegalArgumentException();
    }

    Coord coord = new Coord(col, row);
    this.graph.addNode(coord, CellFactory.create(contents));

    this.assertValidRowColumn(row, col);

    if (!this.graph.contains(new Coord(col, row))) {
      this.graph.addNode(new Coord(col, row), CellFactory.create(contents));
    } else {
      this.graph.replaceNode(new Coord(col, row), CellFactory.create(contents));
    }

    if (col > this.numCols) {
      this.numCols = col;
    }
    if (row > this.numRows) {
      this.numRows = row;
    }

    return this;
  }

  @Override
  public Worksheet createWorksheet() {
    return new Worksheet.WorksheetConstructor()
        .row(numRows).column(numCols).cells(graph).build();
  }

  /**
   * Asserts that the given row and column are positive.
   *
   * @param row the row
   * @param column the column
   */
  private void assertValidRowColumn(int row, int column) {
    if (row < 1) {
      throw new IllegalArgumentException("Invalid row.");
    } else if (column < 1) {
      throw new IllegalArgumentException("Invalid column.");
    }
  }

}
