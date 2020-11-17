package edu.cs3500.spreadsheets.graph;

import edu.cs3500.spreadsheets.function.MapFunctionObject;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * An directed graph where for each edge, the forward direction represents a node referring to
 * another node, and the backwards direction represents a node being referred to by another node.
 *
 * @param <K> the type of each key
 * @param <V> the type of each node
 */
public class ReferentialGraph<K, V> implements Graph<K, V> {

  private Map<K, V> nodes;
  private Map<K, Set<K>> forwardEdges;
  private Map<K, Set<K>> backwardEdges;

  /**
   * Constructor that creates an empty graph.
   */
  public ReferentialGraph() {
    this(new Hashtable<>(), new Hashtable<>(), new Hashtable<>());
  }

  /**
   * Constructor that takes in the list of nodes.
   *
   * @param nodes the list of nodes
   */
  public ReferentialGraph(Map<K, V> nodes) {
    this(nodes, new Hashtable<>(), new Hashtable<>());
  }

  /**
   * Constructor that takes in the list of nodes, the list of forward edges, and the list of
   * backward edges. For each node, if its list of forward or backwards edges are not present in the
   * given lists, initializes them to be empty.
   *
   * @param nodes the list of nodes
   * @param forwardEdges the list of forward edges
   * @param backwardEdges the list of backward edges
   */
  public ReferentialGraph(Map<K, V> nodes, Map<K, Set<K>> forwardEdges,
      Map<K, Set<K>> backwardEdges) {

    class MapIfAbsent implements Function<K, Set<K>> {

      @Override
      public Set<K> apply(K o) {
        return new HashSet<>();
      }
    }

    this.nodes = nodes;
    this.forwardEdges = forwardEdges;
    this.backwardEdges = backwardEdges;

    for (K key : this.nodes.keySet()) {
      this.forwardEdges.computeIfAbsent(key, new MapIfAbsent());
      this.backwardEdges.computeIfAbsent(key, new MapIfAbsent());
    }
  }

  @Override
  public boolean contains(K key) {
    return key != null && this.nodes.containsKey(key);
  }

  @Override
  public V get(K key) {
    return this.nodes.get(key);
  }

  @Override
  public Map<K, V> getNodes() {
    return this.nodes;
  }

  @Override
  public void addNode(K key, V obj) throws IllegalArgumentException {
    if (obj == null) {
      throw new IllegalArgumentException("Value must be non-null.");
    } else if (this.nodes.containsKey(key)) {
      throw new IllegalArgumentException("Node already contained in graph.");
    } else {
      this.nodes.put(key, obj);
      if (!this.forwardEdges.containsKey(key)) {
        this.forwardEdges.put(key, new HashSet<>());
      }
      if (!this.backwardEdges.containsKey(key)) {
        this.backwardEdges.put(key, new HashSet<>());
      }
    }
  }

  @Override
  public void removeNode(K key) throws IllegalArgumentException {
    if (!this.nodes.containsKey(key)) {
      throw new IllegalArgumentException("Node not contained in graph.");
    } else {
      this.forwardEdges.remove(key);
      for (K childNode : this.forwardEdges.keySet()) {
        this.backwardEdges.get(childNode).remove(key);
      }
      this.nodes.remove(key);
    }
  }

  @Override
  public void replaceNode(K key, V value) {
    this.nodes.put(key, value);
    this.forwardEdges.put(key, new HashSet<>());
  }

  @Override
  public void addEdge(K key1, K key2) {
    if (!this.forwardEdges.containsKey(key1)) {
      this.forwardEdges.put(key1, new HashSet<>());
    }
    if (!this.backwardEdges.containsKey(key2)) {
      this.backwardEdges.put(key2, new HashSet<>());
    }

    this.forwardEdges.get(key1).add(key2);
    this.backwardEdges.get(key2).add(key1);
  }

