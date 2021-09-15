import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MobileAppInterfaceTest {

    MobileAppInterface mobileAppInterface;
    JPanel rootPanel;
    DefaultListModel notificationListModel;
    final int MAX_DIMENSION = 5;

    @Before
    public void setUp(){
        notificationListModel = new DefaultListModel();
        rootPanel = new JPanel();
    }

    @Test
    public void testShowNotification() {

        assertTrue("notificationList is Empty", notificationListModel.size()==0);
        StreetApp streetApp = new StreetApp(1, "Via Anzani", 10, true);
        for(int i=0; i<MAX_DIMENSION+1; i++){
        notificationListModel.addElement(streetApp);
        //System.out.println("Index: "+i+" "+" Content: "+notificationListModel.get(i));
        }

        if(notificationListModel.size()==MAX_DIMENSION){
            int lastElement = notificationListModel.size()-1;
            notificationListModel.remove(lastElement);
            assertTrue("Empity notificationList", notificationListModel.get(lastElement)==null);
        }

        assertEquals(10, streetApp.getTrafficAmount());
        JOptionPane.showMessageDialog(rootPanel = new JPanel(),
                "C'è traffico in " + streetApp.getStreetName(),
                "TRAFFIC NOTIFICATION",
                JOptionPane.WARNING_MESSAGE);
    }
}