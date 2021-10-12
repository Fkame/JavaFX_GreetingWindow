package nongroup;

import static org.junit.Assert.assertTrue;

import java.util.function.Consumer;

import org.junit.Test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * TestGWbyShowingUser_NewMethods
 */
public class TestGWbyShowingUser_NewMethods extends Application {
    private Stage appStage;
    private Stage greetingWindow;
    private Thread javafxThread;

    public static final int MAX_TIME_TO_WAIT_IN_MILLS = 15000;

    private static Consumer<Object> testMethodForCurrentTest;

    @Override
    public void start(Stage stage) {
        this.appStage = stage;
        testMethodForCurrentTest.accept(null);
        
    }

    public void launchJavaFXThread() {
        System.out.println("Launch javafx in different thread.");
        javafxThread = new Thread(() -> TestGWbyShowingUser_OldVersion.launch());
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
            assertTrue(false); 
        }
    }
    
    @Test 
    public void testDefaultScenario1() {
        System.out.println("testDefaultScenario1 started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doDefaultScenario1();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doDefaultScenario1() {
        System.out.println("doDefaultScenario1 calls!");
        GreetingWindow gw = new GreetingWindow();
        gw.enableShadowOnText = false;
        gw.setText("Опять ты...").setTimeOfWindowAppearanceInMills(2000)
            .setTimeOfWindowDisappearanceInMills(1000).observersList.add(() -> System.out.println("MEssage about animation end!"));
        Stage stage = gw.createStageWithAnimationOnShowing(AnimaTarget.ONLY_WINDOW, AnimaTarget.ONLY_WINDOW, new int[] {0, 1000}, true);   
        stage.show();
    }

    @Test 
    public void testDefaultScenario2() {
        System.out.println("testDefaultScenario2 started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doDefaultScenario2();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doDefaultScenario2() {
        System.out.println("doDefaultScenario2 calls!");
        GreetingWindow gw = new GreetingWindow();
        Color newText = gw.getSceneBackground();
        Color newBackground = gw.getTextColor();
        gw.setText("Hello there")
            .setTimeOfWindowAppearanceInMills(1000)
            .setTimeOfWindowDisappearanceInMills(1500)
            .setSceneBackground(newBackground)
            .setTextColor(newText)
            .observersList.add(() -> System.out.println("MEssage about animation end!"));
        gw.enableShadowOnText = false;
        Stage stage = gw.createStageWithAnimationOnShowing(AnimaTarget.BOTH, AnimaTarget.BOTH, new int[] {0, 1000}, true);   
        stage.show();
    }


    
}