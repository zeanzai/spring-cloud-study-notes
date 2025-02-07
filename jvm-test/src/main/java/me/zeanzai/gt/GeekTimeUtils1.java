package me.zeanzai.gt;

import me.zeanzai.geektime.GeektimeUtil;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GeekTimeUtils1 {
    public static CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String SCHEME = "https";
    public static String HOST = "learn.lianglianglee.com";

    public static void main(String[] args) throws URISyntaxException, IOException {
        String destinationDirectory = "D:\\03-code\\lianglianglee.com";
        String lessonName = "Flutter入门教程";
        File directory = new File(
                destinationDirectory + File.separator
                        + lessonName + File.separator + "assets"
        );
        if (!directory.exists()) {
            directory.mkdirs(); // 如果目录不存在，创建目录
        }

        List<String> chapterList = getLessonListOrChapterList("/专栏/" + lessonName);
        chapterList.forEach(System.out::println);
        Path path = Paths.get(destinationDirectory + File.separator
                + lessonName + File.separator + "lesson.log");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(path)))) {
            for (String item : chapterList) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        ExecutorService executorService = Executors.newFixedThreadPool(chapterList.size());

        chapterList.forEach(chapter->{
            executorService.submit(()->{
                try {
                    Thread.sleep(GeektimeUtil.getRandomNumber(5, 10));
                    downloadMarkdownFile(chapter, lessonName, chapter.split("/")[3],destinationDirectory);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        executorService.shutdown();
//        httpClient.close();
    }

    public static int getRandomNumber(int n, int m) {
        Random random = new Random();
        // 如果 n > m, 可以交换它们，确保范围正确
        if (n > m) {
            int temp = n;
            n = m;
            m = temp;
        }
        // 生成 [n, m] 范围的随机数
        return random.nextInt(m - n + 1) + n;
    }

    public static List<String> getLessonListOrChapterList(String reqPath) throws URISyntaxException, IOException {
        URI uri = new URIBuilder()
                .setScheme(SCHEME)
                .setHost(HOST)
                .setPath(reqPath)
                .build();
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        if (response.getStatusLine().getStatusCode() == 200 && entity != null) {

            try {
                String result = EntityUtils.toString(entity);
                Document doc = Jsoup.parse(result);
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
            } finally {
                response.close();
            }
        }
        return null;
    }



    public static void downloadMarkdownFile(String reqPath,
                                            String lessonName,
                                            String chapterName,
                                            String destFolder) throws URISyntaxException, IOException {
        URI pageUri = new URIBuilder()
                .setScheme(SCHEME)
                .setHost(HOST)
                .setPath(reqPath)
                .build();
        HttpGet pageReq = new HttpGet(pageUri);
        CloseableHttpResponse pageRsp = httpClient.execute(pageReq);
        try {
            HttpEntity pageRspEntity = pageRsp.getEntity();
            if (pageRsp.getStatusLine().getStatusCode() == 200 && pageRspEntity != null) {
                Document doc = Jsoup.parse(EntityUtils.toString(pageRspEntity));
                Element bookPostDiv = doc.selectFirst(".book-post");

                if (bookPostDiv != null) {

                    // 解析 图片 ，并把图片下载下来保存到文件夹
                    Elements imageList = doc.getElementsByTag("img");
                    if (imageList.size() > 0) {
                        for (Element element : imageList) {
                            String src = element.attributes().get("src");
                            if (src.startsWith("assets")) {
                                URI imgUri = new URIBuilder()
                                        .setScheme(SCHEME)
                                        .setHost(HOST)
                                        .setPath("/专栏/" + lessonName + "/" + src)
                                        .build();
                                HttpGet imgRequest = new HttpGet(imgUri);
                                CloseableHttpResponse imgResp = httpClient.execute(imgRequest);
                                try {
                                    HttpEntity imgEntity = imgResp.getEntity();
                                    if (imgResp.getStatusLine().getStatusCode() == 200 && imgEntity != null) {
                                        String imgFilePath = destFolder + File.separator
                                                + lessonName + File.separator
                                                + "assets" + File.separator
                                                + src.split("/")[1];
                                        FileOutputStream outputStream = new FileOutputStream(imgFilePath);
                                        InputStream inputStream = imgEntity.getContent();
                                        byte[] buffer = new byte[4096];
                                        int bytesRead;
                                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                                            outputStream.write(buffer, 0, bytesRead);
                                        }
                                        EntityUtils.consume(imgEntity);

                                        element.attributes().remove("src");
                                        element.attributes().add("src", "./" + src);
                                    }
                                } finally {
                                    imgResp.close();
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
                    EntityUtils.consume(pageRspEntity);
                }
            }

        } finally {
            pageRsp.close();
        }
    }
}