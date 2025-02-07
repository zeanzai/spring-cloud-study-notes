package me.zeanzai.geektime;

import io.github.furstenheim.CodeBlockStyle;
import io.github.furstenheim.CopyDown;
import io.github.furstenheim.Options;
import io.github.furstenheim.OptionsBuilder;
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

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeektimeUtil {
    public static CloseableHttpClient httpClient = HttpClients.createDefault();

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

    public static String htmlTansToMarkdown(String htmlStr) {
        OptionsBuilder optionsBuilder = OptionsBuilder.anOptions();
        Options options = optionsBuilder.withBr("-").withCodeBlockStyle(CodeBlockStyle.FENCED)
                // more options
                .build();
        CopyDown converter = new CopyDown(options);
        return converter.convert(htmlStr);
    }
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


    /**
     * 1. 读取文件，获取所有课程列表， 并转化为list
     * 2. 处理每一个课程
     *      1. 判断 课程文件夹 是否存在，如果不存在就创建；
     *      2. 请求 每一个课程 ，获得章节名称列表，形成 lessonsList ，并保存到 lessons.log 文件中；
     * 3. 处理每一个章节
     *      1. 判断 章节文件 是否存在，如果不存在就创建；
     *      2. 请求 每一个章节 ，获取章节内容：
     *          1. 下载 图片 等内容；
     *          2. 转化为markdown；
     *          3. 输出到文件；
     */

    /**
     * 1. 读取文件，获取所有课程列表， 并转化为list
     * @param filePath
     * @return
     */
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


    /**
     * 2. 处理每一个课程
     *      1. 判断 课程文件夹 是否存在，如果不存在就创建；
     *      2. 请求 每一个课程 ，获得章节名称列表，形成 lessonsList ，并保存到 lessons.log 文件中；
     *
     *      https://lianglianglee.com/%E4%B8%93%E6%A0%8F/%E9%AB%98%E6%A5%BC%E7%9A%84%E6%80%A7%E8%83%BD%E5%B7%A5%E7%A8%8B%E5%AE%9E%E6%88%98%E8%AF%BE
     *      https://lianglianglee.com/专栏/高楼的性能工程实战课
     */
    public static List<String> getLessonsList(String line) throws IOException {
        String decode = URLDecoder.decode(line, "UTF-8");
        String[] urlsplit = decode.split("/");
        String zhuanlan = urlsplit[3];
        String courseName = urlsplit[4];
        String fullPath = "D:/03-code/lianglianglee.com" + File.separator + zhuanlan + File.separator + courseName;
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs(); // 如果目录不存在，创建目录
        }

        List<String> links = new ArrayList<>();
        HttpGet request = new HttpGet(encodeChineseAndSpaces(line));
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
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


        String filePath = fullPath + File.separator + "index.log";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        links.forEach(t->{

            try {
                writer.write(t);
                writer.newLine();  // 写入换行符
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return links;
    }


    /**
     * 3. 处理每一个章节
     *      1. 判断 章节文件 是否存在，如果不存在就创建；
     *      2. 请求 每一个章节 ，获取章节内容：
     *          1. 下载 图片 等内容；
     *          2. 转化为markdown；
     *          3. 输出到文件；
     *
     * /12步通关求职面试-完/00 开篇词：了解面试“潜规则”，从海选中脱颖而出.md
     *
     */
    public static void downloadMarkdownFile(String fileURL, String destinationDirectory) throws IOException {
        String markdownFileUrl = "https://lianglianglee.com"+fileURL;

        String decode = URLDecoder.decode(markdownFileUrl, "UTF-8");
        String[] urlsplit = decode.split("/");
        String zhuanlan = urlsplit[3];
        String courseName = urlsplit[4];
        String markdownFileName=urlsplit[5];
        String assertsFolderName = "assets";

        // 创建 assets 文件夹
        String fullPath = destinationDirectory + File.separator + zhuanlan + File.separator + courseName + File.separator + assertsFolderName;
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs(); // 如果目录不存在，创建目录
        }

        // 下载  markdownFile
        HttpGet requestMarkdown = new HttpGet(encodeChineseAndSpaces(markdownFileUrl));
        CloseableHttpResponse requestMarkdownResponse = httpClient.execute(requestMarkdown);
        HttpEntity markdownEntity = requestMarkdownResponse.getEntity();
        if (requestMarkdownResponse.getStatusLine().getStatusCode() == 200 && markdownEntity != null) {
            String result = EntityUtils.toString(markdownEntity);
            Document doc = Jsoup.parse(result);
            Element bookPostDiv = doc.selectFirst(".book-post");

            if (bookPostDiv != null) {
                Elements imageList = doc.getElementsByTag("img");
                for (Element element : imageList) {
                    String src = element.attributes().get("src");
                    if (src.startsWith("assets")) {

                        String imgUrl = "https://lianglianglee.com" + "/" + zhuanlan + "/" + courseName + "/" + src;
                        String imgPath = destinationDirectory + File.separator
                                + zhuanlan + File.separator
                                + courseName + File.separator
                                + "assets" + File.separator
                                + src.split("/")[1];
                        HttpGet requestImg = new HttpGet(encodeChineseAndSpaces(imgUrl));
                        CloseableHttpResponse responseImg = httpClient.execute(requestImg);
                        HttpEntity entityImg = responseImg.getEntity();
                        if (responseImg.getStatusLine().getStatusCode() == 200 && entityImg != null) {
                            FileOutputStream outputStream = new FileOutputStream(imgPath);
                            InputStream inputStream = entityImg.getContent();
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                        } else {
                            System.out.println("下载失败，状态码: " + responseImg.getStatusLine().getStatusCode());
                        }
                        EntityUtils.consume(entityImg);

                        element.attributes().remove("src");
                        element.attributes().add("src", "./" + src);
                    }
                }

                // 保存markdownfile
                String content = bookPostDiv.html();
                String markdownFilename = destinationDirectory + File.separator
                        + zhuanlan + File.separator
                        + courseName + File.separator
                        + markdownFileName;
                File markdownFile = new File(markdownFilename);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(markdownFile))) {
                    writer.write(htmlTansToMarkdown(content));
                    writer.newLine();  // 写入换行符
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        EntityUtils.consume(markdownEntity);
    }


















    public static void dealOneSingleLesson(String lesson) throws IOException {

        String url = "https://lianglianglee.com"+encodeChineseAndSpaces(lesson);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity);
                    Document doc = Jsoup.parse(result);

                    Elements imageList = doc.getElementsByTag("img");
                    for (Element element : imageList) {
                        String destinationDirectory = "D:/03-code/lianglianglee.com";
                        String src = element.attributes().get("src");
                        if(src.startsWith("assets")){
                            String[] split = lesson.split("/");
                            String fileURL = "https://lianglianglee.com/"+split[1]+"/"+split[2]+"/"+src;
                            downloadFile(httpClient, fileURL, destinationDirectory);

                            element.attributes().remove("src");
                            element.attributes().add("src", "./" + src);
                        }
                    }

                    Element bookPostDiv = doc.selectFirst(".book-post");
                    if (bookPostDiv != null) {
                        String content = bookPostDiv.html();

                        Path folderPath = Paths.get("D:/03-code/lianglianglee.com", lesson);
                        File markdownFile = new File(folderPath.toString());

                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(markdownFile))) {
                            writer.write(htmlTansToMarkdown(content));
                            writer.newLine();  // 写入换行符
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


//                        System.out.println(htmlTansToMarkdown(content));
                    } else {
                        System.out.println("未找到 class=book-post 内容: " + url);
                    }
                }
            }
        }

    }
    public static void downloadFile(CloseableHttpClient httpClient, String fileURL, String destinationDirectory) throws IOException {
        // 创建HttpClient实例
//        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 解析 URL，获取文件名
        String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1); // 提取文件名
        String subDir = fileURL.replaceFirst("https://[^/]+/", ""); // 获取URL的相对路径部分
        subDir = subDir.substring(0, subDir.lastIndexOf("/")); // 去除文件名部分

        // 在给定路径下创建目录
        String fullPath = destinationDirectory + File.separator + subDir ;
        File directory = new File(fullPath);
        if (!directory.exists()) {
            directory.mkdirs(); // 如果目录不存在，创建目录
        }

        // 设置文件保存路径
        String filePath = fullPath + File.separator + fileName;

        // 创建HttpGet请求
        HttpGet request = new HttpGet(fileURL);

        // 发送请求并获取响应
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();

            // 检查响应状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 下载并保存文件
                if (entity != null) {
                    try (InputStream inputStream = entity.getContent();
                         FileOutputStream outputStream = new FileOutputStream(filePath)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        System.out.println("文件已下载到: " + filePath);
                    }
                }
            } else {
                System.out.println("下载失败，状态码: " + response.getStatusLine().getStatusCode());
            }
            // 确保实体内容完全消费完毕
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

