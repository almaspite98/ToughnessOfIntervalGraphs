package intervalGraph;


import DrawingTool.Line;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
public class Zone {
    public float p1; //start of the zone
    public float p2; //end of the zone
    public float f; // felező pont
    public int n; //number of overlapping intervals in the zone
    public ArrayList<Vertex> zoneVertices;

    public Zone(float x1, float x2, int n){
        zoneVertices=new ArrayList<>();
        p1=x1;
        p2=x2;
        this.n=n;
        f = (p1 + p2)/2;
    }

    public Zone(ArrayList<Vertex> vertices){
        this.zoneVertices=vertices;
        n=vertices.size();
    }
    public ArrayList<Line> getLines(){
        ArrayList<Line> temp=new ArrayList<>();
        for(Vertex v:zoneVertices){
            temp.add(v.line);
        }
        return temp;
    }

    @Override
    public boolean equals(Object o){
         if(o instanceof Zone){
            Zone toCompare = (Zone) o;
            return this.zoneVertices.equals(toCompare.zoneVertices);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return zoneVertices.hashCode();
    }

    @Override
    public String toString(){
        return zoneVertices.toString();
    }

}
