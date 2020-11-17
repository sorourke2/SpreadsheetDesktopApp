package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ImmutableWorksheetInterface;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

/**
 * An {@code AbstractTableModel} that supports paging through the data. Only a set number of rows
 * and columns are shown at once in the table.
 */
public class PagingModel extends AbstractTableModel implements PagingInterface {

  private ImmutableWorksheetInterface worksheet;
  private PagingListener listener;

  private int rowPageSize;
  private int rowPageOffset;

  private int columnPageSize;
  private int columnPageOffset;

  /**
   * Constructor that takes in an immutable worksheet and the size of each page. This determines how
   * much of the data is visible at once without paging.
   *
   * @param worksheet the worksheet
   * @param rowPageSize the number of rows per page
   * @param columnPageSize the number of columns per page
   */
  PagingModel(ImmutableWorksheetInterface worksheet, PagingListener listener, int rowPageSize, int columnPageSize) {
    this.worksheet = worksheet;
    this.listener = listener;
    this.rowPageSize = rowPageSize;
    this.columnPageSize = columnPageSize;
    // initially we are on page (0, 0)
    this.rowPageOffset = 0;
    this.columnPageOffset = 0;
  }

  @Override
  public void pageDown() {
    rowPageOffset++;
    listener.pageChanged();
    super.fireTableDataChanged();
  }

  @Override
  public void pageUp() {
    if (rowPageOffset > 0) {
      rowPageOffset--;
      listener.pageChanged();
      super.fireTableDataChanged();
    }
  }

  @Override
  public void pageLeft() {
    if (columnPageOffset > 0) {
      columnPageOffset--;
      listener.pageChanged();
      super.fireTableDataChanged();
    }
  }

  @Override
  public void pageRight() {
    columnPageOffset++;
    listener.pageChanged();
    super.fireTableDataChanged();
  }

  @Override
  public int getRowPageOffset() {
    return this.rowPageOffset;
  }

  @Override
  public int getColumnPageOffset() {
    return this.columnPageOffset;
  }

  @Override
  public int getRowPageSize() {
    return this.rowPageSize;
  }

  @Override
  public int getColumnPageSize() {
    return this.columnPageSize;
  }

