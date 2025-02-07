package me.zeanzai.gt;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DirectoryTreeToJson {

    public static void main(String[] args) {
        String folderPath = "E:\\极客时间-整理后\\html"; // 输入文件夹路径
        try {
            JSONObject jsonTree = buildDirectoryTree(new File(folderPath));
            try (FileWriter file = new FileWriter("directoryTree.json")) {
                file.write(jsonTree.toString(4)); // 格式化输出
            }
            System.out.println("JSON file has been created.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 构建文件夹目录树
    private static JSONObject buildDirectoryTree(File folder) {
        JSONObject tree = new JSONObject();

        // 如果是文件，返回文件名称和相对路径
        if (folder.isFile()) {
            tree.put(folder.getName(), folder.getPath());
            return tree;
        }

        // 如果是文件夹，遍历该文件夹中的文件
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                // 递归调用，构建子目录树
                tree.put(file.getName(), buildDirectoryTree(file));
            }
        }
        return tree;
    }
}
