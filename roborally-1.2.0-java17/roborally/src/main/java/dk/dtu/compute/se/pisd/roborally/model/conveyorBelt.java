package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import java.awt.*;
import java.util.Vector;

public class conveyorBelt implements Observer {
    private Heading direction;
    private String colour ;
    public conveyorBelt(String colour,Heading heading ){

     this.colour = colour;
     direction = heading;
    }

    public String getColour() {
        return colour;
    }

    public Heading getDirection() {
        return direction;
    }

    @Override
    public void update(Subject subject) {

    }
}
