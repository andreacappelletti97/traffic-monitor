import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RetrieveMapData {

    /*
    The coordinates system start from the top-left corner
    - The x axis goes from left to right
    - The y axis goes from top to bottom

        (0, 0) (1, 0) (2, 0) (3, 0)
        (0, 1) (1, 1) (2, 1) (3, 1)
        (0, 2) (1, 2) (2, 2) (3, 2)
        (0, 3) (1, 3) (2, 3) (3, 3)
        (0, 4) (1, 4) (2, 4) (3, 4)

     */


    public static int[][] getMapFromImage(String mapDirectory) throws IOException {
        return generateMatrixMapFromImage(
                convertImageToBufferedImage(
                        findMapFromResources(mapDirectory)
                )
        );
    }

    private static int[][] generateMatrixMapFromImage(BufferedImage imageSource) {
        // Save indexes of street you already found in the map
        ArrayList<Integer> streetAlreadyFound = new ArrayList<>();

        // Save the specific position which contains a street to send to MapDataSetup
        ArrayList<Position> positionWithStreet = new ArrayList<>();

        // Initialize the mapMatrix to return
        int[][] mapMatrix = new int[imageSource.getHeight()][imageSource.getWidth()];

        System.out.println("Here you are the map");

        for(int y = 0; y < mapMatrix.length; y++) {
            for (int x = 0; x < mapMatrix[y].length; x++) {
                Integer streetColorIndex = imageSource.getRGB(x, y);

                System.out.print("(" + streetColorIndex + ")");

                // -1 is an empty position (white color)
                if (streetColorIndex != -1) {
                    // New street found, add its index to the array
                    if (!streetAlreadyFound.contains(streetColorIndex)) streetAlreadyFound.add(streetColorIndex);

                    streetColorIndex = streetAlreadyFound.indexOf(streetColorIndex);
                    positionWithStreet.add(new Position(x, y));
                }


                mapMatrix[y][x] = streetColorIndex;

                System.out.print(streetColorIndex + " ");
            }

            System.out.println();
        }

        for (int a = 0; a < positionWithStreet.size(); ++a) {
            positionWithStreet.get(a).printPosition();
        }

        // Set Array 'positionWithStreet' in MapDatSetup
        MapDataSetup.setPositionWithStreet(positionWithStreet);
        return mapMatrix;
    }
    private static String findMapFromResources(String mapFileName) throws IOException {
        URL resource = RetrieveMapData.class.getResource(mapFileName + "map.png");
        if (resource == null) {
            throw new IOException("Map image not found in resources directory. Make sure the folder name is correct, the file exist and its name is map.png");
        }

        File mapImageFile = null;
        try {
            mapImageFile = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return String.valueOf(mapImageFile);
    }
    private static BufferedImage convertImageToBufferedImage(String mapImageDir) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(mapImageDir));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bufferedImage;
    }
}
