import edu.princeton.cs.algs4.*;

public class Outcast {
    private WordNet wn;
    
    public Outcast(WordNet wordnet) {         // constructor takes a WordNet object
        wn = wordnet;
    }
    
    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
        String ret = null;
        int cmp = -1;
        for (String s : nouns) {
            int tmp = 0;
            for (String t : nouns) {
                tmp += wn.distance(s, t);
            }
            //StdOut.print(tmp + " " + s + "\n");
            if (tmp > cmp) {
                cmp = tmp;
                ret = s;
            }
        }
        return ret;
    }
    
    public static void main(String[] args) {  // see test client below
        String syn = "/Users/kybconnor/IdeaProjects/WordNet/wordnet/synsets.txt";
        String hyp = "/Users/kybconnor/IdeaProjects/WordNet/wordnet/hypernyms.txt";
        WordNet wordnet = new WordNet(syn, hyp);
        Outcast outcast = new Outcast(wordnet);
        String oc = "wordnet/outcast11.txt";
        In in = new In(oc);
        String[] nouns = in.readAllStrings();
        StdOut.println(oc + ": " + outcast.outcast(nouns));
    }
}