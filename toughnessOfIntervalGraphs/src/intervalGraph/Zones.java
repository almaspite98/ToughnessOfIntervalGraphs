package intervalGraph;


import java.util.ArrayList;

public class Zones {
    public ArrayList<Zone> zones;
    public ArrayList<Zone> cliques;
    public ArrayList<Zone> separators;

    public Zones(ArrayList<Point> numberLine,ArrayList<Vertex> vertices) {
        zones=new ArrayList<>();
        makeZonesFromPoints(numberLine);
        makeZonesFromVertices(vertices);
        makeCliqueAndMinimalSeparatorZones();
    }

    public void makeZonesFromVertices(ArrayList<Vertex> vertices) {
        for(Zone z:zones){
            int i=0;
            int j=0;
            while(i<z.n){
                Vertex v=vertices.get(j);
                if(v.line.x1 <= z.f && z.f <= v.line.x2) {
                    z.zoneVertices.add(v);
                    i++;
                }
                j++;
            }
        }
    }

    public void makeZonesFromPoints(ArrayList<Point> numberLine) {
        int i = 0;
        int n = 0;
        while (i < numberLine.size() - 1) {
            float p1 = numberLine.get(i).x;
            float p2 = numberLine.get(i + 1).x;
            n += numberLine.get(i).c;
            zones.add(new Zone(p1, p2, n));
            i += 1;
        }

    }

    public void printOutZones(ArrayList<Zone> zones1) {
        for (Zone z : zones1) {
            System.out.println(z.p1 + " " + z.p2 + " " + z.n+" "+z.zoneVertices.toString());
        }
    }

    public void printOutEverything(){
        System.out.println("Zones: ");
        printOutZones(zones);
        System.out.println("cliques: ");
        printOutZones(cliques);
        System.out.println("separators: ");
        printOutZones(separators);
    }

    public void makeCliqueAndMinimalSeparatorZones() {
        int i = 0;
        cliques = new ArrayList<>();
        separators = new ArrayList<>();
        boolean maxFind = true;
        while (i < zones.size() - 1) {
            if (maxFind) {
                if (zones.get(i).n > zones.get(i + 1).n) {
                    cliques.add(zones.get(i));
                    maxFind = false;
                }
            } else {
                if (isSeparator(i)) {
                    separators.add(zones.get(i));
                    maxFind = true;
                }
            }
            i++;
        }
        if(maxFind && zones.size()>0)cliques.add(zones.get(zones.size() - 1));
    }
    public boolean isSeparator(int i){
        return zones.get(i).n < zones.get(i + 1).n;
    }
}
