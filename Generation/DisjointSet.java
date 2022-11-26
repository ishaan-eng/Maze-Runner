package maze.generation;

import static java.util.stream.IntStream.range;

public class DisjointSet
{                                //Representatives for disjoint subsets. If the set consists
                                 //only of the one element its parent equals to its id.
    int[] parent;                //Otherwise, its parent is the next element up the tree.
    int[] rank;                  //Heights of the trees corresponding to the subsets. A subset with one element has a rank of zero.
    int size;                    //The number of disjoint subsets.
    
    public DisjointSet(int size)                //Constructs a disjoint set of disjoint subsets(size).
    {
        this.size = size;
        parent = new int[size];
        rank = new int[size];
        range(0, size).forEach(this::makeSet);
    }

    void makeSet(int i)                         //Initializes a particular set.
    {
        parent[i] = i;
        rank[i] = 0;
    }

    public int getSize()                        //Returns the number of disjoint subsets.
    {
        return size;
    }

    public int find(int i)                      //Finds a representative for the set. If the set consists only of the one element its parent equals to its id.
    {
        if (i != parent[i])
            parent[i] = find(parent[i]);
        return parent[i];
    }

    public boolean union(int i, int j)          //Merges two disjoint sets into one by the ids if their ids are not in the same set already.
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
