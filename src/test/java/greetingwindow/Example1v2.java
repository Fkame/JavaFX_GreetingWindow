package greetingwindow;

import org.junit.Test;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Простое появление и исчезновение текста с заданной скоростью анимации и ручным связыванием Stage и анимации.
 */
public class Example1v2 extends Application {

    @Override
    public void start(Stage stage) {
        Example1v2.runGreetingWindow();
    }

    @Test
    public void startTest() {
        Thread javafxThread = new Thread(() -> Application.launch(Example1v2.class, new String[0]));
        javafxThread.setDaemon(true);
        javafxThread.start();
        try { javafxThread.join(); } catch (Exception e) {}
    }

    public static void runGreetingWindow() {
        GreetingWindow gw = new GreetingWindow("Greeting");
        gw.setTimeOfTextAppearanceInMills(2500);
        // Отображаемое имя на Stage.
        gw.setText("Hello there!");

        // Порядок важен! Сначала нужно получить окно - потом анимацию (вот так плохо спроектировано мною окно).
        Stage gwStage = gw.createGreetingWindow();
        // Здесь указываем, что появляться должен только текст, а исчездать текст и окно.
        Animation anima = gw.createAnimation(AnimaTarget.ONLY_TEXT, AnimaTarget.BOTH);

        // Так как последовательное получение Stage и Animation не сшивает анимацию со Stage, делать это придется вручную.
        gwStage.setOnShown((event) -> anima.play());
        gwStage.showAndWait();
    }

}
