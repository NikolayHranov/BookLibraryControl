import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomArrayList<T> implements Iterable<T> {

    private T[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    @SuppressWarnings("unchecked")
    public CustomArrayList() {
        data = (T[]) new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        T[] newData = (T[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    public void add(T item) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[size++] = item;
    }

    public T get(int index) {
        checkIndex(index);
        return data[index];
    }

    public void set(int index, T item) {
        checkIndex(index);
        data[index] = item;
    }

    public T remove(int index) {
        checkIndex(index);
        T removed = data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null; // prevent memory leak
        return removed;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // Iterator support
    @Override
    public Iterator<T> iterator() {
        return new CustomIterator();
    }

    private class CustomIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return data[currentIndex++];
        }
    }

    public static void mergeSort(CustomArrayList<Book> list, String key) {
        if (list.size() <= 1) return;

        int mid = list.size() / 2;
        CustomArrayList<Book> left = new CustomArrayList<>();
        CustomArrayList<Book> right = new CustomArrayList<>();

        for (int i = 0; i < mid; i++) left.add(list.get(i));
        for (int i = mid; i < list.size(); i++) right.add(list.get(i));

        mergeSort(left, key);
        mergeSort(right, key);

        merge(list, left, right, key);
    }

    private static void merge(CustomArrayList<Book> result, CustomArrayList<Book> left,
                    CustomArrayList<Book> right, String key) {
        result.clear();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            Book b1 = left.get(i);
            Book b2 = right.get(j);

            String s1 = getKeyValue(b1, key);
            String s2 = getKeyValue(b2, key);

            if (s1.compareToIgnoreCase(s2) <= 0) {
                result.add(b1);
                i++;
            } else {
                result.add(b2);
                j++;
            }
        }

        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
    }

    private static String getKeyValue(Book book, String key) {
        switch (key.toLowerCase()) {
            case "title": return book.getTitle();
            case "author": return book.getAuthor();
            case "isbn": return book.getIsbn();
            default: return "";
        }
    }

}
