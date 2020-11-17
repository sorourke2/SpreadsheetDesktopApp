import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFactory;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.value.EmptyCellValue;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.formula.value.SelfReferentialError;
import edu.cs3500.spreadsheets.model.function.EvaluateCell;
import edu.cs3500.spreadsheets.model.function.SetSelfReferentialError;
import java.util.Hashtable;
import java.util.Map;
import org.junit.Test;

/**
 * Tests the product cell function object.
 */
public class ProductCellFunctionTest {

  @Test
  public void productTwoNumbers() {
    assertEquals(new NumberCellValue(2),
        CellFactory.create("=(PRODUCT 1 2)").evaluate(new ReferentialGraph<>()));
    assertEquals(new NumberCellValue(-4),
        CellFactory.create("=  (  PRODUCT -2 2  )").evaluate(new ReferentialGraph<>()));
    assertEquals(new NumberCellValue(-4),
        CellFactory.create("=  (PRODUCT -2 2)").evaluate(new ReferentialGraph<>()));
    assertEquals(new NumberCellValue(-2),
        CellFactory.create("=  (PRODUCT -2)").evaluate(new ReferentialGraph<>()));
    assertEquals(new NumberCellValue(0),
        CellFactory.create("=  (PRODUCT true)").evaluate(new ReferentialGraph<>()));

  }

  @Test
  public void productNonNumbers() {
    assertEquals(new NumberCellValue(-50),
        CellFactory.create("=(PRODUCT -10 5 \"haha\" true false \"idk\")")
            .evaluate(new ReferentialGraph<>()));
  }

  @Test
  public void productTwoCellRefToPrimCells() {
    Map<Coord, Cell> cells = new Hashtable<>();
    cells.put(new Coord(1, 1), CellFactory.create("1"));
    cells.put(Coord.strToCoord("B1"), CellFactory.create("2"));
    Graph<Coord, Cell> graph = new ReferentialGraph<>(cells);
    assertEquals(new NumberCellValue(2),
        CellFactory.create("=(PRODUCT A1 B1)").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new NumberCellValue(2),
        CellFactory.create("=(PRODUCT A1:B1)").evaluate(new ReferentialGraph<>(cells)));
  }

  @Test
  public void productRefToRefCell() {
    Map<Coord, Cell> cells = new Hashtable<>();
    cells.put(Coord.strToCoord("A1"), CellFactory.create("1"));
    cells.put(Coord.strToCoord("B1"), CellFactory.create("2"));
    cells.put(Coord.strToCoord("C1"), CellFactory.create("=(PRODUCT A1 A1)"));
    cells.put(Coord.strToCoord("D1"), CellFactory.create("=(PRODUCT A1 B1 C1)"));

    Graph<Coord, Cell> graph = new ReferentialGraph<>(cells);

    assertEquals(new EmptyCellValue(),
        CellFactory.create("=C1").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new EmptyCellValue(),
        CellFactory.create("=D1").evaluate(new ReferentialGraph<>(cells)));

    cells.get(new Coord(3, 1)).evaluate(graph);
    cells.get(new Coord(4, 1)).evaluate(graph);

    assertEquals(new NumberCellValue(1),
        CellFactory.create("=C1").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new NumberCellValue(2),
        CellFactory.create("=D1").evaluate(new ReferentialGraph<>(cells)));
  }

  @Test
  public void productRefToRefCell2() {
    // same test as above except using function objects and graph traversal instead of
    // manually updating the cells we want
    // we also check two separate small cycles

    Map<Coord, Cell> cells = new Hashtable<>();
    cells.put(Coord.strToCoord("A1"), CellFactory.create("1"));
    cells.put(Coord.strToCoord("B1"), CellFactory.create("2"));
    cells.put(Coord.strToCoord("C1"), CellFactory.create("=(PRODUCT A1 A1)"));
    cells.put(Coord.strToCoord("D1"), CellFactory.create("=(PRODUCT A1 B1 C1)"));

    cells.put(Coord.strToCoord("E1"), CellFactory.create("=E1"));
    cells.put(Coord.strToCoord("F1"), CellFactory.create("=G1"));
    cells.put(Coord.strToCoord("G1"), CellFactory.create("=F1"));

    Graph<Coord, Cell> graph = new ReferentialGraph<>(cells);
    graph.addEdge(Coord.strToCoord("C1"), Coord.strToCoord("A1"));
    graph.addEdge(Coord.strToCoord("C1"), Coord.strToCoord("A1"));
    graph.addEdge(Coord.strToCoord("D1"), Coord.strToCoord("A1"));
    graph.addEdge(Coord.strToCoord("D1"), Coord.strToCoord("B1"));
    graph.addEdge(Coord.strToCoord("D1"), Coord.strToCoord("C1"));

    graph.addEdge(Coord.strToCoord("E1"), Coord.strToCoord("E1"));
    graph.addEdge(Coord.strToCoord("F1"), Coord.strToCoord("G1"));
    graph.addEdge(Coord.strToCoord("G1"), Coord.strToCoord("F1"));

    graph.applyCycleTree(new SetSelfReferentialError(), new EvaluateCell(graph));

    assertEquals(new NumberCellValue(1),
        graph.get(Coord.strToCoord("A1")).getCellValue());
    assertEquals(new NumberCellValue(2),
        graph.get(Coord.strToCoord("B1")).getCellValue());
    assertEquals(new NumberCellValue(1),
        graph.get(Coord.strToCoord("C1")).getCellValue());
    assertEquals(new NumberCellValue(2),
        graph.get(Coord.strToCoord("D1")).getCellValue());

    assertEquals(new NumberCellValue(1),
        CellFactory.create("=C1").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new NumberCellValue(2),
        CellFactory.create("=D1").evaluate(new ReferentialGraph<>(cells)));

    assertEquals(new SelfReferentialError(),
        graph.get(Coord.strToCoord("E1")).getCellValue());
    assertEquals(new SelfReferentialError(),
        graph.get(Coord.strToCoord("F1")).getCellValue());
    assertEquals(new SelfReferentialError(),
        graph.get(Coord.strToCoord("G1")).getCellValue());

    assertEquals(new SelfReferentialError(),
        CellFactory.create("=E1").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new SelfReferentialError(),
        CellFactory.create("=F1").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new SelfReferentialError(),
        CellFactory.create("=G1").evaluate(new ReferentialGraph<>(cells)));
  }
}
