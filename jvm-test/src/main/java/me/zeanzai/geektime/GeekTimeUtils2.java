package me.zeanzai.geektime;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GeekTimeUtils2 {
    public static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String SCHEME = "https";
    public static String HOST = "learn.lianglianglee.com";

    // 1. 发送请求，并返回响应结果
    public static HttpEntity getRespEntity(String reqPath) throws IOException, URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme(SCHEME)
                .setHost(HOST)
                .setPath(reqPath)
                .build();
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(request);
        try {
            if (response.getStatusLine().getStatusCode() == 200 ) {
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    return entity;
                }
                return null;
            }
            return null;
        } finally {
            response.close();
        }
    }

    public static Document getRespDoc(String reqPath) throws IOException, URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme(SCHEME)
                .setHost(HOST)
                .setPath(reqPath)
                .build();
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(request);
        try {
            if (response.getStatusLine().getStatusCode() == 200 ) {
                HttpEntity entity = response.getEntity();
                if (entity != null){
                    String result = EntityUtils.toString(entity);
                    return Jsoup.parse(result);
                }
                return null;
            }
            return null;
        } finally {
            response.close();
        }
    }

    // 2. 处理 文本 ，获取 课程列表 或 章节列表
    public static List<String> getLessonListOrChapterList(Document doc) {
        if (doc == null) {
            return null;
        }
        List<String> links = new ArrayList<>();
        Elements bookPostDiv = doc.select(".book-post ul a");
        for (Element link : bookPostDiv) {
            String href = link.attr("href");
            if (!href.isEmpty()) {
                links.add(href);
            }
        }
        return links;
    }

    // 3. 处理 响应文本， 获取 markdown 内容
    public static void downloadMarkdownFile(Document doc,
                                            String lessonName,
                                            String chapterName,
                                            String destFolder) throws URISyntaxException, IOException {
        if (doc != null) {
            Element bookPostDiv = doc.selectFirst(".book-post");
            if (bookPostDiv != null) {

                // 解析 图片 ，并把图片下载下来保存到文件夹
                Elements imageList = doc.getElementsByTag("img");
                if (imageList.size() > 0) {
                    for (Element element : imageList) {
                        String src = element.attributes().get("src");
                        if (src.startsWith("assets")) {
                            URI uri = new URIBuilder()
                                    .setScheme(SCHEME)
                                    .setHost(HOST)
                                    .setPath("/专栏/" + lessonName + "/" + src)
                                    .build();
                            HttpGet request = new HttpGet(uri);
                            CloseableHttpResponse response = httpClient.execute(request);
                            try {
                                if (response.getStatusLine().getStatusCode() == 200 ) {
                                    HttpEntity entity = response.getEntity();
                                    if (entity != null){
                                        String imgFilePath = destFolder + File.separator
                                                + lessonName + File.separator
                                                + "assets" + File.separator
                                                + src.split("/")[1];
                                        FileOutputStream outputStream = new FileOutputStream(imgFilePath);
                                        InputStream inputStream = entity.getContent();
                                        byte[] buffer = new byte[4096];
                                        int bytesRead;
                                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                                            outputStream.write(buffer, 0, bytesRead);
                                        }
                                        EntityUtils.consume(entity);

                                        element.attributes().remove("src");
                                        element.attributes().add("src", "./" + src);
                                    }
                                }
                            } finally {
                                response.close();
                            }
                        }
                    }
                }

                // 下载markdown文件
                String markdownFileFullPath = destFolder + File.separator
                        + lessonName + File.separator
                        + chapterName;
                File markdownFile = new File(markdownFileFullPath);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(markdownFile))) {
                    writer.write(bookPostDiv.html());
                    writer.newLine();  // 写入换行符
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }


}
