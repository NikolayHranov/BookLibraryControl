import java.util.ArrayList;

public class Library {
    
    private ArrayList<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(int index) {
        if (index >= 0 && index < books.size()) {
            books.remove(index);
        }
    }

    public void editBook(int index, Book updated) {
        if (index >= 0 && index < books.size()) {
            books.set(index, updated);
        }
    }

    public Book getBook(int index) {
        return books.get(index);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void clear() {
        books.clear();
    }
}