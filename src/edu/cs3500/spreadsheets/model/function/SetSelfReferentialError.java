package edu.cs3500.spreadsheets.model.function;

import edu.cs3500.spreadsheets.function.MapFunctionObject;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.formula.value.SelfReferentialError;

/**
 * A function object for the cell value to a self referential error.
 */
public class SetSelfReferentialError implements MapFunctionObject<Cell> {

  @Override
  public void apply(Cell obj) {
    obj.setError(new SelfReferentialError());
  }
}
