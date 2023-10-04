package com.company.AES;


import java.io.*;
import java.util.Arrays;

public class AES {


    final static boolean PLAIN = false;
    final static boolean CIPHER = true;
    private static boolean mode; // Either PLAIN or CIPHER

    private static int[] message; // Stores the message in ints.
    private static int[] outMessage;
    private static String inputfile; // Stores the name of the input file.
    private static String outputfile; // Stores the name of the output file.
    private static int[] key; // Stores the key.
    private static boolean msgstatus; // Stores whether message is currently the
    // plain text or cipher text.

    private static int msglength; // Stores the actual message length in bytes.


    private static int[] mixColumn(int [] state){

        int [] tmp = new int [16];

        tmp[0] = (LookTable.mc2[state[0]] ^ LookTable.mc3[state[1]] ^ state[2] ^ state[3]);
        tmp[1] = (state[0] ^ LookTable.mc2[state[1]] ^ LookTable.mc3[state[2]] ^ state[3]);
        tmp[2] = (state[0] ^ state[1] ^ LookTable.mc2[state[2]] ^ LookTable.mc3[state[3]]);
        tmp[3] = (LookTable.mc3[state[0]] ^ state[1] ^ state[2] ^ LookTable.mc2[state[3]]);

        tmp[4] = (LookTable.mc2[state[4]] ^ LookTable.mc3[state[5]] ^ state[6] ^ state[7]);
        tmp[5] = (state[4] ^ LookTable.mc2[state[5]] ^ LookTable.mc3[state[6]] ^ state[7]);
        tmp[6] = (state[4] ^ state[5] ^ LookTable.mc2[state[6]] ^ LookTable.mc3[state[7]]);
        tmp[7] = (LookTable.mc3[state[4]] ^ state[5] ^ state[6] ^ LookTable.mc2[state[7]]);

        tmp[8] = (LookTable.mc2[state[8]] ^ LookTable.mc3[state[9]] ^ state[10] ^ state[11]);
        tmp[9] = (state[8] ^ LookTable.mc2[state[9]] ^ LookTable.mc3[state[10]] ^ state[11]);
        tmp[10] = (state[8] ^ state[9] ^ LookTable.mc2[state[10]] ^ LookTable.mc3[state[11]]);
        tmp[11] = (LookTable.mc3[state[8]] ^ state[9] ^ state[10] ^ LookTable.mc2[state[11]]);

        tmp[12] = (LookTable.mc2[state[12]] ^ LookTable.mc3[state[13]] ^ state[14] ^ state[15]);
        tmp[13] = (state[12] ^ LookTable.mc2[state[13]] ^ LookTable.mc3[state[14]] ^ state[15]);
        tmp[14] = (state[12] ^ state[13] ^ LookTable.mc2[state[14]] ^ LookTable.mc3[state[15]]);
        tmp[15] = (LookTable.mc3[state[12]] ^ state[13] ^ state[14] ^ LookTable.mc2[state[15]]);

        return tmp;
    }

