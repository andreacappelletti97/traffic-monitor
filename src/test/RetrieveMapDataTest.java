import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class RetrieveMapDataTest {

    @Test(expected = IOException.class)
    public void getMapFromImageNoImageFound() throws IOException {
        RetrieveMapData.getMapFromImage("wrongPath");
    }

    @Test
    public void getMapFromImage() throws IOException {
        RetrieveMapData.getMapFromImage(MapDataSetup.getMapDirectory());
    }
}