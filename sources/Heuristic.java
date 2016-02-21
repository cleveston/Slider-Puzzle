
import java.util.LinkedList;

//This class represents the heuristic
public class Heuristic {

    //The current node
    private final Node node;

    //The Constructor Method
    public Heuristic(Node node) {

        //Set the current node
        this.node = node;

    }

    //Generate the heuristic for misplaced tiles
    public int getMisplacedTiles() {

        //The heuristic value
        int h = -1;

        //Get the board
        LinkedList<Integer> board = node.getBoard();

        int index = 0;

        //For each number
        for (Integer i : board) {
            //If the tile is misplaced, sum 1
            h += (Math.abs((index + 1) - i) == 0 ? 0 : 1);
            index++;
        }

        return h;
    }

    //Generate the best heuristic
    public int getBestHeuristic() {

        //Get the board
        LinkedList<Integer> board = node.getBoard();

        //The heuristic value
        int h = board.size();

        //For each number
        for (int i = 1; i < board.size() - 1; i++) {

            //Compare the current number with its subsequent   
            if (board.get(i) == board.get(i - 1) - 1) {
                h--;
            } else if (board.get(i) == board.get(i - 1) + 1) {
                h--;
            }
            if (board.get(i) == board.get(i + 1) - 1) {
                h--;
            } else if (board.get(i) == board.get(i + 1) + 1) {
                h--;
            }

        }
        return h;

    }

}
