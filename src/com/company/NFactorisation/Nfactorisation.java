package com.company.NFactorisation;

public class Nfactorisation {

    private static boolean isPremier(long n) {
        if (n == 1 || n ==2)
            return true;
        for (int i = 2;i <= Math.sqrt(n);i++) {
            if (n%i == 0)
                return false;
        }
        return true;
    }

    public static void main(String[] args) {

        long n = 1037594094337L;
        long p = 0,q = 0;

        for (int i=2;i<= Math.sqrt(n);i++) {
            if (n%i == 0){
                p = n/i;
                q = i;

                if (isPremier(p) && isPremier(q))
                    break;
            }
        }
        System.out.println(" N = " + n);
        System.out.println("p = " + p + " ; q = " + q);
    }
}
