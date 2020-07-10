import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    public static Queue<String> queue = new LinkedList<String>();
    public static Set<String> marked = new HashSet<>();
    public static String regex = "http[s]*://(\\w+\\.)*(\\w+)";

    public static void bfsAlgorithm(String root) throws IOException {
        queue.add(root);
        BufferedReader bufferedReader = null;

        while (!queue.isEmpty()) {

            // using poll() to retrieve and remove the head
            String crawledUrl = queue.poll();

            System.out.println("\n=== Site crawled: " + crawledUrl + " ===");

            if (marked.size() > 100) {
                return;
            }

            boolean ok = false;

            URL url = null;

            while (!ok) {
                try {
                    url = new URL(crawledUrl);
                    //The openStream() method returns a java.io.InputStream object, so reading from a URL is as easy as reading from an input stream.
                    bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                    ok = true;
                } catch (MalformedURLException e) {
                    System.out.println("*** Malformed URL: " + crawledUrl);
                    // using poll() to retrieve and remove the head
                    crawledUrl = queue.poll();
                    ok = false;

                } catch (IOException ioe) {
                    System.out.println("*** IOException URL: " + crawledUrl);
                    // using poll() to retrieve and remove the head
                    crawledUrl = queue.poll();
                    ok = false;
                }
            }
            //Trong java, lớp StringBuilder được sử dụng để tạo chuỗi có thể thay đổi (mutable).
            //Lớp StringBuilder trong java tương tự như lớp StringBuilder ngoại trừ nó không đồng bộ(non-synchronized).

            // create a StringBuilder object
            // usind StringBuilder() constructor
            StringBuilder stringBuilder = new StringBuilder();
            String tmp = null;

            while ((tmp = bufferedReader.readLine()) != null) {
                // Phương thức append() của lớp StringBuilder nối thêm tham số vào cuối chuỗi.
                // Appends the specified string to this character sequence.
                stringBuilder.append(tmp);
            }
            tmp = stringBuilder.toString();

            // Create a Pattern object
            Pattern pattern = Pattern.compile(regex);

            // Now create matcher object.
            Matcher matcher = pattern.matcher(tmp);

            while (matcher.find()) {
                String w = matcher.group();

                if (!marked.contains(w)) {
                    marked.add(w);
                    System.out.println("Site added for crawling: " + w);
                    queue.add(w);
                }
            }
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
    }

    public static void showResult() {
        System.out.println("\n\nResult: ");
        System.out.println("Websites crawled: " + marked.size() + "\n");
        for (String s : marked) {
            System.out.println("* " + s);
        }
    }

    public static void main(String[] args) {
        try {
            bfsAlgorithm("https://www.instagram.com/ms_puiyi/");
            showResult();
        } catch (IOException ignored) {
        }
    }
}
