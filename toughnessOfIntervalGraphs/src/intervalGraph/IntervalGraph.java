package intervalGraph;

import Algorithm.Component;
import Algorithm.ComponentsOfComponent;
import DrawingTool.Line;
import DrawingTool.LinesComponent;
import data.SortInput;
import data.SortPoints;

import java.awt.*;
import java.util.*;

public class IntervalGraph {
    public LinesComponent linesComponent;
    public ArrayList<Vertex> vertices;
    public ArrayList<Component> allComponents;
    public ArrayList<ComponentsOfComponent> allComponentsComponents;
    ArrayList<ArrayList<ArrayList<Integer>>> ciTable;
    //ArrayList[][] ciTable;
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
        //System.out.println("Centrals and widths: ");
        for (int i = 0; i < n; i++) {
            kp = (int) (Math.random() * kpInterval) + kpMin;
            w = (int) (Math.random() * width) + minWidth;
            x1 = kp - (w / 2);
            x2 = kp + (w / 2);
            if (!x1s.contains(x1) && !x2s.contains(x2) && !x1s.contains(x2) && !x2s.contains(x1)) {
                //System.out.println("kp: " + kp + " w: " + w);
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
        //System.out.println("Vertices: ");
        for (Vertex v : vertices) {
            //System.out.println(v.c + " " + v.line.x1 + " " + v.line.x2);
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
        //drawCliquesAndSeparators(z);
        return new Zones(numberLine, v);
    }

    public ArrayList<Line> drawCliquesAndSeparators(Zones z) {
        //TODO: Ezt jól gondolom?
        linesComponent.resetCliquesAndSeparatorsColorsGray();
        float y2 = 56;
        char j = '0';
        ArrayList<Line> temp = new ArrayList<>();
        for (int i=0;i<z.cliques.size();i++) {
            Zone a=z.cliques.get(i);
            Line la = new Line(a.f, 5, a.f, y2, j, Color.RED);
            linesComponent.addLine(la);
            temp.add(la);
            j++;
        }
        j = '0';
        for (int i=0;i<z.separators.size();i++) {
            Zone s=z.separators.get(i);
            Line ls = new Line(s.f, 5, s.f, y2, j, Color.BLUE);
            linesComponent.addLine(ls);
            temp.add(ls);
            j++;
        }
        return temp;
    }

    public void makeLanes() {
        int currentLaneId = 0;
        //lanes.add(new Lane(currentLaneId));
        //lanes.get(currentLaneId).laneVertices.add(vertices.get(0));
        for (int i = 0; i < vertices.size(); i++) { //TODO szélsőérték vizsgálat
            int whereToPutVertex = findVertexALane(i);
            if (whereToPutVertex >= lanes.size()) {
                lanes.add(new Lane(whereToPutVertex));
            }
            lanes.get(whereToPutVertex).laneVertices.add(vertices.get(i));
        }
        for (Lane l : lanes) l.setYToLaneVertices(y0, gap);
    }

    public int findVertexALane(int vertexId) {
        for (Lane l : lanes) {
            if (l.laneVertices.get(l.laneVertices.size() - 1).line.x2 <= vertices.get(vertexId).line.x1) return l.id;
        }
        return lanes.size(); //new lane element needed
    }

    public ArrayList<Component> makeAllComponents(Zones z) {
        String[][] plr = new String[z.cliques.size()][z.cliques.size()];
        ArrayList<Component> components = new ArrayList<>();
        for (int l = 0; l < z.cliques.size(); l++) {
            for (int r = l; r < z.cliques.size(); r++) {
                Component c = makeComponent(l, r, z);
                plr[l][r] = c.mark;
                //System.out.println("plr: " + plr[l][r]);
                components.add(c);
                //System.out.println(components.get(components.size() - 1));
                System.out.println(c);
            }
        }
        //printOut2DArray(plr);
        return components;
    }



    public String markComponents(Component c){
        if (c.componentVertices.isEmpty()) return "empty";
        else if (isComplete(c)) return "complete";
        else if (isConnected(c)) return "noncomplete";
        else return "disconnected";
    }

    /*public Component[][] makeComponenetsArray(Zones z) {
        Component[][] plrArray = new Component[z.cliques.size()][z.cliques.size()];
        //ArrayList<Component> components = new ArrayList<>();
        for (int l = 0; l < z.cliques.size(); l++) {
            for (int r = l; r < z.cliques.size(); r++) {
                Component temp = makeComponent(l, r, z);
                if (temp.componentVertices.isEmpty()) temp.mark = "empty";
                else if (isComplete(temp.componentVertices)) temp.mark = "complete";
                else if (isConnected(temp)) temp.mark = "noncomplete";
                else temp.mark = "disconnected";
                plrArray[l][r] = temp;
            }
        }
        //printOut2DArray(plr);
        return plrArray;
    }*/

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
        for(Zone z:c.componentZones.zones){
            if(z.n==c.componentVertices.size()) return true;
        }
        return false;
    }

    public boolean isDisconnected(Component c){
        for(Zone z:c.componentZones.zones){
            if(z.n==0) return true;
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

    private Component makeComponent(int l, int r, Zones z) { //TODO: 0-tól indexeljük a klikkeket és separatorokat
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
        union.sort(new SortInput()); //ez talán redundáns
        Zones componentZones = new Zones(makeNumberLine(union), union);
        Component newComponent=new Component(union, componentZones, l, r, "null");
        newComponent.mark=markComponents(newComponent);
        return newComponent;
    }

    private void drawComponent(Component component) {
        //linesComponent.clearLines();
        ArrayList<Line> temp = new ArrayList<>();
        for (Vertex v : component.componentVertices) {
            linesComponent.addLine(v.line);
            temp.add(v.line);
        }
        temp.addAll(drawCliquesAndSeparators(component.componentZones));
        linesComponent.resetLineColors(temp);
        //System.out.println(component);
    }

    public ArrayList<ComponentsOfComponent> makeComponentsOfComponenets() {
        // P(l,r) metszet Sp -nek a componenseit kell kiszámolni????? ja..
        //noncomplete components
        //separator kiszámolása: l<=p<=r-1
        //mark minimal, ha csak 1 klikkje van a sub componensnek
        //összefüggőség vizsgálata
        int min=0;
        boolean elso=true;
        //allComponents=makeAllComponents(zones);
        ArrayList<ComponentsOfComponent> ccArray=new ArrayList<>();
        for(int i=0;i<allComponents.size();i++){
            Component c=allComponents.get(i);
            if(!c.mark.equals("complete")){
                for(int p=c.l;p<c.r;p++){
                    ArrayList<Vertex> separator = new ArrayList<>(zones.separators.get(p).zoneVertices);
                    ArrayList<Vertex> temp = new ArrayList<>(c.componentVertices);
                    //temp.retainAll(c.componentZones.separators.get(p).zoneVertices);
                    temp.retainAll(separator); //TODO: Sp az eredeti grág sepaparatora vagy a komponense?
                    //check temp minimalis separatora-e a komponensnek
                    //System.out.println(temp);
                    String mark;
                    if(c.componentZones.separators.contains(new Zone(temp))){ //TODO: ezt lehetne hatékonyabban
                        //System.out.println(c+" "+temp+" minimal");
                        //Ha minimális akkor számold ki a Plr\Sp komponenseit
                        // (p,l,r) -> (l1,r1),...
                        mark="minimal";
                        // |Sp metszet Plr| = temp számossága
                        //összefüggőség = min{|Sp metszet Plr| : (p,l,r) marked minimal}
                        if(elso){
                            min=temp.size();
                            elso=false;
                        }else{
                            if(min>temp.size()) min=temp.size();
                        }
                        //System.out.println("|Sp metszet Plr| = "+temp.size()+" "+temp);
                    }else{
                        //System.out.println(c+" "+temp+" not minimal");
                        mark="not minimal";
                    }
                    ArrayList<Vertex> newComponentVertices = new ArrayList<>(c.componentVertices);
                    newComponentVertices.removeAll(separator);
                    Zones newComponentZones=makeZones(newComponentVertices);
                    Component newComponent=new Component(newComponentVertices,newComponentZones,c.l,c.r,mark);
                    System.out.println("c: "+c+" Sp: "+p);
                    ComponentsOfComponent cc=new ComponentsOfComponent(temp.size(),p,mark,c,newComponent,makeAllDisjunctComponents(newComponentZones.zones,allComponents));
                    ccArray.add(cc);
                }
                //System.out.println();
            }
        }
        //printOutComponentsOfComponents(ccArray);
        //allComponentsComponents=ccArray;
        return ccArray;
    }

    public ArrayList<Component> makeAllDisjunctComponents(ArrayList<Zone> zones,ArrayList<Component> allComponents){
        ArrayList<Component> components=new ArrayList<>();
        ArrayList<Zone> temp=new ArrayList<>();
        for(Zone z:zones){
            if(z.n!=0){
                temp.add(z);
            }else{
                Component c=findComponentByZones(temp,allComponents);
                components.add(c);
                System.out.println("disjunct comp.: "+c);
                temp.clear();
            }
        }
        Component c=findComponentByZones(temp,allComponents);
        components.add(c);
        System.out.println("disjunct comp.: "+c);
        return components;
    }

    public int completeComponentCi(Component c,int i){
        if(i==c.componentVertices.size()) return 0;
        else return 1;
    }

    public void printOutComponentsOfComponents(ArrayList<ComponentsOfComponent> componentsOfComponents){
        for(ComponentsOfComponent cc:componentsOfComponents){
            System.out.println(cc);
        }
    }

    public double toughness(int i, int ci){
        if(ci>1)return (double)i/(double)ci;
        else return -100.0; //Teszt ! kasztolás
    }

    public int connectednessOfComponent(Component c){ //Széleket le kell venni, az első klikktől az utolso klikkig
        int min=0;
        boolean elso=true;
        //Zone minSeparator=new Zone();
        if(c.componentZones.separators.size()>0){
            for(Zone s: c.componentZones.separators){
                //System.out.println("s: "+s.zoneVertices+" "+s.n);
                if(elso){
                    min=s.n;
                    //minSeparator=s;
                    elso=false;
                }
                else{
                    if(s.n<min) min=s.n;
                }
            }
            return min;
        }else return c.componentVertices.size(); //Ha nincs egy separator se
    }

    public ComponentsOfComponent findComponentsOfComponent(ArrayList<ComponentsOfComponent> ccArray,int p,int l,int r){
        for(ComponentsOfComponent cc:ccArray){
            if(cc.p==p && cc.oldComponent.l==l && cc.oldComponent.r==r) return cc;
        }
        System.out.println("("+p+","+l+","+r+") nem található!");
        return null;
    }
    public Component findComponent(int l,int r,ArrayList<Component> components){
        for(Component c:components){
            if(c.l==l && c.r==r) return c;
        }
        System.out.println("("+l+","+r+") nem található!");
        return null;
    }

    public Component findComponentByZones(ArrayList<Zone> zones,ArrayList<Component> components){
        for(Component c:components){
            if(c.componentZones.zones.equals(zones)) return c;
        }
        System.out.println("zones: "+zones+" nem található!");
        return null;
    }

    public int[][] divideI(int i){
        int p=i;
        int q=i-p;
        int[][] pqs=new int[i+1][i+1]; //p q
        //Arrays.fill(pqs,0);
        while(p>=0){
            pqs[p][q]=1;
            p--;
            q++;
        }
        return pqs;
    }

    public int numberOfComponents(Component c){
        int db=1;
        for(Zone z:c.componentZones.zones){
            if(z.n==0) db++;
        }
        return db;
    }

    public int lemma(ArrayList<Integer> LrMinusOne, ArrayList<Integer> Hr,int i){
        int max=0;
        int[][] pqs=divideI(i);
        for(int p=0;p<=i;p++){
            for(int q=0;q<=i;q++){
                if(q<Hr.size() && p<LrMinusOne.size()){
                    //System.out.println("pqs[p][q]==1: "+(pqs[p][q]==1));
                    //System.out.println("LrMinusOne.get(p): "+LrMinusOne.get(p));
                    //System.out.println("Hr.get(q): "+Hr.get(q));
                    if(pqs[p][q]==1 && (LrMinusOne.get(p)+Hr.get(q))>max) max=LrMinusOne.get(p)+Hr.get(q); //Hr-t túlindexeli
                }
            }
        }
        //LrMinusOne.add(max);
        return max;
    }

     /*public ArrayList<Integer> makeHjforSmallPieces(Component c){
        ArrayList<Integer> H=new ArrayList<>();
        for(int i=0;i<=c.componentVertices.size();i++){
            if(i<connectednessOfComponent(c)) H.add(1);
            else if(i==c.componentVertices.size())H.add(0);
            else H.add(2);
        }
        return H;
     }*/

    public ComponentsOfComponent findComponentsOfComponent(Component c,int p){
        for(ComponentsOfComponent cc:allComponentsComponents){
            if(cc.oldComponent.l==c.l && cc.oldComponent.r==c.r && cc.p==p) return cc;
        }
        System.out.println("("+p+","+c.l+","+c.r+") nem található!");
        return null;
    }


     /*public ArrayList<Integer> ComponentCis(int l,int r){
        Component c=findComponent(l,r,allComponents);
        ArrayList<Integer> ci=new ArrayList<>();
        for(int i=0;i<=c.componentVertices.size();i++){
            int maxCi=0;
            for(int p=0;p<c.componentZones.separators.size();p++){
                ComponentsOfComponent cc=findComponentsOfComponent(c,p);
                // B -> ci(Pj)
                ArrayList<ArrayList<Integer>> HLists=new ArrayList<>();
                for(int j=0;j<cc.componentsOfComponent.size();j++){
                    Component Pj=cc.componentsOfComponent.get(j);
                    ArrayList<Integer> Hj=new ArrayList<>();
                    for(int m=0;m<Pj.componentVertices.size();m++){
                        int ciPj=ciTable.get(Pj.l).get(Pj.r).get(m);
                        Hj.add(ciPj);
                    }
                    HLists.add(Hj);
                }
                ArrayList<ArrayList<Integer>> LLists=new ArrayList<>();
                LLists.add(HLists.get(0));
                for(int t=1;t<cc.componentsOfComponent.size();t++){
                    ArrayList<Integer> L=new ArrayList<>();
                    for(int o=0;o<cc.component.componentVertices.size();o++){
                        L.add(lemma(LLists.get(t-1),HLists.get(t),o));
                    }
                    LLists.add(L);
                }
                if(LLists.get(p).get(i)>maxCi) maxCi=LLists.get(p).get(i);
                // 4.5
                //4. equation
                // max(p) Lk[i]
            }
            ci.add(maxCi);
        }
        return ci;
     }*/

    public int ComponentCi(int l,int r,int i){ //TODO határértékek
        Component c=findComponent(l,r,allComponents);
        System.out.println("c: "+c);
        System.out.println("i: "+i);
        int ci=0;
        for(int p=l;p<r;p++){
            System.out.println("p: "+p);
            ComponentsOfComponent cc=findComponentsOfComponent(c,p);
            //System.out.println("cc null: "+(cc==null));
            //System.out.println("cc: "+(cc.componentsOfComponent==null));
            //System.out.println("cc.size: "+cc.componentsOfComponent.size());
            if(i>=cc.i) { //TODO: if(sp minimal)
                ArrayList<ArrayList<Integer>> HLists = new ArrayList<>();
                for (int j = 0; j < cc.componentsOfComponent.size(); j++) {
                    Component Pj = cc.componentsOfComponent.get(j);
                    if(Pj!=null){
                        ArrayList<Integer> Hj = new ArrayList<>();
                        printOutCiTable();
                        //System.out.println("Pj: " + Pj);
                        for (int m = 0; m <= Pj.componentVertices.size(); m++) { //Pj.componentVertices.size()
                            //System.out.println("ciTable.size(): " + ciTable.size());
                            //System.out.println("ciTable.get(Pj.l).size(): " + ciTable.get(Pj.l).size());
                            //System.out.println("ciTable.get(Pj.l).get(Pj.r).size(): " + ciTable.get(Pj.l).get(Pj.r).size());
                            //System.out.println("Pj " + Pj);
                            //System.out.println("Pj c" + m + ": " + ciTable.get(Pj.l).get(Pj.r).get(m));
                            int ciPj = ciTable.get(Pj.l).get(Pj.r).get(m);
                            //System.out.println("Pj c" + m + ": " + ciPj);
                            Hj.add(ciPj);
                        }
                        HLists.add(Hj);
                    }
                }
                ArrayList<ArrayList<Integer>> LLists = new ArrayList<>();
                if(HLists.size()!=0){
                    LLists.add(HLists.get(0));
                    for (int t = 1; t < cc.componentsOfComponent.size(); t++) {
                        ArrayList<Integer> L = new ArrayList<>();
                        for (int o = 0; o < c.componentVertices.size()-cc.i; o++) {
                            L.add(lemma(LLists.get(t - 1), HLists.get(t), o));
                        }
                        System.out.println("L" + t + " " + L);
                        LLists.add(L);
                    }
                    int k=cc.componentsOfComponent.size()-1;
                    if(LLists.get(k).get(i-cc.i)>ci) ci=LLists.get(k).get(i-cc.i); //i-separator size() ??
                }
            }else {
                System.out.println("i kisebb mint sp mérete");
            }
        }
        System.out.println("CI: "+ci);
        return ci;
    }

    public void makeCiTableForCompleteComponents(int t){
        for(int l=0;l<t;l++){
            for(int r=l;r<t;r++){
                ArrayList<Integer> cis=new ArrayList<>();
                Component c=findComponent(l,r,allComponents);
                System.out.println("(makeCiTableForCompleteComponents)c:"+c);
                if(c.mark.equals("complete")){
                    for(int i=0;i<=c.componentVertices.size();i++){
                        cis.add(completeComponentCi(c,i));
                    }
                    ciTable.get(l).get(r).addAll(cis);
                    System.out.println("CITABLE(Complete): "+l+","+r+": "+ciTable.get(l).get(r));
                }
            }
        }
    }

    public void plld(int t){
        ciTable=new ArrayList<>();
        for (int i = 0; i < t; i++) {
            ciTable.add(new ArrayList<>(t));
            for (int j = 0; j < t; j++) {
                ciTable.get(i).add(new ArrayList<>(t));
            }
        }
        //allComponents=makeAllComponents(zones);
        allComponentsComponents=makeComponentsOfComponenets();
        makeCiTableForCompleteComponents(t);
        for(int d=0;d<t;d++){
            for(int l=0;l<t-d;l++){
                Component plr=findComponent(l,l+d,allComponents);
                ArrayList<Integer> cis=new ArrayList<>();
                if(!isComplete(plr)){
                    System.out.println(plr);
                    //compute ci-s X->n-2-ig
                    //ArrayList<Integer> cis=new ArrayList<>();//new int[plr.componentVertices.size()+1];
                    for(int i=0;i<=plr.componentVertices.size();i++){
                        if(i==plr.componentVertices.size()-1) cis.add(1);
                        else cis.add(0);
                    }
                    for(int i=0;i<connectednessOfComponent(plr);i++){
                        cis.set(i,1);
                    }
                    for(int i=connectednessOfComponent(plr);i<=plr.componentVertices.size()-2;i++){
                        System.out.println("ComponentCi c: "+plr);
                        cis.set(i,ComponentCi(l,l+d,i));
                    }
                    ciTable.get(l).get(l+d).addAll(cis);
                    System.out.println("CITABLE: "+l+","+(l+d)+": "+ciTable.get(l).get(l+d));
                }

            }
        }
        printOutCiTable();
    }

    public void printOutCiTable(){
        System.out.println("CITABLE: ");
        for(int i=0;i<ciTable.size();i++){
            for(int j=i;j<ciTable.get(i).size();j++){
                System.out.print("("+i+","+j+"): "+ciTable.get(i).get(j)+"  ");
            }
            System.out.println();
        }
        System.out.println("DONE");
    }
}