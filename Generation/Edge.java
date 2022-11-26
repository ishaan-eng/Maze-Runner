package maze.algo.generation;

public class Edge 
{

    
    final int firstCell;                //The coordinate of the first cell.
    final int secondCell;               //The coordinate of the second cell.

    Edge(int firstCell, int secondCell)   //Creates a new edge with given cells coordinates.
    {
        this.firstCell = firstCell;
        this.secondCell = secondCell;
    }

    int getFirstCell()
    {
        return firstCell;
    }

    int getSecondCell()
    {
        return secondCell;
    }
}
