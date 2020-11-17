package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ImmutableWorksheet;
import edu.cs3500.spreadsheets.model.ImmutableWorksheetInterface;
import edu.cs3500.spreadsheets.model.WorksheetInterface;
import java.io.IOException;
import java.util.Set;

/**
 * A view for the worksheet using text.
 */
public class WorksheetTextualView implements WorksheetViewInterface {

  private ImmutableWorksheetInterface model;
  private Appendable appendable;

  /**
   * Constructs an instance of a textual view.
   * @param model      the model
   * @param appendable the object to display to
   */
  public WorksheetTextualView(ImmutableWorksheetInterface model, Appendable appendable) {
    if (model == null || appendable == null) {
      throw new IllegalArgumentException("Arguments must be non-null.");
    }
    this.model = model;
    this.appendable = appendable;
  }

  @Override
  public void makeVisible() throws IOException {
    for (Coord coord : this.model.getFilledCells()) {
      this.appendable.append(coord.toString());
      this.appendable.append(" ");
      this.appendable.append(this.model.getUserString(coord.col, coord.row));
      this.appendable.append("\n");
    }
  }

  @Override
  public void addFeatures(Features features) {
    // features have no affect on the textual view since there is no interaction with this view
  }

  @Override
  public void refresh(Set<Coord> coords) {
    // nothing to refresh in a textual view
  }
}
