package Algorithm;

import intervalGraph.Vertex;
import intervalGraph.Zones;
import lombok.AllArgsConstructor;
import java.util.ArrayList;

@AllArgsConstructor
public class Component {
    public ArrayList<Vertex> componentVertices;
    public Zones componentZones;
    public int l;
    public int r;
    public String mark;
    /*public Component(ArrayList<Vertex> v,Zones cz,int l, int r,String empty){
        componentVertices=v;
        componentZones=cz;
        this.l=l;
        this.r=r;
        mark=empty;
    }*/
    @Override
    public String toString() {
        return "("+l+","+r+") "+componentVertices+" "+mark;
    }
}
