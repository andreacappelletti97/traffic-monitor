import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppPointerColorTest {

    private AppPointerColor appPointerColorTest;
    private AppPointerColor appPointerColorTestDefault;

    @Before
    public void testSetup() {
        appPointerColorTestDefault = new AppPointerColor();
        appPointerColorTest = new AppPointerColor(0, 0, 0);
    }

    @Test
    public void getR() {
        assertTrue(appPointerColorTest.getR() == 0);
        assertTrue(appPointerColorTestDefault.getR() == 1);
    }

    @Test
    public void getG() {
        assertTrue(appPointerColorTest.getG() == 0);
        assertTrue(appPointerColorTestDefault.getG() == 1);
    }

    @Test
    public void getB() {
        assertTrue(appPointerColorTest.getB() == 0);
        assertTrue(appPointerColorTestDefault.getB() == 0);
    }
}