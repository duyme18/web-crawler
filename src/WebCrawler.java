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
                    bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                    ok = true;
                } catch (MalformedURLException e) {
                    System.out.println("*** Malformed URL: " + crawledUrl);
                    crawledUrl = queue.poll();
                    ok = false;

                } catch (IOException ioe) {
                    System.out.println("*** IOException URL: " + crawledUrl);
                    crawledUrl = queue.poll();
                    ok = false;
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            String tmp = null;

            while ((tmp = bufferedReader.readLine()) != null) {
                stringBuilder.append(tmp);
            }
            tmp = stringBuilder.toString();
            Pattern pattern = Pattern.compile(regex);
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
