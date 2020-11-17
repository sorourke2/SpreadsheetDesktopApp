package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.Features;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ImmutableWorksheetInterface;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A gui view of a worksheet consisting of just the cells. Allows the user to view all the cells in
 * the spreadsheet through paging through pages. Each page allows scrolling and consists of a set
 * amount of rows and columns. Also allows the user to edit the spreadsheet.
 */
public class AdvancedWorksheetView extends JFrame implements WorksheetViewInterface,
    TableListener {

  private JTextField editField;
  private JButton acceptCellEdit;
  private ImmutableWorksheetInterface worksheet;
  private WorksheetView basicWorksheetView;
  private Features features;

  private int selectedRow;
  private int selectedColumn;

  public AdvancedWorksheetView(ImmutableWorksheetInterface worksheet) {
    this.worksheet = worksheet;
    this.basicWorksheetView = new WorksheetView(this.worksheet, this);
    this.selectedRow = 0;
    this.selectedColumn = 0;

    this.initMenuBar();
    this.initEditBar();
    super.getContentPane().add(this.basicWorksheetView
        .getContentPane().getComponent(0), BorderLayout.CENTER);

    this.setTitle("Spreadsheet");
    this.setSize(1024, 576);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public AdvancedWorksheetView(ImmutableWorksheetInterface worksheet, TableListener listener) {
    this.worksheet = worksheet;
    this.basicWorksheetView = new WorksheetView(this.worksheet, listener);
    this.selectedRow = 0;
    this.selectedColumn = 0;

    this.initMenuBar();
    this.initEditBar();
    super.getContentPane().add(this.basicWorksheetView
        .getContentPane().getComponent(0), BorderLayout.CENTER);

    this.setTitle("Spreadsheet");
    this.setSize(1024, 576);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  @Override
  public void makeVisible() throws IOException {
    this.setVisible(true);
  }

  @Override
  public void addFeatures(Features features) {
    this.features = features;
    this.acceptCellEdit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (AdvancedWorksheetView.this.selectedRow > 0) {
          AdvancedWorksheetView.this.features
              .editCellContents(AdvancedWorksheetView.this.selectedColumn,
                  AdvancedWorksheetView.this.selectedRow,
                  AdvancedWorksheetView.this.editField.getText());
        }
      }
    });
  }

  @Override
  public void cellSelected(int row, int column) {
    this.selectedRow = row;
    this.selectedColumn = column;

    if (this.selectedRow == 0) {
      this.editField.setText("");
    } else {
      this.editField.setText(this.worksheet.getUserString(column, row));
    }
  }

  @Override
  public void cellContentsDeleted(int row, int column) {
    this.features.cellContentsDeleted(column, row);
    this.editField.setText(this.worksheet.getUserString(this.selectedColumn, this.selectedRow));
  }

  @Override
  public void refresh(Set<Coord> coords) {
    this.basicWorksheetView.refresh(coords);
  }

  /**
   * Initializes the menu bar which allows user to save and load files.
   */
  private void initMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu file = new JMenu("File");
    menuBar.add(file);

    JMenuItem save = new JMenuItem("Save");
    save.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Saving File");

        int userSelection = fileChooser.showSaveDialog(AdvancedWorksheetView.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
          try {
            File fileToSave = fileChooser.getSelectedFile();
            if (fileToSave.createNewFile()) {
              FileWriter writer = new FileWriter(fileToSave);
              WorksheetTextualView textualView = new WorksheetTextualView(
                  AdvancedWorksheetView.this.worksheet, writer);
              textualView.makeVisible();
              writer.close();
            } else {
              FileWriter writer = new FileWriter(fileToSave, false);
              WorksheetTextualView textualView = new WorksheetTextualView(
                  AdvancedWorksheetView.this.worksheet, writer);
              textualView.makeVisible();
              writer.close();
            }
          } catch (IOException error) {
            // do nothing if file cannot be saved to for some reason
          }
        }
      }
    });
    file.add(save);

//    JMenuItem load = new JMenuItem("Load");
//    load.addActionListener(new ActionListener() {
//      @Override
//      public void actionPerformed(ActionEvent e) {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Loading File");
//
//        int userSelection = fileChooser.showDialog(AdvancedWorksheetView.this, "Load");
//
//        if (userSelection == JFileChooser.APPROVE_OPTION) {
//          try {
//            File fileToLoad = fileChooser.getSelectedFile();
//            WorksheetInterface worksheet = WorksheetReader
//                .read(new WorksheetBuilder(), new FileReader(fileToLoad));
//            Set<Coord> cells = AdvancedWorksheetView.this.worksheet.getFilledCells();
//            cells.addAll(AdvancedWorksheetView.this.worksheet.getFilledCells());
//
//
//
//            AdvancedWorksheetView.this.refresh(cells);
//          } catch (IOException error) {
//            // do nothing if file cannot be loaded to for some reason
//          } catch (IllegalStateException | NullPointerException error) {
//            // do nothing if there are formatting errors in the file
//          }
//        }
//      }
//    });
//    file.add(load);

    this.setJMenuBar(menuBar);
  }

  /**
   * Initializes the top of the view which allows the user to edit cell contents.
   */
  private void initEditBar() {
    JPanel editBar = new JPanel();
    editBar.setFocusTraversalKeysEnabled(false);
    this.acceptCellEdit = new JButton("✓");
    JButton deny = new JButton("✕");
    this.editField = new JTextField();

    this.editField.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // nothing should happen when keys are typed
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // nothing should happen when keys are pressed
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
          AdvancedWorksheetView.this.acceptCellEdit.doClick();
        }
      }
    });

    deny.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (AdvancedWorksheetView.this.selectedRow > 0) {
          AdvancedWorksheetView.this.editField.setText(
              AdvancedWorksheetView.this.worksheet.getUserString(
                  AdvancedWorksheetView.this.selectedColumn,
                  AdvancedWorksheetView.this.selectedRow));
        }
      }
    });

    editBar.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;

    constraints.weightx = 0.025;
    constraints.gridx = 0;
    constraints.gridy = 0;
    editBar.add(this.acceptCellEdit, constraints);

    constraints.weightx = 0.025;
    constraints.gridx = 1;
    constraints.gridy = 0;
    editBar.add(deny, constraints);

    constraints.weightx = 0.95;
    constraints.ipady = 20;
    constraints.gridx = 2;
    constraints.gridy = 0;
    editBar.add(this.editField, constraints);

    super.getContentPane().add(editBar, BorderLayout.NORTH);
  }
}
