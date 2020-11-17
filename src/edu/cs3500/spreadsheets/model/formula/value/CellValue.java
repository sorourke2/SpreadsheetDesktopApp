package edu.cs3500.spreadsheets.model.formula.value;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import edu.cs3500.spreadsheets.model.visitor.FormulaVisitor;

/**
 * The value of a cell. Provides default implementations for methods checking which type of formula
 * this is, and for throwing an error on unsupported methods.
 */
public interface CellValue<T> extends Formula {

  /**
   * Returns the value.
   *
   * @return the value
   */
  T getValue();

  /**
   * Determines if this value is a number.
   *
   * @return true if this value is a number
   */
  boolean isNumber();

  /**
   * Determines if this value is a String.
   *
   * @return true if this value is a String
   */
  boolean isString();

  /**
   * Determines if this value is a boolean.
   *
   * @return true if this value is a boolean
   */
  boolean isBoolean();

  /**
   * Determines if this value is an error.
   *
   * @return true if this value is an error
   */
  boolean isError();


  @Override
  default CellValue<?> accept(FormulaVisitor visitor) {
    return visitor.visit(this);
  }

  @Override
  default CellValue<?> compute(EvaluateFormula visitor) {
    throw new UnsupportedOperationException();
  }

  @Override
  default String getCellRef() {
    throw new UnsupportedOperationException();
  }

  @Override
  default boolean isMultiRef() {
    return false;
  }

  @Override
  default boolean isFunction() {
    return false;
  }

  @Override
  default boolean isReference() {
    return false;
  }

  @Override
  default boolean isCellValue() {
    return true;
  }
}
