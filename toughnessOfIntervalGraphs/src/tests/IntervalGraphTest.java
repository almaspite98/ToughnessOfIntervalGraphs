package tests;

import Algorithm.Component;
import Algorithm.ComponentsOfComponent;
import data.SortInput;
import intervalGraph.IntervalGraph;
import intervalGraph.Vertex;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

class IntervalGraphTest {
    public IntervalGraph ig;
    public int n=15;

    @BeforeEach
    void setUp() {
        ig=new IntervalGraph();
        ig.inputGenerator(n); //auto generated input
        //ig.readInGraph();
        ig.makeLanes();

        ig.allComponents=ig.makeAllComponents(ig.zones);
    }

    @AfterEach
    void tearDown() {
        ig=null;
    }

    @Test
    void makeNumberLineTest() {
    }

    @Test
    void makeZonesTest() {
    }

    @Test
    void makeLanesTest() {
    }

    @Test
    void findVertexALaneTest() {
    }

    @Test
    void makeAllComponentsTest() {
    }

    @Test
    void isConnectedTest() {
        for(Component c:ig.allComponents){
            if(c.componentZones.separators.size()>0){
                System.out.println("Before c: "+c);
                c.componentVertices.removeAll(c.componentZones.separators.get(0).zoneVertices);
                c.componentZones=ig.makeZones(c.componentVertices);
                System.out.println("After c: "+c);
                assertFalse(ig.isConnected(c));
            }
        }
    }

    @Test
    void isCompleteTest() {
        for(Component c:ig.allComponents){
            assertEquals(c.mark.equals("complete"),ig.isComplete(c));
        }
    }

    @Test
    void testIsCompleteTest() {
        for(Component c:ig.allComponents){
            System.out.println("Component: "+c+" size: "+c.componentVertices.size());
            System.out.println("ComponentConnectedness: "+ig.connectednessOfComponent(c));
            System.out.println("Expected: "+ig.isComplete(c));
            System.out.println("Actual: "+ig.isComplete(c.componentVertices));
            //assertEquals(ig.isComplete(c),ig.isComplete(c.componentVertices));
        }
    }

    @Test
    void makeComponentTest() {
    }

    @Test
    void makeComponentsOfComponenetsTest() {
    }

    /*@Test
    void makeAllDisjunctComponentsTest() {
        for(Component c:ig.allComponents){
            System.out.println("Component: "+c);
            ig.makeAllDisjunctComponents(c);
        }
    }*/

    @Test
    void completeComponentCiTest() {
    }

    @Test
    void connectednessOfComponentTest() {
        for(Component c:ig.allComponents){
            switch (c.mark) {
                case "complete":
                    assertEquals(c.componentVertices.size(), ig.connectednessOfComponent(c));
                    break;
                case "empty":
                case "disconnected":
                    assertEquals(0, ig.connectednessOfComponent(c));
                    break;
            }

        }
    }

    @Test
    void findComponentsOfComponentTest() {
        ig.allComponentsComponents=ig.makeComponentsOfComponenets();
        for(ComponentsOfComponent cc:ig.allComponentsComponents){
            assertEquals(cc,ig.findComponentsOfComponent(cc.oldComponent,cc.p));
        }
    }

    @Test
    void findComponentTest() {
        for(int i=0;i<ig.allComponents.size();i++){
            Component c=ig.allComponents.get(i);
            assertEquals(c,ig.findComponent(c.l,c.r,ig.allComponents));
        }
    }

    @Test
    void divideITest() {
        int i=new Random().nextInt(20);
        int[][] pqs=ig.divideI(i);
        for(int p=0;p<pqs.length;p++){
            for(int q=0;q<pqs[p].length;q++){
                if(p+q==i) assertEquals(1,pqs[p][q]);
                else assertEquals(0,pqs[p][q]);
            }
        }
    }

    /*@Test
    void numberOfComponentsTest() {
        for(Component c:ig.allComponents){
            ArrayList<Component> components=ig.makeAllDisjunctComponents(c);
            assertEquals(components.size(),ig.numberOfComponents(c));
        }
    }*/

    @Test
    void lemmaTest() {
    }

    @Test
    void componentCiTest() {
    }

    @Test
    void makeCiTableForCompleteComponentsTest() {
    }

    @Test
    void maxNumberOfComponentsInLanesTest(){
        int db=0;
        for(int i=0;i<20;i++){
            n=new Random().nextInt(30);
            setUp();
            ig.printOutLanes();
            System.out.println("MAX POSSIBLE CI: "+ig.maxNumberOfComponentsInLanes());
            assertEquals(ig.lanes.get(0).laneVertices.size(),ig.maxNumberOfComponentsInLanes());
            if(ig.lanes.size()>=2){
                if(ig.lanes.get(1).laneVertices.size()>ig.lanes.get(0).laneVertices.size()) db++;
            }
        }
        System.out.println("DB: "+db);
    }

    @Test
    void plldTest() {
        ig.plld(ig.zones.cliques.size());
        Integer max=ig.maxNumberOfComponentsInLanes();
        for(Integer i=ig.vertices.size()-max;i<=ig.vertices.size();i++){
            System.out.println("index: "+i+" ertek: "+max);
            assertEquals(max--,ig.ciTable.get(0).get(ig.zones.cliques.size()-1).get(i));
        }
    }
}