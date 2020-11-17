package edu.cs3500.spreadsheets.model.value;

/**
 * Represents a value.
 * @param <T> the type of the value
 */
public interface Value<T> {

  /**
   * Returns the value.
   * @return the value
   */
  T getValue();

  /**
   * Determines if this value is a string.
   * @return true if this value is a string
   */
  boolean isString();

  /**
   * Determines if this value is a number.
   * @return true if this value is a number
   */
  boolean isNumber();

  /**
   * Determines if this value is a boolean.
   * @return true if this value is a boolean
   */
  boolean isBoolean();
}
