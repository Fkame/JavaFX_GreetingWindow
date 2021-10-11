package nongroup;

import java.util.function.Consumer;

import org.junit.Test;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TestGWResponce extends Application {

    private Stage appStage;
    private Stage greetingWindow;
    private Thread javafxThread;

    public static final int MAX_TIME_TO_WAIT_IN_MILLS = 15000;
    public static final int MAX_TIME_FOR_CLOSING_JAVAFX_WINDOW = 3000;

    private static Consumer<Object> testMethodForCurrentTest;
    
    @Override
    public void start(Stage stage) {
        this.appStage = stage;
        testMethodForCurrentTest.accept(null);
        
    }

    public void launchJavaFXThread() {
        System.out.println("Launch javafx in different thread.");
        javafxThread = new Thread(() -> TestGWResponce.launch());
        javafxThread.start();
    }

    public void terminateJavaFXThread() {
        System.out.println("Termination javafx thread!");
        Platform.exit();
    }

    public void waitFXThreadToEnd(boolean debugMode) {
        try {
            if (debugMode) javafxThread.join();
            else javafxThread.join(MAX_TIME_TO_WAIT_IN_MILLS);
            terminateJavaFXThread();
        } catch (Exception e) { 
            e.printStackTrace();
        }
    }

    @Test 
    public void testSignalAboutAnimationEnd() {
        System.out.println("testSignalAboutAnimationEnd started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doSignalAboutAnimationEnd();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWResponce.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doSignalAboutAnimationEnd() {
        System.out.println("testSignalAboutAnimationEnd calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.BOTH, AnimaTarget.ONLY_TEXT, null, false);
        gw.observersList.add(() -> { 
            System.out.println("Signal catched!!"); 
            stage.close(); 
        });
        stage.show();
        animation.play();
    }

}