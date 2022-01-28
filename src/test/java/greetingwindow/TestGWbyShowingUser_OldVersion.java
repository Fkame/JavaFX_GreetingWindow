package greetingwindow;

import static org.junit.Assert.assertTrue;

import java.util.function.Consumer;

import org.junit.Test;

import greetingwindow.AnimaTarget;
import greetingwindow.GreetingWindow;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TestGWbyShowingUser_OldVersion extends Application {
    
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
    public void testSimpleStageShow() {
        System.out.println("testSimpleStageShow started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doSimpleStageShow();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(false);
        System.out.println("Test Finished!");
    }

    public void doSimpleStageShow() {
        System.out.println("doSimpleStageShow calls!");
        
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        stage.show();
    }

    @Test 
    public void testStageAppearance() {
        System.out.println("testStageAppearance started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doStageAppearance();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doStageAppearance() {
        System.out.println("doStageAppearance calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.ONLY_WINDOW, null, null, false);
        stage.show();
        animation.play();
    }

    @Test 
    public void testTextAppearance() {
        System.out.println("testTextAppearance started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doTextAppearance();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doTextAppearance() {
        System.out.println("doTextAppearance calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.ONLY_TEXT, null, null, false);
        stage.show();
        animation.play();
    }

    @Test 
    public void testStageAndTextAppearance() {
        System.out.println("testStageAndTextAppearance started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doStageAndTestAppearance();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doStageAndTestAppearance() {
        System.out.println("doStageAndTestAppearance calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.BOTH, null, null, false);
        stage.show();
        animation.play();
    }

    @Test 
    public void testStageAndTextDisappearance() {
        System.out.println("testStageAndTextDisappearance started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doStageAndTextDisappearance();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doStageAndTextDisappearance() {
        System.out.println("doStageAndTextDisappearance calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.BOTH, AnimaTarget.BOTH, null, false);
        stage.show();
        animation.play();
    }

    @Test 
    public void testTextDisappearance() {
        System.out.println("testTextDisappearance started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doTextDisappearance();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doTextDisappearance() {
        System.out.println("testTextDisappearance calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.BOTH, AnimaTarget.ONLY_TEXT, null, false);
        stage.show();
        animation.play();
    }

    @Test 
    public void testStageAndTextAppearanceWithDelays() {
        System.out.println("testStageAndTextAppearanceWithDelays started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doStageAndTextAppearanceWithDelays();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doStageAndTextAppearanceWithDelays() {
        System.out.println("doStageAndTextAppearanceWithDelays calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.BOTH, AnimaTarget.BOTH, new int[] {0, 2000, 3000}, false);
        stage.show();
        animation.play();
    }

    @Test 
    public void testDelayBeforeShow() {
        System.out.println("testDelayBeforeShow started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doDelayBeforeShow();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(false);
        System.out.println("Test Finished!");
    }

    public void doDelayBeforeShow() {
        System.out.println("doDelayBeforeShow calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.BOTH, AnimaTarget.BOTH, new int[] {2000}, false);
        stage.show();
        animation.play();
    }

    @Test 
    public void testWindowClose() {
        System.out.println("testWindowClose started! Method will wait until all windows close.");
        testMethodForCurrentTest = (obj) -> doWindowClose();
        launchJavaFXThread();

        System.out.println(String.format("Set %d millisecond timeout.", TestGWbyShowingUser_OldVersion.MAX_TIME_TO_WAIT_IN_MILLS));
        waitFXThreadToEnd(true);
        System.out.println("Test Finished!");
    }

    public void doWindowClose() {
        System.out.println("doWindowClose calls!");
        GreetingWindow gw = new GreetingWindow();
        Stage stage = gw.createGreetingWindow();
        Animation animation = gw.createAnimation(AnimaTarget.BOTH, AnimaTarget.ONLY_TEXT, new int[] {0, 0, 1000}, true);
        stage.show();
        animation.play();
    }
}
