/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.view;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.CheckPoint;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 75;
    final public static int SPACE_WIDTH = 60; // 75;

   final public static Color wallColor = Color.BLUEVIOLET;
    final public static double wallThickness = 2;
    ImageView imageView = new ImageView();
    Image image = new Image("Green.png",60,60,false,false);

    public final Space space;


    public SpaceView(@NotNull Space space) {
        this.space = space;
        //ImageView imageView = new ImageView(image);


        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);




        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        }

        else {
            this.setStyle("-fx-background-color: black;");
        }




        /**
         * The following code detects based on position of PlacedWall and Heading.
         * It then creates a stroke(which fills out based on wallThinkness) for each wall and of course updates the space.
         */

        double up = space.PlacedWall(Heading.NORTH) ? wallThickness : 0;
        double right = space.PlacedWall(Heading.EAST) ? wallThickness : 0;
        double down = space.PlacedWall(Heading.SOUTH) ? wallThickness : 0;
        double left = space.PlacedWall(Heading.WEST) ? wallThickness : 0;

        BorderWidths borderWidths = new BorderWidths(up, right, down, left);
        BorderStroke borderStroke = new BorderStroke(wallColor, BorderStrokeStyle.SOLID, null, borderWidths);
        Border border = new Border(borderStroke);
        this.setBorder(border);

        space.attach(this);
        update(space);
    }



    private void updatePlayer() {
        this.getChildren().clear();
        if(space.getCheckPoint()!=null &&space.getCheckPoint().getOrderNo()==0){
            ImageView imageView = new ImageView();
            Image image = new Image("cp1.png",60,60,false,false);
            imageView.setImage(image);
            this.getChildren().add(imageView);
        }
        else if (space.getCheckPoint()!=null &&space.getCheckPoint().getOrderNo()==1){
            ImageView imageView = new ImageView();
            Image image = new Image("cp2.png",60,60,false,false);
            imageView.setImage(image);
            this.getChildren().add(imageView);
        }
        else if (space.getCheckPoint()!=null && space.getCheckPoint().getOrderNo()==2){
            ImageView imageView = new ImageView();
            Image image = new Image("cp3.png",60,60,false,false);
            imageView.setImage(image);
            this.getChildren().add(imageView);
        }
        if (space.getConveyor()!=null && space.getConveyor().getColour()=="blue") {
            ImageView imageView = new ImageView();
            Image image = null;
            if(space.getConveyor().getDirection() == Heading.WEST) {
                image = new Image("BlueWest.png", 60, 60, false, false);
            } else if(space.getConveyor().getDirection() == Heading.EAST ) {
                image = new Image("BlueEast.png", 60, 60, false, false);
            } else if ( space.getConveyor().getDirection() == Heading.SOUTH ) {
                image = new Image("BlueSouth.png", 60, 60, false, false);
            }else if (space.getConveyor().getDirection() == Heading.NORTH ) {
                image = new Image("Blue.png", 60, 60, false, false);
            }
            imageView.setImage(image);
            //if (space.getConveyor().getDirection() == Heading.EAST) {
            //   ImageView.setRotate((90*space.getConveyor().getDirection().ordinal())%360);

            this.getChildren().add(imageView);
            //}
        }

        else if (space.getConveyor()!=null && space.getConveyor().getColour()=="green") {
            ImageView imageView = new ImageView();
            Image image = null;
            if(space.getConveyor().getDirection() == Heading.WEST) {
                image = new Image("GreenWest.png", 60, 60, false, false);
            } else if(space.getConveyor().getDirection() == Heading.EAST ) {
                image = new Image("GreenEastt.png", 60, 60, false, false);
            } else if ( space.getConveyor().getDirection() == Heading.SOUTH ) {
                image = new Image("GreenSouth.png", 60, 60, false, false);
            }else if (space.getConveyor().getDirection() == Heading.NORTH ) {
                image = new Image("Green.png", 60, 60, false, false);
            }
            imageView.setImage(image);
            //if (space.getConveyor().getDirection() == Heading.EAST) {
            //   ImageView.setRotate((90*space.getConveyor().getDirection().ordinal())%360);

            this.getChildren().add(imageView);
            //}
        }






        Player player = space.getPlayer();




        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }

    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }

}
