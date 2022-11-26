package maze.algo.generation;

import maze.model.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static maze.model.Cell.Type.PASSAGE;

public class PassageTree 
{

    int height;               //The height of the maze in an imaginary edge form.
    int width;                //The width of the maze in an imaginary edge form.

    public PassageTree(int height, int width)     //Creates a new imaginary edge form
    {
        this.height = (height - 1) / 2;
        this.width = (width - 1) / 2;
    }

    public List<Cell> generate()                  //Generates a random list of cells that connect passages in an original form such that a maze is simply connected.
    {
        var edges = createEdges();
        Collections.shuffle(edges);
        var tree = buildRandomSpanningTree(edges);
        return createPassages(tree);
    }

    private List<Edge> createEdges()              //Creates a list of all possible edges in an imaginary edge form.
    {
        var edges = new ArrayList<Edge>();
        for (int column = 1; column < width; column++) 
        {
            edges.add(new Edge(toIndex(0, column),
                               toIndex(0, column - 1)));
        }
        for (int row = 1; row < height; row++)
        {
            edges.add(new Edge(toIndex(row, 0),
                               toIndex(row - 1, 0)));
        }
        for (int row = 1; row < height; row++) 
        {
            for (int column = 1; column < width; column++) 
            {
                edges.add(new Edge(toIndex(row, column),
                                   toIndex(row, column - 1)));
                edges.add(new Edge(toIndex(row, column),
                                   toIndex(row - 1, column)));
            }
        }
        return edges;
    }

    private int toIndex(int row, int column)        //Transforms the coordinates in a 2-dimensional array to the coordinate in a 1-dimensional array 
    {                                               //using row * width + column formula.
        return row * width + column;
    }

    private List<Edge> buildRandomSpanningTree(List<Edge> edges) //Generates a list of edges that connect passages. Its a Randomized Kruskals algorithm implementation.
    {                                                            //On each step of the algorithm an edge is added to the list only if it connects two disjoint subsets.
        var disjointSets = new DisjointSet(width * height);
        return edges
            .stream()
            .filter(edge -> connects(edge, disjointSets))
            .collect(toList());
    }

    
    private boolean connects(Edge edge, DisjointSet disjointSet)            //Checks if an edge connects two disjoint subsets.
    {
        return disjointSet.union(edge.getFirstCell(), edge.getSecondCell());
    }

    
    private List<Cell> createPassages(List<Edge> spanningTree)              //Scales and converts edges in an imaginary edge form to the cells
    {                                                                       //which connect passages in a original form.
        return spanningTree
            .stream()
            .map(edge -> {
                var first = fromIndex(edge.getFirstCell());
                var second = fromIndex(edge.getSecondCell());
                return getPassage(first, second);
            }).collect(toList());
    }

    private Cell fromIndex(int index)     //Transforms the coordinate in a 1-dimensional array back to the coordinates in a 2-dimensional array using the
    {                                     //row = index / width and column = index % width formulas.
        var row = index / width;
        var column = index % width;
        return new Cell(row, column, PASSAGE);
    }

    private Cell getPassage(Cell first, Cell second)        //Given the coordinates of two cells that compose an edge in an imaginary edge form,
    {                                                       //it scales and transforms them to the coordinates of the cell that connect passages in an
        var row = first.getRow() + second.getRow() + 1;     //original form. Returns a passage cell with this coordinates.
        var column = first.getColumn() + second.getColumn() + 1;
        return new Cell(row, column, PASSAGE);
    }
}
