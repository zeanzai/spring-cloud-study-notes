package me.zeanzai.gt;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileTypeFetcher {

    /**
     * 获取指定文件夹下所有文件的文件类型
     * @param folderPath 要扫描的文件夹路径
     * @return 包含文件类型的集合
     * @throws IOException 如果文件访问发生错误
     */
    public static Set<String> getAllFileTypes(String folderPath) throws IOException {
        Set<String> fileTypes = new HashSet<>();

        Path folder = Paths.get(folderPath);

        // 使用 Files.walk 遍历文件夹及其子文件夹
        Files.walk(folder)
                .filter(Files::isRegularFile) // 过滤掉文件夹，只保留文件
                .forEach(file -> {
                    // 获取文件扩展名并加入结果集合
                    String fileType = getFileExtension(file.toString());
                    if (!fileType.isEmpty()) {
                        fileTypes.add(fileType);
                    }

                    System.out.println(file.toAbsolutePath());
                });

        return fileTypes;
    }

    /**
     * 获取文件的扩展名
     * @param filePath 文件路径
     * @return 文件扩展名（小写）
     */
    private static String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filePath.substring(dotIndex).toLowerCase();
    }

    public static void main(String[] args) {
        try {
            // 示例：传入文件夹路径，获取文件类型
            String folderPath = "E:\\极客时间"; // 替换为你自己的文件夹路径
            Set<String> fileTypes = getAllFileTypes(folderPath);

            // 打印所有文件类型
            System.out.println("所有文件类型：");
            fileTypes.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
