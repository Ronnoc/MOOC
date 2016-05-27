import edu.princeton.cs.algs4.*;

import java.util.*;
import java.util.Vector;

public class WordNet {
    
    private int S;
    private Digraph G;
    private SAP sap;
    private int[] v, c;
    private boolean fail;
    private Set<String> what;
    private Map<String, Vector<Integer>> toId;
    private Vector<String> toStr;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new java.lang.NullPointerException();
        S = 0;
        toId = new TreeMap<String, Vector<Integer>>();
        toStr = new Vector<String>();
        what = new TreeSet<String>();
        In ins = new In(synsets);
        In inh = new In(hypernyms);
        while (!ins.isEmpty()) {
            String s = ins.readLine();
            String[] L = s.split(",");
            int id = Integer.parseInt(L[0]);
            String[] R = L[1].split(" ");
            for (String r : R) {
                if (toId.get(r) == null) {
                    Vector<Integer> l = new Vector<Integer>();
                    toId.put(r, l);
                }
                toId.get(r).add(S);
                what.add(r);
            }
            toStr.add(L[1]);
            S++;
        }
        G = new Digraph(S);
        while (!inh.isEmpty()) {
            String s = inh.readLine();
            String[] L = s.split(",");
            int u = Integer.parseInt(L[0]);
            for (int i = 1; i < L.length; i++) {
                int v = Integer.parseInt(L[i]);
                G.addEdge(u, v);
            }
        }
        sap = new SAP(G);
        fail = false;
        v = new int[S];
        for (int i = 0; i < S; i++) v[i] = 0;
        c = new int[S];
        for (int i = 0; i < S; i++)
            c[i] = -1;
        for (int i = 0; i < S; i++)
            if (c[i] == -1) {
                dfs(i);
            }
        for (int i = 0; i < S; i++)
            if (c[i] == 1) {
                fail = true;
            }
        int count = 0;
        for (int i = 0; i < S; i++) {
            if (G.outdegree(i) == 0) count++;
        }
        if (count > 1) fail = true;
        if (fail)
            throw new java.lang.IllegalArgumentException();
    }
    
    private int dfs(int u) {
        v[u] = 1;
        if (c[u] == -1) {
            c[u] = 0;
            for (int w : G.adj(u)) {
                if (v[w] == 1) {
                    c[u] = 1;
                } else if (dfs(w) == 1) {
                    c[u] = 1;
                }
            }
        }
        v[u] = 0;
        return c[u];
    }
    
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return what;
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new java.lang.NullPointerException();
        return what.contains(word);
    }
    
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.NullPointerException();
        Vector<Integer> A, B;
        A = toId.get(nounA);
        B = toId.get(nounB);
        if (A == null || B == null)
            throw new java.lang.IllegalArgumentException();
        return sap.length(A, B);
    }
    
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.NullPointerException();
        Vector<Integer> A, B;
        A = toId.get(nounA);
        B = toId.get(nounB);
        if (A == null || B == null)
            throw new java.lang.IllegalArgumentException();
        int id = sap.ancestor(A, B);
        return toStr.get(id);
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        String syn = "/Users/kybconnor/IdeaProjects/WordNet/wordnet/synsets.txt";
        String hyp = "/Users/kybconnor/IdeaProjects/WordNet/wordnet/hypernyms.txt";
        new WordNet(syn, hyp);
    }
}