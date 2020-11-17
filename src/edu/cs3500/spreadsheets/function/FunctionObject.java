package edu.cs3500.spreadsheets.function;

/**
 * A function object.
 *
 * @param <I> input type
 * @param <O> output type
 */
public interface FunctionObject<I, O> {

  /**
   * Evaluates the object.
   *
   * @param obj the object
   * @return the evaluated value
   */
  O evaluate(I obj);
}
