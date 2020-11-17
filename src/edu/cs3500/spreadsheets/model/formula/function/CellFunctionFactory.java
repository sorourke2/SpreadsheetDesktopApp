package edu.cs3500.spreadsheets.model.formula.function;

import edu.cs3500.spreadsheets.function.FunctionObject;
import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.reference.MultiReference;
import edu.cs3500.spreadsheets.model.formula.reference.SingleReference;
import edu.cs3500.spreadsheets.model.formula.value.ParseSexpError;
import edu.cs3500.spreadsheets.model.function.MultiToSingle;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory for creating cell functions from the name of the function and the formulas in the
 * function.
 */
public interface CellFunctionFactory {

  /**
   * Creates a new cell function object.
   *
   * @param name the name of the function
   * @param formulas the formulas in the function
   * @return a new cell function object
   */
  public static Formula create(String name, List<Formula> formulas) {
    if (formulas.size() == 0) {
      return new ParseSexpError();
    }

    switch (name.toUpperCase()) {
      case "SUM":
        return new SumCellFunction(flatten(formulas));
      case "PRODUCT":
        return new ProductCellFunction(flatten(formulas));
      case "<":
        int count = 0;
        List<Formula> flatFormulas = new ArrayList<>();
        FunctionObject<MultiReference, List<SingleReference>> func = new MultiToSingle();
        for (Formula formula : formulas) {
          count++;
          if (count > 2 || formula.isMultiRef()) {
            return new ParseSexpError();
          } else {
            flatFormulas.add(formula);
          }
        }
        if (count != 2) {
          return new ParseSexpError();
        }
        return new LessThanCellFunction(flatFormulas);
      case "CONCAT":
        return new ConcatCellFunction(flatten(formulas));
      default:
        return new ParseSexpError();
    }
  }

  /**
   * Turns any multi-cell references in the list to a list of single-cell references, inserted where
   * the original multi-cell reference was in the list.
   *
   * @param formulas the list of formulas
   * @return a list of formulas without multi-cell references
   */
  private static List<Formula> flatten(List<Formula> formulas) {
    List<Formula> flatFormulas = new ArrayList<>();
    FunctionObject<MultiReference, List<SingleReference>> func = new MultiToSingle();
    for (Formula formula : formulas) {
      if (formula.isMultiRef()) {
        flatFormulas.addAll(func.evaluate((MultiReference) formula));
      } else {
        flatFormulas.add(formula);
      }
    }
    return flatFormulas;
  }
}
