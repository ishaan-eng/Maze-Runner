package Maze;

import java.util.Objects;

public class Cell
{
    public enum Type
    {
        PASSAGE,
        WALL,
        ESCAPE;
    }
  
    private final int row;
    private final int column;
    private final Type type;

    public Cell(int row, int column, Type type) 
    {
        this.row = row;
        this.column = column;
        this.type = type;
    }

    public int getRow() 
    {
        return row;
    }

    public int getColumn() 
    {
        return column;
    }

    public boolean isPassage() 
    {
        return type == Type.PASSAGE;
    }

    public boolean isWall() 
    {
        return type == Type.WALL;
    }

    public boolean isEscape() 
    {
        return type == Type.ESCAPE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var cell = (Cell) o;
        return row == cell.row &&
            column == cell.column &&
            type == cell.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, type);
    }

    @Override
    public String toString() {
        return "Cell{" +
            "row=" + row +
            ", column=" + column +
            ", type=" + type +
            '}';
    }
}
