package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ImmutableWorksheetInterface;
import edu.cs3500.spreadsheets.model.Worksheet;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;
import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

/**
 * A gui view of a worksheet consisting of just the cells. Allows the user to view all the cells in
 * the spreadsheet through paging through pages. Each page allows scrolling and consists of a set
 * amount of rows and columns.
 */
public class WorksheetView extends JFrame implements WorksheetViewInterface, PagingListener {

  private ImmutableWorksheetInterface worksheet;
  private JTable table;
  private int selectedRow;
  private int selectedColumn;
  private TableListener listener;
  private PagingModel pagingModel;

  /**
   * Constructs a gui with an immutable worksheet. Any cell selection events are ignored.
   *
   * @param worksheet the worksheet
   */
  public WorksheetView(ImmutableWorksheetInterface worksheet) {
    this(worksheet, new TableListener() {
      @Override
      public void cellSelected(int row, int column) {
        // should ignore cell selected events
      }

      @Override
      public void cellContentsDeleted(int row, int column) {
        // should ignore cell contents deleted events
      }
    });
  }

  /**
   * Constructs a gui with an immutable worksheet and a {@code TableListener} and listens to when a
   * new cell is selected in the table.
   *
   * @param worksheet the worksheet
   * @param listener the event listener
   */
  public WorksheetView(ImmutableWorksheetInterface worksheet, TableListener listener) {
    super();
    this.worksheet = worksheet;
    this.selectedRow = 0;
    this.selectedColumn = 0;
    this.listener = listener;

    this.setTitle("Spreadsheet");
    this.setSize(1024, 576);
    this.initTable();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public WorksheetView(ImmutableWorksheetInterface worksheet, PagingModel listener) {
    super();
    this.worksheet = worksheet;
    this.selectedRow = 0;
    this.selectedColumn = 0;
    this.pagingModel = listener;

    this.setTitle("Spreadsheet");
    this.setSize(1024, 576);
    this.initTable();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }


  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void addFeatures(Features features) {
    // no features to add to this view
  }

  @Override
  public void refresh(Set<Coord> coords) {
    // update the text in cells with the given coordinates
    for (Coord coord : coords) {
      if (coord.col > this.pagingModel.getColumnPageSize() * this.pagingModel.getColumnPageOffset()
          && coord.col < this.pagingModel.getColumnPageSize() * (
          this.pagingModel.getColumnPageOffset() + 1)
          && coord.row > this.pagingModel.getRowPageSize() * this.pagingModel.getRowPageOffset()
          && coord.row < this.pagingModel.getRowPageSize() * (this.pagingModel.getRowPageOffset()
          + 1)) {
        this.table.getModel().setValueAt(this.worksheet.getCellValue(coord.col, coord.row),
            coord.row - 1 - this.pagingModel.getRowPageSize() * this.pagingModel.getRowPageOffset(),
            coord.col - 1 - this.pagingModel.getColumnPageSize() * this.pagingModel
                .getColumnPageOffset());
      }
    }

    // visually update the table
    ((AbstractTableModel) this.table.getModel()).fireTableDataChanged();
    // reselected the previously selected cell
    if (this.selectedRow > 0 && this.selectedColumn > 0) {
      this.table.setRowSelectionInterval(this.selectedRow - 1, this.selectedRow - 1);
      this.table.setColumnSelectionInterval(this.selectedColumn - 1, this.selectedColumn - 1);
    }
  }

  @Override
  public void pageChanged() {
    this.listener.cellSelected(0, 0);
  }

  /**
   * Initializes the table containing the cells.
   */
  private void initTable() {
    this.pagingModel = new PagingModel(
        this.worksheet, this, 100, 26);
    this.table = new JTable(this.pagingModel);

    // when a cell is selected, we do not want to select the entire row
    this.table.setRowSelectionAllowed(false);
    // cells should not auto resize
    // not having this line causes the rendering of cells to be very messed up
    this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    // user should not able to move columns around
    this.table.getTableHeader().setReorderingAllowed(false);

    this.table.setCellSelectionEnabled(true);
    this.table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.table.getColumnModel().getSelectionModel()
        .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JScrollPane jScrollPane = this.pagingModel.createPagingScrollPane(this.table);

    JList<String> rowHeader = new JList<>(new AbstractListModel<>() {
      @Override
      public int getSize() {
        return worksheet.getRows();
      }

      @Override
      public String getElementAt(int index) {
        // the labels for the row headers
        return Integer.toString(index + 1);
      }
    });

    rowHeader.setFixedCellWidth(50);
    // row headers should have same height as rest of row
    rowHeader.setFixedCellHeight(this.table.getRowHeight());
    // customizes what the row headers look like
    rowHeader.setCellRenderer(new RowHeaderRenderer(this.table));
    // adds row headers
    jScrollPane.setRowHeaderView(rowHeader);

    super.getContentPane().add(jScrollPane, BorderLayout.CENTER);

    // set up event listener for when ne cell is selected
    this.table.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
          // escape key should deselect any selected cell(s)
          WorksheetView.this.table.clearSelection();
          WorksheetView.this.triggerListenerIfNewSelection(-1, -1);
        } else if (e.getKeyChar() == KeyEvent.VK_DELETE
            || e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
          // delete and backspace key should delete anything in the cell
          int row = WorksheetView.this.table.getSelectedRow() + 1
              + WorksheetView.this.pagingModel.getRowPageOffset() * WorksheetView.this.pagingModel
              .getRowPageSize();
          int column = WorksheetView.this.table.getSelectedColumn() + 1
              + WorksheetView.this.pagingModel.getColumnPageOffset()
              * WorksheetView.this.pagingModel.getColumnPageSize();
          WorksheetView.this.listener.cellContentsDeleted(row, column);
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // nothing to listen for for key presses
      }

      @Override
      public void keyReleased(KeyEvent e) {
        WorksheetView.this.triggerListenerIfNewSelection(
            table.getSelectedRow(), table.getSelectedColumn());
      }
    });

    table.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {

      }

      @Override
      public void mousePressed(MouseEvent e) {
        // listening for mouse clicked
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        WorksheetView.this.triggerListenerIfNewSelection(
            table.getSelectedRow(), table.getSelectedColumn());
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        // listening for mouse clicked
      }

      @Override
      public void mouseExited(MouseEvent e) {
        // listening for mouse clicked
      }
    });
  }

  /**
   * Triggers the {@code TableListener} if a new cell is selected.
   *
   * @param row the row with starting index 0
   * @param column the column with starting index 0
   */
  private void triggerListenerIfNewSelection(int row, int column) {
    int shiftedRow =
        row + 1 + this.pagingModel.getRowPageSize() * this.pagingModel.getRowPageOffset();
    int shiftedColumn =
        column + 1 + this.pagingModel.getColumnPageSize() * this.pagingModel.getColumnPageOffset();
    if (!(this.selectedRow == shiftedRow && this.selectedColumn == shiftedColumn)
        && shiftedRow > 0 && shiftedColumn > 0) {
      this.selectedRow =
          shiftedRow - this.pagingModel.getRowPageSize() * this.pagingModel.getRowPageOffset();
      this.selectedColumn = shiftedColumn - this.pagingModel.getColumnPageSize() * this.pagingModel
          .getColumnPageOffset();
      this.listener.cellSelected(shiftedRow, shiftedColumn);
    }
  }
}