    private static int[] invMixColumn(int [] state){
        int [] tmp = new int [16];

        tmp[0] = (LookTable.mce[state[0]] ^ LookTable.mcb[state[1]] ^ LookTable.mcd[state[2]] ^ LookTable.mc9[state[3]]);
        tmp[1] = (LookTable.mc9[state[0]] ^ LookTable.mce[state[1]] ^ LookTable.mcb[state[2]] ^ LookTable.mcd[state[3]]);
        tmp[2] = (LookTable.mcd[state[0]] ^ LookTable.mc9[state[1]] ^ LookTable.mce[state[2]] ^ LookTable.mcb[state[3]]);
        tmp[3] = (LookTable.mcb[state[0]] ^ LookTable.mcd[state[1]] ^ LookTable.mc9[state[2]] ^ LookTable.mce[state[3]]);

        tmp[4] = (LookTable.mce[state[4]] ^ LookTable.mcb[state[5]] ^ LookTable.mcd[state[6]] ^ LookTable.mc9[state[7]]);
        tmp[5] = (LookTable.mc9[state[4]] ^ LookTable.mce[state[5]] ^ LookTable.mcb[state[6]] ^ LookTable.mcd[state[7]]);
        tmp[6] = (LookTable.mcd[state[4]] ^ LookTable.mc9[state[5]] ^ LookTable.mce[state[6]] ^ LookTable.mcb[state[7]]);
        tmp[7] = (LookTable.mcb[state[4]] ^ LookTable.mcd[state[5]] ^ LookTable.mc9[state[6]] ^ LookTable.mce[state[7]]);

        tmp[8] = (LookTable.mce[state[8]] ^ LookTable.mcb[state[9]] ^ LookTable.mcd[state[10]] ^ LookTable.mc9[state[11]]);
        tmp[9] = (LookTable.mc9[state[8]] ^ LookTable.mce[state[9]] ^ LookTable.mcb[state[10]] ^ LookTable.mcd[state[11]]);
        tmp[10] = (LookTable.mcd[state[8]] ^ LookTable.mc9[state[9]] ^ LookTable.mce[state[10]] ^ LookTable.mcb[state[11]]);
        tmp[11] = (LookTable.mcb[state[8]] ^ LookTable.mcd[state[9]] ^ LookTable.mc9[state[10]] ^ LookTable.mce[state[11]]);

        tmp[12] = (LookTable.mce[state[12]] ^ LookTable.mcb[state[13]] ^ LookTable.mcd[state[14]] ^ LookTable.mc9[state[15]]);
        tmp[13] = (LookTable.mc9[state[12]] ^ LookTable.mce[state[13]] ^ LookTable.mcb[state[14]] ^ LookTable.mcd[state[15]]);
        tmp[14] = (LookTable.mcd[state[12]] ^ LookTable.mc9[state[13]] ^ LookTable.mce[state[14]] ^ LookTable.mcb[state[15]]);
        tmp[15] = (LookTable.mcb[state[12]] ^ LookTable.mcd[state[13]] ^ LookTable.mc9[state[14]] ^ LookTable.mce[state[15]]);

        return tmp;
    }

    private static int[] subBytes(int [] state){
        SBox sBox = SBox.getInstance();

        return Arrays.stream(state).map(st -> sBox.box[st]).toArray();

    }

    private static int[] invSubBytes(int [] state){
        SBox sBox = SBox.getInstance();

        return Arrays.stream(state).map(st -> sBox.invBox[st]).toArray();

    }

    private static int[] addRoundKey(int []state, int [] key){
        int [] tmp = new int [16];

        for(int i=0;i<16;i++)
            tmp[i]  = state[i] ^ key [i];

        return tmp;
    }

    private static int[] shiftRows(int [] state) {
        int [] tmp = new int [16];

        tmp[0] = state [0];
        tmp[1] = state [5];
        tmp[2] = state [10];
        tmp[3] = state [15];

        tmp[4] = state [4];
        tmp[5] = state [9];
        tmp[6] = state [14];
        tmp[7] = state [3];

        tmp[8] = state [8];
        tmp[9] = state [13];
        tmp[10] = state [2];
        tmp[11] = state [7];

        tmp[12] = state [12];
        tmp[13] = state [1];
        tmp[14] = state [6];
        tmp[15] = state [11];

        return tmp;
    }

    private static int[] invShiftRows(int [] state) {
        int [] tmp = new int [16];

        tmp[0] = state [0];
        tmp[1] = state [13];
        tmp[2] = state [10];
        tmp[3] = state [7];

        tmp[4] = state [4];
        tmp[5] = state [1];
        tmp[6] = state [14];
        tmp[7] = state [11];

        tmp[8] = state [8];
        tmp[9] = state [5];
        tmp[10] = state [2];
        tmp[11] = state [15];

        tmp[12] = state [12];
        tmp[13] = state [9];
        tmp[14] = state [6];
        tmp[15] = state [3];

        return tmp;
    }

