import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;

// import org.w3c.dom.events.MouseEvent;

class MainFrame extends JFrame implements ActionListener, MouseListener, ListSelectionListener, TableModelListener {
    // new Color(30, 30, 30)
    // new Color(255, 255, 255)
    // new Color(48, 50, 51)
    Color color1 = new Color(30, 30, 30);
    Color color2 = new Color(255, 255, 255);
    Color color3 = new Color(48, 50, 51);
    Color color4;

    JButton addButton;
    JButton editButton;
    JButton deleteButton;
    JButton loadButton;
    JButton saveButton;

    JTable table;

    int selectedRow;

    MainFrame() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Library Control System");
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("assets/icon.png"));
        this.setSize(900, 600);
        this.setResizable(true);
        this.getContentPane().setBackground(color1);

        JPanel header = new JPanel();

        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(100, 100));
        header.setBackground(color3);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(color1);

        JPanel toolBarPanel = new JPanel();
        JToolBar toolBar = new JToolBar();
        toolBarPanel.setBackground(color3);
        toolBarPanel.setPreferredSize(new Dimension(100, 50));
        toolBar.setBackground(color3);


        JLabel label = new JLabel("Library Control System");
        label.setForeground(color2);
        label.setFont(new Font("Calibri", Font.PLAIN, 50));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        header.add(label);

        this.add(header, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(toolBarPanel, BorderLayout.NORTH);
        toolBarPanel.add(toolBar);

        this.setVisible(true);


        addButton = new JButton("Add");
        // addButton.setMargin(new Insets(0, 0, 0, 0));
        addButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        // addButton.setBounds(10, 10, 40, 30);
        addButton.addActionListener(this);
        // addButton.setSize(30, 30);


        editButton = new JButton("Edit");
        editButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        editButton.addActionListener(this);
        editButton.setEnabled(false);


        deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        deleteButton.addActionListener(this);
        deleteButton.setEnabled(false);


        loadButton = new JButton("Load");
        loadButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        loadButton.addActionListener(this);


        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        saveButton.addActionListener(this);


        toolBar.add(addButton);
        toolBar.add(editButton);
        toolBar.add(deleteButton);
        toolBar.add(loadButton);
        toolBar.add(saveButton);
        // toolBar.setPreferredSize(new Dimension(100, 100));

        table = new JTable(Main.tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(color3);
        tableHeader.setForeground(color2);
        tableHeader.setFont(new Font("Calibri", Font.BOLD, 20));
        // tableHeader.setRowHeight(26);
        table.setBackground(color1);
        table.setForeground(color2);
        table.setGridColor(color2);
        table.setFont(new Font("Calibri", Font.PLAIN, 20));
        table.setRowHeight(26);
        table.addMouseListener(this);
        table.getSelectionModel().addListSelectionListener(this);
        table.getModel().addTableModelListener(this);
        table.setDefaultEditor(Object.class, new CustomCellEditor());


        // scrollPane.setBackground(color1);
        scrollPane.getViewport().setBackground(color1);
        // table.setShowGrid(false);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }


    public void saveLibraryToCSV(File file) {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (Book book : Main.library.getBooks()) {
                String line = String.format("%s,%s,%s",
                        book.getIsbn(),
                        book.getTitle().replace(",", "\\,"),
                        book.getAuthor().replace(",", "\\,"));
                pw.println(line);
            }
            System.out.println("Library saved to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
        }
    }


    public void loadLibraryFromCSV(File file) {
        Main.library.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("(?<!\\\\),", -1);
                if (parts.length >= 3) {
                    String isbn = parts[0].trim();
                    String title = parts[1].replace("\\,", ",").trim();
                    String author = parts[2].replace("\\,", ",").trim();
                    Main.library.addBook(new Book(isbn, title, author));
                }
            }
            refreshTableFromLibrary();
            System.out.println("Library loaded from " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }


    private void refreshTableFromLibrary() {
        Main.tableModel.setRowCount(0);

        for (Book book : Main.library.getBooks()) {
            Object[] rowData = {
                book.getIsbn(),
                book.getTitle(),
                book.getAuthor()
            };
            Main.tableModel.addRow(rowData);
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton || e.getSource() == editButton) {
            JTextField isbnField = new JTextField(10);
            JTextField titleField = new JTextField(20);
            JTextField authorField = new JTextField(20);

            Object[] message = {
                "ISBN:", isbnField,
                "Title:", titleField,
                "Author:", authorField
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Enter Book Details", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String isbn = isbnField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                if (isbn.trim().isEmpty() || title.trim().isEmpty() || author.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Cell cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (e.getSource() == addButton) {
                    Main.tableModel.addRow(new String[] {isbn, title, author});
                    Main.library.addBook(new Book(isbn, title, author));
                } else if (e.getSource() == editButton) {

                    table.getModel().removeTableModelListener(this);

                    Main.tableModel.setValueAt(isbn, selectedRow, 0);
                    Main.tableModel.setValueAt(title, selectedRow, 1);
                    Main.tableModel.setValueAt(author, selectedRow, 2);

                    Main.library.editBook(selectedRow, new Book(isbn, title, author));

                    table.getModel().addTableModelListener(this);
                }
            }
        } else if (e.getSource() == deleteButton) {
            Main.tableModel.removeRow(selectedRow);
            Main.library.removeBook(selectedRow);
        } else if (e.getSource() == saveButton) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveLibraryToCSV(file);
            }
        } else if (e.getSource() == loadButton) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                loadLibraryFromCSV(file);
            }
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == TableModelEvent.ALL_COLUMNS) return;

            String isbn = (String) Main.tableModel.getValueAt(row, 0);
            String title = (String) Main.tableModel.getValueAt(row, 1);
            String author = (String) Main.tableModel.getValueAt(row, 2);

            Book updatedBook = new Book(isbn, title, author);
            Main.library.editBook(row, updatedBook);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            selectedRow = table.getSelectedRow();
            editButton.setEnabled(selectedRow >= 0);
            deleteButton.setEnabled(selectedRow >= 0);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println("Mouse clicked!");
        int row = table.rowAtPoint(e.getPoint());
        // System.out.println(row);
        if (row >= 0) {
            // System.out.println("Row clicked: " + row);
        }
    }

    @Override 
    public void mousePressed(MouseEvent e) {
        
    }

    @Override public void mouseReleased(MouseEvent e) {}

    @Override 
    public void mouseEntered(MouseEvent e) {}

    @Override 
    public void mouseExited(MouseEvent e) {}
}