package Algorithm;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
public class ComponentsOfComponent {
    public int i; //ahány csúcsot kivettünk
    public int p; //Separator indexe
    //int numberOfComponents ?
    public String mark;
    public Component oldComponent;
    public Component newComponent; //ez P(l,r)\Sp
    public ArrayList<Component> componentsOfComponent;

    /*public ComponentsOfComponent(int i, int p, String mark, Component component, ArrayList<Component> componentsOfComponent) {
        this.i=i;
        this.p=p;
        this.mark=mark;
        this.component=component;
        this.componentsOfComponent=componentsOfComponent;
    }*/

    @Override
    public String toString() {
        StringBuilder x= new StringBuilder();
        for(Component c:componentsOfComponent){
            x.append(c.l).append(" ").append(c.r).append("\n");
        }
        return x.toString();
    }
}
