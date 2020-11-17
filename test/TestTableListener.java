import static junit.framework.TestCase.assertEquals;

import edu.cs3500.spreadsheets.controller.WorksheetController;
import edu.cs3500.spreadsheets.model.ImmutableWorksheet;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.builder.WorksheetBuilder;
import edu.cs3500.spreadsheets.view.AdvancedWorksheetView;
import edu.cs3500.spreadsheets.view.MockTableListener;
import edu.cs3500.spreadsheets.view.PagingModel;
import edu.cs3500.spreadsheets.view.WorksheetView;
import java.io.IOException;
import org.junit.Test;

public class TestTableListener {


  @Test
  public void testListenerWithControllerAdvanced() {

    Worksheet ws = new WorksheetBuilder().createWorksheet();
    StringBuilder ap = new StringBuilder();
    MockTableListener mock = new MockTableListener(ap);
    ImmutableWorksheet im = new ImmutableWorksheet(ws);
    AdvancedWorksheetView advancedView = new AdvancedWorksheetView(im, mock);
    WorksheetController c = new WorksheetController();
    c.go(ws, advancedView);
    c.cellContentsDeleted(1, 1);
    c.cellContentsDeleted(2, 2);

    assertEquals(ap.toString(), new StringBuilder("cellContentsDeleted(1, 1)\n"
        + "cellContentsDeleted(2, 2)\n").toString());

  }

  @Test
  public void testListenerWithControllerBasic() {

    Worksheet ws = new WorksheetBuilder().createWorksheet();
    StringBuilder ap = new StringBuilder();
    MockTableListener mock = new MockTableListener(ap);
    ImmutableWorksheet im = new ImmutableWorksheet(ws);
    WorksheetView view = new WorksheetView(im, mock);
    WorksheetController c = new WorksheetController();
    c.go(ws, view);
    c.cellContentsDeleted(1, 1);
    c.cellContentsDeleted(2, 2);

    assertEquals(ap.toString(), new StringBuilder("cellContentsDeleted(1, 1)\n"
        + "cellContentsDeleted(2, 2)\n").toString());

  }

  @Test
  public void testListenerWithAdvanced() {

    WorksheetBuilder wsb =
        new WorksheetBuilder().createCell(1, 1, "A1");
    Worksheet ws = wsb.createWorksheet();
    StringBuilder ap = new StringBuilder();
    MockTableListener mock = new MockTableListener(ap);
    ImmutableWorksheet im = new ImmutableWorksheet(ws);
    AdvancedWorksheetView advancedView = new AdvancedWorksheetView(im, mock);

    try {
      advancedView.makeVisible();
    } catch (IOException e) {
      e.printStackTrace();
    }

    advancedView.cellSelected(1, 1);
    advancedView.addFeatures(new WorksheetController());
    advancedView.cellContentsDeleted(1, 1);
    assertEquals(ap.toString(), new StringBuilder("cellselected(1, 1)\n"
        + "cellContentsDeleted(1, 1)\n").toString());

  }






  @Test
  public void testListenerWithBasic() {

    WorksheetBuilder wsb =
        new WorksheetBuilder().createCell(1, 1, "A1");
    Worksheet ws = wsb.createWorksheet();
    StringBuilder ap = new StringBuilder();
    MockTableListener mock = new MockTableListener(ap);
    ImmutableWorksheet im = new ImmutableWorksheet(ws);
    WorksheetView view = new WorksheetView(im, mock);

      view.makeVisible();

    view.addFeatures(new WorksheetController());
    view.pageChanged();
    view.pageChanged();
    view.pageChanged();
    view.pageChanged();
    assertEquals(ap.toString(), new StringBuilder("cellselected(0, 0)\n"
        + "cellselected(0, 0)\n"
        + "cellselected(0, 0)\n"
        + "cellselected(0, 0)\n").toString());

  }


}

