README

This program implements a solution for the slider game puzzle.
This solution is implemented with A* algorithm using three heuristics.

The BEST HEURISTIC was implemented by using comparisons between the current number and its successor.
So that we can measure how sorted is the board. 
We start with h = board.size().
For each number followed by its successor, we do h-- and so on.
If the board is fully sorted the return will be 0.
This approach gives us a good measure for this problem.


The program works with the following commands:
---------------------------------------------------------
java -jar SliderGame.jar {debugMode} boardSize dataSource heuristic
---------------------------------------------------------

debugMode can be optional or as follow:
    -d1 - Debug mode 1
    -d2 - Debug mode 2

boardSize can be any value above 5

dataSource can be:
    - A text file with the number, eg. input.txt
    - The word "random", which will generate the numbers. eg. random

heuristics can be:
    1 - NO HEURISTIC
    2 - MISPLACED TILES
    3 - BEST HEURISTIC


eg.
java -jar SliderGame.jar -d1 6 input.txt 3
java -jar SliderGame.jar -d2 10 random 1

---------------------------------------------------------------------------------

Some interesting result can be seen below. We can note that the BEST HEURISTIC provides a very good result compared to the others.

The initial board was 1 2 3 4 8 7 6 5 9 10 11 12 16 15 14 13

NO HEURISTIC

SUMMARY INFORMATION
+---------+--------------+----------+---------------------------------------+
|Path Cost|Nodes Expanded|Fringe Max|              Final Board              |
+---------+--------------+----------+---------------------------------------+
|    2    |     4005     |   1336   |13 14 15 16 1 2 3 4 5 6 7 8 9 10 11 12 |
+---------+--------------+----------+---------------------------------------+

MISPLACED TILES

SUMMARY INFORMATION
+---------+--------------+----------+---------------------------------------+
|Path Cost|Nodes Expanded|Fringe Max|              Final Board              |
+---------+--------------+----------+---------------------------------------+
|    2    |   15896341   |  84571   |13 14 15 16 1 2 3 4 5 6 7 8 9 10 11 12 |
+---------+--------------+----------+---------------------------------------+

BEST HEURISTIC

SUMMARY INFORMATION
+---------+--------------+----------+---------------------------------------+
|Path Cost|Nodes Expanded|Fringe Max|              Final Board              |
+---------+--------------+----------+---------------------------------------+
|    2    |      3       |    31    |13 14 15 16 1 2 3 4 5 6 7 8 9 10 11 12 |
+---------+--------------+----------+---------------------------------------+


And for the following board: 17 20 19 18 6 7 8 9 10 11 12 16 15 14 13 2 3 4 1 5

NO HEURISTIC
Not Found in reasonable time

MISPLACED TILES
Not Found in reasonable time

BEST HEURISTIC

SUMMARY INFORMATION
+---------+--------------+----------+---------------------------------------------------+
|Path Cost|Nodes Expanded|Fringe Max|                    Final Board                    |
+---------+--------------+----------+---------------------------------------------------+
|    9    |    20706     |   3858   |17 18 19 20 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 |
+---------+--------------+----------+---------------------------------------------------+