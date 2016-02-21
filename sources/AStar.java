
import java.util.ArrayList;
import com.inamik.utils.*;

//This Class implements the AStar Algorithm
public class AStar {

    //The currentNode
    private final Node root;

    //Define the AStar heuristic
    private final Main.heuristics heuristic;

    //Define the run mode
    private final Main.runModes runMode;

    //Nodes Expanded Counter
    private int nodesExpanded = 0;

    //Path Cost
    private int pathCost = 0;

    //Fringe Max
    private int fringeMax = 0;

    //Iterations
    private int iteration = 1;

    //The Constructor Method
    public AStar(Node root, Main.heuristics heuristic, Main.runModes runMode) {

        //Set the current node
        this.root = root;

        //Set the heuristic
        this.heuristic = heuristic;

        //Set the run mode
        this.runMode = runMode;

    }

    //Find the best path
    public Node find() {

        //The current node
        Node current = null;

        //Create the two lists
        ArrayList<Node> expandedList = new ArrayList<>();
        ArrayList<Node> fringeList = new ArrayList<>();

        //Add initial root node
        fringeList.add(root);

        //While the fringeList is not empty
        while (!fringeList.isEmpty()) {

            current = null;

            //Calculate the maximum value for the fringe list
            fringeMax = (fringeList.size() > fringeMax) ? fringeList.size() : fringeMax;

            //Increment the expanded list counter
            nodesExpanded += expandedList.size();

            //Get the node with the lowest f(n)
            for (Node n : fringeList) {

                //find the lowest cost based on the heuristic
                if (current == null || this.findCost(n) < this.findCost(current)) {

                    current = n;
                }

            }

            //Get the node`s children
            ArrayList<Node> children = current.getChildren();

            //Print the current board
            this.printCurrentBoard(current);

            //Print the nodes depending on the runMode (DEBUG_1, DEBUG_2)
            switch (runMode) {

                case DEBUG_1:
                    this.printExpandedNodes(children);
                    break;

                case DEBUG_2:
                    this.printFringeList(fringeList);
                    break;

            }

            //Check if the goal was reached
            if (this.reached(current)) {

                //Set the path cost
                pathCost = current.getDepth();

                return current;

            }

            //Remove the node from the fringeList
            fringeList.remove(current);

            //Add it to the expanded list
            expandedList.add(current);

            //For each children
            for (Node neighbor : children) {

                //Ignore when it is already evaluated
                if (expandedList.contains(neighbor)) {
                    continue;
                }

                //Add node to the fringe list
                if (!fringeList.contains(neighbor)) {
                    fringeList.add(neighbor);
                }

            }

        }

        return current;

    }

    //Find the cost based on the heuristic
    private int findCost(Node node) {

        //Define the heuristic value
        int cost = 0;

        //Select the heuristic to be used
        switch (heuristic) {
            case NO_HEURISTIC:
                //Just the g value, f = g + 0
                cost = node.getDepth();
                break;

            case MISPLACED_TILES:
                //Get the MisplacedTiles Cost
                cost = node.getMisplacedCost();
                break;

            case BEST_HEURISTIC:
                //Get the Best Cost
                cost = node.getBestCost();
                break;

        }

        return cost;

    }

//Verify if the goal was reached
    private boolean reached(Node current) {

        //Start with the first number
        int index = current.getBoard().getFirst();

        //For each number
        for (Integer i : current.getBoard()) {

            //If the number does not match
            if (i != index) {
                return false;
            }

            //If the end was reached, restart
            if (i == current.getBoard().size()) {
                index = 0;
            }

            index++;
        }

        return true;

    }

    //Print the Current Board
    private void printCurrentBoard(Node current) {

        String previousBoard = "";
        String newBoard = "";

        //For each number, print it
        newBoard = current.getBoard().stream().map((i) -> i + " ").reduce(newBoard, String::concat);

        //Verify if the node`s parent is not null
        if (current.getParent() != null) {

            //For each number, print it
            previousBoard = current.getParent().getBoard().stream().map((i) -> i + " ").reduce(previousBoard, String::concat);
        }

        //Create the header for the table
        TableFormatter tf = new SimpleTableFormatter(true)
                .nextRow()
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Iteration")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Previous Board")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Movements")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("New Board")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Depth")
                .nextRow()
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(String.valueOf(iteration++))
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(previousBoard)
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(String.valueOf(current.getSlides()))
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(newBoard)
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(String.valueOf(current.getDepth()));

        String[] table = tf.getFormattedTable();

        for (int o = 0, sizeTable = table.length; o < sizeTable; o++) {

            System.out.println(table[o]);
        }

    }

    //Get the nodes expanded
    public int getNodesExpanded() {

        return this.nodesExpanded;

    }

    //Get the path cost
    public int getPathCost() {

        return this.pathCost;
    }

    //Get the fringe max
    public int getFringeMax() {
        return this.fringeMax;
    }

    //Print expanded list
    private void printExpandedNodes(ArrayList<Node> children) {

        //Create the header for the table
        TableFormatter tf = new SimpleTableFormatter(true)
                .nextRow()
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Expanded Node")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Cost");

        int index = 0;

        //For each number
        for (Node n : children) {

            String board = "";

            //For each number, print it
            board = n.getBoard().stream().map((i) -> i + " ").reduce(board, String::concat);

            tf.nextRow()
                    .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                    .addLine(board)
                    .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                    .addLine(String.valueOf(this.findCost(n)));

            index++;
        }

        String[] table = tf.getFormattedTable();

        for (int o = 0, sizeTable = table.length; o < sizeTable; o++) {

            System.out.println(table[o]);
        }
    }

    private void printFringeList(ArrayList<Node> fringeList) {
        //Create the header for the table
        TableFormatter tf = new SimpleTableFormatter(true)
                .nextRow()
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Fringe Node")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Movements")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Cost");

        int index = 0;

        //For each number
        for (Node n : fringeList) {

            String board = "";

            //For each number, print it
            board = n.getBoard().stream().map((i) -> i + " ").reduce(board, String::concat);

            tf.nextRow()
                    .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                    .addLine(board)
                    .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                    .addLine(String.valueOf(n.getSlides()))
                    .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                    .addLine(String.valueOf(this.findCost(n)));

            index++;
        }

        String[] table = tf.getFormattedTable();

        for (int o = 0, sizeTable = table.length; o < sizeTable; o++) {

            System.out.println(table[o]);
        }
    }

}
