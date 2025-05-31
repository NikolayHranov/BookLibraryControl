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

    public ArrayList<Book> searchBooks(String query) {
        ArrayList<Book> searchedBooks = new ArrayList<>();
        String[] prefixes = {"isbn: ", "title: ", "author: "};
        String prefix = "";

        query = query.toLowerCase().trim();

        for (String pre: prefixes) {
            if (query.startsWith(pre)) {
                prefix = pre;
                query = query.substring(pre.length());
                break;
            }
        }

        if (query.isEmpty()) {
            return books;
        }

        for (Book book: books) {
            if (
                (book.getTitle().toLowerCase().contains(query) && (prefix.isEmpty() || prefix.equals("title: "))) ||
                (book.getIsbn().toLowerCase().contains(query) && (prefix.isEmpty() || prefix.equals("isbn: "))) ||
                (book.getAuthor().toLowerCase().contains(query) && (prefix.isEmpty() || prefix.equals("author: ")))
            ) {
                searchedBooks.add(book);
            }
        }

        return searchedBooks;
    }

    public void clear() {
        books.clear();
    }
}