package me.zeanzai.geektime;

import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GeekTimeMainApp {


    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        String destinationDirectory = "D:\\03-code\\lianglianglee.com";
        String lessonName = "22 讲通关 Go 语言-完";
        File directory = new File(
                destinationDirectory + File.separator
                        + lessonName + File.separator + "assets"
        );
        if (!directory.exists()) {
            directory.mkdirs(); // 如果目录不存在，创建目录
        }

        String lessonPath = "/专栏/"+lessonName;
        Document chapterListDoc = GeekTimeUtils2.getRespDoc(lessonPath);
        List<String> chapterList = GeekTimeUtils2.getLessonListOrChapterList(chapterListDoc);
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
        for (String chapterFileName : chapterList) {
            Document chapterDoc = GeekTimeUtils2.getRespDoc(chapterFileName);
            String chapterName = chapterFileName.split("/")[3];
            GeekTimeUtils2.downloadMarkdownFile(chapterDoc, lessonName, chapterName, destinationDirectory);
            Thread.sleep(2000);
        }
    }
}
