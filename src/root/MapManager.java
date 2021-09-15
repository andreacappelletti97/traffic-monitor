import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MapManager {

    private static MapManager mapManager;
    private int[][] map;

    private ArrayList<String> streetNames;
    private ArrayList<Integer> centralinaPositionList;

    private MapManager() {
        initializeMap();
        initializeStreetDataFromFile();
    }
    public static MapManager getInstance() {
        if (mapManager == null) {
            mapManager = new MapManager();
        }

        return mapManager;
    }

    private void initializeMap() {
        // Retrieve 'map' from image into 'resources' path
        try {
            this.map = RetrieveMapData.getMapFromImage(MapDataSetup.getMapDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeStreetDataFromFile() {
        // Initialize ArrayList
        streetNames = new ArrayList<>();
        centralinaPositionList = new ArrayList<>();

        // Get street data from resources
        try (BufferedReader br = new BufferedReader(new FileReader(findStreetDataFromResources()))) {
            String line;
            int currentStreetId = 0;
            while ((line = br.readLine()) != null) {
                // Read and process street data
                String[] singleStreetData = line.split(" / ");
                if (singleStreetData[0].equals("C")) centralinaPositionList.add(currentStreetId);
                streetNames.add(singleStreetData[1]);

                ++currentStreetId;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create list of streets (then stored in TrafficReportDatabase), StreetCovered if there is Centralina and StreetEmpty if not
    public StreetList retrieveStreetList() {
        StreetList streetListToReturn = new StreetList();

        for (int streetIndex = 0; streetIndex < streetNames.size(); ++streetIndex) {
            Street street;
            String nameOfTheStreet = streetNames.get(streetIndex);

            // If it is registered a Centralina in that street, then pass a StreetCovered
            if (centralinaPositionList.contains(streetIndex)) {
                street = new StreetCovered(streetIndex, nameOfTheStreet, 0);
            } else {
                street = new StreetEmpty(streetIndex, nameOfTheStreet);
            }

            streetListToReturn.addStreet(street);
        }

        return streetListToReturn;
    }
    public ArrayList<Integer> retrieveListOfStreetInRadius(int radius, Position basePosition) {
        ArrayList<Integer> listOfStreetReturn = new ArrayList<>();

        for (int mapYSize = calculateRadiusLowerBound(radius, basePosition.getyPosition());
             mapYSize < calculateRadiusUpperBound(map.length, radius, basePosition.getyPosition());
             ++mapYSize) {
            for (int mapXSize = calculateRadiusLowerBound(radius, basePosition.getxPosition());
                 mapXSize < calculateRadiusUpperBound(map[0].length, radius, basePosition.getxPosition());
                 ++mapXSize) {
                int mapStreetId = map[mapYSize][mapXSize];
                if (mapStreetId != -1 && !listOfStreetReturn.contains(mapStreetId)) {
                    listOfStreetReturn.add(mapStreetId);
                }
            }
        }

        return listOfStreetReturn;
    }
    public int retrieveStreetIdFromPosition(Position position) throws IllegalStateException {
        if (isPositionValidInTheMap(position)) {
            return getStreetIdFromPosition(position);
        } else {
            throw new IllegalStateException("Position not valid in current map");
        }
    }
    public boolean isPositionValidInTheMap(Position position) {
        return isPositionInsideBoundsOfMap(position) && isPositionInsideRealStreet(position);
    }
    public int[][] getMap() {
        return map;
    }

    /*
    Calculate the boundaries of a radius given a certain position
    Restrain to 0 or [maxSize] if it goes outside the boundaries
    */
    private int calculateRadiusUpperBound(int lengthSize, int radius, int basePosition) {
        if (basePosition + radius >= lengthSize) {
            return (lengthSize - 1);
        }

        return (basePosition + radius);
    }
    private int calculateRadiusLowerBound(int radius, int basePosition) {
        if (basePosition - radius < 0) {
            return 0;
        }

        return (basePosition - radius);
    }
    private boolean isPositionInsideBoundsOfMap(Position position) {
        return position.getyPosition() >= 0 && position.getyPosition() < getMapHeight() &&
                position.getxPosition() >= 0 && position.getxPosition() < getMapWidth();

    }
    private boolean isPositionInsideRealStreet(Position position) {
        return getStreetIdFromPosition(position) != -1;
    }
    private File findStreetDataFromResources() {
        URL resource = RetrieveMapData.class.getResource(MapDataSetup.getMapDirectory() + "streetData.txt");
        File streetDataFile = null;
        try {
            streetDataFile = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return streetDataFile;
    }
    public int getStreetIdFromPosition(Position position) throws IndexOutOfBoundsException, NullPointerException {
        if (position == null) {
            throw new NullPointerException("Null position sent to getStreetIdFromPosition");
        } if (!isPositionInsideBoundsOfMap(position)) {
            throw new IndexOutOfBoundsException("Position sent is outside the current map");
        }

        return getMap()[position.getyPosition()][position.getxPosition()];
    }
    public int getMapWidth() {
        return getMap()[0].length;
    }
    public int getMapWidth(int rowIndex) {
        return getMap()[rowIndex].length;
    }
    public int getMapHeight() {
        return getMap().length;
    }
}
