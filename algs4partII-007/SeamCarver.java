import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private int w, h;
    private int[][] p, q;
    
    public SeamCarver(Picture picture) { // create a seam carver object based on the given picture
        w = picture.width();
        h = picture.height();
        p = new int[w][h];
        q = new int[h][w];
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                p[i][j] = picture.get(i, j).getRGB();
                q[j][i] = picture.get(i, j).getRGB();
            }
    }
    
    public Picture picture() {   // current picture
        Picture ret = new Picture(w, h);
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                ret.set(i, j, new Color(p[i][j]));
            }
        return ret;
    }
    
    public int width() { // width of current picture
        return w;
    }
    
    public int height() {    // height of current picture
        return h;
    }
    
    public double energy(int x, int y) {    // energy of pixel at column x and row y
        if (x < 0 || y < 0 || x >= w || y >= h)
            throw new java.lang.IndexOutOfBoundsException();
        if (x == 0 || y == 0) return 1000;
        if (x == w - 1 || y == h - 1) return 1000;
        double ret = 0;
        double dx, dy;
        dx = new Color(p[x - 1][y]).getRed() - new Color(p[x + 1][y]).getRed();
        dy = new Color(p[x][y - 1]).getRed() - new Color(p[x][y + 1]).getRed();
        ret += dx * dx + dy * dy;
        dx = new Color(p[x - 1][y]).getGreen() - new Color(p[x + 1][y]).getGreen();
        dy = new Color(p[x][y - 1]).getGreen() - new Color(p[x][y + 1]).getGreen();
        ret += dx * dx + dy * dy;
        dx = new Color(p[x - 1][y]).getBlue() - new Color(p[x + 1][y]).getBlue();
        dy = new Color(p[x][y - 1]).getBlue() - new Color(p[x][y + 1]).getBlue();
        ret += dx * dx + dy * dy;
        return Math.sqrt(ret);
    }
    
    public int[] findHorizontalSeam() {    // sequence of indices for horizontal seam
        double[][] dph = new double[w][h];
        int[] ret = new int[w];
        int i, j, k;
        for (j = 0; j < h; j++) {
            dph[0][j] = energy(0, j);
        }
        for (i = 1; i < w; i++)
            for (j = 0; j < h; j++) {
                dph[i][j] = -1;
                for (k = -1; k <= 1; k++)
                    if (j + k >= 0 && j + k < h) {
                        double tmp = dph[i - 1][j + k];
                        if (dph[i][j] < 0 || tmp < dph[i][j])
                            dph[i][j] = tmp;
                    }
                dph[i][j] += energy(i, j);
            }
        ret[w - 1] = 0;
        for (j = 0; j < h; j++) if (dph[w - 1][j] < dph[w - 1][ret[w - 1]]) ret[w - 1] = j;
        for (i = w - 1; i > 0; i--) {
            ret[i - 1] = -1;
            for (k = -1; k <= 1; k++) {
                if (ret[i] + k < 0 || ret[i] + k >= h) continue;
                if (ret[i - 1] == -1 || dph[i - 1][ret[i] + k] < dph[i - 1][ret[i - 1]])
                    ret[i - 1] = ret[i] + k;
            }
        }
        return ret;
    }
    
    public int[] findVerticalSeam() {  // sequence of indices for vertical seam
        double[][] dpv = new double[h][w];
        int[] ret = new int[h];
        int i, j, k;
        for (j = 0; j < w; j++) {
            dpv[0][j] = energy(j, 0);
        }
        for (i = 1; i < h; i++)
            for (j = 0; j < w; j++) {
                dpv[i][j] = -1;
                for (k = -1; k <= 1; k++)
                    if (j + k >= 0 && j + k < w) {
                        double tmp = dpv[i - 1][j + k];
                        if (dpv[i][j] < 0 || tmp < dpv[i][j])
                            dpv[i][j] = tmp;
                    }
                dpv[i][j] += energy(j, i);
            }
        ret[h - 1] = 0;
        for (j = 0; j < w; j++) if (dpv[h - 1][j] < dpv[h - 1][ret[h - 1]]) ret[h - 1] = j;
        for (i = h - 1; i > 0; i--) {
            ret[i - 1] = -1;
            for (k = -1; k <= 1; k++) {
                if (ret[i] + k < 0 || ret[i] + k >= w) continue;
                if (ret[i - 1] == -1 || dpv[i - 1][ret[i] + k] < dpv[i - 1][ret[i - 1]])
                    ret[i - 1] = ret[i] + k;
            }
        }
        return ret;
    }
    
    private boolean check(int[] s, int x) {
        boolean flag = false;
        for (int i : s) {
            if (i < 0 || i >= x) flag = true;
        }
        for (int i = 1; i < s.length; i++) {
            int d = s[i] - s[i - 1];
            if (d > 1 || d < -1)
                flag = true;
        }
        return flag;
    }
    
    public void removeHorizontalSeam(int[] s) {    // remove horizontal seam from current picture
        if (check(s, h) || s.length != w)
            throw new java.lang.IllegalArgumentException();
        for (int i = 0; i < w; i++) {
            System.arraycopy(p[i], s[i] + 1, p[i], s[i], h - s[i] - 1);
        }
        h--;
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                q[j][i] = p[i][j];
    }
    
    public void removeVerticalSeam(int[] s) {  // remove vertical seam from current picture
        if (check(s, w) || s.length != h)
            throw new java.lang.IllegalArgumentException();
        for (int i = 0; i < h; i++) {
            System.arraycopy(q[i], s[i] + 1, q[i], s[i], w - s[i] - 1);
        }
        w--;
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                p[j][i] = q[i][j];
    }
}