
import java.io.*;
import java.util.*;

import java.util.concurrent.ThreadLocalRandom;
import com.inamik.utils.*;

//The Main Class
public class Main {

    //Default Board Size
    private static int boardSize;

    //Run Mode Types
    public static enum runModes {
        NORMAL,
        DEBUG_1,
        DEBUG_2,
    };

    //Define the heuristics available
    public static enum heuristics {
        NO_HEURISTIC, //Uniform Cost Search
        MISPLACED_TILES,
        BEST_HEURISTIC //Our own heuristic
    };

    //Default Run Mode
    private static runModes runMode = runModes.NORMAL;

    //Default Heuristic
    private static heuristics heuristic = heuristics.BEST_HEURISTIC;

    //The Root Node
    private static Node root;

    //The Main Method
    public static void main(String[] args) {

        //Check the parameters
        try {

            int argsIndex = 0;

            //Verify if the program will run in debug mode
            if (args[0].equals("-d1")) {
                runMode = runModes.DEBUG_1;
                argsIndex++;
            } else if (args[0].equals("-d2")) {
                runMode = runModes.DEBUG_2;
                argsIndex++;
            }

            //Define the board sizes
            boardSize = Integer.parseInt(args[argsIndex]);

            //Verify if the board will be randomly
            if (args[++argsIndex].equals("random")) {

                //Random the board
                root = new Node(randomBoard());
            } else {

                //Read the file
                root = new Node(boardFromFile(args[argsIndex]));

            }

            //Define the heuristic
            switch (args[++argsIndex]) {
                case "1":
                    heuristic = heuristics.NO_HEURISTIC;
                    break;
                case "2":
                    heuristic = heuristics.MISPLACED_TILES;
                    break;
                case "3":
                    heuristic = heuristics.BEST_HEURISTIC;
                    break;
                default:
                    break;
            }

        } catch (Exception exception) {

            System.out.println("You must specify all parameters. Please enter: -d1/-d2 sizeBoard file.txt/random");

            System.exit(0);

        }

        System.out.println("Board is Constructed");

        System.out.println(
                "-----------------------------------------");

        for (Integer i : root.getBoard()) {

            System.out.print(i + " ");
        }

        System.out.println();

        //Initialize the AStart Algorithm - NO HEURISTIC = UNIFORM COST SEARCH
        AStar aStar = new AStar(root, heuristic, runMode);

        //Find the best path
        Node finalNode = aStar.find();

        //Check if the goal was reached
        if (finalNode != null) {

            //Print all the movements
            System.out.println();
            System.out.println("ALL MOVEMENTS");
            printPath(finalNode);

            //Print some statistics
            System.out.println();
            System.out.println("SUMMARY INFORMATION");
            printStatistics(finalNode, aStar);

        } else {

            System.out.print("FINAL BOARD: Not Found");
        }

    }

//Print some statistics
    private static void printStatistics(Node finalNode, AStar aStar) {

        String finalBoard = "";

        //For each number, print it
        finalBoard = finalNode.getBoard().stream().map((i) -> i + " ").reduce(finalBoard, String::concat);

        //Create the header for the table
        TableFormatter tf = new SimpleTableFormatter(true)
                .nextRow()
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Path Cost")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Nodes Expanded")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Fringe Max")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Final Board")
                .nextRow()
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(String.valueOf(aStar.getPathCost()))
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(String.valueOf(aStar.getNodesExpanded()))
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(String.valueOf(aStar.getFringeMax()))
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine(finalBoard);

        String[] table = tf.getFormattedTable();

        //Print the table
        for (int o = 0, sizeTable = table.length; o < sizeTable; o++) {

            System.out.println(table[o]);
        }
    }

    //Print the final path
    private static void printPath(Node finalNode) {

        //Create the header for the table
        TableFormatter tf = new SimpleTableFormatter(true)
                .nextRow()
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Board")
                .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                .addLine("Movements");

        Node current = finalNode;

        do {

            String board = "";

            //For each number, print it
            board = current.getBoard().stream().map((i) -> i + " ").reduce(board, String::concat);

            tf.nextRow()
                    .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                    .addLine(board)
                    .nextCell(TableFormatter.ALIGN_CENTER, TableFormatter.VALIGN_CENTER)
                    .addLine(String.valueOf(current.getSlides()));

            //Update the node
            current = current.getParent();

        } while (current != null);

        String[] table = tf.getFormattedTable();

        //Print the table
        for (int o = 0, sizeTable = table.length; o < sizeTable; o++) {

            System.out.println(table[o]);
        }
    }

    //Random the board
    private static LinkedList randomBoard() {

        //Initial board
        LinkedList<Integer> board = new LinkedList<>();

        //While the board is not full
        while (board.size() < boardSize) {

            //Random a number
            int randomNumber = ThreadLocalRandom.current().nextInt(1, boardSize + 1);

            //Check if the number is not inserted
            if (!board.contains(randomNumber)) {

                //Add random number to the board
                board.add(randomNumber);

            }

        }

        //Return the board
        return board;

    }

    //Read the file that contains the sequence
    private static LinkedList boardFromFile(String path) {

        //Initial board
        LinkedList<Integer> board = new LinkedList<>();

        Scanner file = null;

        try {

            //Open the file
            file = new Scanner(new File(path));

            //For each line in the file
            while (file.hasNextLine()) {

                //Get the line content
                String input = file.nextLine().trim();

                //Chech if the line is empty
                if (!input.isEmpty()) {

                    //Break the content into whitespaces
                    Scanner numbers = new Scanner(input);
                    numbers.useDelimiter(" ");

                    //For each number
                    while (numbers.hasNext()) {

                        //Add the number
                        board.add(Integer.parseInt(numbers.next()));

                    }
                }

            }
        } catch (FileNotFoundException exception) {
            System.out.println("Unable to open file '" + path + "'");
            System.exit(0);
        } catch (Exception exception) {
            System.out.println("Error.");
            System.exit(0);
        }

        //Return the board
        return board;

    }

}
