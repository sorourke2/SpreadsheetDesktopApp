import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import edu.cs3500.spreadsheets.function.MapFunctionObject;
import edu.cs3500.spreadsheets.graph.Graph;
import edu.cs3500.spreadsheets.graph.ReferentialGraph;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@code ReferentialGraph}, mainly to test that the method {@code containsCycle} works
 * properly.
 */
public class ReferentialGraphTest {

  /**
   * A mutable integer data type for testing that the graph implementation works.
   */
  private class MInteger {

    private int i;

    private MInteger(int i) {
      this.i = i;
    }

    private void set(int i) {
      this.i = i;
    }

    private int get() {
      return this.i;
    }

    @Override
    public int hashCode() {
      return this.i;
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof MInteger
          && this.i == ((MInteger) obj).i;
    }
  }

  /**
   * Sets the value of the {@code MInteger} to zero.
   */
  private class SetZero implements MapFunctionObject<MInteger> {

    @Override
    public void apply(MInteger obj) {
      obj.set(0);
    }
  }

  /**
   * Adds one to the value of the {@code MInteger}.
   */
  private class AddOne implements MapFunctionObject<MInteger> {

    @Override
    public void apply(MInteger obj) {
      obj.set(obj.get() + 1);
    }
  }

  private Graph<Integer, MInteger> graph;

  /**
   * Adds nodes {1, 2, ..., size} to the graph.
   *
   * @param size the size of the graph
   */
  private void addNodes(int size) {
    for (int i = 1; i <= size; i++) {
      this.graph.addNode(i, new MInteger(i));
    }
  }

  @Before
  public void initGraph() {
    this.graph = new ReferentialGraph<>();
  }

  /**
   * Creates the graph as shown below.
   */
  private void initGraph1() {
    /*       6
     *     /  \
     *    2    4
     *  /  \    \
     * 1    3    5
     *
     * 8
     *    10
     *   / \
     * 7 == 9 (edge from 7 to 9 and from 9 to 7)
     */
    this.addNodes(10);

    this.graph.addEdge(6, 2);
    this.graph.addEdge(2, 1);
    this.graph.addEdge(2, 3);

    this.graph.addEdge(6, 4);
    this.graph.addEdge(4, 5);

    this.graph.addEdge(10, 7);
    this.graph.addEdge(10, 9);
    this.graph.addEdge(7, 9);
    this.graph.addEdge(9, 7);
  }

  // =============================================

  @Test
  public void testContains() {
    this.initGraph1();

    assertFalse(this.graph.contains(null));
    assertFalse(this.graph.contains(0));
    assertFalse(this.graph.contains(-5));
    assertTrue(this.graph.contains(1));
    assertTrue(this.graph.contains(3));
    assertTrue(this.graph.contains(10));
  }

  @Test
  public void testContainsAddEdgeToFromNodeDNE() {
    this.initGraph1();
    this.graph.addEdge(11, 12);

    assertFalse(this.graph.contains(11));
    assertFalse(this.graph.contains(12));
  }

  // =============================================

  @Test
  public void testGet() {
    this.initGraph1();

    assertEquals(new MInteger(1), this.graph.get(1));
    assertNull(this.graph.get(11));
  }

  // =============================================

  @Test
  public void testGetNodes() {
    this.initGraph1();

    Map<Integer, MInteger> graph = new HashMap<>();
    for (int i = 1; i <= 10; i++) {
      graph.put(i, new MInteger(i));
    }

    assertEquals(graph, this.graph.getNodes());
  }

  @Test
  public void testGetNodesEmpty() {
    assertEquals(new HashMap<Integer, MInteger>(), this.graph.getNodes());
  }

  // =============================================

  @Test
  public void testAddNode() {
    assertFalse(this.graph.contains(1));
    this.graph.addNode(1, new MInteger(1));
    assertTrue(this.graph.contains(1));
  }

  // =============================================

  @Test
  public void testRemoveNode() {
    initGraph1();
    assertTrue(this.graph.contains(1));
    this.graph.removeNode(1);
    assertFalse(this.graph.contains(1));
  }

  // =============================================

