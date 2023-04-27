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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {

    public final Board board;

    private final ArrayList<Heading> wall;

    public final int x;
    public final int y;

    private Player player;
    private conveyorBelt Conveyor;
    private CheckPoint checkPoint;

    public Space(Board board, int x, int y) {
        this(board, x, y, new Heading[0]);
    }

    public Space(Board board, int x, int y,Heading...wall) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.wall = new ArrayList<>(Arrays.stream(wall).toList());
        player = null;
    }

    public Player getPlayer() {
        return player;
    }



    public void addWall(Heading playerDirection) {
        if(!PlacedWall(playerDirection)) {
            wall.add(playerDirection);
            notifyChange();
        }
    }

    public boolean PlacedWall(Heading playerDirection) {
     return wall.contains(playerDirection);
    }


    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    public boolean wallFace(Heading wallFace) {
        if (PlacedWall(wallFace) == true) {
            return false;
        }
        Space checkNeighbor = board.getNeighbour(this, wallFace);


        if (checkNeighbor.getPlayer() == null) {
            return true;
        }
    return checkNeighbor.wallFace(wallFace);
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

    public conveyorBelt getConveyor() {
        return Conveyor;
    }

    public void setConveyor(conveyorBelt conveyor) {
        Conveyor = conveyor;
    }

    public CheckPoint getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(CheckPoint checkPoint) {
        this.checkPoint = checkPoint;
    }
}
