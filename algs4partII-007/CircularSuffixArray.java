/**
 * Created by kybconnor on 16/5/26.
 */
public class CircularSuffixArray {
    private String ss;
    private int[] sa;
    
    private boolean cmp(int[][] w, int y, int a, int b, int x, int n) {
        return w[y][a] == w[y][b] && w[y][(a + x) % n] == w[y][(b + x) % n];
    }
    
    private void da(String r, int n, int m) {
        int i, j, k, p;
        int len = Math.max(m, n + 5);
        sa = new int[len];
        int w[][] = new int[2][len];
        int wd[] = new int[len];
        int wv[] = new int[len];
        int x = 0, y = 1, t;
        for (i = 0; i < m; i++) wd[i] = 0;
        for (i = 0; i < n; i++) wd[w[x][i] = r.charAt(i)]++;
        for (i = 1; i < m; i++) wd[i] += wd[i - 1];
        for (i = n - 1; i >= 0; i--) sa[--wd[w[x][i]]] = i;
        for (j = 1, p = 0; j < n && p < n; j <<= 1, m = p) {
            for (p = 0, i = 0; i < n; i++) w[y][p++] = (sa[i] - j + n) % n;
            for (i = 0; i < n; i++) wv[i] = w[x][w[y][i]];
            for (i = 0; i < m; i++) wd[i] = 0;
            for (i = 0; i < n; i++) wd[wv[i]]++;
            for (i = 1; i < m; i++) wd[i] += wd[i - 1];
            for (i = n - 1; i >= 0; i--) sa[--wd[wv[i]]] = w[y][i];
            for (t = x, x = y, y = t, p = 1, w[x][sa[0]] = 0, i = 1; i < n; i++)
                w[x][sa[i]] = cmp(w, y, sa[i - 1], sa[i], j, n) ? p - 1 : p++;
        }
    }
    
    public CircularSuffixArray(String s) {  // circular suffix array of s
        ss = s;
        if (s == null)
            throw new java.lang.NullPointerException();
        da(s, s.length(), 256);/*
        for (int i = 0; i < s.length(); i++)
            System.out.print(sa[i] + " ");*/
        
    }
    
    public int length() {                   // length of s
        return ss.length();
    }
    
    public int index(int i) {               // returns index of ith sorted suffix
        if (i >= length() || i < 0)
            throw new java.lang.IndexOutOfBoundsException();
        return sa[i];
    }
    
    public static void main(String[] args) {// unit testing of the methods (optional)
        String s = "ABABABABAB";
        CircularSuffixArray CSA = new CircularSuffixArray(s);
    }
}