    private static int[] keyExpansionCore(int []in,int indexStep) {
        int temp [] = new int[4];

        // Rotate left
        temp [0] = in [1];
        temp [1] = in [2];
        temp [2] = in [3];
        temp [3] = in [0];

        //Sbox
        SBox sBox = SBox.getInstance();
        temp = Arrays.stream(temp).map(st -> sBox.box[st]).toArray();

        temp [0] ^= LookTable.rcon[indexStep];

        return temp;
    }

    private static int[] keyExpansion(int [] key) {
        int [] expandedKey = new int [176];

        // the first 16 bytes are the original key
        System.arraycopy(key, 0, expandedKey, 0, 16);

        int byteGenretad = 16; //we have generated 16 bytes so far
        int rconIteration = 1; // Rcon iteration at 1
        int[] temp = new int [4]; //temporary storage for core

        while (byteGenretad < 176) {
            // READ 4 bytes from core
            for (int i=0;i<4;i++) {
                temp [i] = expandedKey[i + byteGenretad-4];
            }

            // perform the core once for ech 16 bytes key
            if(byteGenretad % 16 == 0) {
                temp = keyExpansionCore(temp,rconIteration);
                rconIteration +=1;
            }

            //XOR temp with  [byteGenerated -16], and store it in expandKey
            for(int i=0;i<4;i++) {
                expandedKey[byteGenretad] = expandedKey [byteGenretad - 16] ^ temp [i];

                byteGenretad += 1;
            }
        }

        return expandedKey;
    }

    public static void AES_Encrypt(String inFile, String outFile, int key []) throws IOException {
        mode = PLAIN;

        inputfile = inFile;
        outputfile = outFile;
        message = new int[getMessageLength()];
        outMessage = new int[message.length];
        readMessage();


        for (int i=0;i<message.length;i += 16) {
            int []STATE = new int [16];

            for(int j=0;j<16;j++) {
                STATE [j] = message[i+j];
            }

            STATE = AES_Encrypt_state(STATE,key);

            for(int j=0;j<16;j++) {
                outMessage[i+j] = STATE[j];
            }
        }

        writeOutput();
    }

    public static void AES_Decrypt(String inFile, String outFile, int key []) throws IOException {
        mode = CIPHER;

        inputfile = inFile;
        outputfile = outFile;
        message = new int[getMessageLength()];
        outMessage = new int[message.length];
        readMessage();


        for (int i=0;i<message.length;i += 16) {
            int []STATE = new int [16];

            for(int j=0;j<16;j++) {
                STATE [j] = message[i+j];
            }

            STATE = AES_Decrypt_state(STATE,key);

            for(int j=0;j<16;j++) {
                outMessage[i+j] = STATE[j];
            }
        }

        BufferedWriter fout = new BufferedWriter(new FileWriter(outputfile));
        for (int i=0; i<message.length; i++) {
            if(outMessage[i] != 0)
                fout.write((char)outMessage[i]);
        }
        fout.close();
    }

    private static int[] AES_Decrypt_state(int[] message, int key []) throws IOException {

        int numberRound = 9;
        int state[] = new int[16];
        System.arraycopy(message, 0, state, 0, 16);

        int [] keyExpansion = keyExpansion(key);

        int [] keyStep = new int[16];
        for (int j=0;j<16;j++)
            keyStep[j] = keyExpansion[160 + j];

        state = addRoundKey(state,keyStep);

        for (int i=0;i<numberRound;i++) {

            for (int j=0;j<16;j++)
                keyStep[j] = keyExpansion[(160-16*(i+1)) + j];

            state = invShiftRows(state);

            state = invSubBytes(state);

            state = addRoundKey(state, keyStep);

            state = invMixColumn(state);
        }

        //final round

        state = invShiftRows(state);
        state = invSubBytes(state);

        state = addRoundKey(state,keyExpansion);

        return state;
    }

