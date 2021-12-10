package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class window extends JFrame implements MouseListener, ActionListener {

    private JPanel btnOK, btnRst, mainPanel, tablePanel;
    private JLabel resetLabel, description, income, THB_Label1, expense, THB_Label2; // resetLabel is the label of button clear
    private JTable table;
    private JTableHeader header;
    private DBManager dbManager;
    private JComboBox<String> comboBox;
    private JTextField income_textField, expense_textField;
    private JMenuItem toXLSX, toCSV, refresh, exit, changeBGColor, changeTextColor,
            changeHeaderColor, changeBodyColor, about, dropTable;
    String password;

    public window() {
        password = getPassword();
       // Hard Code XD
        while (!password.equals("MY_PASSWORD")) {
            JOptionPane.showMessageDialog(null, "Not yet!",
                    "Try again", JOptionPane.ERROR_MESSAGE);
            password = getPassword();
            this.dispose();
        }
        JOptionPane.showMessageDialog(null, "Welcome back QUINTHR4X",
                "Welcome!", JOptionPane.INFORMATION_MESSAGE);
        makeGUI();
    }

    public String getPassword() {
        JPasswordField passwordField = new JPasswordField(12);
        passwordField.setToolTipText("Enter your Password");
        JCheckBox checkbox = new JCheckBox("Show passwords");

        checkbox.addActionListener(listener -> {
            JCheckBox listenCheck = (JCheckBox) listener.getSource();
            passwordField.setEchoChar(listenCheck.isSelected() ? '\u0000' : (Character) UIManager.get("PasswordField.echoChar"));
        });

        JLabel label = new JLabel("Enter password : ");
        Box boxHorizontal = Box.createHorizontalBox();
        boxHorizontal.add(label);
        boxHorizontal.add(passwordField);
        Box boxVertical = Box.createVerticalBox();
        boxVertical.add(boxHorizontal);
        boxVertical.add(checkbox);

        int x = JOptionPane.showConfirmDialog(null, boxVertical,
                "Enter Password", JOptionPane.OK_CANCEL_OPTION);

        if (x == JOptionPane.OK_OPTION) {
            return String.valueOf(passwordField.getPassword());
        }
        return null;
    }

    private void makeGUI() {

        dbManager = new DBManager("jdbc:mysql://localhost:MY_PORT/ledger"
                , "root", password);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        /* ============================  Description Time  ================================ */

        description = new JLabel("Description");
        description.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        description.setForeground(Color.WHITE);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 45, 0, 0);  // manage space between outer object
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 25;
        mainPanel.add(description, gbc);

        String[] items = {"7-Eleven", "Breakfast", "Launch", "Dinner", "Motorcycle Taxi", "Gain Money", "Travelling",
                "ETC.", "Top-up Mobile", "Donate", "Transfer Money", ""};
        comboBox = new JComboBox<>(items);
        comboBox.setEditable(true);
        comboBox.setPreferredSize(new Dimension(25, 30));
        comboBox.setFont(new Font("Browallia New", Font.PLAIN, 22));
        gbc.insets = new Insets(10, 0, 0, 0); // manage space between outer object
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.ipadx = 250;
        mainPanel.add(comboBox, gbc);


        /* ============================  Income Time  ================================ */

        income = new JLabel("Income");
        income.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        income.setForeground(Color.WHITE);
        gbc.fill = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 10, 0, 0); // manage space between outer object
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipadx = 25;
        gbc.ipady = 10;
        mainPanel.add(income, gbc);

        income_textField = new JTextField();

        income_textField.setFont(new Font("Browallia New", Font.PLAIN, 22));
        income_textField.setToolTipText("Enter your Income Amount");
        gbc.insets = new Insets(10, -30, 0, 0);  // manage space between outer object
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.ipadx = 100;
        gbc.ipady = 0;
        mainPanel.add(income_textField, gbc);

        THB_Label1 = new JLabel("THB");
        THB_Label1.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        THB_Label1.setForeground(Color.WHITE);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.ipadx = 50;
        gbc.ipady = 10;
        mainPanel.add(THB_Label1, gbc);


        /* ============================  Expense Time  ================================ */

        expense = new JLabel("Expense");
        expense.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        expense.setForeground(Color.WHITE);
        gbc.fill = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(10, 10, 0, 0); // manage space between outer object
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.ipadx = 25;
        gbc.ipady = 10;
        mainPanel.add(expense, gbc);

        expense_textField = new JTextField();
        expense_textField.setToolTipText("Enter your Expense Amount");
        expense_textField.setFont(new Font("Browallia New", Font.PLAIN, 22));
        gbc.insets = new Insets(10, -30, 0, 0);  // manage space between outer object
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.ipadx = 100;
        gbc.ipady = 0;
        mainPanel.add(expense_textField, gbc);

        THB_Label2 = new JLabel("THB");
        THB_Label2.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        THB_Label2.setForeground(Color.WHITE);
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.ipadx = 50;
        gbc.ipady = 10;
        mainPanel.add(THB_Label2, gbc);

        /* =================================================================================== */


        /* +++++++++++++++++++++++++++++++  Button  ++++++++++++++++++++++++++++++++++++++++++ */

        btnOK = new JPanel();
        btnOK.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnOK.setBorder(BorderFactory.createLineBorder(new Color(110, 217, 161), 3, true));
        btnOK.setBackground(new Color(110, 217, 161));
        btnOK.setPreferredSize(new Dimension(100, 40));

        JLabel insertLabel = new JLabel("Insert");
        insertLabel.setToolTipText("Add Data");
        insertLabel.setForeground(Color.WHITE);
        insertLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        btnOK.add(insertLabel);
        btnOK.addMouseListener(this);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.insets = new Insets(30, 90, 10, 0);  // manage space between outer object
        gbc.weighty = -5;
        gbc.ipady = 0;
        gbc.gridx = 0;
        gbc.gridy = 3;

        mainPanel.add(btnOK, gbc);


        btnRst = new JPanel();
        btnRst.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnRst.setBackground(new Color(41, 54, 63));
        btnRst.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
        btnRst.setPreferredSize(new Dimension(50, 40));
        btnRst.addMouseListener(this);

        resetLabel = new JLabel("Clear");
        resetLabel.setToolTipText("Clear Data");
        resetLabel.setForeground(Color.WHITE);
        resetLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
        btnRst.add(resetLabel);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.insets = new Insets(30, 90, 10, 50);  // manage space between outer object
        gbc.weighty = -5;
        gbc.ipady = 0;
        gbc.gridx = 1;
        gbc.gridy = 3;

        mainPanel.add(btnRst, gbc);
        /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */


        /* ***************************** Table ********************************************** */
        tablePanel = new JPanel();
        tablePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        tablePanel.setBackground(new Color(41, 54, 63));

        table = new JTable();
        table.setDefaultEditor(Object.class, null);
        table.setCellSelectionEnabled(true);
        table.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
        table.setShowGrid(true);
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane sp = new JScrollPane(table);

        sp.setPreferredSize(new Dimension(700, 250));
        tablePanel.add(sp);

        header = table.getTableHeader();
        header.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        header.setBackground(new Color(4, 226, 195));

        table.setBackground(new Color(0, 255, 216));
        /* *********************************************************************************** */


        mainPanel.setBackground(new Color(41, 54, 63));  // for debugging
        mainPanel.setForeground(Color.WHITE);
        setLayout(new GridLayout(2, 1));
        add(mainPanel);
        add(tablePanel);
        getContentPane().setBackground(new Color(41, 54, 63));


        /* ####################################  Menu Bar  ################################### */
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu help = new JMenu("Help");
        about = new JMenuItem("About");
        changeBGColor = new JMenuItem("Change Background Color");
        changeTextColor = new JMenuItem("Change Text Color");
        JMenu export = new JMenu("Export Database");
        toXLSX = new JMenuItem("To .xlsx File");
        toCSV = new JMenuItem("To .csv File");
        refresh = new JMenuItem("Refresh Table");
        exit = new JMenuItem("Exit");
        JMenu tableColor = new JMenu("Change Table Color");
        changeHeaderColor = new JMenuItem("Change Header Color");
        changeBodyColor = new JMenuItem("Change Body Color");
        dropTable = new JMenuItem("Drop and create new table");

        exit.addActionListener(this);
        refresh.addActionListener(this);
        toCSV.addActionListener(this);
        toXLSX.addActionListener(this);
        changeBGColor.addActionListener(this);
        changeTextColor.addActionListener(this);
        changeHeaderColor.addActionListener(this);
        changeBodyColor.addActionListener(this);
        about.addActionListener(this);
        dropTable.addActionListener(this);

        help.add(about);
        edit.add(changeTextColor);
        edit.add(changeBGColor);
        tableColor.add(changeHeaderColor);
        tableColor.add(changeBodyColor);
        edit.add(tableColor);
        export.add(toCSV);
        export.add(toXLSX);
        file.add(refresh);
        file.add(dropTable);
        file.addSeparator();
        file.add(export);
        file.addSeparator();
        file.add(exit);

        mb.add(file);
        mb.add(edit);
        mb.add(help);

        /* ################################################################################### */

        setJMenuBar(mb);
        pack();
        File obj = new File("res\\icon.png");

        try {
            setIconImage(ImageIO.read(obj));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
        setResizable(false);
        setTitle("Amass");
        setLocationRelativeTo(null); // makes window at the center of the screen.
        repaint();

    }

    public static void main(String[] args) {
        /*
         * <code>window::new</code> means new runnable(){ public void run(){ new window(); } }
         * (Used Anonymous Class)
         *
         * @Anonymous Class is the no name class creation that implements interface;
         */
        javax.swing.SwingUtilities.invokeLater(window::new);
    }

    public void insert_Value(String description, BigDecimal income, BigDecimal expense, BigDecimal balance) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        BigDecimal newBalance = balance.add(income);
        newBalance = newBalance.subtract(expense);
        dbManager.Update_Query("insert into ledger.ledger (DateTime, Description, Income, Expense, Balance) " +
                "values ('" + formatter.format(date) + "', '" + description + "', " + income + ", " + expense + ", " + newBalance + ");");
    }

    public void createTable() {
        dbManager.Update_Query("CREATE TABLE ledger (\n" +
                "    DateTime datetime PRIMARY KEY,\n" +
                "    Description text,\n" +
                "    Income decimal(10,3),\n" +
                "    Expense decimal(10,3),\n" +
                "    Balance decimal(10,3)\n" +
                ");");
        System.out.println("Table created!");
        insert_Value(dbManager.getRows()[0][1].toString(),
                BigDecimal.valueOf(Double.parseDouble(dbManager.getRows()[0][2].toString())),
                BigDecimal.valueOf(Double.parseDouble(dbManager.getRows()[0][3].toString())),
                BigDecimal.valueOf(Double.parseDouble(dbManager.getRows()[0][4].toString())));
        System.out.println("Data Inserted");
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == btnOK) {

            try {
                dbManager.query("select * from ledger");
                insert_Value((String) comboBox.getSelectedItem(),
                        BigDecimal.valueOf(Double.parseDouble(income_textField.getText()))
                        , BigDecimal.valueOf(Double.parseDouble(expense_textField.getText()))
                        , dbManager.getOldBalance());

                TableModel newModel = new DefaultTableModel(
                        dbManager.getRows(),
                        dbManager.getColumns());

                table.setModel(newModel);
                JOptionPane.showMessageDialog(null, "Data has been inserted.",
                        "Success!", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error : " + ex.getMessage(),
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }

        }
        if (e.getSource() == btnRst) {
            income_textField.setText("");
            expense_textField.setText("");
            comboBox.setSelectedIndex(0);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == btnOK) {
            btnOK.setBorder(BorderFactory.createLineBorder(new Color(85, 192, 136), 3, true));
            btnOK.setBackground(new Color(85, 192, 136));
        }
        if (e.getSource() == btnRst) {
            btnRst.setBackground(new Color(16, 29, 38));
            btnRst.setBorder(BorderFactory.createLineBorder(Color.RED, 3, true));
            resetLabel.setForeground(Color.RED);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == btnOK) {
            btnOK.setBorder(BorderFactory.createLineBorder(new Color(97, 204, 148), 3, true));
            btnOK.setBackground(new Color(97, 204, 148));
        }
        if (e.getSource() == btnRst) {
            btnRst.setBackground(new Color(28, 41, 50));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == btnOK) {
            btnOK.setBorder(BorderFactory.createLineBorder(new Color(97, 204, 148), 3, true));
            btnOK.setBackground(new Color(97, 204, 148));
        }
        if (e.getSource() == btnRst) {
            btnRst.setBackground(new Color(28, 41, 50));
            btnRst.setBorder(BorderFactory.createLineBorder(Color.RED, 3, true));
            resetLabel.setForeground(Color.RED);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == btnOK) {
            btnOK.setBorder(BorderFactory.createLineBorder(new Color(110, 217, 161), 3, true));
            btnOK.setBackground(new Color(110, 217, 161));
        }
        if (e.getSource() == btnRst) {
            btnRst.setBackground(new Color(41, 54, 63));
            btnRst.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3, true));
            resetLabel.setForeground(Color.WHITE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == changeBGColor) {
            Color c = JColorChooser.showDialog(this, "Choose",
                    new Color(41, 54, 63));
            mainPanel.setBackground(c);
            tablePanel.setBackground(c);
        }
        if (e.getSource() == changeTextColor) {
            Color c = JColorChooser.showDialog(this, "Choose",
                    new Color(41, 54, 63));
            description.setForeground(c);
            income.setForeground(c);
            THB_Label1.setForeground(c);
            expense.setForeground(c);
            THB_Label2.setForeground(c);
        }
        if (e.getSource() == changeHeaderColor) {
            Color c = JColorChooser.showDialog(this, "Choose",
                    new Color(41, 54, 63));
            header.setBackground(c);
        }
        if (e.getSource() == changeBodyColor) {
            Color c = JColorChooser.showDialog(this, "Choose",
                    new Color(41, 54, 63));
            table.setBackground(c);
        }
        if (e.getSource() == toCSV) {
            try {
                JFileChooser fc = new JFileChooser();
                int retrieval = fc.showSaveDialog(this);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setDialogTitle("Export as");
                fc.getIcon(new File("res\\icon.png"));
                String path = "";
                if (retrieval == JFileChooser.APPROVE_OPTION) {
                    File directory = fc.getSelectedFile();
                    path = directory.getPath();
                    System.out.println(path);
                }
                dbManager.export_to_csv(path);

            } catch (Exception ioException) {
                JOptionPane.showMessageDialog(null, "Error : " + ioException.getMessage(),
                        "Error!", JOptionPane.ERROR_MESSAGE);
                ioException.printStackTrace();
            }
        }
        if (e.getSource() == refresh) {
            dbManager.query("select * from ledger");
            TableModel newModel = new DefaultTableModel(
                    dbManager.getRows(),
                    dbManager.getColumns());
            table.setModel(newModel);
        }
        if (e.getSource() == exit) {
            this.dispose();
        }
        if (e.getSource() == toXLSX) {
            try {
                JFileChooser fc = new JFileChooser();
                int retrieval = fc.showSaveDialog(this);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.setDialogTitle("Export as");
                fc.getIcon(new File("res\\icon.png"));
                String path = "";
                if (retrieval == JFileChooser.APPROVE_OPTION) {
                    File directory = fc.getSelectedFile();
                    path = directory.getPath();
                    System.out.println(path);
                }
                dbManager.export_to_xlsx(path);
            } catch (Exception ioException) {
                JOptionPane.showMessageDialog(null, "Error : " + ioException.getMessage(),
                        "Error!", JOptionPane.ERROR_MESSAGE);
                ioException.printStackTrace();
            }
        }
        if(e.getSource() == about){
            JDialog subWindow = new JDialog((Dialog) null, "About", true);
            subWindow.setLayout(new BorderLayout());

            JPanel abovePanel = new JPanel();
            JPanel belowPanel = new JPanel();
            JLabel aboutMe = new JLabel("About Me!");
            aboutMe.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            JLabel name = new JLabel("Dev : Slickle DarlosheeyZ");
            name.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            JLabel contact = new JLabel("Email : kitikornfluke@gmail.com");
            contact.setFont(new Font("Comic Sans MS", Font.PLAIN, 12));
            JLabel msg = new JLabel("Plz enjoy ;)");
            msg.setFont(new Font("Comic Sans MS", Font.BOLD, 16));

            abovePanel.setLayout(new FlowLayout());
            belowPanel.setLayout(new FlowLayout());

            abovePanel.add(aboutMe);
            abovePanel.add(name);
            abovePanel.add(contact);
            subWindow.add(abovePanel, BorderLayout.CENTER);
            belowPanel.add(msg);
            subWindow.add(belowPanel, BorderLayout.SOUTH);

            subWindow.setIconImage(this.getIconImage());
            subWindow.setSize(200, 200);
            subWindow.setLocationRelativeTo(null);
            subWindow.setVisible(true);
        }
        if(e.getSource() == dropTable){
            dbManager.dropTable();
            createTable();
            JOptionPane.showMessageDialog(null, "Drop and create table successfully.",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
