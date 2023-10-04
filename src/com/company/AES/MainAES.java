package com.company.AES;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainAES {
    public static void main(String[] args) throws IOException {

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        // Determine whether the user is encrypting or decrypting.
        boolean mode;
        System.out.println("Voulez-vous (E)ncrypt ou (D)ecrypt?");
        char ans = (stdin.readLine()).charAt(0);
        if  (ans == 'D')
            mode = true;
        else
            mode = false;

        // Read in input and output files.
        System.out.println("Entrer le fichier de départ.");
        String input = stdin.readLine();
        System.out.println("Entrer le fichier de sortie.");
        String output = stdin.readLine();


        int [] key = {
                1,2,3,4,
                5,6,7,8,
                9,10,11,12,
                13,14,15,16
        };

        if (mode)
            AES.AES_Decrypt(input,output,key);
        else
            AES.AES_Encrypt(input,output,key);
    }
}
