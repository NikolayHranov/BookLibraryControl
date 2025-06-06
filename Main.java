import javax.swing.table.DefaultTableModel;

public class Main {
    public static Library library = new Library();
    public static String[] columnNames = {"ISBN", "Title", "Author"};
    public static CustomArrayList<String[]> data;
    public static DefaultTableModel tableModel;
    public static MainFrame frame;

    public static void main(String[] args) {
        
        data = new CustomArrayList<>();
        tableModel = new DefaultTableModel(columnNames, 0);

        frame = new MainFrame();
        frame.revalidate();
        frame.repaint();

    }

    public static boolean isValidISBN(String isbn) {
        isbn = isbn.replaceAll("-", "").replaceAll(" ", "");

        if (isbn.length() == 10) {
            return isValidISBN10(isbn);
        } else if (isbn.length() == 13) {
            return isValidISBN13(isbn);
        }
        return false;
    }

    private static boolean isValidISBN10(String isbn) {
        if (!isbn.matches("\\d{9}[\\dXx]")) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (isbn.charAt(i) - '0') * (i + 1);
        }

        char lastChar = isbn.charAt(9);
        int check = (lastChar == 'X' || lastChar == 'x') ? 10 : (lastChar - '0');
        sum += check * 10;

        return sum % 11 == 0;
    }

    private static boolean isValidISBN13(String isbn) {
        if (!isbn.matches("\\d{13}")) return false;

        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = isbn.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        return sum % 10 == 0;
    }

}