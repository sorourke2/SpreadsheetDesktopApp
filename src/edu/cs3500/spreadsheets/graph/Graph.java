package edu.cs3500.spreadsheets.graph;

import edu.cs3500.spreadsheets.function.MapFunctionObject;
import java.util.Map;

/**
 * A graph with nodes where the nodes are stored as values with a unique key. Edges are stored as
 * paths from key to key. It is possible to have paths to a key which has no associated node.
 *
 * @param <K> the type of each key
 * @param <V> the type of each node
 */
public interface Graph<K, V> {

  /**
   * Determines if this graph contains the node with the associated key.
   *
   * @param key the key to check
   * @return true if this graph contains the node
   */
  boolean contains(K key);

  /**
   * Gets the value with the given key.
   *
   * @param key the key
   * @return the value
   */
  V get(K key);

  /**
   * Returns all the key-node pairs in the graph.
   *
   * @return the key-node pairs
   */
  Map<K, V> getNodes();

  /**
   * Adds a node to the graph.
   *
   * @param key the key for the node
   * @param obj the node to be added
   * @throws IllegalArgumentException if the node is already in the graph
   */
  void addNode(K key, V obj) throws IllegalArgumentException;

  /**
   * Removes a node from the graph.
   *
   * @param key the key whose node to remove
   * @throws IllegalArgumentException if the node is not in the graph
   */
  void removeNode(K key) throws IllegalArgumentException;

  /**
   * Replaces the node associated with the given key, or if it does not already exist, adds it to
   * the graph.
   *
   * @param key the key
   * @param value the node to be placed into the graph
   */
  void replaceNode(K key, V value);

  /**
   * Adds an edge to the graph.
   *
   * @param key1 the key of the node the edge starts from
   * @param key2 the key of the node the edge leads to
   */
  void addEdge(K key1, K key2);

  /**
   * Removes an edge from the graph.
   *
   * @param key1 the key of the node the edge starts from
   * @param key2 the key of the node the edge leads to
   * @throws IllegalArgumentException if no such edge exists in the graph
   */
  void removeEdge(K key1, K key2) throws IllegalArgumentException;

  /**
   * Applies a function to all cells in a cycle, and a separate function to all cells in a tree.
   *
   * @param cycleFunc the function to apply to cells in a cycle
   * @param treeFunc the function to apply to cells in a tree
   */
  void applyCycleTree(MapFunctionObject<V> cycleFunc, MapFunctionObject<V> treeFunc);

  /**
   * If the node associated with the given key is in a sub-graph containing a cycle, apply the cycle
   * function to all nodes in the sub-graph. Otherwise apply the tree function too all nodes in the
   * sub-graph that are ancestors of the given node, including the given node.
   *
   * @param key the key associated with the node
   * @param cycleFunc the function to apply to the cells in the sub-graph with a cycle
   * @param treeFunc the function to apply to the cells in the sub-graph without a cycle
   * @throws IllegalArgumentException if the key does not exist in the graph
   */
  void applyCycleTreeAncestors(K key,
      MapFunctionObject<V> cycleFunc, MapFunctionObject<V> treeFunc)
      throws IllegalArgumentException;
}
