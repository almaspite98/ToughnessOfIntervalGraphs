package intervalGraph;

import DrawingTool.Line;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Vertex {
    public Line line;
    public char c;

    /*@Override
    public String toString() {
        return String.valueOf(c);
    }*/

    @Override
    public String toString() {
        return String.valueOf(c);
    }
}
