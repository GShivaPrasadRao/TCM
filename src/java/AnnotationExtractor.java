package com.buildtechknowledge.spring.data.mongodb;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class AnnotationExtractor {

    // Regex pattern for Java annotations
    private static final Pattern ANNOTATION_PATTERN = Pattern.compile("@\\w+(\\([^)]*\\))?");

    public static void main(String[] args) throws IOException {
        String packagePath = "src/main/java/com/buildtechknowledge/spring/data/mongodb";  // Update to your actual package path

        Set<String> distinctAnnotations = extractDistinctAnnotations(packagePath);

        System.out.println("Extracted Annotations:");
        distinctAnnotations.forEach(System.out::println);
    }

    public static Set<String> extractDistinctAnnotations(String packagePath) throws IOException {
        Set<String> annotations = new HashSet<>();

        Files.walk(Paths.get(packagePath))
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".java"))
                .forEach(path -> {
                    try {
                        String content = Files.readString(path);
                        Matcher matcher = ANNOTATION_PATTERN.matcher(content);
                        while (matcher.find()) {
                            // Extract only the annotation name (e.g., @RequestMapping from @RequestMapping("/api"))
                            String fullAnnotation = matcher.group();
                            String annotationName = fullAnnotation.split("\\(")[0];
                            annotations.add(annotationName);
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading file: " + path);
                        e.printStackTrace();
                    }
                });

        return annotations;
    }
}
