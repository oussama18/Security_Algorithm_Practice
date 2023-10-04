package com.company.BruteForce;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMD5(String input){
        try {
            MessageDigest ms = MessageDigest.getInstance("MD5");

            byte[] messageDigest = ms.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);


            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