  @Override
  public JScrollPane createPagingScrollPane(JTable jTable) {
    JScrollPane jScrollPane = new JScrollPane(jTable);

    final JButton upButton = new JButton();
    upButton.setEnabled(false);
    final JButton downButton = new JButton();

    upButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        PagingModel.this.pageUp();
        if (PagingModel.this.rowPageOffset == 0) {
          // disables paging up if we are at the top of the table
          upButton.setEnabled(false);
        }
        // need to update row headers when we page up
        PagingModel.this.updateRowHeader(jTable, jScrollPane);
      }
    });

    downButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        PagingModel.this.pageDown();
        // re-enables paging in case we were at the top of the table
        upButton.setEnabled(true);
        // need to update row headers when we page down
        PagingModel.this.updateRowHeader(jTable, jScrollPane);
      }
    });

    final JButton leftButton = new JButton();
    leftButton.setEnabled(false);
    final JButton rightButton = new JButton();

    leftButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        PagingModel.this.pageLeft();
        if (PagingModel.this.columnPageOffset == 0) {
          // disables paging up if we are at the left edge of the table
          leftButton.setEnabled(false);
        }
        // need to update column headers when we page left
        PagingModel.this.updateColumnHeader(jTable, jScrollPane);
      }
    });

    rightButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        PagingModel.this.pageRight();
        // re-enables paging in case we were at the left edge of the table
        leftButton.setEnabled(true);
        // need to update column headers when we page right
        PagingModel.this.updateColumnHeader(jTable, jScrollPane);
      }
    });

    // the buttons for paging left and right are together
    final JPanel bottomLeftButtons = new JPanel();
    bottomLeftButtons.setLayout(new GridLayout(0, 2));
    bottomLeftButtons.add(leftButton);
    bottomLeftButtons.add(rightButton);

    // scroll bars should always be visible
    jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    // up button in upper right corner
    // down button in lower right corner
    // left and right buttons are side by side in the bottom left corner
    jScrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, upButton);
    jScrollPane.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, downButton);
    jScrollPane.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, bottomLeftButtons);

    // enable the use of 'wasd' to efficiently move around when the table has focus
    // there is some repetitive code but this seems cleaner than using a switch statement
    // that was run whenever any key was pressed
    jTable.getInputMap().put(KeyStroke.getKeyStroke("W"), "page up");
    jTable.getInputMap().put(KeyStroke.getKeyStroke("A"), "page left");
    jTable.getInputMap().put(KeyStroke.getKeyStroke("S"), "page down");
    jTable.getInputMap().put(KeyStroke.getKeyStroke("D"), "page right");

    jTable.getActionMap().put("page up", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (jTable.hasFocus()) {
          upButton.doClick();
        }
      }
    });
    jTable.getActionMap().put("page left", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (jTable.hasFocus()) {
          leftButton.doClick();
        }
      }
    });
    jTable.getActionMap().put("page down", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (jTable.hasFocus()) {
          downButton.doClick();
        }
      }
    });
    jTable.getActionMap().put("page right", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (jTable.hasFocus()) {
          rightButton.doClick();
        }
      }
    });

    // add images to buttons
    leftButton.setText("<");
    leftButton.setMargin(new Insets(0, 0, 0, 0));
    rightButton.setText(">");
    rightButton.setMargin(new Insets(0, 0, 0, 0));
    upButton.setText("^");
    upButton.setMargin(new Insets(0, 0, 0, 0));
    downButton.setText("v");
    downButton.setMargin(new Insets(0, 0, 0, 0));

    return jScrollPane;
  }

  @Override
  public int getRowCount() {
    // the number of rows in the table is the number of rows in each page
    return this.rowPageSize;
  }

  @Override
  public int getColumnCount() {
    // the number of columns in the table is the number of columns in each page
    return this.columnPageSize;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    // the value in each cell in the table is the cell value of the corresponding cell in the
    // worksheet
    return this.worksheet
        .getCellValue(columnIndex + 1 + this.columnPageOffset * this.columnPageSize,
            rowIndex + 1 + this.rowPageOffset * this.rowPageSize);
  }

  /**
   * Updates the row header. Called whenever the table is paged up or down.
   *
   * @param jTable the table with information regarding the set up of the new row headers
   * @param jScrollPane the scroll pane whose row headers need updating
   */
  private void updateRowHeader(JTable jTable, JScrollPane jScrollPane) {
    // new row header with updated labels
    JList<String> rowHeader = new JList<>(new AbstractListModel<>() {
      @Override
      public int getSize() {
        return PagingModel.this.rowPageSize;
      }

      @Override
      public String getElementAt(int index) {
        // the labels for the row headers
        return Integer
            .toString(index + 1 + PagingModel.this.rowPageOffset * PagingModel.this.rowPageSize);
      }
    });
    // the row header is currently a bit narrower than a cell width
    rowHeader.setFixedCellWidth(50);
    // row headers should have same height as rest of row
    rowHeader.setFixedCellHeight(jTable.getRowHeight());
    // customizes what the row headers look like
    rowHeader.setCellRenderer(new RowHeaderRenderer(jTable));
    // adds row headers
    jScrollPane.setRowHeaderView(rowHeader);

    // scroll to the top of the new page
    jScrollPane.getVerticalScrollBar().setValue(0);
  }

  /**
   * Updates the column header. Called whenever the table is paged left or right.
   *
   * @param jTable the table with information regarding the set up of the new row headers
   * @param jScrollPane the scroll pane whose row headers need updating
   */
  private void updateColumnHeader(JTable jTable, JScrollPane jScrollPane) {
    TableColumnModel tableColumnModel = jTable.getColumnModel();
    for (int i = 0; i < this.columnPageSize; i++) {
      tableColumnModel.getColumn(i).setHeaderValue(
          Coord.colIndexToName(i + 1 + this.columnPageSize * this.columnPageOffset));
    }
    // otherwise the column headers would not change
    jTable.getTableHeader().repaint();

    // scroll to left of new page
    jScrollPane.getHorizontalScrollBar().setValue(0);
  }
}
