package edu.cs3500.spreadsheets.function;

/**
 * A function object that alters the input in place.
 *
 * @param <I> the type of the input
 */
public interface MapFunctionObject<I> {

  /**
   * Applies some function to the object in place.
   *
   * @param obj the object
   */
  void apply(I obj);
}
