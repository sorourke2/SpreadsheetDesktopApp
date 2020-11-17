package edu.cs3500.spreadsheets.view;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

/**
 * A {@code ListCellRenderer} that allows row header cells to imitate the style of the column header
 * cells in the table given to the constructor.
 */
public class RowHeaderRenderer extends JLabel implements ListCellRenderer<String> {

  RowHeaderRenderer(JTable table) {
    JTableHeader header = table.getTableHeader();
    // greys out the header cells so that they looked like the column headers
    setOpaque(true);
    // sets the border of the cells to that of the column header cells
    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    // centers the text
    setHorizontalAlignment(CENTER);
    // matches the text to that of the column headers text
    setFont(header.getFont());
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends String> list,
      String value, int index, boolean isSelected, boolean cellHasFocus) {
    // value is the value obtained from calling getElementAt.
    setText(value);  // sets the text for the row header
    return this;
  }
}
