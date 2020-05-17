package intervalGraph;

import lombok.AllArgsConstructor;
import java.util.ArrayList;


public class Lane {
    public ArrayList<Vertex> laneVertices;
    public int id; //0,1,....
    public Lane(int id){
        laneVertices = new ArrayList<>();
        this.id = id;
    }

    public void setYToLaneVertices(int y0,int y){
        for(Vertex v: laneVertices){
            v.line.y1 = y0+id*y;
            v.line.y2 = y0+id*y;
        }
    }
}
