package intervalGraph;

import Algorithm.Component;
import Algorithm.ComponentsOfComponent;
import DrawingTool.Line;
import DrawingTool.LinesComponent;
import data.SortInput;
import data.SortPoints;
import data.SortSeparators;

import java.awt.*;
import java.util.*;

public class IntervalGraph {
    public LinesComponent linesComponent;
    public ArrayList<Vertex> vertices;
    public ArrayList<Component> allComponents;
    public ArrayList<ComponentsOfComponent> allComponentsComponents;
    public ArrayList<ArrayList<ArrayList<Integer>>> ciTable;
    public Zones zones;
    public ArrayList<Lane> lanes;
    public int kpInterval = 100; // 0-100
    public int kpMin = 25; // 0-100
    public int width = 30; // 0-10
    public int minWidth = 10;
    public int y0 = 10;
    public int gap = 5;

    public IntervalGraph() {
        linesComponent = new LinesComponent();
        vertices = new ArrayList<>();
        lanes = new ArrayList<>();
    }

    public void readInGraph() {
        Scanner in = new Scanner(System.in);
        int numOfVertices = in.nextInt();
        char c = 'a';
        int x1;
        int y = y0;
        int x2;
        for (int i = 0; i < numOfVertices; i++) {
            x1 = in.nextInt();
            x2 = in.nextInt();
            Line l = new Line(x1, y, x2, y, c, new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
            vertices.add(new Vertex(l, c));
            linesComponent.addLine(l);
            c++;
        }
        vertices.sort(new SortInput());
        zones = makeZones(vertices);
        drawCliquesAndSeparators(zones);
    }

    public void inputGenerator(int n) {
        float kp, w;
        float x1, x2;
        float y = y0;
        char c = 'a';
        ArrayList<Float> x1s = new ArrayList<>();
        ArrayList<Float> x2s = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            kp = (int) (Math.random() * kpInterval) + kpMin;
            w = (int) (Math.random() * width) + minWidth;
            x1 = kp - (w / 2);
            x2 = kp + (w / 2);
            if (!x1s.contains(x1) && !x2s.contains(x2) && !x1s.contains(x2) && !x2s.contains(x1)) {
                Line l = new Line(x1, y, x2, y, c, new Color((float) Math.random(), (float) Math.random(), (float) Math.random()));
                vertices.add(new Vertex(l, c));
                linesComponent.addLine(l);
                x1s.add(x1);
                x2s.add(x2);
                c++;
            } else {
                i--;
            }
        }
        vertices.sort(new SortInput());
        zones = makeZones(vertices);
        drawCliquesAndSeparators(zones);
    }

    public void printOutVertices() {
        int i = 0;
        for (Vertex v : vertices) {
            System.out.println(v.c + " " + v.line.x1 + " " + v.line.x2);
            v.line.y1 += gap * i;
            v.line.y2 += gap * i;
            i++;
        }
    }

    public ArrayList<Point> makeNumberLine(ArrayList<Vertex> vertices) {
        int i = 0;
        ArrayList<Point> numberLine = new ArrayList<>();
        for (Vertex v : vertices) {
            numberLine.add(new Point(v.line.x1, 1));
            numberLine.add(new Point(v.line.x2, -1));
            i++;
        }
        numberLine.sort(new SortPoints());
        return numberLine;
    }

    public void printOutPoints(ArrayList<Point> numberLine) {
        System.out.println("Points: ");
        for (Point p : numberLine) {
            System.out.println(p.x + " " + p.c);
        }
    }

    public Zones makeZones(ArrayList<Vertex> v) {
        ArrayList<Point> numberLine = makeNumberLine(v);
        return new Zones(numberLine, v);
    }

    public ArrayList<Line> drawCliquesAndSeparators(Zones z) {
        linesComponent.resetCliquesAndSeparatorsColorsGray();
        float y2 = 56;
        char j = '0';
        ArrayList<Line> temp = new ArrayList<>();
        for (int i = 0; i < z.cliques.size(); i++) {
            Zone a = z.cliques.get(i);
            Line la = new Line(a.f, 5, a.f, y2, j, Color.RED);
            linesComponent.addLine(la);
            temp.add(la);
            j++;
        }
        j = '0';
        for (int i = 0; i < z.separators.size(); i++) {
            Zone s = z.separators.get(i);
            Line ls = new Line(s.f, 5, s.f, y2, j, Color.BLUE);
            linesComponent.addLine(ls);
            temp.add(ls);
            j++;
        }
        return temp;
    }

    public void makeLanes() {
        for (int i = 0; i < vertices.size(); i++) {
            int whereToPutVertex = findVertexALane(i);
            if (whereToPutVertex >= lanes.size()) {
                lanes.add(new Lane(whereToPutVertex));
            }
            lanes.get(whereToPutVertex).laneVertices.add(vertices.get(i));
        }
        for (Lane l : lanes) l.setYToLaneVertices(y0, gap);
    }

