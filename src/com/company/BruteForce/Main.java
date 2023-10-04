package com.company.BruteForce;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        String hash = MD5.getMD5("BEFG");

        // i est l identifiant d'exercice(1 majuscule, 2 mini et majusc, 3 lettre et chiffre)
        String claireText = Bruteforce.bruteforce(hash,1);
        System.out.println(claireText);


//        String c = "6";
//
//        byte[] f = c.getBytes();
//
//        BigInteger d = new BigInteger(1,f);
//
//        String hex = d.toString(16);
//
//        System.out.println(hex);

    }
}
