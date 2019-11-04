import java.util.*;

public class Main {
    public static void main(String[] args) {
        int m = 4, c = 4;
        String initialState = "" + m + c + "1-000";
        String finalState = "000-" + m + c + "1";
        Node tree = new Node();
        createNewState(tree, initialState, "", finalState);
        System.out.println("List of all possible paths:");
        tree.dfs("", finalState);
    }

    private static void createNewState(Node tree, String curState, String path, String finalState) {
        char[] cs = curState.toCharArray();
        int mw = cs[0] - '0';
        int cw = cs[1] - '0';
        int me = cs[4] - '0';
        int ce = cs[5] - '0';
        if (path.contains(curState)) {
            return;
        }
        if ((mw >= cw || mw == 0) && (me >= ce || me == 0) && mw >= 0 && cw >= 0 && me >= 0 && ce >= 0) {
            path += " " + curState;
            boolean bw = cs[2] == '1';
            Node current = new Node(curState);
            tree.addChild(current);
            if (curState.equals(finalState)) {
                return;
            }
            if (bw) {
                createNewState(current, "" + (mw - 1) + cw + "0-" + (me + 1) + ce + "1", path, finalState);
                createNewState(current, "" + (mw - 2) + cw + "0-" + (me + 2) + ce + "1", path, finalState);
                createNewState(current, "" + (mw - 1) + (cw - 1) + "0-" + (me + 1) + (ce + 1) + "1", path, finalState);
                createNewState(current, "" + mw + (cw - 1) + "0-" + me + (ce + 1) + "1", path, finalState);
                createNewState(current, "" + mw + (cw - 2) + "0-" + me + (ce + 2) + "1", path, finalState);
            } else {
                createNewState(current, "" + (mw + 1) + cw + "1-" + (me - 1) + ce + "0", path, finalState);
                createNewState(current, "" + (mw + 2) + cw + "1-" + (me - 2) + ce + "0", path, finalState);
                createNewState(current, "" + (mw + 1) + (cw + 1) + "1-" + (me - 1) + (ce - 1) + "0", path, finalState);
                createNewState(current, "" + mw + (cw + 1) + "1-" + me + (ce - 1) + "0", path, finalState);
                createNewState(current, "" + mw + (cw + 2) + "1-" + me + (ce - 2) + "0", path, finalState);
            }
        }
    }

    static class Node {
        String state;
        ArrayList<Node> children;

        Node(String state) {
            this.state = state;
            children = new ArrayList<>();
        }

        Node() {
            state = "";
            children = new ArrayList<>();
        }

        void addChild(Node n) {
            children.add(n);
        }

        void dfs(String path, String finalState) {
            path += state;
            if (children.size() == 0) {
                System.out.print(path);
                if (state.equals(finalState)) {
                    System.out.println(" *right path*");
                } else {
                    System.out.println(" *wrong path*");
                }
            } else {
                if (!state.equals("")) {
                    path += " -> ";
                }
                for (Node child : children) {
                    child.dfs(path, finalState);
                }
            }
        }
    }
}
