package me.zeanzai.gt;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileOrganizer {

    private static final String SOURCE_FOLDER = "E:\\极客时间"; // 源文件夹路径
    private static final String TARGET_FOLDER = "E:\\极客时间-整理后"; // 目标文件夹路径

    private static final Set<String> HTML_EXTENSIONS = new HashSet<>(Arrays.asList(".html", ".ecdl"));
    private static final Set<String> PDF_EXTENSIONS = new HashSet<>(Arrays.asList(".pdf", ".pptx"));
    private static final Set<String> AUDIO_EXTENSIONS = new HashSet<>(Arrays.asList(".mp3", ".m4a", ".mp4"));

    public static void main(String[] args) {
        try {
            organizeFiles(Paths.get(SOURCE_FOLDER), Paths.get(TARGET_FOLDER));
            System.out.println("整理完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void organizeFiles(Path sourceFolder, Path targetFolder) throws IOException {
        Files.walkFileTree(sourceFolder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // 如果是文件
                if (Files.isRegularFile(file)) {
                    organizeFile(file, sourceFolder, targetFolder);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // 遇到目录时创建课程目录结构
                String relativePath = sourceFolder.relativize(dir).toString();
                if (!relativePath.isEmpty()) {
                    createCourseStructure(relativePath, targetFolder);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void createCourseStructure(String relativePath, Path targetFolder) {
        String[] parts = relativePath.split("\\\\"); // 根据反斜杠分隔课程和章节
        if (parts.length > 0) {
            String courseName = parts[0];
            Path htmlFolder = targetFolder.resolve("html").resolve(courseName);
            Path pdfFolder = targetFolder.resolve("pdf").resolve(courseName);
            Path audioFolder = targetFolder.resolve("audio").resolve(courseName);
            createFolderIfNotExists(htmlFolder);
            createFolderIfNotExists(pdfFolder);
            createFolderIfNotExists(audioFolder);

            // 继续创建章节结构
            if (parts.length > 1) {
                String chapterName = parts[1];
                htmlFolder.resolve(chapterName);
                pdfFolder.resolve(chapterName);
                audioFolder.resolve(chapterName);
            }
        }
    }

    private static void createFolderIfNotExists(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void organizeFile(Path file, Path sourceFolder, Path targetFolder) throws IOException {
        String fileName = file.getFileName().toString();
        String fileExtension = getFileExtension(fileName).toLowerCase();
        String relativePath = sourceFolder.relativize(file.getParent()).toString();
        String[] parts = relativePath.split("\\\\");

        if (parts.length > 0) {
            String courseName = parts[0];
            String chapterName = (parts.length > 1) ? parts[1] : "";

            Path targetSubfolder = null;

            // 分类文件
            if (HTML_EXTENSIONS.contains(fileExtension)) {
                targetSubfolder = targetFolder.resolve("html").resolve(courseName).resolve(chapterName);
            } else if (PDF_EXTENSIONS.contains(fileExtension)) {
                targetSubfolder = targetFolder.resolve("pdf").resolve(courseName).resolve(chapterName);
            } else if (AUDIO_EXTENSIONS.contains(fileExtension)) {
                targetSubfolder = targetFolder.resolve("audio").resolve(courseName).resolve(chapterName);
            }

            // 如果是目标文件类型，则移动文件
            if (targetSubfolder != null) {
                createFolderIfNotExists(targetSubfolder);
                Path targetFile = targetSubfolder.resolve(fileName);
                Files.move(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
    }
}