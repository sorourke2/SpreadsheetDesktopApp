package edu.cs3500.spreadsheets.model.formula.function;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.value.BooleanCellValue;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.EvaluationError;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import java.util.List;

/**
 * A cell function for checking if one number is less than another.
 */
public class LessThanCellFunction extends AbstractCellFunction {

  private List<Formula> formulas;

  /**
   * Creates a new cell function for checking if one number is less than another.
   *
   * @param formulas the two numbers to compare
   */
  LessThanCellFunction(List<Formula> formulas) {
    this.formulas = formulas;
  }

  @Override
  public CellValue<?> compute(EvaluateFormula visitor) {
    int count = 0;
    double firstNumberValue = 0.0;
    double secondNumberValue = 0.0;
    for (Formula formula : this.formulas) {
      CellValue<?> cellValue = formula.accept(visitor);
      if (cellValue.isNumber()) {
        if (count == 0) {
          firstNumberValue = (Double) cellValue.getValue();
        } else if (count == 1) {
          secondNumberValue = (Double) cellValue.getValue();
        }
        count++;
      } else if (cellValue.isError()) {
        return new EvaluationError();
      }
    }

    if (count < 2) {
      return new EvaluationError();
    }

    return new BooleanCellValue(firstNumberValue < secondNumberValue);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof LessThanCellFunction
        && this.formulas.equals(((LessThanCellFunction) obj).formulas);
  }

  @Override
  public int hashCode() {
    return this.formulas.hashCode();
  }

  @Override
  public String toString() {
    return "LESS THAN " + this.formulas.toString();
  }
}
