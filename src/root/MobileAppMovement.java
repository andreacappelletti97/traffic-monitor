import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class MobileAppMovement {

    private static boolean isFirstMove = true;
    private static Position lastRegisteredPosition = null;

    // Called when new random position requested
    public static void reset() {
        isFirstMove = true;
        lastRegisteredPosition = null;
    }

    // Return the new currentPosition after [speed] single moves
    public static Position movement(Position currentPosition, int speed) {
        // First move, return a random valid position
        if (isFirstMove) {
            isFirstMove = false;
            return generateRandomPositionWithinStreet();
        }

        // Perform [n = speed] single movement
        for (int step = 0; step < speed; ++step) {
            // Save backup of currentPosition to set 'lastRegisteredPosition' after the movement
            Position currentBackup = new Position(currentPosition.getxPosition(), currentPosition.getyPosition());

            currentPosition = singleMove(currentPosition);
            lastRegisteredPosition = currentBackup;
        }

        return currentPosition;
    }

    private static Position singleMove(Position currentPosition) {
        ArrayList<Position> possibleMoves = getListOfPossibleMoves(currentPosition);

        // If no move is possible, come back to last position
        if (possibleMoves.isEmpty()) {
            return lastRegisteredPosition;
        }

        return possibleMoves.get(ThreadLocalRandom.current().nextInt(0, possibleMoves.size()));
    }

    public static Position generateRandomPositionWithinStreet() {
        return MapDataSetup.getPositionWithStreet().get(
                ThreadLocalRandom.current().nextInt(0, MapDataSetup.getPositionWithStreet().size())
        );
    }

    private static ArrayList<Position> getListOfPossibleMoves(Position sourcePosition) {
        ArrayList<Position> possibleMoves = new ArrayList<>();
        if (isMovementValid(moveNorth(sourcePosition))) possibleMoves.add(moveNorth(sourcePosition));
        if (isMovementValid(moveSouth(sourcePosition))) possibleMoves.add(moveSouth(sourcePosition));
        if (isMovementValid(moveEast(sourcePosition))) possibleMoves.add(moveEast(sourcePosition));
        if (isMovementValid(moveWest(sourcePosition))) possibleMoves.add(moveWest(sourcePosition));
        /*if (isMovementValid(moveNorthEast(sourcePosition))) possibleMoves.add(moveNorthEast(sourcePosition));
        if (isMovementValid(moveNorthWest(sourcePosition))) possibleMoves.add(moveNorthWest(sourcePosition));
        if (isMovementValid(moveSouthEast(sourcePosition))) possibleMoves.add(moveSouthEast(sourcePosition));
        if (isMovementValid(moveSouthWest(sourcePosition))) possibleMoves.add(moveSouthWest(sourcePosition));*/

        return possibleMoves;
    }

    private static boolean isMovementValid(Position position) {
        return MapDataSetup.getPositionWithStreet().contains(position) && !position.equals(lastRegisteredPosition);
    }

    private static Position moveNorth(Position fromPosition) {
        return new Position(fromPosition.getxPosition(), fromPosition.getyPosition() - 1);
    }
    private static Position moveSouth(Position fromPosition) {
        return new Position(fromPosition.getxPosition(), fromPosition.getyPosition() + 1);
    }
    private static Position moveEast(Position fromPosition) {
        return new Position(fromPosition.getxPosition() + 1, fromPosition.getyPosition());
    }
    private static Position moveWest(Position fromPosition) {
        return new Position(fromPosition.getxPosition() - 1, fromPosition.getyPosition());
    }
    private static Position moveNorthEast(Position fromPosition) {
        return moveNorth(moveEast(fromPosition));
    }
    private static Position moveNorthWest(Position fromPosition) {
        return moveNorth(moveWest(fromPosition));
    }
    private static Position moveSouthEast(Position fromPosition) {
        return moveSouth(moveEast(fromPosition));
    }
    private static Position moveSouthWest(Position fromPosition) {
        return moveSouth(moveWest(fromPosition));
    }
}
