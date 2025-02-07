package me.zeanzai.geektime;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetSite {


    public static void main(String[] args) throws Exception {

//        extracted();

        String destinationDirectory = "D:\\03-code\\lianglianglee.com";
        List<String> lines = GeektimeUtil.readFileToList("D:\\03-code\\lianglianglee.com\\lessons.log");
        List<String> lessonsList = GeektimeUtil.getLessonsList(lines.get(11));
        ExecutorService executorService = Executors.newFixedThreadPool(lessonsList.size());
        lessonsList.forEach(lesson->{
            try {
                executorService.submit(()->{

                    try {
                        GeektimeUtil.downloadMarkdownFile(lesson, destinationDirectory);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
//        GeektimeUtil.httpClient.close();

    }

    private static void extracted() throws IOException {
        String destinationDirectory = "D:\\03-code\\geektime";
        List<String> lines = GeektimeUtil.readFileToList("D:\\03-code\\geektime\\lessons.log");
        lines.forEach(iterm->{
            try {
                Thread.sleep(GeektimeUtil.getRandomNumber(5, 10));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<String> lessonsList = null;
            try {
                lessonsList = GeektimeUtil.getLessonsList(iterm);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lessonsList.forEach(lesson->{
                try {
                    Thread.sleep(GeektimeUtil.getRandomNumber(5, 10));
                    GeektimeUtil.downloadMarkdownFile(lesson, destinationDirectory);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        GeektimeUtil.httpClient.close();
    }


}
