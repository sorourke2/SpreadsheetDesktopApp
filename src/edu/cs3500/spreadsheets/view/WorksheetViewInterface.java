package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import java.io.IOException;
import java.util.Set;

/**
 * An interface describing the view for a worksheet.
 */
public interface WorksheetViewInterface {

  /**
   * Makes the view visible.
   * @throws IOException if view has error appending to an appendable
   */
  void makeVisible() throws IOException;

  /**
   * Adds a set of features to the view to be handled.
   * @param features the features
   */
  void addFeatures(Features features);

  /**
   * Refreshes the cells at the given coordinates.
   * @param coords the coordinates
   */
  void refresh(Set<Coord> coords);
}
