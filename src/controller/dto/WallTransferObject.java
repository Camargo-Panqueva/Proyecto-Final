package controller.dto;

import model.wall.WallType;

import java.awt.*;

public record WallTransferObject(PlayerTransferObject owner, WallType wallType, int creationTurn, Point position, WallType[][] wallShape){
}
