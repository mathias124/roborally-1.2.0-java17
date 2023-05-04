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
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class BoardView extends VBox implements ViewObserver {

    private Board board;

    private GridPane mainBoardPane;
    private SpaceView[][] spaces;

    private PlayersView playersView;

    private Label statusLabel;

    private SpaceEventHandler spaceEventHandler;

    public BoardView(@NotNull GameController gameController) {
        board = gameController.board;

         ;

        mainBoardPane = new GridPane();
        playersView = new PlayersView(gameController);
        statusLabel = new Label("<no status>");

        this.getChildren().add(mainBoardPane);
        this.getChildren().add(playersView);
        this.getChildren().add(statusLabel);

        spaces = new SpaceView[board.width][board.height];

        spaceEventHandler = new SpaceEventHandler(gameController);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);

                SpaceView spaceView = new SpaceView(space);
                /**
                 * This takes an image using imageview loaded from our resource folder and then detects what @OrderNo is equally set as. This is made in BoardClass.
                 * @board is used as the infrastructure to devide the points, because it calls the checkpoint object.(here we detect the checkpoint) and use the matching picture for OrderNo.
                 */
                spaces[x][y] = spaceView;
                /**
                 * This takes an image using imageview loaded from our resource folder and then detects what getDirection is equally set as from the object of the conveyer. This is made in BoardClass.
                 *  * Creates color for the spaces of each conveyer and creates picture.
                 */

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

                    spaceView.getChildren().add(imageView);
                    //}
                }

                if (space.getConveyor()!=null && space.getConveyor().getColour()=="green") {
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

                        spaceView.getChildren().add(imageView);
                    //}
                }


                if (space.getCheckPoint()!=null && space.getCheckPoint().getOrderNo() ==0){

                    ImageView imageView = new ImageView();
                    Image image = new Image("cp1.png",60,60,false,false);

                    imageView.setImage(image);
                    spaceView.getChildren().add(imageView);
                }
                if (space.getCheckPoint()!=null && space.getCheckPoint().getOrderNo() ==1){
                    ImageView imageView = new ImageView();
                    Image image = new Image("cp2.png",60,60,false,false);
                    imageView.setImage(image);
                    spaceView.getChildren().add(imageView);
                }
                if (space.getCheckPoint()!=null && space.getCheckPoint().getOrderNo() ==2){

                    ImageView imageView = new ImageView();
                    Image image = new Image("cp3.png",60,60,false,false);
                    imageView.setImage(image);
                    spaceView.getChildren().add(imageView);
                }


                mainBoardPane.add(spaceView, x, y);
                spaceView.setOnMouseClicked(spaceEventHandler);
            }
        }


        board.attach(this);
        update(board);

    }

    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            conveyorBelt conveyor;
            Phase phase = board.getPhase();
            statusLabel.setText(board.getStatusMessage());


        }
    }

    // XXX this handler and its uses should eventually be deleted! This is just to help test the
    //     behaviour of the game by being able to explicitly move the players on the board!
    private class SpaceEventHandler implements EventHandler<MouseEvent> {

        final public GameController gameController;

        public SpaceEventHandler(@NotNull GameController gameController) {
            this.gameController = gameController;
        }

        @Override
        public void handle(MouseEvent event) {
            Object source = event.getSource();
            if (source instanceof SpaceView) {
                SpaceView spaceView = (SpaceView) source;
                Space space = spaceView.space;
                Board board = space.board;

                if (board == gameController.board) {
                    gameController.moveCurrentPlayerToSpace(space);
                    event.consume();
                }
            }
        }

    }

}
