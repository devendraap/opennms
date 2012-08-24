package org.opennms.features.topology.netutils.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.Application;
import com.vaadin.ui.Window;

public class ResourceGraphsWindowTest {

    ResourceGraphsWindow window1;

    ResourceGraphsWindow window2;

    Window mainWindow;

    Application app;

    @Before
    public void setUp() throws Exception {
        Node testNode1 = new Node(9, "172.20.1.10", "Cartman");
        final URL url = new URL("http://localhost:8080/");
        window1 = new ResourceGraphsWindow(testNode1, url);
        window2 = new ResourceGraphsWindow(null, url);
        mainWindow = new Window();
        app = new Application() { // Empty Application
            @Override
            public void init() {
            }
        };
    }

    @Test
    public void testAttach() {
        app.setMainWindow(mainWindow);
        app.getMainWindow().addWindow(window1);
        assertTrue(app.getMainWindow().getChildWindows().contains(window1));
        app.getMainWindow().removeWindow(window1);
        assertFalse(app.getMainWindow().getChildWindows().contains(window1));
    }

}
