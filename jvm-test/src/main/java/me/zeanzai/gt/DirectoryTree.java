package me.zeanzai.gt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class DirectoryTree {

    public static void main(String[] args) {
        // 测试路径，替换成实际的路径
        String folderPath = "E:\\极客时间-整理后\\html";

        // 获取文件夹的目录树
        try {
            Map<String, Object> directoryTree = getDirectoryTree(folderPath);
            // 输出 JSON 格式的目录树
            System.out.println(toJson(directoryTree));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取文件夹的目录树
    public static Map<String, Object> getDirectoryTree(String folderPath) throws Exception {
        Path rootPath = Paths.get(folderPath);
        Map<String, Object> directoryTree = new LinkedHashMap<>();

        Files.walk(rootPath)
                .filter(Files::isDirectory) // 只处理文件夹
                .forEach(path -> {
                    // 获取相对路径
                    Path relativePath = rootPath.relativize(path);
                    if (!relativePath.toString().equals("")) {
                        // 添加文件夹路径
                        Map<String, Object> subTree = getDirectorySubTree(path, rootPath);
                        directoryTree.put(relativePath.toString(), subTree);
                    }
                });

        return directoryTree;
    }

    // 获取子目录的树，包含文件的相对路径
    private static Map<String, Object> getDirectorySubTree(Path folderPath, Path rootPath) {
        Map<String, Object> subTree = new LinkedHashMap<>();

        try {
            Files.walk(folderPath)
                    .filter(Files::isRegularFile) // 只处理文件
                    .forEach(filePath -> {
                        Path relativePath = rootPath.relativize(filePath);
                        subTree.put(filePath.getFileName().toString(), relativePath.toString());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subTree;
    }

    // 将目录树转换为JSON格式字符串
    private static String toJson(Map<String, Object> directoryTree) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");

        directoryTree.forEach((key, value) -> {
            jsonBuilder.append("  \"").append(key).append("\": ");
            jsonBuilder.append(value instanceof Map ? toJson((Map<String, Object>) value) : "\""+value+"\"");
            jsonBuilder.append(",\n");
        });

        // 删除最后一个逗号
        if (jsonBuilder.length() > 2) {
            jsonBuilder.delete(jsonBuilder.length() - 2, jsonBuilder.length());
        }

        jsonBuilder.append("\n}");
        return jsonBuilder.toString();
    }
}

