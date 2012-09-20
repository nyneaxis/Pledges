package pledges;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Main class for Pledges which extend JFrame and implements WindowListener.
 * Class initilizes all JFrame components and WindowListener components.
 *
 * @author Brandon Wilson <brandon@nyneaxis.com>
 * @date Aug 9, 2012
 * @email exdox77@gmail.com
 *
 * Beginning of Java Class Pledges
 */
public class Pledges extends JFrame implements WindowListener {

    //create textfield ojbects
    private JTextField nameTextField;
    private JTextField charityNameTextField;
    private JTextField pledgeAmountTextField;
    //create label objects
    private JLabel nameLabel;
    private JLabel charityNameLabel;
    private JLabel pledgeAmountLabel;
    //create default table objects
    private JScrollPane scrollPane;
    private JTable pledgesTable;
    private DefaultTableModel tableModel;
    //create button object
    private JButton saveButton;
    private FileExecution fileExecution;
    private DBHelperClass DBH;

    /**
     * Take String values and add them to the JTable. The third value is the
     * String array should be a number and will be formated as $123.45.
     *
     * @param pledges String[] - Three params String[]{String, String, String}
     * @throws NumberFormatException Shows message dialog to user.
     */
    private void addToTable(String[] pledges) {
        //adds to table
        DecimalFormat df = new DecimalFormat("$#,###,###.00"); //formats the entered value
        try {
            tableModel.addRow(new Object[]{pledges[0], pledges[1], df.format(Double.valueOf(pledges[2]))}); //add values to table
            pledgesTable.scrollRectToVisible(pledgesTable.getCellRect(pledgesTable.getRowCount() - 1, 0, true)); //moves to last row on table
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Incorrect number entered"); //throws exception and display dialog if value is not a number
        }
    }

    private Boolean processFileToDatabase() {
        //processes opening the file in its own thread for performance
        //call the readFile method in fileExecution and places it in buffered memory
        BufferedReader bufferedReader = fileExecution.readFile();

        if (bufferedReader != null) {
            String lineString;
            //take string and read the line from the buffered memory
            try {
                while ((lineString = bufferedReader.readLine()) != null) {
                    //in the while loop we split the values up using | delimiter
                    String delimiter = "\\|";
                    String[] lineContents = lineString.split(delimiter);
                    //call add to table
                    DBH.insertRecord(lineContents[0], lineContents[1], lineContents[2]);
                }
            } catch (Exception ex) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private void actions() {
        //actions for buttons
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("".equals(nameTextField.getText())) { //checks text field for value
                    JOptionPane.showMessageDialog(null, "You must enter Name");
                } else if ("".equals(charityNameTextField.getText())) { //checks text field for value
                    JOptionPane.showMessageDialog(null, "You must enter charity name");
                } else if ("".equals(pledgeAmountTextField.getText())) { //checks text field for value
                    JOptionPane.showMessageDialog(null, "You must enter pledge amount");
                } else { //if there are values in textfield addToTable is called
                    fileExecution.writeFile(nameTextField.getText(), charityNameTextField.getText(), pledgeAmountTextField.getText());
                    addToTable(new String[]{nameTextField.getText(), charityNameTextField.getText(), pledgeAmountTextField.getText()});
                    nameTextField.setText("");
                    charityNameTextField.setText("");
                    pledgeAmountTextField.setText("");
                }
            }
        });
    }

    /**
     * Constructor for class calls init to initialize objects
     */
    private Pledges() {
        init();
    }

    /**
     * Sets JFrame values and initilizes objects.
     */
    private void init() {
        //<editor-fold defaultstate="collapsed" desc=" GUI Coponents ">
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //sets default close operation
        setSize(new Dimension(590, 370)); //default dimension
        setTitle("Pledges 3.0"); //sets title of window
        setResizable(false); //sets resizable to false.
        setLocationRelativeTo(null); //sets location to center of screen
        addWindowListener(this); //adds window listener

        //initialize textfields
        nameTextField = new JTextField();
        charityNameTextField = new JTextField();
        pledgeAmountTextField = new JTextField();

        //initialize labels
        nameLabel = new JLabel();
        charityNameLabel = new JLabel();
        pledgeAmountLabel = new JLabel();

        //initialize table objects
        scrollPane = new JScrollPane();
        pledgesTable = new JTable();
        tableModel = new DefaultTableModel(null, new String[]{"Name", "Charity", "Amount"});

        //initialize button
        saveButton = new JButton();

        //set values for text fields
        nameTextField.setText("");
        charityNameTextField.setText("");
        pledgeAmountTextField.setText("");

        //set values for labels
        nameLabel.setText("Name:");
        charityNameLabel.setText("Charity:");
        pledgeAmountLabel.setText("Pledge:");

        //set value for button
        saveButton.setText("Save");

        //set file execution
        fileExecution = new FileExecution();

        //set DB helper class
        DBH = new DBHelperClass();

        //set defaults for table
        pledgesTable.setModel(tableModel);
        scrollPane.setViewportView(pledgesTable);

        //set dafualt layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        //allows the jframe to create gaps 
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        //creates horizontal layout
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                .addComponent(nameLabel)
                .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addComponent(charityNameLabel)
                .addComponent(charityNameTextField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addComponent(pledgeAmountLabel)
                .addComponent(pledgeAmountTextField, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addComponent(saveButton)))));

        //creates vertical layout
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(charityNameLabel)
                .addComponent(charityNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(pledgeAmountLabel)
                .addComponent(pledgeAmountTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(saveButton)
                .addComponent(nameLabel))
                .addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)));

        //calls actions method to set button actions
        actions();
        //</editor-fold>
    }

    /**
     * Main method for Pledges class. Creates new instance of Pledges and set it
     * visible.
     *
     * @param args String[] - optional
     */
    public static void main(String[] args) {
        new Pledges().setVisible(true);
    }

    /**
     * When window is opened completes operation for retrieving data from
     * database.
     *
     * @param e WindowEvent
     */
    @Override
    public void windowOpened(WindowEvent e) {
        //Starts new thread to perform operation
        Thread openFileThread;
        openFileThread = new Thread() {
            @Override
            public void run() {
                ArrayList<String[]> arrayList = DBH.selectAllFromDatabase(); //Creates String Array ArrayList from database method
                for (int i = 0; i < arrayList.size(); i++) { //Loops through ArrayList
                    addToTable(arrayList.get(i)); //Adds each array to table
                }
            }
        };
        openFileThread.start(); //Starts thread
    }

    /**
     * When window is closing add temp file to database.
     *
     * @param e WindowEvent
     */
    @Override
    public void windowClosing(WindowEvent e) {
        if (processFileToDatabase()) { //Call procesFileToDatabase() returns true if sucessful 
            fileExecution.deleteFile(); //If true, deletes file
        }
    }

    /**
     * Method not used
     *
     * @param e WindowEvent
     */
    @Override
    public void windowClosed(WindowEvent e) {
    }

    /**
     * Method not used
     *
     * @param e WindowEvent
     */
    @Override
    public void windowIconified(WindowEvent e) {
    }

    /**
     * Method not used
     *
     * @param e WindowEvent
     */
    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    /**
     * Method not used
     *
     * @param e WindowEvent
     */
    @Override
    public void windowActivated(WindowEvent e) {
    }

    /**
     * Method not used
     *
     * @param e WindowEvent
     */
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
/**
 *
 * End of Java Class Pledges
 *
 */
