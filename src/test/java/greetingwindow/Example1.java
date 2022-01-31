package greetingwindow;

import org.junit.Test;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Простое появление и исчезновение текста с заданной скоростью анимации.
 */
public class Example1 extends Application  {

    @Override
    public void start(Stage stage) {
        Example1.runGreetingWindow();
    }

    @Test
    public void startTest() {
        Thread javafxThread = new Thread(() -> Application.launch(Example1.class, new String[0]));
        javafxThread.setDaemon(true);
        javafxThread.start();
        try { javafxThread.join(); } catch (Exception e) {}
    }

    public static void runGreetingWindow() {
        // Можно передать имя заголовка окна в аргументы, а можно и без него. Заголовок будет отображаться в панели задач внизу.
        GreetingWindow gw = new GreetingWindow("Greeting");
        // Многие методы возвращают тот же объект GreetingWindow для удобства вызова методов в цепочке
        gw.setTimeOfWindowAppearanceInMills(2000)
            .setTimeOfTextAppearanceInMills(1000);
            
        // Метод вшивает анимации на момент вызова Stage.show().
        // Также указываем, что нужно создать появление окна и текста, а также исчезновение окна и текста.
        Stage gwStage = gw.createStageWithAnimationOnShowing(AnimaTarget.BOTH, AnimaTarget.BOTH);
        gwStage.showAndWait();
    }

}
