import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class Main {
    public static Library library = new Library();
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
}