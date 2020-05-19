import DrawingTool.LinesComponent;
import data.SortInput;
import intervalGraph.IntervalGraph;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.swing.*;

public class main {
    public static int n = 15; //intervallumok száma
    public static void main(String args[])
    {
        IntervalGraph ig=new IntervalGraph();
        //ig.inputGenerator(n); //auto generated input
        ig.readInGraph(); //manual input
        //ig.makeZones(ig.vertices);
        ig.makeLanes();


        JFrame testFrame = new JFrame();
        testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ig.linesComponent.setPreferredSize(new Dimension((int) Math.max(1200, 10*(Collections.max(ig.vertices, new SortInput()).line.x2)+50), 600));
        testFrame.getContentPane().add(ig.linesComponent, BorderLayout.CENTER);
        testFrame.pack();
        testFrame.setVisible(true);
        ig.allComponents=ig.makeAllComponents(ig.zones);
        //ig.readInPlr(ig.zones);

        //ig.makeComponentsOfComponenets();
        //ig.makeComponentsOfComponenets();
        ig.plld(ig.zones.cliques.size());
    }

}

