import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class Main {
    public static String[] columnNames = {"ISBN", "Title", "Author"};
    public static ArrayList<String[]> data;
    public static DefaultTableModel tableModel;
    public static MainFrame frame;

    public static void main(String[] args) {
        
        data = new ArrayList<>();
        tableModel = new DefaultTableModel(columnNames, 0);

        frame = new MainFrame();
        frame.revalidate();
        frame.repaint();

    }


    public static void writeTableToCSV(DefaultTableModel model, String filePath) throws IOException {
        try (FileWriter csv = new FileWriter(filePath)) {
            int columnCount = model.getColumnCount();

            // Write column headers
            for (int i = 0; i < columnCount; i++) {
                csv.write(model.getColumnName(i));
                if (i != columnCount - 1) csv.write(",");
            }
            csv.write("\n");

            // Write rows
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < columnCount; col++) {
                    Object value = model.getValueAt(row, col);
                    csv.write(value != null ? value.toString() : "");
                    if (col != columnCount - 1) csv.write(",");
                }
                csv.write("\n");
            }
        }
    }
}