  @Test
  public void testReplaceNodeStillContained() {
    initGraph1();
    assertTrue(this.graph.contains(1));
    this.graph.replaceNode(1, new MInteger(1));
    assertTrue(this.graph.contains(1));
  }

  // =============================================

  @Test
  public void testApplyCycleTree() {
    initGraph1();
    this.graph.applyCycleTree(new SetZero(), new AddOne());
    assertEquals(new MInteger(2), this.graph.get(1));
    assertEquals(new MInteger(3), this.graph.get(2));
    assertEquals(new MInteger(4), this.graph.get(3));
    assertEquals(new MInteger(5), this.graph.get(4));
    assertEquals(new MInteger(6), this.graph.get(5));
    assertEquals(new MInteger(7), this.graph.get(6));
    assertEquals(new MInteger(0), this.graph.get(7));
    assertEquals(new MInteger(9), this.graph.get(8));
    assertEquals(new MInteger(0), this.graph.get(9));
    assertEquals(new MInteger(0), this.graph.get(10));
  }

  // =============================================

  @Test
  public void testApplyCycleTreeAncestorsTree1() {
    initGraph1();
    this.graph.applyCycleTreeAncestors(1,  new SetZero(), new AddOne());
    assertEquals(new MInteger(2), this.graph.get(1));
    assertEquals(new MInteger(3), this.graph.get(2));
    assertEquals(new MInteger(3), this.graph.get(3));
    assertEquals(new MInteger(4), this.graph.get(4));
    assertEquals(new MInteger(5), this.graph.get(5));
    assertEquals(new MInteger(7), this.graph.get(6));
    assertEquals(new MInteger(7), this.graph.get(7));
    assertEquals(new MInteger(8), this.graph.get(8));
    assertEquals(new MInteger(9), this.graph.get(9));
    assertEquals(new MInteger(10), this.graph.get(10));
  }

  @Test
  public void testApplyCycleTreeAncestorsTree2() {
    initGraph1();
    this.graph.applyCycleTreeAncestors(2,  new SetZero(), new AddOne());
    assertEquals(new MInteger(1), this.graph.get(1));
    assertEquals(new MInteger(3), this.graph.get(2));
    assertEquals(new MInteger(3), this.graph.get(3));
    assertEquals(new MInteger(4), this.graph.get(4));
    assertEquals(new MInteger(5), this.graph.get(5));
    assertEquals(new MInteger(7), this.graph.get(6));
    assertEquals(new MInteger(7), this.graph.get(7));
    assertEquals(new MInteger(8), this.graph.get(8));
    assertEquals(new MInteger(9), this.graph.get(9));
    assertEquals(new MInteger(10), this.graph.get(10));
  }

  @Test
  public void testApplyCycleTreeAncestorsTree3() {
    initGraph1();
    this.graph.applyCycleTreeAncestors(6,  new SetZero(), new AddOne());
    assertEquals(new MInteger(1), this.graph.get(1));
    assertEquals(new MInteger(2), this.graph.get(2));
    assertEquals(new MInteger(3), this.graph.get(3));
    assertEquals(new MInteger(4), this.graph.get(4));
    assertEquals(new MInteger(5), this.graph.get(5));
    assertEquals(new MInteger(7), this.graph.get(6));
    assertEquals(new MInteger(7), this.graph.get(7));
    assertEquals(new MInteger(8), this.graph.get(8));
    assertEquals(new MInteger(9), this.graph.get(9));
    assertEquals(new MInteger(10), this.graph.get(10));
  }

  @Test
  public void testApplyCycleTreeAncestorsSingleton() {
    initGraph1();
    this.graph.applyCycleTreeAncestors(8,  new SetZero(), new AddOne());
    assertEquals(new MInteger(1), this.graph.get(1));
    assertEquals(new MInteger(2), this.graph.get(2));
    assertEquals(new MInteger(3), this.graph.get(3));
    assertEquals(new MInteger(4), this.graph.get(4));
    assertEquals(new MInteger(5), this.graph.get(5));
    assertEquals(new MInteger(6), this.graph.get(6));
    assertEquals(new MInteger(7), this.graph.get(7));
    assertEquals(new MInteger(9), this.graph.get(8));
    assertEquals(new MInteger(9), this.graph.get(9));
    assertEquals(new MInteger(10), this.graph.get(10));
  }

