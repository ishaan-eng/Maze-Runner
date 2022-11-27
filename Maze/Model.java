package maze.model;

import maze.algo.generation.PassageTree;
import maze.algo.solving.Fugitive;

import java.util.function.Consumer;

import static java.lang.Integer.parseInt;
import static maze.model.Cell.Type.PASSAGE;
import static maze.model.Cell.Type.WALL;


public class Model
{

    final int height;
    final int width;
    final Cell[][] grid;

    boolean isSolved = false;

    public Model(int height, int width) 
    {
        if (height < 3 || width < 3)
        {
            throw new IllegalArgumentException(
                "Both the height and the width " +
                    "of the maze must be at least 3");
        }
        this.height = height;
        this.width = width;
        grid = new Cell[height][width];
        fillGrid();
    }

    public Model(int size) 
    {
        this(size, size);
    }

    private void fillGrid()
    {
        fillAlternately();
        fillGaps();
        makeEntranceAndExit();
        generatePassages();
    }

    private void putCell(int row, int column, Cell.Type type) 
    {
        grid[row][column] = new Cell(row, column, type);
    }

    private void fillAlternately() 
    {
        for (int i = 0; i < height; i++) 
        {
            for (int j = 0; j < width; j++) 
            {
                if ((i & 1) == 0 || (j & 1) == 0) 
                {
                    putCell(i, j, WALL);
                } 
              else 
              {
                    putCell(i, j, PASSAGE);
              }
            }
        }
    }

 
    private void fillGaps() {
        if (height % 2 == 0) wallLastRow();
        if (width % 2 == 0) wallLastColumn();
    }

    private void wallLastColumn() 
    {
        for (int i = 0; i < height; i++)
            putCell(i, width - 1, WALL);
    }

    private void wallLastRow() 
    {
        for (int i = 0; i < width; i++)
            putCell(height - 1, i, WALL);
    }

    private int getExitColumn() 
    {
        return width - 3 + width % 2;
    }

    private void makeEntranceAndExit() 
    {
        putCell(0, 1, PASSAGE);
        putCell(height - 1, getExitColumn(), PASSAGE);
        if (height % 2 == 0)
            putCell(height - 2, getExitColumn(), PASSAGE);
    }

    private void generatePassages() 
    {
        new PassageTree(height, width)
            .generate()
            .forEach(putCell());
    }

    private Consumer<Cell> putCell() 
    {
        return cell -> grid[cell.getRow()][cell.getColumn()] = cell;
    }

    
    public String findEscape() 
    {
        if (!isSolved) 
        {
            new Fugitive(grid, getEntrance(), getExit())
                .findEscape()
                .forEach(putCell());
            isSolved = true;
        }
        return toString(true);
    }

    private Cell getEntrance() 
    {
        return grid[0][1];
    }

    private Cell getExit() 
    {
        return grid[height - 1][getExitColumn()];
    }

    
    private String toString(boolean showEscape) 
    {
        var sb = new StringBuilder();
        for (var row : grid)
        {
            for (var cell : row) 
            {
                if (cell.isWall()) 
                {
                    sb.append("██");
                } 
              else if (showEscape && cell.isEscape()) {
                    sb.append("▓▓");
                } else {
                    sb.append("  ");
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public String toString() 
    {
        return toString(false);
    }


    public static Model load(String str) {
        try {
            var whole = str.split("\n");
            var size = whole[0].split(" ");
            var height = parseInt(size[0]);
            var width = parseInt(size[1]);
            var grid = new Cell[height][width];
            for (int i = 0; i < height; i++) {
                var row = whole[i + 1].split(" ");
                for (int j = 0; j < width; j++)
                    grid[i][j] = new Cell(
                        i, j, intToType(parseInt(row[j]))
                    );
            }
            return new Model(height, width, grid);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Cannot load the maze. " +
                    "It has an invalid format"
            );
        }
    }

    private Model(int height, int width, Cell[][] grid) 
    {
        this.height = height;
        this.width = width;
        this.grid = grid;
    }

    private static Cell.Type intToType(int val) 
    {
        return val == 1 ? WALL : PASSAGE;
    }

    public String export()
    {
        var sb = new StringBuilder();
        sb.append(height).append(' ')
          .append(width).append('\n');
        for (var row : grid) 
        {
            for (var cell : row)
                sb.append(typeToInt(cell))
                  .append(' ');
            sb.append('\n');
        }
        return sb.toString();
    }
  
    private int typeToInt(Cell cell) 
    {
        return cell.isWall() ? 1 : 0;
    }
}
