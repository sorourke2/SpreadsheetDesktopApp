package edu.cs3500.spreadsheets.model.function;

import edu.cs3500.spreadsheets.function.MapFunctionObject;
import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * Function object that evaluates a cell in place.
 */
public class EvaluateCell implements MapFunctionObject<Cell> {

  private Graph<Coord, Cell> cells;

  /**
   * Constructor that takes in a graph of cells.
   * @param cells the graph of cells
   */
  public EvaluateCell(Graph<Coord, Cell> cells) {
    this.cells = cells;
  }

  @Override
  public void apply(Cell obj) {
    obj.evaluate(cells);
  }
}