  @Test
  public void testApplyCycleTreeAncestorsCycle1() {
    initGraph1();
    this.graph.applyCycleTreeAncestors(7,  new SetZero(), new AddOne());
    assertEquals(new MInteger(1), this.graph.get(1));
    assertEquals(new MInteger(2), this.graph.get(2));
    assertEquals(new MInteger(3), this.graph.get(3));
    assertEquals(new MInteger(4), this.graph.get(4));
    assertEquals(new MInteger(5), this.graph.get(5));
    assertEquals(new MInteger(6), this.graph.get(6));
    assertEquals(new MInteger(0), this.graph.get(7));
    assertEquals(new MInteger(8), this.graph.get(8));
    assertEquals(new MInteger(0), this.graph.get(9));
    assertEquals(new MInteger(0), this.graph.get(10));
  }

  @Test
  public void testApplyCycleTreeAncestorsCycle2() {
    initGraph1();
    this.graph.applyCycleTreeAncestors(9,  new SetZero(), new AddOne());
    assertEquals(new MInteger(1), this.graph.get(1));
    assertEquals(new MInteger(2), this.graph.get(2));
    assertEquals(new MInteger(3), this.graph.get(3));
    assertEquals(new MInteger(4), this.graph.get(4));
    assertEquals(new MInteger(5), this.graph.get(5));
    assertEquals(new MInteger(6), this.graph.get(6));
    assertEquals(new MInteger(0), this.graph.get(7));
    assertEquals(new MInteger(8), this.graph.get(8));
    assertEquals(new MInteger(0), this.graph.get(9));
    assertEquals(new MInteger(0), this.graph.get(10));
  }

  @Test
  public void testApplyCycleTreeAncestorsCycle3() {
    initGraph1();
    this.graph.applyCycleTreeAncestors(10,  new SetZero(), new AddOne());
    assertEquals(new MInteger(1), this.graph.get(1));
    assertEquals(new MInteger(2), this.graph.get(2));
    assertEquals(new MInteger(3), this.graph.get(3));
    assertEquals(new MInteger(4), this.graph.get(4));
    assertEquals(new MInteger(5), this.graph.get(5));
    assertEquals(new MInteger(6), this.graph.get(6));
    assertEquals(new MInteger(0), this.graph.get(7));
    assertEquals(new MInteger(8), this.graph.get(8));
    assertEquals(new MInteger(0), this.graph.get(9));
    assertEquals(new MInteger(0), this.graph.get(10));
  }

  @Test
  public void testApplyCycleTreeAncestorsTreeEdgeRemoved() {
    initGraph1();
    this.graph.removeEdge(6 ,2);
    this.graph.applyCycleTreeAncestors(1,  new SetZero(), new AddOne());
    assertEquals(new MInteger(2), this.graph.get(1));
    assertEquals(new MInteger(3), this.graph.get(2));
    assertEquals(new MInteger(3), this.graph.get(3));
    assertEquals(new MInteger(4), this.graph.get(4));
    assertEquals(new MInteger(5), this.graph.get(5));
    assertEquals(new MInteger(6), this.graph.get(6));
    assertEquals(new MInteger(7), this.graph.get(7));
    assertEquals(new MInteger(8), this.graph.get(8));
    assertEquals(new MInteger(9), this.graph.get(9));
    assertEquals(new MInteger(10), this.graph.get(10));
  }

  // =============================================

  @Test(expected = IllegalArgumentException.class)
  public void addNodeExists() {
    this.addNodes(4);

    this.graph.addNode(3, new MInteger(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void addNodeNullValue() {
    this.graph.addNode(3, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeNodeDNE() {
    this.addNodes(4);

    this.graph.removeNode(5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void removeEdgeDNE() {
    this.addNodes(4);

    this.graph.addEdge(1, 2);
    this.graph.addEdge(2, 3);
    this.graph.addEdge(3, 4);
    this.graph.addEdge(4, 1);

    this.graph.removeEdge(1, 3);
  }
}
