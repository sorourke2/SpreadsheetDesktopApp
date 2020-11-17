import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

import edu.cs3500.spreadsheets.model.ImmutableWorksheet;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.builder.WorksheetBuilder;
import edu.cs3500.spreadsheets.model.builder.WorksheetReader;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetViewInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.Test;

/**
 * Tests the textual view of the worksheet.
 */
public class WorksheetTextualViewTest {

  /**
   * Tests the given input.
   *
   * @param model the model
   * @param interactions the interactions with the model
   * @throws IOException if appending to the appendable fails
   */
  private void testInput(Worksheet model, Interaction... interactions) throws IOException {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    for (Interaction interaction : interactions) {
      interaction.apply(fakeUserInput, expectedOutput);
    }
    StringBuilder actualOutput = new StringBuilder();

    WorksheetViewInterface textualView = new WorksheetTextualView(new ImmutableWorksheet(model),
        actualOutput);

    textualView.makeVisible();

    assertEquals(actualOutput.toString(), expectedOutput.toString());
  }

  /**
   * Tests the view with an inputted file and checks that it is equal to the interactions.
   * @param model        the worksheet model
   * @param file         the file to write to
   * @param interactions the list of interactions
   * @throws IOException if there is an error writing to the file
   */
  private void testFileInput(Worksheet model, File file, Interaction... interactions)
      throws IOException {
    StringBuilder fakeUserInput = new StringBuilder();
    StringBuilder expectedOutput = new StringBuilder();

    for (Interaction interaction : interactions) {
      interaction.apply(fakeUserInput, expectedOutput);
    }

    PrintWriter printWriter = new PrintWriter(file);

    WorksheetViewInterface textualView = new WorksheetTextualView(new ImmutableWorksheet(model),
        printWriter);
    textualView.makeVisible();
    printWriter.close();
    StringBuilder fileOne = new StringBuilder();

    FileReader fileReader = new FileReader(file);

    try (BufferedReader br = new BufferedReader(fileReader)) {

      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) {
        fileOne.append(sCurrentLine).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertEquals(fileOne.toString(), expectedOutput.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorModelNull() {
    WorksheetViewInterface wsView = new WorksheetTextualView(null, new StringBuilder());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorAppendableNull() {
    WorksheetBuilder ws = new WorksheetBuilder();
    Worksheet worksheet = ws.createWorksheet();
    WorksheetViewInterface wsView = new WorksheetTextualView(new ImmutableWorksheet(worksheet),
        null);
  }

  @Test
  public void testVeryBasic() {

    WorksheetBuilder ws = new WorksheetBuilder();
    ws.createCell(1, 1, "2");
    ws.createCell(2, 1, "3");
    ws.createCell(3, 1, "4");

    Worksheet worksheet = ws.createWorksheet();
    assertNotNull(worksheet);

    try {
      testInput(worksheet,
          Interaction.prints("A1 2\n"),
          Interaction.prints("B1 3\n"),
          Interaction.prints("C1 4\n")
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = IOException.class)
  public void testIOException() throws IOException {

    WorksheetBuilder ws = new WorksheetBuilder();
    ws.createCell(1, 1, "1");

    Worksheet worksheet = ws.createWorksheet();
    File f = new File("");
    Appendable ap = new FileWriter(f);
    WorksheetViewInterface textualView
        = new WorksheetTextualView(new ImmutableWorksheet(worksheet), ap);
  }


  @Test
  public void testComplexSheetView() {

    WorksheetBuilder ws = new WorksheetBuilder();

    ws.createCell(1, 1, "3.0");
    ws.createCell(1, 2,
        "=(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0)))");
    ws.createCell(1, 3, "=(SUM A2 B2)");
    ws.createCell(2, 1, "4.0");
    ws.createCell(2, 2, "=(PRODUCT (SUM D1 (PRODUCT B1 -1.0)) (SUM D1 (PRODUCT B1 -1.0)))");
    ws.createCell(2, 3, "=(< A3 100.0)");
    ws.createCell(3, 1, "9.0");
    ws.createCell(4, 1, "12.0");

    Worksheet worksheet = ws.createWorksheet();
    assertNotNull(worksheet);

    try {
      testInput(worksheet,
          Interaction
              .prints("A2 =(PRODUCT (SUM C1 (PRODUCT A1 -1.0)) (SUM C1 (PRODUCT A1 -1.0)))\n"),
          Interaction.prints("B3 =(< A3 100.0)\n"),
          Interaction.prints("A1 3.0\n"),
          Interaction
              .prints("B2 =(PRODUCT (SUM D1 (PRODUCT B1 -1.0)) (SUM D1 (PRODUCT B1 -1.0)))\n"),
          Interaction.prints("B1 4.0\n"),
          Interaction.prints("C1 9.0\n"),
          Interaction.prints("D1 12.0\n"),
          Interaction.prints("A3 =(SUM A2 B2)\n")
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCreateMoreComplexCellsView() {

    WorksheetBuilder ws = new WorksheetBuilder();

    /* A1: 2
     * A3: =(CONCAT A5 A4 A6)
     * A4:"second"
     * A5:"first"
     * A6:"third"
     * B3: =(SUM A1 C2)
     * B6: =3
     * C1: =(SUM 1 D1:E2)
     * C2: 3
     * C4: =(< 1 D1:E2)
     * C5: =(< 1 2 3)
     * C6: =(PRODUCT (SUM B3 A1) B6)
     * D1: 1
     * D2: 1
     * D3: =(< B6 4)
     * D5: =(PRODUCT B3 A1)
     * E1: 1
     * E2: 1
     * E3: =(SUM (SUM B3 A1) A1)
     * E4: =(SUM 1 C5)
     * E5: =(SUM E6 5)
     * E6: =(SUM E5 6)
     */

    ws.createCell(1, 1, "2");
    ws.createCell(1, 3, "=(CONCAT A5 A4 A6)");
    ws.createCell(1, 4, "second");
    ws.createCell(1, 5, "first");
    ws.createCell(1, 6, "third");
    ws.createCell(2, 3, "=(SUM A1 C2)");
    ws.createCell(2, 6, "3");
    ws.createCell(3, 1, "=(SUM 1 D1:E2)");
    ws.createCell(3, 2, "3");
    ws.createCell(3, 4, "=(< 1 D1:E2)");
    ws.createCell(3, 5, "=(< 1 2 3)");
    ws.createCell(3, 6, "=(PRODUCT (SUM B3 A1) B6)");
    ws.createCell(4, 1, " 1");
    ws.createCell(4, 2, " 1");
    ws.createCell(4, 3, "=(< B6 4)");
    ws.createCell(4, 5, "=(PRODUCT B3 A1)");
    ws.createCell(5, 1, "1");
    ws.createCell(5, 2, "1");
    ws.createCell(5, 3, "=(SUM (SUM B3 A1) A1)");
    ws.createCell(5, 4, "=(SUM 1 C5)");
    ws.createCell(5, 5, "=(SUM E6 5)");
    ws.createCell(5, 6, "=(SUM E5 6)");
    Worksheet worksheet = ws.createWorksheet();
    assertNotNull(worksheet);

    try {
      testInput(worksheet,
          Interaction.prints("D5 =(PRODUCT B3 A1)\n"
              + "E6 =(SUM E5 6)\n"
              + "B3 =(SUM A1 C2)\n"
              + "C4 =(< 1 D1:E2)\n"
              + "E5 =(SUM E6 5)\n"
              + "A1 2\n"
              + "C2 3\n"
              + "D3 =(< B6 4)\n"
              + "E4 =(SUM 1 C5)\n"
              + "D2  1\n"
              + "E3 =(SUM (SUM B3 A1) A1)\n"
              + "C1 =(SUM 1 D1:E2)\n"
              + "E2 1\n"
              + "D1  1\n"
              + "E1 1\n"
              + "A6 third\n"
              + "A5 first\n"
              + "B6 3\n"
              + "C6 =(PRODUCT (SUM B3 A1) B6)\n"
              + "A4 second\n"
              + "C5 =(< 1 2 3)\n"
              + "A3 =(CONCAT A5 A4 A6)\n")
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testNoCells() {
    WorksheetBuilder ws = new WorksheetBuilder();
    Worksheet worksheet = ws.createWorksheet();
    assertNotNull(worksheet);

    try {
      testInput(worksheet,
          Interaction.prints("")
      );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSavingAndLoadingCreatesSameFile() {

    WorksheetBuilder ws = new WorksheetBuilder();
    ws.createCell(1, 1, "2");
    ws.createCell(2, 1, "3");
    ws.createCell(3, 1, "4");

    Worksheet worksheet = ws.createWorksheet();

    try {
      File f = new File("test/testFileInput.txt");

      // Run the view, test that it creates appendable equivalent to the interactions
      testFileInput(worksheet, f, Interaction.prints("A1 2\n"),
          Interaction.prints("B1 3\n"),
          Interaction.prints("C1 4\n"));

      WorksheetBuilder builder = new WorksheetBuilder();
      // Create another worksheet with the output of the previous textualView
      Worksheet worksheetTwo = WorksheetReader.read(builder, new FileReader(f));

      // Run the second view with the new model,
      // test that it creates appendable equivalent to the same interactions as above.
      testInput(worksheetTwo, Interaction.prints("A1 2\n"),
          Interaction.prints("B1 3\n"),
          Interaction.prints("C1 4\n"));

      assertEquals(worksheet, worksheetTwo);
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }
}