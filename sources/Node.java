
import java.util.*;

//This class represents the node
public class Node {

    //The Depth value
    private final int g;

    //The parent node
    private final Node parent;

    //The Board
    private LinkedList<Integer> board = new LinkedList<>();

    //The children list
    private ArrayList<Node> children = new ArrayList();

    //The Heuristic Object
    private Heuristic heuristic;

    //Store the state
    private int slidesUntilRotate = 0;

    //The Constructor Method for Root Node
    public Node(LinkedList board) {

        //Set the parent node
        this.parent = null;

        //Set 0 for the g
        this.g = 0;

        //Define initial board
        this.board = board;

        //Define the slides until rotate
        this.slidesUntilRotate = 0;

        //Instantiate the heuristic object
        heuristic = new Heuristic(this);

    }

    //The Constructor Method for normal nodes
    public Node(Node parent, LinkedList board, int slides) {

        //Set the parent node
        this.parent = parent;

        //Increment the g from the parent
        this.g = parent.getDepth() + 1;

        //Define board
        this.board = board;

        //Define the slides until rotate
        this.slidesUntilRotate = slides;

        //Instantiate the heuristic object
        heuristic = new Heuristic(this);

    }

    //Get the Cost for Misplaced Tiles
    public int getMisplacedCost() {

        return this.g + this.heuristic.getMisplacedTiles();

    }

    //Get the Best Cost
    public int getBestCost() {

        return this.g + this.heuristic.getBestHeuristic();

    }

    //Get the Depth
    public int getDepth() {

        //Return the g value
        return this.g;

    }

    //Get the node`s parent
    public Node getParent() {

        //Return the parent
        return this.parent;

    }

    //Get the slides
    public int getSlides() {

        //Return the slides until rotate
        return this.slidesUntilRotate;

    }

    //Get the board
    public LinkedList<Integer> getBoard() {

        return this.board;

    }

    //Get the children
    public ArrayList<Node> getChildren() {

        //Verify if the children list is empty
        if (children.isEmpty()) {

            //Expand the children list
            this.expand();

        }

        //Return the children list
        return this.children;
    }

    //Expand the node
    private void expand() {

        //Get the board
        LinkedList<Integer> currentBoard = this.board;

        //Get the first element
        Integer firstElement = currentBoard.getLast();

        Integer currentElement = null;

        Node newNode;

        //Store the slides until rotate
        int slides = 0;

        //For each number
        while (!Objects.equals(firstElement, currentElement)) {

            //Create the new board
            LinkedList<Integer> newBoard = (LinkedList<Integer>) board.clone();

            //Get the four first elements
            List<Integer> temporary = newBoard.subList(0, 4);

            //Reverse the elements
            Collections.reverse(temporary);

            //Create the new node
            newNode = new Node(this, newBoard, slides);

            //Add the new child to the node
            this.children.add(newNode);

            slides++;

            //Slides numbers
            currentElement = currentBoard.removeFirst();

            currentBoard.addLast(currentElement);

        }

    }

}
