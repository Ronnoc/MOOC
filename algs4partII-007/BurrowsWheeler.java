import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Vector;

/**
 * Created by kybconnor on 16/5/26.
 */
public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        Vector<Character> L = new Vector<Character>();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            L.add(c);
        }
        char c[] = new char[L.size()];
        for (int i = 0; i < L.size(); i++)
            c[i] = L.elementAt(i);
        String s = new String(c);
        CircularSuffixArray CSA = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++)
            if (CSA.index(i) == 0)
                BinaryStdOut.write(i);
        for (int i = 0; i < s.length(); i++)
            BinaryStdOut.write(s.charAt((CSA.index(i) + s.length() - 1) % s.length()));
        BinaryStdOut.flush();
    }
    
    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int fi = BinaryStdIn.readInt();
        Vector<Character> L = new Vector<Character>();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            L.add(c);
        }
        int next[] = new int[L.size()];
        char c[] = new char[L.size()];
        Vector<Vector<Integer>> S = new Vector<Vector<Integer>>();
        for (int i = 0; i < 256; i++) {
            Vector<Integer> R = new Vector<Integer>();
            S.add(R);
        }
        for (int i = 0; i < L.size(); i++) {
            S.elementAt(L.elementAt(i)).add(i);
        }
        int id = 0;
        for (char k = 0; k < 256; k++)
            for (int j = 0; j < S.elementAt(k).size(); j++) {
                int i = S.elementAt(k).elementAt(j);
                c[id] = k;
                next[id++] = i;
            }
        for (int k = 0; k < L.size(); k++) {
            BinaryStdOut.write(c[fi]);
            fi = next[fi];
        }
        BinaryStdOut.flush();
    }
    
    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].charAt(0) == '-') encode();
        else decode();
    }
}