    private static int[] AES_Encrypt_state(int[] message, int key []) throws IOException {



        int numberRound = 9;
        int state[] = new int[16];
        System.arraycopy(message, 0, state, 0, 16);

        int [] keyExpansion = keyExpansion(key);

        state = addRoundKey(state,keyExpansion);

        for (int i=0;i<numberRound;i++) {
            int [] keyStep = new int[16];

            for (int j=0;j<16;j++)
                keyStep[j] = keyExpansion[16*(i+1) + j];


            state = subBytes(state);

            state = shiftRows(state);

            state = mixColumn(state);

            state = addRoundKey(state,keyStep);

        }

        //final round

        state = subBytes(state);
        state = shiftRows(state);

        int [] keyStep = new int[16];
        for (int j=0;j<16;j++)
            keyStep[j] = keyExpansion[160 + j];

        state = addRoundKey(state,keyStep);

        return state;
    }

    // Determine the length of the message.
    private static int getMessageLength() throws IOException {

        BufferedReader fin = new BufferedReader(new FileReader(inputfile));
        int caracount = 0;

        // For reading a plaintext file.
        if (mode == PLAIN) {

            // Count the number of characters in the plain text.
            while (fin.ready()) {
                int c = fin.read(); // Each char is one byte.
                caracount++;
            }
        }
        else {

            // For reading a ciphertext file.
            while (fin.ready()) {

                // Only could HEX chars.
                int c = fin.read();
                if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F'))
                    caracount++;
            }
            caracount /= 2; // Account for a Hex char being 1/2 a byte.
        }
        fin.close();

        msglength = caracount;

        // Rounds up to the nearest multiple of 16 bytes, to account for the
        // block size.
        return 16*((caracount +15)/16);
    }

    // Returns the integer value of a given HEX character.
    private static int hexVal(char c) {
        if (c >= '0' && c <= '9')
            return (int)(c-'0');
        else
            return (int)(c-'A'+10);
    }

    private static void readMessage() throws IOException {

        BufferedReader fin = new BufferedReader(new FileReader(inputfile));

        int icnt, bytecnt, hexcnt, linecnt;

        // Initialize the message buffer.
        for (icnt=0; icnt<message.length; icnt++)
            message[icnt] = 0;

        // For plaintext files.
        if (mode == PLAIN) {

            // Read in each actual character of the plain text.
            for (bytecnt=0; bytecnt<msglength; bytecnt++) {
                int c = fin.read();
                message[bytecnt] = c ;
            }

            // Pad the plaintext with spaces to fill the block length.
            for (bytecnt=msglength; bytecnt<message.length; bytecnt++)
                message[bytecnt] = 0;
        }else {

            // Read in cipher text file one line at a time.
            for (linecnt=0; linecnt<(msglength+16)/16; linecnt++) {
                String line = fin.readLine();

                if (line == null)
                    break;

                // Fill in each hex character one by one.
                for (hexcnt=0; hexcnt<line.length(); hexcnt = hexcnt + 2)
                    message[16 * linecnt + hexcnt / 2] = 16*hexVal(line.charAt(hexcnt)) +
                                                        hexVal(line.charAt(hexcnt + 1));
            }
        }

        fin.close();
    }

        // Should only be called if the message is in CIPHER status. This
    // method writes out the ciphertext to the output file, writing 64
    // hex characters per line. This corresponds to 4 blocks of ciphertext
    // per line.
    private static void writeOutput() throws IOException {

        BufferedWriter fout = new BufferedWriter(new FileWriter(outputfile));
        for (int i=0; i<message.length; i++) {


            if(outMessage[i] < 16)
                fout.write("0" + Integer.toHexString(outMessage[i]).toUpperCase());
            else
                fout.write(Integer.toHexString(outMessage[i]).toUpperCase());

            // Advance to the next line after 32 chars have been written.
            if (i%16 == 15)
                fout.write('\n');
        }
        fout.close();
    }


}
