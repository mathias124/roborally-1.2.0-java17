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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Abdi og Mathias
 * @version 1.0 prototype.
 *  @since 2023-04-16
 */
public class GameController {
    /**
     * Creates an empty gameboard which the game controller is associated with.
     */
    final public Board board;
    final private List<String> OPTIONS_Interactive = Arrays.asList("Left","Right");
    int turn=0;
    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**

     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        /**
         * Moves the current player to the specified space and sets the current player to the next player by +1 and currentplayer.
         *
         * @param space the space to which the current player should move too.
         */

        if (space != null && space.board == board) {
            Player currentPlayer = board.getCurrentPlayer();
            if (currentPlayer != null && space.getPlayer() == null) {
                currentPlayer.setSpace(space);
                int playerNumber = turn;
                board.setCurrentPlayer(board.getPlayer(playerNumber));
            }
        }

    }

    // XXX: V2
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(turn));
        board.setStep(turn);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    /**
     * This is the method for the button of FinishProgrammignPhase. It hides the fields, and  makes the first field visible for each player,
     * It will set the games phase to activationPhase after Clicked(input).It will also set the current player to the first player, and setting the current step to zero.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(turn);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(turn));
        board.setStep(turn);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX: V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();



        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if(command.isInteractive()){
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;

                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(turn));
                    } else {
                        turn=(turn+1) % board.getPlayersNumber();
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
       // turn=1;
       // board.setCurrentPlayer(board.getPlayer(turn));
    }

    /**
     * ExecuteCommand Checks the player and command and then uses a switch statement to determine the form's of Commandcards.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {


            switch (command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case UTURN:
                    this.uTurn(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                default:

                case OPTION_LEFT_RIGHT:
                   /* command.getOptions();
                    ChoiceDialog<String> dialog = new ChoiceDialog<>(OPTIONS_Interactive.get(0), OPTIONS_Interactive);
                    dialog.setTitle("Player choice");
                    Optional<String> result = dialog.showAndWait();
                    if(result == OPTIONS_Interactive.get(0).describeConstable()){ //Issue is here. (Take the correct string to turn).
                        this.turnLeft(player);
                    }
                    else if(result.equals("Optional[Right]")) {
                        this.turnRight(player);
                        System.out.println(result);

                    */
                  //  }
                    break;



                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * Moves the player 1 vector direction forward, depending on the heading.
     */
    public void moveForward(@NotNull Player player) {
        Space space = player.getSpace();
        Space playerSpace = player.getSpace();


        //moveForward(playerSpace.getPlayer(), player.getHeading(),check);

        //}
        //if(spaceTaken(playerSpace)) {
        // return;
        //}
        //player.setSpace(playerSpace);
        Heading heading = player.getHeading();
        Space target = board.getNeighbour(space, heading);

        if (player != null && player.board == board && space != null && playerSpace.wallFace(player.getHeading())) {

            if (target != null) {
                // XXX note that this removes an other player from the space, when there
                //     is another player on the target. Eventually, this needs to be
                //     implemented in a way so that other players are pushed away!

                robotCollide(target);

                target.setPlayer(player);
                player.setSpace(target);

                conveyerTransport(player);
                CheckPointTokener(player);
            }
            /**
             * Detects the player's heading and check if meets a wall on the same space as the player with a macthing heading.
             * if it is the same it cannot move.(Continue if not).
             */
        } else if (player != null && player.board == board && space != null) {
            int check = 1;
            //int check =1;
            for (int i = 0; i < check; i++) {
                if (!playerSpace.PlacedWall(player.getHeading()) || this.Checkheading(player) == Heading.NORTH ) {
                    continue;
                }
                playerSpace = player.board.getNeighbour(playerSpace, player.getHeading());
                if (playerSpace.getPlayer() != null && !playerSpace.PlacedWall(player.getHeading())) {
                    moveForward(player);
                }
            }


        }
    }

    /**
     * Moves the player 2 vector direction forward, depending on the heading. by calling the forward method
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * Moves the player's heading direction 90 degress, or right and checks for the current phasing direction.
     */
    public void turnRight(@NotNull Player player) {
       /* if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next());
        }

        */

        if(board.getCurrentPlayer().getHeading()==Heading.NORTH)
            player.setHeading(Heading.EAST);

        else if(board.getCurrentPlayer().getHeading()==Heading.EAST)
            player.setHeading(Heading.SOUTH);
        else if(board.getCurrentPlayer().getHeading()==Heading.SOUTH)
            player.setHeading(Heading.WEST);
        else if(board.getCurrentPlayer().getHeading()==Heading.WEST)
            player.setHeading(Heading.NORTH);
    }



    /**
     * Moves the player's heading direction 90 degress, or left and checks for the current phasing direction.
     */
    public void turnLeft(@NotNull Player player) {
       /* if (player != null && player.board == board) {

            player.setHeading(player.getHeading().prev());
        }

        */

        if(board.getCurrentPlayer().getHeading()==Heading.NORTH)
            player.setHeading(Heading.WEST);
        else if(board.getCurrentPlayer().getHeading()==Heading.EAST)
            player.setHeading(Heading.NORTH);
        else if(board.getCurrentPlayer().getHeading()==Heading.SOUTH)
            player.setHeading(Heading.EAST);
        else if(board.getCurrentPlayer().getHeading()==Heading.WEST)
            player.setHeading(Heading.SOUTH);
    }


    public void uTurn(@NotNull Player player) {
        turnLeft(player);
        turnLeft(player);
    }
    /**
     * Moves a command card from a source field to a target field.
     * @param source the source field from which to move the command card
     * @param target the target field to which to move the command card
     * @return true if the move was successful, false otherwise
     * @throws NullPointerException if either source or target is null
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }
    public Heading Checkheading(Player player) {
        Heading playerDirection = player.getHeading();
        Heading newDirect;
        if(playerDirection == Heading.WEST) {
            newDirect = Heading.EAST;
        } else if (playerDirection == Heading.EAST) {
            newDirect = Heading.WEST;
        } else if (playerDirection == Heading.NORTH) {
            newDirect = Heading.SOUTH;
        }
        else newDirect = Heading.NORTH;
    return newDirect;

    }

    /**
     Executes a command option for a player and continues the game with the OptionInteractive card. It sets phase to Activation Phase.
     @param player the player for whom to execute the command option

     @param comman2 the command  the option to execute with userinput.
     */
    public void executeCommandOptionAndContinue(Player player,Command comman2){
        board.setPhase(Phase.ACTIVATION);
        this.executeCommand(player,comman2);



            int step = board.getStep();
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(turn));
                    } else {
                        startProgrammingPhase();
                    }
                    continuePrograms();
            }

            public void conveyerTransport(Player player){
                Space space3 = player.getSpace();
                conveyorBelt conveyor = space3.getConveyor();
                if(conveyor!=null){
                    String colour  = conveyor.getColour();
                    Heading heading2= conveyor.getDirection();
                    player.setHeading(heading2);
                    if(colour.equals("blue")){
                        this.fastForward(player);
                    }
                    else {
                        this.moveForward(player);
                    }


                }
            }
            public void robotCollide(Space target){
                if (target.getPlayer() != null) {
                    Player old = target.getPlayer();
                    moveForward(old);
                }


            }
            public void CheckPointTokener(Player player){
            Space space=player.getSpace();
              CheckPoint checkPoint = space.getCheckPoint();
              if(checkPoint!=null && player.getTokens()==checkPoint.orderNo ){
                player.setTokens(player.getTokens()+1);
               }



             }





}