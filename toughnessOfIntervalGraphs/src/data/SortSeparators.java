package data;

import intervalGraph.Zone;

import java.util.Comparator;

public class SortSeparators implements Comparator<Zone> {
    @Override
    public int compare(Zone z1, Zone z2) {
        return z1.zoneVertices.size()-z2.zoneVertices.size();
    }
}
