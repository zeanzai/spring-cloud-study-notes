package me.zeanzai.test2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@JsonInclude(JsonInclude.Include.NON_NULL)
class TreeNode {
    private String name;
    private String path;
    private List<TreeNode> children;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public List<TreeNode> getChildren() { return children; }
    public void setChildren(List<TreeNode> children) { this.children = children; }
}

public class CourseJsonGenerator {
    public static void generateCourseJson(String testDirPath, String outputJsonPath) throws IOException {
        Path rootPath = Paths.get(testDirPath);
        List<TreeNode> courseNodes = new ArrayList<>();

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(rootPath)) {
            for (Path coursePath : dirStream) {
                if (Files.isDirectory(coursePath)) {
                    TreeNode node = processDirectory(coursePath, rootPath);
                    courseNodes.add(node);
                }
            }
        }

        courseNodes.sort(Comparator.comparing(TreeNode::getName));
        new ObjectMapper().writeValue(new File(outputJsonPath), courseNodes);
    }

    private static TreeNode processDirectory(Path dirPath, Path rootPath) throws IOException {
        TreeNode node = new TreeNode();
        node.setName(dirPath.getFileName().toString());
        node.setChildren(new ArrayList<>());

        List<Path> children = Files.list(dirPath)
                .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                .collect(Collectors.toList());

        for (Path child : children) {
            if (Files.isDirectory(child)) {
                node.getChildren().add(processDirectory(child, rootPath));
            } else if (child.toString().endsWith(".html")) {
                TreeNode fileNode = new TreeNode();
                String fileName = child.getFileName().toString().replace(".html", "");
                fileNode.setName(fileName);
                fileNode.setPath(rootPath.relativize(child).toString().replace(File.separator, "/"));
                node.getChildren().add(fileNode);
            }
        }

        node.getChildren().sort(Comparator.comparing(TreeNode::getName));
        return node;
    }

    public static void main(String[] args) throws IOException {
        generateCourseJson("E:\\极客时间-整理后\\html", "E:\\极客时间-整理后\\html\\courses.json");
    }
}