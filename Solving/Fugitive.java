package Solving;

import Maze.Cell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import static java.util.Comparator.comparingInt;
import static Maze.Cell.Type.ESCAPE;

public class Fugitive 
{
    private static final int[][] DELTAS = {{-1, 0}, {0, -1}, {0, 1}, {1, 0}};       //Moves to up, left, right and down from the current cell.
    private int height;                         //The height of the maze in nodes.
    private int width;                          //The width of the maze in node.
    private Node[][] grid;                      //Two-dimensional array of nodes representing maze.
    private Node start;                         //The start point to find a path from.
    private Node end;                           //The end point to find a path to.
    private PriorityQueue<Node> open = new PriorityQueue<>(comparingInt(Node::getFinalCost));   //A priority queue to perform the selection of min. estimated cost node
    private Set<Node> closed = new HashSet<>();  //Already processed nodes

    /**
     * Constructs a new object with given grid of cells
     * and start and end cells. Creates a grid of nodes
     * based on that.
     *
     * @param grid  a grid of cells of a maze
     * @param start a start point to find a path from
     * @param end   an end point to find a path to
     */
    public Fugitive(Cell[][] grid, Cell start, Cell end) //Constructs new object with given grid of cells and start and end cells. Creates a grid of nodes based on it.
    {
        this.height = grid.length;
        this.width = grid[0].length;
        this.grid = new Node[height][width];
        this.start = new Node(start.getRow(), start.getColumn(), false);
        this.end = new Node(end.getRow(), end.getColumn(), false);
        createNodes(grid);
    }

    private void createNodes(Cell[][] grid)         //For each cell in a given grid it creates the corresponding node in a grid of nodes. Calculates an estimated cost
    {                                               //to the end for each node.
        for (int i = 0; i < height; i++) 
        {
            for (int j = 0; j < width; j++) 
            {
                var node = new Node(i, j, grid[i][j].isWall());
                node.calcHeuristicTo(end);
                this.grid[i][j] = node;
            }
        }
    }

    public List<Cell> findEscape()      //Find a path from the start to the end using A* search algorithm
    {
        open.add(start);
        while (!open.isEmpty()) 
        {
            var cur = open.poll();
            if (isEnd(cur))
                return reconstructPath(cur);
            closed.add(cur);
            updateNeighbors(cur);
        }
        return new ArrayList<>();
    }

    private boolean isEnd(Node currentNode)     // Check if a node is the end point to find a path to.
    {
        return currentNode.equals(end);
    }

    private List<Cell> reconstructPath(Node cur)    //Reconstructs the path from the given node to the start node, i.e. node having no parent. Returns a
    {                                               //list of cells in the format: start -> ... -> cur.
        var path = new LinkedList<Cell>();
        path.add(toCell(cur));
        while (cur.getParent() != cur) 
        {
            var parent = cur.getParent();
            path.addFirst(toCell(parent));
            cur = parent;
        }
        return path;
    }

    private Cell toCell(Node node)          //Converts a node back to the cell format. Cell type is escape path.
    {
        return new Cell(node.getRow(), node.getColumn(), ESCAPE);
    }

    /**
     * Updates an estimated and a final costs of neighboring
     * cells according to the
     * <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">
     * A* search algorithm</a>.
     *
     * @param cur a node which neighbors are updated
     */
    private void updateNeighbors(Node cur)  //Updates an estimated and a final costs of neighboring cells according to the A* search algorithm.
    {
        for (var delta : DELTAS) 
        {
            var row = cur.getRow() + delta[0];
            var column = cur.getColumn() + delta[1];
            if (inBounds(row, column)) 
            {
                var node = grid[row][column];
                if (!node.isWall() && !closed.contains(node))
                {
                    if (open.contains(node)) 
                    {
                        if (node.hasBetterPath(cur)) 
                        {
                            open.remove(node);
                        } 
                        else 
                        {
                            continue;
                        }
                    }
                    node.updatePath(cur);
                    open.add(node);
                }
            }
        }
    }

    private boolean inBounds(int row, int column)      //Checks if given cell indices are in bounds of the 2-dimensional array.
    {
        return row >= 0 && row < height
            && column >= 0 && column < width;
    }
}
