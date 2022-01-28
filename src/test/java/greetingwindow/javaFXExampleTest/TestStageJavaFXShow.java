package greetingwindow.javaFXExampleTest;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TestStageJavaFXShow extends Application implements IJavaFxTest
{
    private Stage stage;
    private Thread javafxThread;

    public static final int MAX_TIME_TO_WAIT_IN_MILLS = 7000;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setOnShowing(e -> doMainTestStaff());
        this.stage.setOnCloseRequest(e -> terminateJavaFXThread());
        System.out.println("Stage created");
        this.stage.show();
    }

    /** {@inheritDoc} */
    @Before
    @Override
    public void launchJavaFXThread() {
        System.out.println("Launch javafx in different thread");
        javafxThread = new Thread(() -> TestStageJavaFXShow.launch());
        javafxThread.start();
    }

    /** {@inheritDoc} */
    @Test
    @Override
    public void enterTestPoint()
    {
        System.out.println("Test started! Method will wait until window close.");   
        try {
            javafxThread.join(MAX_TIME_TO_WAIT_IN_MILLS);
        } catch (Exception e) { 
            e.printStackTrace();
            assertTrue(false); 
        }
        
        System.out.println("Test Finished!");
    }

    /** {@inheritDoc} */
    @Override
    public void doMainTestStaff() {
        System.out.println("Window has shown!");
        //// To some test stuff
        //....
    }

    public void terminateJavaFXThread() {
        System.out.println("Closing window! Notifying main thread!");
        Platform.exit();
    }
}
