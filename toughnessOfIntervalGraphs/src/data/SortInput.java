package data;

import intervalGraph.Vertex;

import java.util.Comparator;

public class SortInput implements Comparator<Vertex> {

    @Override
    public int compare(Vertex v1, Vertex v2) {
        return Math.round(v1.line.x2 - v2.line.x2);
    }
}
