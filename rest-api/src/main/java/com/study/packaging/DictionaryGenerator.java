package com.study.packaging;

import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class DictionaryGenerator {

    public static void main(String[] args) throws IOException {
//        args = new String[]{
//                "D:\\workspace\\SelfStudy\\Examples\\MavenOptionalModules\\rest-api\\target\\test.txt",
//                "100",
//        };

        /*
            args[0] : dictionary file name with full path
            args[1] : dictionary type (1 = packages, 2 = classes, 3 = members)
         */
        if(args.length < 2) {
            throw new RuntimeException("Provide valid arguments");
        }

        Path filePath = Paths.get(args[0]);
        int lengthOfFile = Integer.parseInt(args[1]);

        System.out.println("Generating Dictionary File (" + filePath.getFileName() +
                ") with unique random string lines of length " + lengthOfFile + "...");

        Set<String> lines = new HashSet<>();
        while (lengthOfFile-- != 0) {
            if(!lines.add(getRandomString(5))){
                lengthOfFile++;
            }
        }

        Files.writeString(filePath, String.join(System.lineSeparator(), lines));

        System.out.println("Dictionary File (" + filePath.getFileName() +
                ") has been generated successfully.");

    }

    private static SecureRandom random = new SecureRandom();
    public static String getRandomString(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        String randomString = "void";
        while(SourceVersion.isKeyword(randomString)) {
            randomString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }
        return randomString;
    }
}
