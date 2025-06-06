import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class BookJava {
    public static void main(String[] args) {
        String isbn = "9780140328721"; // replace with your ISBN
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            JSONObject json = new JSONObject(response.toString());

            if (json.getInt("totalItems") > 0) {
                JSONObject info = json.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");
                String title = info.optString("title", "No title");
                JSONArray authors = info.optJSONArray("authors");
                String author = (authors != null) ? authors.join(", ").replaceAll("\"", "") : "No author";
                String description = info.optString("description", "No description");

                System.out.println("Valid ISBN ✅");
                System.out.println("Title: " + title);
                System.out.println("Author(s): " + author);
                System.out.println("Description: " + description);
            } else {
                System.out.println("Invalid ISBN ❌");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
