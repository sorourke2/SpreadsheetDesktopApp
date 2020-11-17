package edu.cs3500.spreadsheets.model.function;

import edu.cs3500.spreadsheets.function.FunctionObject;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.formula.reference.MultiReference;
import edu.cs3500.spreadsheets.model.formula.reference.SingleReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A function object that turns a multi-cell reference into a list of single-cell references.
 */
public class MultiToSingle implements FunctionObject<MultiReference, List<SingleReference>> {

  @Override
  public List<SingleReference> evaluate(MultiReference obj) {
    String cellsRegex = "^([A-Z]+)([1-9]\\d*):([A-Z]+)([1-9]\\d*)$";
    Pattern pattern = Pattern.compile(cellsRegex);
    Matcher matcher = pattern.matcher(obj.getCellRef());

    if (!matcher.matches()) {
      // other possible errors are not checked for here, just this simple one
      throw new IllegalStateException("Invalid multi-reference when converting from "
          + "multi-reference to list of single references.");
    } else {
      int initialColumn = Coord.colNameToIndex(matcher.group(1));
      int initialRow = Integer.parseInt(matcher.group(2));
      int finalColumn = Coord.colNameToIndex(matcher.group(3));
      int finalRow = Integer.parseInt(matcher.group(4));

      List<SingleReference> list = new ArrayList<>();

      for (int col = initialColumn; col <= finalColumn; col++) {
        for (int row = initialRow; row <= finalRow; row++) {
          String cell = Coord.colIndexToName(col) + String.valueOf(row);
          list.add(new SingleReference(cell));
        }
      }

      return list;
    }
  }
}
