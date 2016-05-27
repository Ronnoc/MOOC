import edu.princeton.cs.algs4.*;

import java.util.Vector;

/**
 * Created by kybconnor on 16/5/26.
 */
public class MoveToFront {
    
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] id = new char[256];
        char[] is = new char[256];
        for (int i = 0; i < 256; i++)
            id[i] = is[i] = (char) i;
        
        while (!BinaryStdIn.isEmpty()) {
            char w = BinaryStdIn.readChar();
            BinaryStdOut.write(id[w]);
            for (int i = id[w] - 1; i >= 0; i--) {
                char g = is[i];
                is[++id[g]] = g;
            }
            is[id[w] = 0] = w;
        }
        BinaryStdOut.flush();
    }
    
    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] id = new char[256];
        char[] is = new char[256];
        for (char i = 0; i < 256; i++)
            id[i] = is[i] = i;
        Vector<Character> L = new Vector<Character>();
        while (!BinaryStdIn.isEmpty()) {
            char w = BinaryStdIn.readChar();
            char g = is[w];
            for (int i = w - 1; i >= 0; i--) {
                char t = is[i];
                is[++id[t]] = t;
            }
            is[id[g] = 0] = g;
            L.add(w);
        }
        Vector<Character> Ans = new Vector<Character>();
        for (int k = L.size() - 1; k >= 0; k--) {
            char l = L.elementAt(k);
            Character g = (char) is[0];
            Ans.add(g);
            for (char i = 1; i <= l; i++) {
                char t = is[i];
                is[--id[t]] = t;
            }
            is[id[g] = l] = g;
        }
        for (int i = Ans.size() - 1; i >= 0; i--)
            BinaryStdOut.write(Ans.elementAt(i));
        BinaryStdOut.flush();
    }
    
    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].charAt(0) == '-') encode();
        else decode();
    }
}

