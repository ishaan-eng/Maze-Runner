package maze.algo.solving;

import java.util.Objects;

class Node 
{

    private static final int EDGE_COST = 1;                   //The cost of moving to neighboring nodes.
    private final int row;                                    //The vertical coordinate of this node in a grid.
    private final int column;                                 //The horizontal coordinate of this node in a grid.
    private final boolean isWall;                             //Indicates if this node is a wall.

    private Node parent;           // A parent node is saved to reconstruct a path if it goes through this node. If node has no parent its parent is equal to this node
    private int g;                 //The cost of the path from the start node to this node.
    private int h;                 //The estimated cost of the path from this node to the end node.
    private int f;                  //The final cost of the path from the start node to the end node through this node.

    Node(int row, int column, boolean isWall)     // Creates a new node with given row and column and sets its parent to itself.
    {
        this.row = row;
        this.column = column;
        this.isWall = isWall;
        parent = this;
    }

    int getRow() 
    {
        return row;
    }

    int getColumn() 
    {
        return column;
    }

    boolean isWall() 
    {
        return isWall;
    }

    Node getParent() 
    {
        return parent;
    }

    int getFinalCost() 
    {
        return f;
    }

    void calcHeuristicTo(Node node) //Calculates the estimated cost of the path from this node to the end node. Manhattan distance to calculate the heuristic is used.
    {
        this.h = Math.abs(node.row - this.row)
            + Math.abs(node.column - this.column);
    }

    boolean hasBetterPath(Node node)   //Checks if the path through the given node is better (i.e. cheaper) than the current path.
    {
        return node.g + EDGE_COST < this.g;
    }

    void updatePath(Node node)          //Updates the path such that the given node becomes the new parent and recalculates the final cost through it.
    {
        this.parent = node;
        this.g = node.g + EDGE_COST;
        f = g + h;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var node = (Node) o;
        return row == node.row &&
            column == node.column &&
            isWall == node.isWall;
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(row, column, isWall);
    }
}
