import edu.princeton.cs.algs4.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class BaseballElimination {
    private int n;
    private int[] win, lost, rem;
    private Vector<String> name;
    private Map<String, Integer> toId;
    private int[][] g;
    private int[][] id;
    private int[] nid;
    
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();
        win = new int[n];
        lost = new int[n];
        rem = new int[n];
        name = new Vector<String>();
        g = new int[n][n];
        id = new int[n][n];
        nid = new int[n];
        int nextid = 1;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++)
                id[i][j] = nextid++;
        for (int i = 0; i < n; i++)
            nid[i] = nextid++;
        toId = new TreeMap<String, Integer>();
        for (int i = 0; i < n; i++) {
            String nm = in.readString();
            name.add(nm);
            win[i] = in.readInt();
            lost[i] = in.readInt();
            rem[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                g[i][j] = in.readInt();
            }
            toId.put(nm, i);
        }
    }                    // create a baseball division from given filename in format specified below
    
    public int numberOfTeams() {
        return n;
    }                        // number of teams
    
    public Iterable<String> teams() {
        return new Vector<String>(name);
    }                                // all teams
    
    public int wins(String team) {
        if (toId.get(team) == null)
            throw new java.lang.IllegalArgumentException();
        return win[toId.get(team)];
    }                      // number of wins for given team
    
    public int losses(String team) {
        if (toId.get(team) == null)
            throw new java.lang.IllegalArgumentException();
        return lost[toId.get(team)];
    }                    // number of losses for given team
    
    public int remaining(String team) {
        if (toId.get(team) == null)
            throw new java.lang.IllegalArgumentException();
        return rem[toId.get(team)];
    }                 // number of remaining games for given team
    
    public int against(String team1, String team2) {
        if (toId.get(team1) == null)
            throw new java.lang.IllegalArgumentException();
        if (toId.get(team2) == null)
            throw new java.lang.IllegalArgumentException();
        return g[toId.get(team1)][toId.get(team2)];
    }    // number of remaining games between team1 and team2
    
    private FordFulkerson getFlow(int u) {
        int cnt = 1 + n * (n - 1) / 2 + n + 1;
        FlowNetwork mf = new FlowNetwork(cnt);
        for (int i = 0; i < n; i++)
            if (i != u && win[i] <= win[u] + rem[u])
                for (int j = i + 1; j < n; j++)
                    if (j != u && win[j] <= win[u] + rem[u]) {
                        mf.addEdge(new FlowEdge(0, id[i][j], g[i][j]));
                        mf.addEdge(new FlowEdge(id[i][j], nid[i], g[i][j]));
                        mf.addEdge(new FlowEdge(id[i][j], nid[j], g[i][j]));
                    }
        for (int i = 0; i < n; i++)
            if (i != u && win[i] <= win[u] + rem[u])
                mf.addEdge(new FlowEdge(nid[i], cnt - 1, win[u] + rem[u] - win[i]));
        FordFulkerson Flow = new FordFulkerson(mf, 0, cnt - 1);
        return Flow;
    }
    
    public boolean isEliminated(String team) {
        if (toId.get(team) == null)
            throw new java.lang.IllegalArgumentException();
        int u = toId.get(team);
        for (int i = 0; i < n; i++)
            if (win[i] > win[u] + rem[u])
                return true;
        FordFulkerson Flow = getFlow(u);
        int flow = (int) Flow.value();
        int aim = 0;
        for (int i = 0; i < n; i++)
            if (i != u)
                for (int j = i + 1; j < n; j++)
                    if (j != u)
                        aim += g[i][j];
        return aim != flow;
    }              // is given team eliminated?
    
    public Iterable<String> certificateOfElimination(String team) {
        if (toId.get(team) == null)
            throw new java.lang.IllegalArgumentException();
        if (!isEliminated(team))
            return null;
        Vector<String> ret = new Vector<String>();
        int u = toId.get(team);
        FordFulkerson Flow = getFlow(u);
        for (int v = 0; v < n; v++)
            if (v != u) {
                if (win[v] > win[u] + rem[u])
                    ret.add(name.get(v));
                else if (Flow.inCut(nid[v]))
                    ret.add(name.get(v));
            }
        return ret;
    }  // subset R of teams that eliminates given team; null if not eliminated
    
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("test/teams5.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
