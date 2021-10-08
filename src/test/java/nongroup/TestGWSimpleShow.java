package nongroup;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TestGWSimpleShow extends Application implements IJavaFxTest {
    
    private Stage appStage;
    private Stage greetingWindow;
    private Thread javafxThread;

    public static final int MAX_TIME_TO_WAIT_IN_MILLS = 15000;
    public static final int MAX_TIME_FOR_CLOSING_JAVAFX_WINDOW = 3000;

    @Override
    public void start(Stage stage) {
        this.appStage = stage;
        doMainTestStaff();
    }

    /** {@inheritDoc} */
    @Before
    @Override
    public void launchJavaFXThread() {
        System.out.println("Launch javafx in different thread");
        javafxThread = new Thread(() -> TestGWSimpleShow.launch());
        javafxThread.start();
    }

    /** {@inheritDoc} */
    @Test
    @Override
    public void enterTestPoint()
    {
        System.out.println("Test started! Method will wait until all windows close.");  
        System.out.println(String.format("Set %d millisecond timeout.", TestGWSimpleShow.MAX_TIME_TO_WAIT_IN_MILLS));
        try {
            javafxThread.join(MAX_TIME_TO_WAIT_IN_MILLS);
            terminateJavaFXThread();
        } catch (Exception e) { 
            e.printStackTrace();
            assertTrue(false); 
        }
        
        System.out.println("Test Finished!");
    }

    /** {@inheritDoc} */
    @Override
    public void doMainTestStaff() {
        System.out.println("Javafx configurated itself. Start creating greeting window");
        
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        stage.show();
    }

    public void terminateJavaFXThread() {
        System.out.println("Termination javafx thread!");
        Platform.exit();
    }
}
