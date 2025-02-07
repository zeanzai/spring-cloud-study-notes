package me.zeanzai.geektime;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebScraper {

    // 1. 创建文件夹
    public static Path createFolder(String folderName) throws IOException {
        Path folderPath = Paths.get("D:/03-code/lianglianglee.com", folderName);
        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
            System.out.println("文件夹已创建: " + folderPath.toString());
        } else {
            System.out.println("文件夹已存在: " + folderPath.toString());
        }
        return folderPath;
    }

    // 2. 使用 Apache HttpClient 请求页面并提取 a 标签的 href
    public static List<String> fetchMarkdownLinks(String url) throws IOException {
        List<String> links = new ArrayList<>();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    Document doc = Jsoup.parse(result);

                    Elements bookPostDiv = doc.select(".book-post ul a");
                    for (Element link : bookPostDiv) {
                        String href = link.attr("href");
                        if (!href.isEmpty()) {
                            links.add(href);
                        }
                    }
                }
            }
        }
        return links;
    }
    // 只对中文字符进行 URL 编码
    public static String encodeChineseAndSpaces(String input) {
        StringBuilder result = new StringBuilder();
        // 匹配中文字符的正则表达式
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5\\s]");  // 中文字符和空格
        Matcher matcher = pattern.matcher(input);

        int lastPos = 0;
        while (matcher.find()) {
            // 获取中文字符或空格前的部分（保持原样）
            result.append(input, lastPos, matcher.start());
            try {
                // 对匹配的中文字符或空格进行编码
                String matched = matcher.group();
                String encoded;

                // 判断是否是空格，空格编码为 %20
                if (matched.equals(" ")) {
                    encoded = "%20"; // 将空格编码为 %20
                } else {
                    encoded = URLEncoder.encode(matched, "UTF-8"); // 编码中文字符
                }
                result.append(encoded);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            lastPos = matcher.end();
        }
        // 追加剩下的部分
        result.append(input.substring(lastPos));

        return result.toString();
    }

    public static void fetchMarkdownFile(String url) throws IOException {
        String[] split = url.split("/");
        String markdownFileName = split[split.length - 1];
        System.out.println(markdownFileName);
        List<String> links = new ArrayList<>();
        String encodeURL = encodeChineseAndSpaces(url);
        System.out.println("==https://lianglianglee.com"+encodeURL);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("https://lianglianglee.com"+encodeURL);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    Document doc = Jsoup.parse(result);

                    Element bookPostDiv = doc.selectFirst(".book-post");
                    if (bookPostDiv != null) {
                        String content = bookPostDiv.html();
                        System.out.println(content);
                    } else {
                        System.out.println("未找到 class=book-post 内容: " + url);
                    }
                }
            }
        }
    }



    public static List<String> readFileToList(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // 逐行读取文件
            while ((line = br.readLine()) != null) {
                lines.add(line);  // 将每行添加到 List 中
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\03-code\\lianglianglee.com\\lessons.log";  // 指定文件路径
        List<String> lines = readFileToList(filePath);
        lines.forEach(item->{
            try {
                String decode = URLDecoder.decode(item, "UTF-8");
                String[] ttt = decode.split("专栏/");
                String folder = ttt[1];
                System.out.println(folder);
//                createFolder(folder);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        List<String> strings = fetchMarkdownLinks(lines.get(0));
        // strings 写入 index.html
        strings.forEach(str->{
            System.out.println(str);

        });

        fetchMarkdownFile(strings.get(0));

    }

    // 4. 使用 Apache HttpClient 请求每个 markdown 页面并保存 class=book-post 的内容
//    public static void saveMarkdownContent(String baseUrl, Path folderPath, List<String> markdownLinks) throws IOException {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            for (String link : markdownLinks) {
//                String pageUrl = baseUrl + "/" + link;
//                HttpGet request = new HttpGet(pageUrl);
//                try (CloseableHttpResponse response = httpClient.execute(request)) {
//                    HttpEntity entity = response.getEntity();
//                    if (entity != null) {
//                        String result = EntityUtils.toString(entity);
//                        Document doc = Jsoup.parse(result);
//
//                        // 4.2 提取 class=book-post 的 div 内容
//                        Element bookPostDiv = doc.selectFirst(".book-post");
//                        if (bookPostDiv != null) {
//                            String content = bookPostDiv.html();
//                            Path filePath = folderPath.resolve(link + ".html");
//                            Files.writeString(filePath, content);
//                            System.out.println("内容已保存到: " + filePath.toString());
//                        } else {
//                            System.out.println("未找到 class=book-post 内容: " + pageUrl);
//                        }
//                    }
//                }
//            }
//        }
//    }

    // 主函数
//    public static void main(String[] args) {
//        String A = "your-course-name";  // 替换为实际的课程名称
//        try {
//            // 1. 创建文件夹
//            Path folderPath = createFolder(A);
//
//            // 2. 拼接 URL
//            String baseUrl = "https://lianglianglee.com/%E4%B8%93%E6%A0%8F/" + A;
//
//            // 3. 请求课程页面，获取 markdown 链接列表
//            List<String> markdownLinks = fetchMarkdownLinks(baseUrl);
//            if (!markdownLinks.isEmpty()) {
//                System.out.println("找到的 Markdown 链接: " + markdownLinks);
//
//                // 4. 遍历数组并保存页面内容
//                saveMarkdownContent(baseUrl, folderPath, markdownLinks);
//            } else {
//                System.out.println("未找到任何 markdown 链接");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
