package maze.generation;

class DisjointSet
{

    int[] parent;
    int[] rank;
    int size;
    
    public DisjointSet(int size)
    {
        this.size = size;
        parent = new int[size];
        rank = new int[size];
        range(0, size).forEach(this::makeSet);
    }

    void makeSet(int i) 
    {
        parent[i] = i;
        rank[i] = 0;
    }

    public int getSize() 
    {
        return size;
    }

    public int find(int i) 
    {
        if (i != parent[i])
            parent[i] = find(parent[i]);
        return parent[i];
    }

    public boolean union(int i, int j) 
    {
        var iRoot = find(i);
        var jRoot = find(j);
        if (iRoot == jRoot)
            return false;
        if (rank[iRoot] < rank[jRoot]) 
        {
            parent[iRoot] = jRoot;
        } else 
        {
            parent[jRoot] = iRoot;
            if (rank[iRoot] == rank[jRoot])
                rank[iRoot]++;
        }
        size--;
        return true;
    }
}





class Edge 
{

    
    final int firstCell;
    final int secondCell;

    Edge(int firstCell, int secondCell)
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



import maze.model.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static maze.model.Cell.Type.PASSAGE;