  @Override
  public void removeEdge(K key1, K key2) throws IllegalArgumentException {

    Set<K> adjList1 = this.forwardEdges.get(key1);
    Set<K> adjList2 = this.backwardEdges.get(key2);

    if (!adjList1.contains(key2) || !adjList2.contains(key1)) {
      throw new IllegalArgumentException("Edge not contained in graph.");
    } else {
      adjList1.remove(key2);
      adjList2.remove(key1);
    }
  }

  @Override
  public void applyCycleTree(MapFunctionObject<V> cycleFunc,
      MapFunctionObject<V> treeFunc) {
    Set<K> hasCycle = new HashSet<>();
    Set<K> hasNoCycle = new HashSet<>();

    // essentially performs a DFS to determine which nodes are in trees and which aren't
    for (K node : this.nodes.keySet()) {

      // calls graphIncludesCycle for each node not already calculated
      if (!hasNoCycle.contains(node) && !hasCycle.contains(node)) {
        this.graphIncludesCycle(node, new HashSet<>(), hasNoCycle, hasCycle);
      }
    }

    for (K node : hasCycle) {
      cycleFunc.apply(this.nodes.get(node));
    }
    this.applyBottomUp(treeFunc, hasNoCycle, new HashSet<>());
  }

  @Override
  public void applyCycleTreeAncestors(K key, MapFunctionObject<V> cycleFunc,
      MapFunctionObject<V> treeFunc) throws IllegalArgumentException {
    this.assertKeyExists(key);

    Set<K> hasCycle = new HashSet<>();
    if (this.graphIncludesCycle(key, new HashSet<>(), new HashSet<>(), hasCycle)) {
      hasCycle.addAll(this.allAncestors(key));
      for (K cycleNode : hasCycle) {
        cycleFunc.apply(this.nodes.get(cycleNode));
      }
    } else if (this.backwardEdges.get(key) != null) {
      // point of reference, should not change
      final Set<K> ancestors = this.allAncestors(key);
      Set<K> alreadyApplied = new HashSet<>();

      // inclusive of this node
      treeFunc.apply(this.nodes.get(key));
      for (K ancestor : ancestors) {
        if (!alreadyApplied.contains(ancestor)) {
          applyAncestorsBottomUp(ancestor, treeFunc, alreadyApplied, ancestors);
        }
      }
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ReferentialGraph
        && this.nodes.equals(((ReferentialGraph) obj).nodes)
        && this.forwardEdges.equals(((ReferentialGraph) obj).forwardEdges)
        && this.backwardEdges.equals(((ReferentialGraph) obj).backwardEdges);
  }

  @Override
  public int hashCode() {
    return this.nodes.hashCode() + this.forwardEdges.hashCode() + this.backwardEdges.hashCode();
  }

  /**
   * Checks to see if a given key is in the graph.
   *
   * @param key the key to check
   */
  private void assertKeyExists(K key) {
    if (!this.nodes.containsKey(key)
        && !this.forwardEdges.containsKey(key)
        && !this.backwardEdges.containsKey(key)) {
      throw new IllegalArgumentException("Node does not exist.");
    }
  }

  /**
   * Applies the given function to a list of nodes that contains no cycles, from the bottom most
   * node first.
   *
   * @param func the function to apply
   * @param toVisit the nodes to visit
   * @param visited the visited nodes
   */
  private void applyBottomUp(MapFunctionObject<V> func,
      Set<K> toVisit, Set<K> visited) {
    for (K node : toVisit) {
      if (!visited.contains(node) && this.nodes.containsKey(node)) {
        visited.add(node);
        for (K childNode : this.forwardEdges.get(node)) {
          if (!visited.contains(childNode) && toVisit.contains(childNode)) {
            this.applyBottomUpHelper(childNode, func, toVisit, visited);
          }
        }
        func.apply(this.nodes.get(node));
      }
    }
  }

  /**
   * Applies the given function to all children of the given node, from the bottom up, and then to
   * the given node at the end.
   *
   * @param node the initial node
   * @param func the function to apply
   * @param toVisit the nodes to visit
   * @param visited the visited nodes
   */
  private void applyBottomUpHelper(K node, MapFunctionObject<V> func,
      Set<K> toVisit, Set<K> visited) {
    visited.add(node);
    for (K childNode : this.forwardEdges.get(node)) {
      if (toVisit.contains(childNode) && !visited.contains(childNode)) {
        this.applyBottomUpHelper(childNode, func, toVisit, visited);
      }
    }
    func.apply(this.nodes.get(node));
  }

  /**
   * Applies the given function to all a nodes ancestors, from the bottom up, including the given
   * node.
   *
   * @param node the initial node
   * @param treeFunc the function to apply
   * @param alreadyApplied nodes which the function has already been applied to
   * @param allAncestors all the given nodes ancestors
   */
  private void applyAncestorsBottomUp(K node, MapFunctionObject<V> treeFunc,
      Set<K> alreadyApplied, Set<K> allAncestors) {
    if (!alreadyApplied.contains(node)) {
      for (K childNode : this.forwardEdges.get(node)) {
        if (allAncestors.contains(childNode) && !alreadyApplied.contains(childNode)) {
          applyAncestorsBottomUp(childNode, treeFunc, alreadyApplied, allAncestors);
        }
      }
      treeFunc.apply(this.nodes.get(node));
      alreadyApplied.add(node);
    }
  }

  /**
   * Returns all ancestors of a node.
   *
   * @param node the node
   * @return the ancestors
   */
  private Set<K> allAncestors(K node) {
    Set<K> ancestors = new HashSet<>();
    for (K parent : this.backwardEdges.get(node)) {
      if (!ancestors.contains(parent)) {
        ancestors.addAll(this.allAncestorsAcc(parent, ancestors));
      }
    }
    return ancestors;
  }

  /**
   * Returns all ancestors of a node, keeping track of which ancestors have already been accounted
   * for.
   *
   * @param node the node
   * @param ancestors the ancestors already accounted for
   * @return the ancestors
   */
  private Set<K> allAncestorsAcc(K node, Set<K> ancestors) {
    ancestors.add(node);
    for (K parent : this.backwardEdges.get(node)) {
      if (!ancestors.contains(parent)) {
        allAncestorsAcc(parent, ancestors);
      }
    }
    return ancestors;
  }

  /**
   * Determines if the graph connected to the given node contains a cycle.
   *
   * @param node the node of whose graph we are checking
   * @param ancestors the ancestors of the node that we have encountered so far
   * @param hasNoCycle the set of nodes that have no cycles leaving the node
   * @param hasCycle the set of nodes that contain a cycle leaving or entering the node
   * @return true if the connected graph containing the node contains a cycle
   */
  private boolean graphIncludesCycle(K node, Set<K> ancestors, Set<K> hasNoCycle,
      Set<K> hasCycle) {
    ancestors.add(node);
    for (K neighbor : this.forwardEdges.get(node)) {
      if (ancestors.contains(neighbor) || hasCycle.contains(node)) {
        //  we have hit a backwards edge, which implies a cycle in a directed graph
        hasCycle.addAll(ancestors);
        hasCycle.add(node);
        return true;
      } else if (hasNoCycle.contains(node)) {
        return false;
      } else {
        // calls graphIncludesCycle for each node not already calculated
        if (this.nodes.containsKey(neighbor) && !hasNoCycle.contains(neighbor)
            && graphIncludesCycle(neighbor, ancestors, hasNoCycle, hasCycle)) {
          return true;
        } else {
          // this node has no cycles, so add it to list of calculated nodes
          hasNoCycle.add(neighbor);
        }
      }
    }
    ancestors.remove(node);
    hasNoCycle.add(node);
    return false;
  }
}
