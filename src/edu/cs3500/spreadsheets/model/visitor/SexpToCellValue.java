package edu.cs3500.spreadsheets.model.visitor;

import edu.cs3500.spreadsheets.model.formula.value.BooleanCellValue;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.DivByZeroError;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Visitor that converts a s-expression to a cell value.
 */
public class SexpToCellValue implements SexpVisitor<CellValue<?>> {

  private String userString;

  /**
   * Constructor that takes in the string the user typed in.
   *
   * @param userString the string the user typed in
   */
  public SexpToCellValue(String userString) {

    this.userString = userString;
  }

  @Override
  public CellValue<?> visitBoolean(boolean b) {
    return new BooleanCellValue(b);
  }

  @Override
  public CellValue<?> visitNumber(double d) {
    return new NumberCellValue(d);
  }

  @Override
  public CellValue<?> visitSList(List<Sexp> l) {
    return new StringCellValue(userString);
  }

  @Override
  public CellValue<?> visitSymbol(String s) {

    if (isDouble(s)) {
      return new NumberCellValue(Double.parseDouble(s));
    } else if (Pattern.compile("^[0-9]+/[0-9]+$")
        .matcher(s).matches()) {
      String[] frac = s.split("/");
      if (Double.parseDouble(frac[1]) == 0) {
        return new DivByZeroError();
      } else {
        return new NumberCellValue(Double.parseDouble(frac[0]) / Double.parseDouble(frac[1]));
      }
    } else {
      return new StringCellValue(userString);
    }
  }

  @Override
  public CellValue<?> visitString(String s) {
    return new StringCellValue("\"" + s + "\"");
  }

  private boolean isDouble(String str) {
    try {
      return Double.isNaN(Double.parseDouble(str));
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
