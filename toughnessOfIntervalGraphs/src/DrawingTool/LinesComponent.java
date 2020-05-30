package DrawingTool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LinesComponent extends JPanel{

    private final ArrayList<Line> lines = new ArrayList<>();
    public float scale=10;

    public void addLine(Line l) {
        lines.add(l);
        repaint();
    }

    public void clearLines() {
        lines.clear();
        repaint();
    }

    public void resetLineColors(ArrayList<Line> shinyLines){
        for(Line l:lines){
            if(!shinyLines.contains(l)) l.color=Color.GRAY;
            else{
                if(l.color==Color.GRAY) l.color=Color.CYAN;
            }
        }
        repaint();
    }
    public void resetCliquesAndSeparatorsColorsGray(){
        for(Line l:lines){
            if(l.x1==l.x2) l.color=Color.GRAY;
        }
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        for (int i=0;i<lines.size();i++) {
            Line line=lines.get(i);
            g2d.setColor(line.color);
            if(line.y1==line.y2){
                g2d.setStroke(new BasicStroke(3));
            }else{
                float[] dash = { 10.0f };
                g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            }
            g2d.drawLine(Math.round(line.x1*scale), Math.round(line.y1*scale), Math.round(line.x2*scale), Math.round(line.y2*scale));
            if(line.x1!=line.x2) g2d.drawString(String.valueOf(line.c).toUpperCase(),((line.x1+line.x2)/2)*scale,(line.y1+1.5f)*scale);
            else g2d.drawString(String.valueOf(line.c),(line.x1-0.4f)*scale,(line.y1-1.0f)*scale);

        }
    }
}