package edu.cs3500.spreadsheets.model.visitor;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.function.CellFunctionFactory;
import edu.cs3500.spreadsheets.model.formula.reference.MultiReference;
import edu.cs3500.spreadsheets.model.formula.reference.SingleReference;
import edu.cs3500.spreadsheets.model.formula.value.BooleanCellValue;
import edu.cs3500.spreadsheets.model.formula.value.NumberCellValue;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A visitor that converts an s-expression to a formula.
 */
public class SexpToFormula implements SexpVisitor<Formula> {

  @Override
  public Formula visitBoolean(boolean b) {
    return new BooleanCellValue(b);
  }

  @Override
  public Formula visitNumber(double d) {
    return new NumberCellValue(d);
  }

  @Override
  public Formula visitSList(List<Sexp> l) {
    if (l.size() < 2) {
      // functions must take in at least one argument
      throw new IllegalArgumentException();
    } else {
      Sexp sexp = l.get(0);

      List<Formula> formulas = new ArrayList<>();
      for (int i = 1; i < l.size(); i++) {
        formulas.add(l.get(i).accept(this));
      }

      return CellFunctionFactory.create(sexp.toString(), formulas);
    }
  }

  @Override
  public Formula visitSymbol(String s) {

    if (Pattern.compile("^([A-Z]+)([1-9]\\d*)$")
        .matcher(s).matches()) {
      return new SingleReference(s);
    } else if (Pattern.compile("^([A-Z]+)([1-9]\\d*):([A-Z]+)([1-9]\\d*)$")
        .matcher(s).matches()) {
      return new MultiReference(s);
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override

  public Formula visitString(String s) {
    return new StringCellValue(s);
  }
}
