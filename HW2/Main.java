import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Which part do you want to work with? (1 or 2)");
        int part = in.nextInt();
        //for part one (#missionaries and #cannibals equals to 6)
        if (part == 1) {
            //initial number of missionaries and cannibals
            int m = 6;
            int c = 6;
            int boatCapacity = 5;

            //initial and final states represented by a string
            String initialState = "" + m + c + "1-000";
            String finalState = "000-" + m + c + "1";

            //initializing the tree
            Node tree = new Node();
            createNewState(tree, initialState, "", finalState, boatCapacity);

            //list of states that we traverse before reaching goal node
            //combining them gives us a path that is the shortest
            ArrayList<String> shortestPath = new ArrayList<>();

            //performing A* search on root node
            tree.aStarSearch(shortestPath, finalState);

            //printing the length of shortest path
            System.out.println(tree.getLengthAndNumberOfTheShortestPaths(finalState)[0]);

            //printing the number of shortest paths
            System.out.println(tree.getLengthAndNumberOfTheShortestPaths(finalState)[1]);

            //printing the sequence of moves that missionaries and cannibals perform
            //the code below prints those moves similar to what's given in the assignment document
            for (int k = 0; k < shortestPath.size(); ++k) {
                String state = shortestPath.get(k);
                if (k != 0) {
                    //if the boat is not on the west that means
                    //we sent some missionaries and cannibals to
                    //the east side
                    if (state.charAt(2) != '1') {
                        System.out.printf("SEND\t%d MISSIONARIES %d CANNIBALS\n",
                                (state.charAt(4) - shortestPath.get(k - 1).charAt(4)),
                                (state.charAt(5) - shortestPath.get(k - 1).charAt(5))
                        );
                    }
                    //if it is on the west side
                    //that means some missionaries
                    //or cannibals returned to the west side
                    else {
                        System.out.printf("RETURN\t%d MISSIONARIES %d CANNIBALS\n",
                                (state.charAt(0) - shortestPath.get(k - 1).charAt(0)),
                                (state.charAt(1) - shortestPath.get(k - 1).charAt(1))
                        );
                    }
                }
                //printing number of missionaries and cannibals on each side
                for (int i = 0; i < state.charAt(0) - '0'; i++) {
                    System.out.print("M");
                }
                System.out.printf("%-10s", " ");
                for (int i = 0; i < state.charAt(4) - '0'; i++) {
                    System.out.print("M");
                }
                System.out.println();
                for (int i = 0; i < state.charAt(1) - '0'; i++) {
                    System.out.print("C");
                }
                System.out.printf("%-10s", " ");
                for (int i = 0; i < state.charAt(5) - '0'; i++) {
                    System.out.print("C");
                }
                System.out.println("\n");
            }
        }
        //for part two (#missionaries and #cannibals equals to 4)
        else {
            /* START OF PART 2 */
            int m = 4;
            int c = 4; //initial number of missionaries and cannibals
            int boatCapacity = 3;

            //initial and final states represented by a string
            String initialState = "" + m + c + "1-000";
            String finalState = "000-" + m + c + "1";

            //initializing the tree
            Node tree = new Node();
            createNewState(tree, initialState, "", finalState, boatCapacity);

            //printing the length of the shortest path
            System.out.println("Shortest length: " + tree.getLengthAndNumberOfTheShortestPaths(finalState)[0]);

            //printing the number of shortest paths
            System.out.println("The number of the shortest paths: " + tree.getLengthAndNumberOfTheShortestPaths(finalState)[1]);
            tree.dfs("", finalState);
        }
    }

    private static void createNewState(Node tree, String curState, String path, String finalState, int boatCapacity) {
        char[] currentStateCharArray = curState.toCharArray();
        int missionariesOnWest = currentStateCharArray[0] - '0';
        int cannibalsOnWest = currentStateCharArray[1] - '0';
        int missionariesOnEast = currentStateCharArray[4] - '0';
        int cannibalsOnEast = currentStateCharArray[5] - '0';
        //we stop if the path we covered so far contains
        //the current state to ignore duplicates
        if (path.contains(curState)) {
            return;
        }
        // the if statement ensures cannibals don not outnumber missionaries
        // and that their number is non-negative
        if ((missionariesOnWest >= cannibalsOnWest || missionariesOnWest == 0) &&
                (missionariesOnEast >= cannibalsOnEast || missionariesOnEast == 0) &&
                missionariesOnWest >= 0 && cannibalsOnWest >= 0 && missionariesOnEast >= 0
                && cannibalsOnEast >= 0) {

            System.out.println(curState);

            //adds current state to current path
            path += " " + curState;

            //bw is true if boat is on the west side
            boolean bw = currentStateCharArray[2] == '1';

            //creating node from current state and adding it to the tree
            Node current = new Node(curState);
            tree.addChild(current);

            //we terminate if we reach final state
            if (curState.equals(finalState)) {
                return;
            }

            //for all valid combinations of missionaries and cannibals we create new states
            for (int cannibals = 0; cannibals <= boatCapacity; cannibals++) {
                for (int missionaries = 0; missionaries <= boatCapacity; missionaries++) {

                    //the if statement checks the validity of # of the missionaries and cannibals
                    if (cannibals + missionaries > 0 && cannibals + missionaries <= boatCapacity
                            && (missionaries >= cannibals || missionaries == 0)) {
                        if (bw) {
                            createNewState(current, "" + (missionariesOnWest - missionaries) +
                                    (cannibalsOnWest - cannibals) + "0-" + (missionariesOnEast + missionaries) +
                                    (cannibalsOnEast + cannibals) + "1", path, finalState, boatCapacity);
                        } else {
                            createNewState(current, "" + (missionariesOnWest + missionaries) +
                                    (cannibalsOnWest + cannibals) + "1-" + (missionariesOnEast - missionaries) +
                                    (cannibalsOnEast - cannibals) + "0", path, finalState, boatCapacity);
                        }
                    }
                }
            }
        }
    }

    //node class
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

        //adds a node as a child to the list of current node's children
        void addChild(Node n) {
            children.add(n);
        }

        //this method returns a 2 element array
        //first element is the length of shortest path
        //second element is the number of such paths^
        int[] getLengthAndNumberOfTheShortestPaths(String finalState) {
            ArrayList<Integer> lengths = new ArrayList<>();

            getLengthsOfPaths(lengths, 0, finalState);

            int min = Integer.MAX_VALUE;
            for (int length : lengths) {
                min = Math.min(min, length);
            }

            int count = 0;

            for (int length : lengths) {
                if (length == min) {
                    count++;
                }
            }
            return new int[]{min, count};
        }

        void getLengthsOfPaths(ArrayList<Integer> allLengths, int length, String finalState) {
            //if the current node is a leaf or we reach the final state
            //we add the length we covered so far to the lengths list
            if (children.size() == 0 && state.equals(finalState)) {
                //Since initial node is an empty state and
                //our state space's root is added as its child
                //we should deduct 1 from the total  length
                allLengths.add(length - 1);
            } else {
                //we recursively perform length calculation fo each child
                //and subsequently add their length to the list provided as an argument
                for (Node child : children) {
                    child.getLengthsOfPaths(allLengths, length + 1, finalState);
                }
            }
        }

        // A* search method
        void aStarSearch(ArrayList<String> path, String finalState) {
            //since the first node we created was an empty string
            //we neglect the case where state is equal to an empty string
            if (!state.equals("")) {
                path.add(state);
            }

            //Since g(n) is same for all children of a node
            //we consider only the h(n) values that contribute to f(n)
            //we choose child with the least value of h(n), which we didn't need to
            //estimate, since we could easily calculate its exact value
            //being the length from that child to the target Node(final state)

            //Node min represents child with smallest value of h(n)
            Node min = null;

            //for iteration purposes minH represents minimum value of h(n) so far
            int minH = Integer.MAX_VALUE;

            //we iterate over all children and find the one with minimum value of h(n)
            for (Node child : children) {

                //the method that we call for each children calculates the length
                //of shortest path from that child to the target node(final state)
                //and the number of such paths
                int childToTarget = child.getLengthAndNumberOfTheShortestPaths(finalState)[0];
                if (childToTarget < minH) {
                    minH = childToTarget;
                    min = child;
                }
            }

            //we terminate if the current node is a leaf
            if (children.size() == 0) {
                return;
            }
            //if the "min node" is not null we perform A* search on it
            assert min != null;
            min.aStarSearch(path, finalState);
        }

        //dfs method from HW1 used as an auxiliary method to
        //test and for visualization
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
