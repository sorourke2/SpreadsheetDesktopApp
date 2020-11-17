import static org.junit.Assert.assertEquals;

import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.CellFactory;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.value.EmptyCellValue;
import edu.cs3500.spreadsheets.model.formula.value.SelfReferentialError;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import edu.cs3500.spreadsheets.model.function.EvaluateCell;
import edu.cs3500.spreadsheets.model.function.SetSelfReferentialError;
import java.util.Hashtable;
import java.util.Map;
import org.junit.Test;

/**
 * Tests the concatenation cell function object.
 */
public class ConcatCellFunctionTest {

  @Test
  public void concatTwoNumbers() {
    assertEquals(new StringCellValue("hellohi"),
        CellFactory.create("=(CONCAT \"hello\" \"hi\")").evaluate(new ReferentialGraph<>()));
    assertEquals(new StringCellValue(""),
        CellFactory.create("=(CONCAT \"\")").evaluate(new ReferentialGraph<>()));
    assertEquals(new StringCellValue(" "),
        CellFactory.create("=(CONCAT \" \")").evaluate(new ReferentialGraph<>()));
  }

  @Test
  public void concatThanMultipleNumbers() {
    assertEquals(new StringCellValue("A,B,C,D"),
        CellFactory.create("=(CONCAT \"A\" \",\" \"B\" \",\" \"C\" \",\" \"D\")")
            .evaluate(new ReferentialGraph<>()));
  }


  @Test
  public void concatThanNonNumbers() {
    assertEquals(new StringCellValue("A,B,C,D"),
        CellFactory.create("=(CONCAT \"A\" \",\" 14 \"B\" 22 \",\""
            + " \"C\" \",\" \"D\")").evaluate(new ReferentialGraph<>()));
  }

  @Test
  public void concatThanTwoCellRefToPrimCells() {
    Map<Coord, Cell> cells = new Hashtable<>();
    cells.put(new Coord(1, 1), CellFactory.create("a"));
    cells.put(Coord.strToCoord("B1"), CellFactory.create("b"));
    Graph<Coord, Cell> graph = new ReferentialGraph<>(cells);
    assertEquals(new StringCellValue("ab"),
        CellFactory.create("=(CONCAT A1 B1)").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new StringCellValue("ab"),
        CellFactory.create("=(CONCAT A1:B1)").evaluate(new ReferentialGraph<>(cells)));
  }

  @Test
  public void concatThanRefToRefCell() {
    Map<Coord, Cell> cells = new Hashtable<>();
    cells.put(Coord.strToCoord("A1"), CellFactory.create("a"));
    cells.put(Coord.strToCoord("B1"), CellFactory.create("b"));
    cells.put(Coord.strToCoord("C1"), CellFactory.create("=(CONCAT A1 A1)"));
    cells.put(Coord.strToCoord("D1"), CellFactory.create("=(CONCAT A1 B1 C1)"));

    Graph<Coord, Cell> graph = new ReferentialGraph<>(cells);

    assertEquals(new EmptyCellValue(),
        CellFactory.create("=C1").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new EmptyCellValue(),
        CellFactory.create("=D1").evaluate(new ReferentialGraph<>(cells)));

    cells.get(new Coord(3, 1)).evaluate(graph);
    cells.get(new Coord(4, 1)).evaluate(graph);

    assertEquals(new StringCellValue("aa"),
        CellFactory.create("=C1").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new StringCellValue("abaa"),
        CellFactory.create("=D1").evaluate(new ReferentialGraph<>(cells)));
  }

  @Test
  public void concatRefToRefCell2() {
    // same test as above except using function objects and graph traversal instead of
    // manually updating the cells we want
    // we also check two separate small cycles

    Map<Coord, Cell> cells = new Hashtable<>();
    cells.put(Coord.strToCoord("A1"), CellFactory.create("a"));
    cells.put(Coord.strToCoord("B1"), CellFactory.create("b"));
    cells.put(Coord.strToCoord("C1"), CellFactory.create("=(CONCAT A1 A1)"));
    cells.put(Coord.strToCoord("D1"), CellFactory.create("=(CONCAT A1 B1 C1)"));

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

    assertEquals(new StringCellValue("a"),
        graph.get(Coord.strToCoord("A1")).getCellValue());
    assertEquals(new StringCellValue("b"),
        graph.get(Coord.strToCoord("B1")).getCellValue());
    assertEquals(new StringCellValue("aa"),
        graph.get(Coord.strToCoord("C1")).getCellValue());
    assertEquals(new StringCellValue("abaa"),
        graph.get(Coord.strToCoord("D1")).getCellValue());

    assertEquals(new StringCellValue("aa"),
        CellFactory.create("=C1").evaluate(new ReferentialGraph<>(cells)));
    assertEquals(new StringCellValue("abaa"),
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
