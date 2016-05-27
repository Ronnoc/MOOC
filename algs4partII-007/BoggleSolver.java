import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Vector;

/**
 * Created by kybconnor on 16/5/25.
 */

public class BoggleSolver {
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    private static class Node {
        private Node[] next = new Node[27];
        private boolean w = false;
        private int mark = -1;
        private String s;
    }
    
    private Node root;
    
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String s : dictionary) {
            Node now = root;
            for (int i = 0; i < s.length(); i++) {
                int id = s.charAt(i) - 'A';
                if (id + 'A' == 'Q') {
                    if (i + 1 < s.length() && s.charAt(i + 1) == 'U') {
                        i++;
                    } else id = 26;
                }
                if (now.next[id] == null)
                    now.next[id] = new Node();
                now = now.next[id];
            }
            now.w = true;
            now.s = s;
        }
    }
    
    private int dx[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    private int dy[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    private Boolean[][] vis;
    private int n, m;
    private int chuo = 0;
    private Vector<String> ret;
    
    private void dfs(Node now, int u, int v, BoggleBoard board) {
        int id = board.getLetter(u, v) - 'A';
        //System.out.println(u+" "+v+" "+board.getLetter(u,v));
        now = now.next[id];
        if (now == null) return;
        if (now.w) {
            if (now.mark != chuo) {
                now.mark = chuo;
                if (now.s.length() >= 3)
                    ret.add(now.s);
            }
        }
        vis[u][v] = true;
        for (int k = 0; k < 8; k++) {
            int x = u + dx[k];
            int y = v + dy[k];
            if (x < 0 || x >= n) continue;
            if (y < 0 || y >= m) continue;
            if (vis[x][y]) continue;
            dfs(now, x, y, board);
        }
        vis[u][v] = false;
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        n = board.rows();
        m = board.cols();
        ret = new Vector<String>();
        vis = new Boolean[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                vis[i][j] = false;
        chuo++;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++) {
                //System.out.println("start @"+i+" "+j);
                dfs(root, i, j, board);
            }
        return ret;
    }
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    private boolean inDic(String s) {
        Node now = root;
        for (int i = 0; i < s.length(); i++) {
            int id = s.charAt(i) - 'A';
            if (id + 'A' == 'Q') {
                if (i + 1 < s.length() && s.charAt(i + 1) == 'U') {
                    i++;
                } else id = 26;
            }
            if (now.next[id] == null)
                return false;
            now = now.next[id];
        }
        return now.w;
    }
    
    public int scoreOf(String word) {
        if (!inDic(word))
            return 0;
        int l = word.length();
        if (l >= 8) return 11;
        if (l >= 7) return 5;
        if (l >= 6) return 3;
        if (l >= 5) return 2;
        if (l >= 3) return 1;
        return 0;
    }
    
    public static void main(String[] args) {
        String dicAt = "/Users/kybconnor/IdeaProjects/BoggleSolver/boggle/dictionary-yawl.txt";
        String boardAt = "/Users/kybconnor/IdeaProjects/BoggleSolver/boggle/board4x4.txt";
        In in = new In(dicAt);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(boardAt);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}