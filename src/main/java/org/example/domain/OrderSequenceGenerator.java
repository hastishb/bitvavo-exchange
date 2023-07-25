package org.example.domain;

public class OrderSequenceGenerator {
    private static long sequence = 0;

    public static long getSequence() {
        long n = sequence;
        sequence++;
        return n;
    }
}

