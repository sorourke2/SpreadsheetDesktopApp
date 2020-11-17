package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.formula.Formula;
import edu.cs3500.spreadsheets.model.formula.value.CellValue;
import edu.cs3500.spreadsheets.model.formula.value.ParseSexpError;
import edu.cs3500.spreadsheets.model.formula.value.StringCellValue;
import edu.cs3500.spreadsheets.model.visitor.SexpToCellValue;
import edu.cs3500.spreadsheets.model.visitor.SexpToFormula;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * A factory for creating cells.
 */
public interface CellFactory {

  /**
   * Creates a cell object from the string the user typed into the cell.
   *
   * @param userString the string the user typed into the cell
   * @return a cell object
   */
  public static Cell create(String userString) {
    if (userString == null) {
      throw new IllegalArgumentException();
    } else {
      if (userString.length() <= 0 || userString.charAt(0) != '=') {
        try {
          Sexp sexp = Parser.parse(userString.trim());
          SexpVisitor<CellValue<?>> visitor = new SexpToCellValue(userString);
          CellValue<?> cellValue = sexp.accept(visitor);
          return new PrimitiveCell(userString, cellValue);
        } catch (IllegalArgumentException e) {
          // because of dumb starter code not parsing the string "=asdf" properly
          return new PrimitiveCell(userString, new StringCellValue(userString));
        }
      } else {
        try {
          userString = userString.replace("\\", "\\\\");
          Sexp sexp = Parser.parse(userString.substring(1, userString.length()));
          SexpVisitor<Formula> visitor = new SexpToFormula();
          try {
            Formula formula = sexp.accept(visitor);
            return new FormulaCell(userString, formula);
          } catch (IllegalArgumentException e) {
            return new FormulaCell(userString, new ParseSexpError());
          }
        } catch (IllegalArgumentException e) {
          return new FormulaCell(userString, new ParseSexpError());
        }
      }
    }
  }
}