    public int maxNumberOfVerticesInLanes() {
        int max = 0;
        for (Lane l : lanes) {
            if (l.laneVertices.size() > max) max = l.laneVertices.size();
        }
        return max;
    }


    public int findVertexALane(int vertexId) {
        for (Lane l : lanes) {
            if (l.laneVertices.get(l.laneVertices.size() - 1).line.x2 <= vertices.get(vertexId).line.x1) return l.id;
        }
        return lanes.size();
    }

    public void printOutLanes() {
        for (Lane l : lanes) System.out.println("laneVertices: " + l.laneVertices);
    }

    public ArrayList<Component> makeAllComponents(Zones z) {
        String[][] plr = new String[z.cliques.size()][z.cliques.size()];
        ArrayList<Component> components = new ArrayList<>();
        for (int l = 0; l < z.cliques.size(); l++) {
            for (int r = l; r < z.cliques.size(); r++) {
                Component c = makeComponent(l, r, z);
                plr[l][r] = c.mark;
                components.add(c);
            }
        }
        return components;
    }

    public String markComponents(Component c) {
        if (c.componentVertices.isEmpty()) return "empty";
        else if (isComplete(c)) return "complete";
        else if (isConnected(c)) return "noncomplete";
        else return "disconnected";
    }

    public void printOut2DArray(String[][] x) {
        for (String[] strings : x) {
            for (String string : strings) {
                if (string != null) {
                    switch (string.length()) {
                        case 5:
                            System.out.print(string + "        ");
                            break;
                        case 8:
                            System.out.print(string + "     ");
                            break;
                        case 11:
                            System.out.print(string + "  ");
                            break;
                        default:
                            System.out.print(string + " ");
                            break;
                    }
                } else {
                    System.out.print(null + "         ");
                }

            }
            System.out.println();
        }
    }

    public boolean isConnected(Component component) {
        int i = 0;
        for (Zone z : component.componentZones.zones) {
            if (z.n == 0) return false;
            i++;
        }
        return true;
    }

    public boolean isComplete(ArrayList<Vertex> v) {
        for (Vertex v1 : v) {
            for (Vertex v2 : v) {
                if (v1.line.x2 < v2.line.x1 || v1.line.x1 > v2.line.x2) return false;
            }
        }
        return true;
    }

    public boolean isComplete(Component c) {
        for (Zone z : c.componentZones.zones) {
            if (z.n == c.componentVertices.size()) return true;
        }
        return false;
    }

    public boolean isDisconnected(Component c) {
        for (Zone z : c.componentZones.zones) {
            if (z.n == 0) return true;
        }
        return false;
    }

    public boolean isNoncomplete(Component c) {
        return c.mark.equals("noncomplete");
    }

    public void readInPlr(Zones z) {
        Scanner in = new Scanner(System.in);
        int l = in.nextInt();
        int r = in.nextInt();
        Component component = makeComponent(l, r, z);
        drawComponent(component);
    }

    private Component makeComponent(int l, int r, Zones z) {
        ArrayList<Vertex> union = new ArrayList<>();
        for (int i = l; i <= r; i++) {
            for (Vertex v : z.cliques.get(i).zoneVertices) {
                if (!union.contains(v)) {
                    union.add(v);
                }
            }
        }
        if (l > 0) union.removeAll(z.cliques.get(l - 1).zoneVertices);
        if (r < z.cliques.size() - 1) union.removeAll(z.cliques.get(r + 1).zoneVertices);
        union.trimToSize();
        union.sort(new SortInput());
        Zones componentZones = new Zones(makeNumberLine(union), union);
        Component newComponent = new Component(union, componentZones, l, r, "null");
        newComponent.mark = markComponents(newComponent);
        return newComponent;
    }

    private void drawComponent(Component component) {
        ArrayList<Line> temp = new ArrayList<>();
        for (Vertex v : component.componentVertices) {
            linesComponent.addLine(v.line);
            temp.add(v.line);
        }
        temp.addAll(drawCliquesAndSeparators(component.componentZones));
        linesComponent.resetLineColors(temp);
    }

