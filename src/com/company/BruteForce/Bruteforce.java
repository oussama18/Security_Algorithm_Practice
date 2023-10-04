package com.company.BruteForce;


public class Bruteforce {
    private static String carac1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String carac2 = carac1 + carac1.toLowerCase();
    private static String carac3 = carac2 + "0123456789";



    public static String bruteforce(String hashText,int i) {
        return textTest(hashText,"AAAA",i);
    }

    private static String textTest(String hashtext, String textTest,int i) {

        System.out.println(textTest);

        while (true) {
            if (MD5.getMD5(textTest).equals(hashtext))
                return textTest;


            textTest = textIncre(textTest, 3,i);
            System.out.println(textTest);
        }

    }

    // i est l'indice de question: l'ensemble de recherche de mot de passe
    // cette fonction prend une chaine de caractère et retourne la chaine suivante
    // pour tester sa validité
    // par example : on lui passe la chaine AAAC, elle va retourner AAAD
    private static String textIncre(String textTest, int indiceOfsset,int i) {
        String carac;
        switch (i) {
            case 1 : carac = carac1;break;
            case 2 : carac = carac2;break;
            case 3 : carac = carac3;break;
            default:
                throw new IllegalStateException("Unexpected value: " + i);
        }

        if (indiceOfsset == -1)
            return null;

        if (textTest.charAt(indiceOfsset) == carac.charAt(carac.length()-1)) {
            char[] test = textTest.toCharArray();
            test[indiceOfsset] = 'A';
            return textIncre(String.valueOf(test), indiceOfsset-1,i);
        }
        char[] test = textTest.toCharArray();
        test[indiceOfsset] = carac.charAt(carac.indexOf(test[indiceOfsset]) + 1);

        return String.valueOf(test);
    }
}
