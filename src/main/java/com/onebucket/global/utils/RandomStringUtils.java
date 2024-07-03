package com.onebucket.global.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * <br>package name   : com.onebucket.global.utils
 * <br>file name      : RandomStringUtils
 * <br>date           : 2024-07-01
 * <pre>
 * <span style="color: white;">[description]</span>
 * Utility class for generate random string.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 * randomStringUtils.generateRandomNum(4); // 0~1000까지의 랜덤한 수
 * randomStringUtils.generateRangeRandomNum(100,1000); // 100~ 1000까지의 랜덤한 수
 * randomStringUtils.generateRandomStr(10); //10자리의 무작위 문자열
 * randomStringUtils.generateRandomStr(10, true, false); // 대문자로만 구성된 무작위 문자열
 * } </pre>
 * <pre>
 * modified log :
 * ====================================================
 * DATE           AUTHOR               NOTE
 * ----------------------------------------------------
 * 2024-07-01        jack8              init create
 * </pre>
 */
@Component
public class RandomStringUtils {

    public int generateRandomNum(int length) {
        SecureRandom random = new SecureRandom();
        int upperLimit =(int) Math.pow(10, length);
        return random.nextInt(upperLimit);
    }

    public int generateRandomNum(int start, int end) {
        SecureRandom secureRandom = new SecureRandom();
        return start + secureRandom.nextInt(end + 1);
    }

    public String generateRandomStr(int length) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String characters;


        characters = upperCaseLetters + lowerCaseLetters + numbers;


        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    public String generateRandomStr(int length, boolean isUpperCaseOnly, boolean isLowerCaseOnly) {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String characters;

        if (isUpperCaseOnly) {
            characters = upperCaseLetters + numbers;
        } else if (isLowerCaseOnly) {
            characters = lowerCaseLetters + numbers;
        } else {
            characters = upperCaseLetters + lowerCaseLetters + numbers;
        }

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

}
