import edu.princeton.cs.algs4.*;

import java.util.Vector;

public class SAP {
    private Digraph G;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph g) {
        G = new Digraph(g);
    }
    
    private int play(Vector<Vector<Integer>> L, boolean flag) {
        int n = G.V();
        int d[][] = new int[2][n];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < n; j++) d[i][j] = -1;
            for (int j : L.get(i)) d[i][j] = 0;
        }
        for (int j = 0; j < n; j++)
            if (d[0][j] == 0 && d[1][j] == 0) {
                if (flag) return j;
                else return 0;
            }
        int id = 0;
        int ret = -1, uid = -1;
        while (!L.get(0).isEmpty() || !L.get(1).isEmpty()) {
            if (L.get(id).isEmpty()) id ^= 1;
            Vector<Integer> R = new Vector<Integer>();
            for (int u : L.get(id))
                for (int v : G.adj(u))
                    if (d[id][v] == -1) {
                        d[id][v] = d[id][u] + 1;
                        if (ret == -1 || d[id][v] < ret)
                            R.addElement(v);
                        if (d[0][v] != -1 && d[1][v] != -1) {
                            int tmp = d[0][v] + d[1][v];
                            if (ret == -1 || ret > tmp) {
                                ret = tmp;
                                uid = v;
                            }
                        }
                    }
            L.get(id).clear();
            for (int r : R) L.get(id).add(r);
            id ^= 1;
        }
        if (flag) return uid;
        else return ret;
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Vector<Vector<Integer>> L = new Vector<Vector<Integer>>();
        for (int i = 0; i < 2; i++)
            L.addElement(new Vector<Integer>());
        L.get(0).addElement(v);
        L.get(1).addElement(w);
        return play(L, false);
    }
    
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Vector<Vector<Integer>> L = new Vector<Vector<Integer>>();
        for (int i = 0; i < 2; i++)
            L.addElement(new Vector<Integer>());
        L.get(0).addElement(v);
        L.get(1).addElement(w);
        return play(L, true);
    }
    
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Vector<Vector<Integer>> L = new Vector<Vector<Integer>>();
        for (int i = 0; i < 2; i++)
            L.addElement(new Vector<Integer>());
        for (int x : v) L.get(0).addElement(x);
        for (int x : w) L.get(1).addElement(x);
        return play(L, false);
    }
    
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Vector<Vector<Integer>> L = new Vector<Vector<Integer>>();
        for (int i = 0; i < 2; i++)
            L.addElement(new Vector<Integer>());
        for (int x : v) L.get(0).addElement(x);
        for (int x : w) L.get(1).addElement(x);
        return play(L, true);
    }
    
    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
