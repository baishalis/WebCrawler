/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Baishali
 */
public class WebCrawler {

    /**
     * @param args the command line arguments
     */
    
    private static final String HTML_A_HREF_TAG_PATTERN
            = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
    private Pattern pattern;
    private Set<String> visitedUrls = new HashSet<String>();

    public WebCrawler() {
        pattern = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
    }

    private void fetchContentFromURL(String strLink) {
        String content = null;
        URLConnection connection = null;
        System.setProperty("https.proxyHost", "172.31.1.4");
        System.setProperty("https.proxyPort", "8080");
        System.setProperty("http.proxyHost", "172.31.1.4");
        System.setProperty("http.proxyPort", "8080");
        try {
            connection = new URL(strLink).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            scanner.useDelimiter("\\Z");
            if (scanner.hasNext()) {
                content = scanner.next();
                visitedUrls.add(strLink);
                fetchURL(content);
             //   visitedUrls.remove(strLink);
            }
        } catch (Exception ex) {
           // ex.printStackTrace();
        }
    }
    
    private void fetchURL(String content) {
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String group = matcher.group();
            if (group.toLowerCase().contains("http") || group.toLowerCase().contains("https")) {
                group = group.substring(group.indexOf("=") + 1);
                group = group.replaceAll("'", "");
                group = group.replaceAll("\"", "");
                System.out.println("lINK " + group);
                if (!visitedUrls.contains(group) && visitedUrls.size() < 200) {
                    fetchContentFromURL(group);
                }
            }
        }

        System.out.println("DONE");
    }

    public static void main(String[] args) throws MalformedURLException, IOException {
        // TODO code application logic here

//        URL url = new URL("https://en.wikipedia.org/wiki/India");
//        URLConnection con = url.openConnection();
//        System.setProperty("https.proxyHost", "172.31.1.4");
//        System.setProperty("https.proxyPort", "8080");
//        System.setProperty("http.proxyHost", "172.31.1.4");
//        System.setProperty("http.proxyPort", "8080");
//        InputStream is = con.getInputStream();
//
//        Scanner sc = new Scanner(is);
//        sc.useDelimiter("\\Z");
//        while (sc.hasNextLine()) {
//            System.out.println(sc.nextLine());
//        }

        String str = "https://en.wikipedia.org/wiki/India";
        new WebCrawler().fetchContentFromURL(str.trim());

    }

}