    public ArrayList<ComponentsOfComponent> makeComponentsOfComponenets() {
        ArrayList<ComponentsOfComponent> ccArray = new ArrayList<>();
        for (int i = 0; i < allComponents.size(); i++) {
            Component c = allComponents.get(i);
            if (c.mark.equals("noncomplete") || c.mark.equals("disconnected")) {
                for (int p = c.l; p < c.r; p++) {
                    ArrayList<Vertex> separator = new ArrayList<>(zones.separators.get(p).zoneVertices);
                    ArrayList<Vertex> temp = new ArrayList<>(c.componentVertices);
                    temp.retainAll(separator);
                    String mark;
                    if(temp.size()==0 && c.mark.equals("disconnected")){
                        mark="minimal";
                    }
                    else{
                        if (c.componentZones.separators.contains(new Zone(temp))) mark = "minimal";
                        else  mark = "not minimal";
                    }

                    ArrayList<Vertex> newComponentVertices = new ArrayList<>(c.componentVertices);
                    newComponentVertices.removeAll(separator);
                    Zones newComponentZones = makeZones(newComponentVertices);
                    Component newComponent = new Component(newComponentVertices, newComponentZones, c.l, c.r, mark);
                    ComponentsOfComponent cc = new ComponentsOfComponent(temp.size(), p, mark, c, newComponent, makeAllDisjunctComponents(newComponentZones.zones, allComponents));
                    ccArray.add(cc);
                }
            }
        }
        return ccArray;
    }

    public ArrayList<Component> makeAllDisjunctComponents(ArrayList<Zone> zones, ArrayList<Component> allComponents) {
        ArrayList<Component> components = new ArrayList<>();
        ArrayList<Zone> temp = new ArrayList<>();
        for (Zone z : zones) {
            if (z.n != 0) {
                temp.add(z);
            } else {
                Component c = findComponentByZones(temp, allComponents);
                components.add(c);
                temp.clear();
            }
        }
        Component c = findComponentByZones(temp, allComponents);
        components.add(c);
        return components;
    }

    public int completeComponentCi(Component c, int i) {
        if (i == c.componentVertices.size()) return 0;
        else return 1;
    }

    public void printOutComponentsOfComponents(ArrayList<ComponentsOfComponent> componentsOfComponents) {
        for (ComponentsOfComponent cc : componentsOfComponents) {
            System.out.println(cc);
        }
    }

    public double toughness(ArrayList<Integer> ci) {
        double min = 0;
        boolean elso = true;
        for (int i = 0; i < ci.size(); i++) {
            double temp = (double) i / (double) ci.get(i);
            if (ci.get(i) > 1) {
                if (elso) {
                    min = temp;
                    elso = false;
                } else if (temp < min) min = temp;
            }
        }
        return min;
    }

    public int connectednessOfComponent(Component c) {
        int min = 0;
        boolean elso = true;
        if (c.componentZones.separators.size() > 0) {
            for (Zone s : c.componentZones.separators) {
                if (elso) {
                    min = s.n;
                    elso = false;
                } else {
                    if (s.n < min) min = s.n;
                }
            }
            return min;
        } else return c.componentVertices.size();
    }

    public ComponentsOfComponent findComponentsOfComponent(ArrayList<ComponentsOfComponent> ccArray, int p, int l, int r) {
        for (ComponentsOfComponent cc : ccArray) {
            if (cc.p == p && cc.oldComponent.l == l && cc.oldComponent.r == r) return cc;
        }
        System.out.println("(" + p + "," + l + "," + r + ") nem található!");
        return null;
    }

    public Component findComponent(int l, int r, ArrayList<Component> components) {
        for (Component c : components) {
            if (c.l == l && c.r == r) return c;
        }
        System.out.println("(" + l + "," + r + ") nem található!");
        return null;
    }

    public Component findComponentByZones(ArrayList<Zone> zones, ArrayList<Component> components) {
        for (Component c : components) {
            if (c.componentZones.zones.equals(zones)) return c;
        }
        System.out.println("zones: " + zones + " nem található!");
        return null;
    }

    public int[][] divideI(int i) {
        int p = i;
        int q = i - p;
        int[][] pqs = new int[i + 1][i + 1];
        while (p >= 0) {
            pqs[p][q] = 1;
            p--;
            q++;
        }
        return pqs;
    }

    public int numberOfComponents(Component c) {
        int db = 1;
        for (Zone z : c.componentZones.zones) {
            if (z.n == 0) db++;
        }
        return db;
    }

    public int lemma(ArrayList<Integer> LrMinusOne, ArrayList<Integer> Hr, int i) {
        int max = 0;
        int[][] pqs = divideI(i);
        for (int p = 0; p <= i; p++) {
            for (int q = 0; q <= i; q++) {
                if (q < Hr.size() && p < LrMinusOne.size()) {
                    if (pqs[p][q] == 1 && (LrMinusOne.get(p) + Hr.get(q)) > max)
                        max = LrMinusOne.get(p) + Hr.get(q);
                }
            }
        }
        return max;
    }

