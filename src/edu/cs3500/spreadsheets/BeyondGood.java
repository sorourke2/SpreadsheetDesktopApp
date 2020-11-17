package edu.cs3500.spreadsheets;

import static java.lang.Double.parseDouble;

import edu.cs3500.spreadsheets.controller.WorksheetController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ImmutableWorksheet;
import edu.cs3500.spreadsheets.model.ImmutableWorksheetInterface;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.builder.WorksheetBuilder;
import edu.cs3500.spreadsheets.model.builder.WorksheetReader;
import edu.cs3500.spreadsheets.view.AdvancedWorksheetView;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetView;
import edu.cs3500.spreadsheets.view.WorksheetViewInterface;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * The main class for our program.
 */
public class BeyondGood {

  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {

    // most of these blocks of code will eventually be moved to helper functions

    boolean in = false;
    String fileName = "";
    boolean eval = false;
    String cellCoord = "";
    boolean save = false;
    String newFileName = "";
    boolean gui = false;
    boolean edit = false;

    try {
      Set<String> evaluatedArgs = new HashSet<>();

      for (int i = 0; i < args.length; i++) {
        if (evaluatedArgs.contains(args[i])) {
          throw new IllegalArgumentException("Invalid command line argument.");
        }

        switch (args[i]) {
          case "-in":
            in = true;
            fileName = args[i + 1];
            break;
          case "-eval":
            eval = true;
            cellCoord = args[i + 1];
            break;
          case "-save":
            save = true;
            newFileName = args[i + 1];
            break;
          case "-gui":
            gui = true;
            break;
          case "-edit":
            edit = true;
            break;
          default:
            throw new IllegalArgumentException("Invalid command line argument: " + args[i]);
        }

        evaluatedArgs.add(args[i++]);
      }
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    Worksheet worksheet;
    if (in) {
      Readable file;
      try {
        file = new FileReader(fileName);
        worksheet = WorksheetReader.read(new WorksheetBuilder(), file);
      } catch (FileNotFoundException e) {
        System.out.println(e.getMessage());
        return;
      } catch (IllegalStateException | NullPointerException e) {
        System.out.println("Formatting errors within the file.");
        return;
      }
    } else {
      worksheet = new Worksheet.WorksheetConstructor().build();
    }

    ImmutableWorksheetInterface immutableWorksheet = new ImmutableWorksheet(worksheet);

    try {
      if (save) {
        if (newFileName.matches("^.*[.]txt$")) {
          PrintWriter writer = new PrintWriter(newFileName, StandardCharsets.UTF_8);
          WorksheetViewInterface view = new WorksheetTextualView(immutableWorksheet, writer);
          view.makeVisible();
          writer.close();
        } else {
          System.out.println("Invalid name for file to write to.");
          return;
        }
      }
    } catch (IOException e) {
      System.out.println("Error writing to file.");
      return;
    }

    WorksheetViewInterface view;

    try {
      if (gui && edit) {
        throw new IllegalArgumentException(
            "Cannot have both -gui and -edit command line arguments.");
      } else if (gui) {
        view = new WorksheetView(immutableWorksheet);
      } else if (edit) {
        view = new AdvancedWorksheetView(immutableWorksheet);
      } else {
        view = new WorksheetTextualView(immutableWorksheet, System.out);
      }
      view.makeVisible();
    } catch (IOException e) {
      System.out.println("Error rendering view.");
      return;
    }

    try {
      if (eval) {
        boolean error = false;
        for (Coord coord : worksheet.getFilledCells()) {
          if (worksheet.isCellError(coord.col, coord.row)) {
            System.out.print("Error in cell ");
            System.out.print(Coord.colIndexToName(coord.col));
            System.out.print(coord.row);
            System.out.print(": ");
            System.out.println(worksheet.getCellValue(coord.col, coord.row));

            error = true;
          }
        }
        if (!error) {
          printCellValue(Coord.strToCoord(cellCoord), worksheet);
        }
      }

      // set up controller
      WorksheetController controller = new WorksheetController();
      controller.go(worksheet, view);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Prints out the cell value of the cell represented by the given coordinate.
   *
   * @param coordinate A coordinate representing the given Cell's coordinates in the worksheet.
   * @param worksheet The Worksheet containing the given Cell.
   */
  private static void printCellValue(Coord coordinate, Worksheet worksheet) {
    String stringOutput = worksheet.getCellValue(coordinate.col, coordinate.row).toString();

    try {
      double numOutput = parseDouble(stringOutput);
      System.out.print(String.format("%f", numOutput));
    } catch (NumberFormatException e) {
      if (stringOutput.length() >= 2
          && stringOutput.charAt(0) == '"'
          && stringOutput.charAt(stringOutput.length() - 1) == '"') {
        stringOutput = stringOutput.replace("\\", "\\\\");
        stringOutput = stringOutput.substring(1, stringOutput.length() - 1).replace("\"", "\\\"");
        System.out.print("\"" + stringOutput + "\"");
      } else {
        System.out.print(stringOutput);
      }
    }
  }
}
