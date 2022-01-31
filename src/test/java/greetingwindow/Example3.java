package greetingwindow;

import org.junit.Test;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Создание окна без анимаций (видимых), но с работающим автозакрытием и оповещением наблюдателей.
 */
public class Example3 extends Application {
    @Override
    public void start(Stage stage) {
        Example3.runGreetingWindow();
    }

    @Test
    public void startTest() {
        Thread javafxThread = new Thread(() -> Application.launch(Example3.class, new String[0]));
        javafxThread.setDaemon(true);
        javafxThread.start();
        try { javafxThread.join(); } catch (Exception e) {}
    }

    public static void runGreetingWindow() {
        GreetingWindow gw = new GreetingWindow();

        // Не стоит включать этот параметр при наличии каких-либо видимых анимаций, иначе они начинают лагать.
        gw.enableShadowOnText = true;

        // Если анимаций к форме не прикреплено, то никакие наблюдатели не вызовутся, а автозакрытие не произойдёт.
        // Поэтому всё-таки придется сделать псевдо-анимаци, но очень быструю.
        gw.setTimeOfWindowAppearanceInMills(1);
        // После этого необходимо создать окно с одной лишь быстрой анимацией.
        // Чтобы произошла задержка после окончания анимации можно либо выключить автозакрытие, либо добавить задержку.
        Stage gwStage = gw.createStageWithAnimationOnShowing(
            AnimaTarget.ONLY_WINDOW, AnimaTarget.NO_ANIMATION, new int[] {0, 5000}, true);

        gw.observersList.add(() -> {System.out.println("End of animation");});

        gwStage.showAndWait();
    }
}