    public ComponentsOfComponent findComponentsOfComponent(Component c, int p) {
        for (ComponentsOfComponent cc : allComponentsComponents) {
            if (cc.oldComponent.l == c.l && cc.oldComponent.r == c.r && cc.p == p) return cc;
        }
        System.out.println("(" + p + "," + c.l + "," + c.r + ") nem található!");
        return null;
    }

    public ComponentsOfComponent findComponentsOfComponent(Component c) {
        for (ComponentsOfComponent cc : allComponentsComponents) {
            if (cc.oldComponent.l == c.l && cc.oldComponent.r == c.r) return cc;
        }
        System.out.println("(" + c.l + "," + c.r + ") nem található!");
        return null;
    }

    int sizeOfFirstNComponent(ComponentsOfComponent cc,int n){
        int db=0;
        for(int i=0;i<=n;i++){
            if(cc.componentsOfComponent.get(i)!=null){
                db+=cc.componentsOfComponent.get(i).componentVertices.size();
            }
        }
        return db;
    }

    public int ComponentCi(int l, int r, int i) {
        Component c = findComponent(l, r, allComponents);
        int ci = 0;
        for (int p = l; p < r; p++) {
            ComponentsOfComponent cc = findComponentsOfComponent(c, p);
            if (i >= cc.i && cc.mark.equals("minimal")) {
                ArrayList<ArrayList<Integer>> HLists = new ArrayList<>();
                for (int j = 0; j < cc.componentsOfComponent.size(); j++) {
                    Component Pj = cc.componentsOfComponent.get(j);
                    if (Pj != null) {
                        ArrayList<Integer> Hj = new ArrayList<>();
                        for (int m = 0; m <= Pj.componentVertices.size(); m++) {
                            int ciPj = ciTable.get(Pj.l).get(Pj.r).get(m);
                            Hj.add(ciPj);
                        }
                        HLists.add(Hj);
                    }
                }
                ArrayList<ArrayList<Integer>> LLists = new ArrayList<>();
                if (HLists.size() != 0) {
                    LLists.add(HLists.get(0));
                    for (int t = 1; t < cc.componentsOfComponent.size(); t++) {
                        ArrayList<Integer> L = new ArrayList<>();
                        for (int o = 0; o < sizeOfFirstNComponent(cc,t); o++) {
                            L.add(lemma(LLists.get(t - 1), HLists.get(t), o));
                        }
                        LLists.add(L);
                    }
                    int k = cc.componentsOfComponent.size() - 1;
                    if (LLists.get(k).get(i - cc.i) > ci) ci = LLists.get(k).get(i - cc.i);
                }
            } else {
            }
        }
        return ci;
    }

    public void makeCiTableForCompleteComponents(int t) {
        for (int l = 0; l < t; l++) {
            for (int r = l; r < t; r++) {
                ArrayList<Integer> cis = new ArrayList<>();
                Component c = findComponent(l, r, allComponents);
                if (c.mark.equals("complete")) {
                    for (int i = 0; i <= c.componentVertices.size(); i++) {
                        cis.add(completeComponentCi(c, i));
                    }
                    ciTable.get(l).get(r).addAll(cis);
                }
            }
        }
    }

    public void plld(int t) {
        ciTable = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            ciTable.add(new ArrayList<>(t));
            for (int j = 0; j < t; j++) {
                ciTable.get(i).add(new ArrayList<>(t));
            }
        }
        allComponentsComponents = makeComponentsOfComponenets();
        makeCiTableForCompleteComponents(t);
        for (int d = 0; d < t; d++) {
            for (int l = 0; l < t - d; l++) {
                Component plr = findComponent(l, l + d, allComponents);
                ArrayList<Integer> cis = new ArrayList<>();
                if (!plr.mark.equals("complete")) {
                    for (int i = 0; i <= plr.componentVertices.size(); i++) {
                        if (i == plr.componentVertices.size() - 1) cis.add(1);
                        else cis.add(0);
                    }
                    for (int i = 0; i < connectednessOfComponent(plr); i++) {
                        cis.set(i, 1);
                    }
                    for (int i = connectednessOfComponent(plr); i <= plr.componentVertices.size() - 2; i++) {
                        cis.set(i, ComponentCi(l, l + d, i));
                    }
                    ciTable.get(l).get(l + d).addAll(cis);
                }

            }
        }
        printOutCiTable();
    }

    public void printOutCiTable() {
        System.out.println("CITABLE: ");
        for (int i = 0; i < ciTable.size(); i++) {
            for (int j = i; j < ciTable.get(i).size(); j++) {
                System.out.print("(" + i + "," + j + "): " + ciTable.get(i).get(j) + "  ");
            }
            System.out.println();
        }
        System.out.println("TOUGHNESS: " + toughness(ciTable.get(0).get(zones.cliques.size() - 1)));
        System.out.println("DONE");
    }
}