package edu.cs3500.spreadsheets.model.formula.function;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.EvaluationError;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import edu.cs3500.spreadsheets.model.visitor.EvaluateFormula;
import java.util.List;

/**
 * A cell function for concatenating strings.
 */
public class ConcatCellFunction extends AbstractCellFunction {

  private List<Formula> formulas;

  /**
   * Creates a new cell function for concatenating strings.
   *
   * @param formulas the list of formulas in this cell function
   */
  ConcatCellFunction(List<Formula> formulas) {
    this.formulas = formulas;
  }

  @Override
  public CellValue<?> compute(EvaluateFormula visitor) {
    String output = "";
    for (Formula formula : this.formulas) {
      CellValue<?> cellValue = formula.accept(visitor);
      if (cellValue.isString()) {
        output = (output.concat((String) cellValue.getValue()));
        output = output.replace("\"", "");
      } else if (cellValue.isError()) {
        return new EvaluationError();
      }
    }
    return new StringCellValue(output);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof ConcatCellFunction
        && this.formulas.equals(((ConcatCellFunction) obj).formulas);
  }

  @Override
  public int hashCode() {
    return this.formulas.hashCode();
  }

  @Override
  public String toString() {
    return "CONCAT FUNCTION " + this.formulas.toString();
  }
}
