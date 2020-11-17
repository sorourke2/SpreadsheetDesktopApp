package edu.cs3500.spreadsheets.model.formula.function;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.EvaluationError;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import java.util.List;

/**
 * A cell function for multiplying numbers.
 */
public class ProductCellFunction extends AbstractCellFunction {

  private List<Formula> formulas;

  /**
   * Creates a new cell function for multiplying numbers.
   *
   * @param formulas the list of formulas in this cell function
   */
  ProductCellFunction(List<Formula> formulas) {
    this.formulas = formulas;
  }

  @Override
  public CellValue<?> compute(EvaluateFormula visitor) {
    double total = 0;
    boolean firstNumberValue = true;
    for (Formula formula : this.formulas) {
      CellValue<?> cellValue = formula.accept(visitor);
      if (cellValue.isNumber()) {
        // If there contains a numeric cell value in the list of formulas
        if (firstNumberValue) {
          total = 1;
          firstNumberValue = false;
        }
        total *= (Double) cellValue.getValue();
      } else if (cellValue.isError()) {
        return new EvaluationError();
      }
    }
    return new NumberCellValue(total);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ProductCellFunction
        && this.formulas.equals(((ProductCellFunction) obj).formulas);
  }

  @Override
  public int hashCode() {
    return this.formulas.hashCode();
  }

  @Override
  public String toString() {
    return "PRODUCT FUNCTION " + this.formulas.toString();
  }
}
