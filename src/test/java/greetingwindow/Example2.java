package greetingwindow;

import org.junit.Test;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Появление окна, задержка после последней анимации, отключение автоматического закрытия окна и вызов прослушивателей.
 */
public class Example2 extends Application {
    @Override
    public void start(Stage stage) {
        Example2.runGreetingWindow();
    }

    @Test
    public void startTest() {
        Thread javafxThread = new Thread(() -> Application.launch(Example2.class, new String[0]));
        javafxThread.setDaemon(true);
        javafxThread.start();
        try { javafxThread.join(); } catch (Exception e) {}
    }

    public static void runGreetingWindow() {
        GreetingWindow gw = new GreetingWindow("Greeting");
        // По умолчанию автозакрытие всегда включено включено. Также это можно указать в методе, возвращающим Stage.
        gw._needToCloseStageAtEndOfAnimation = false;

        // Полный список задержек выглядит следующим образом: (1) + Stage_in + (2) + Text_in + (3) + Text_out + (4) + Stage_out + (5)
        // Задержки представлены числами в скобках.
        // При 2х начальных анимациях из 4х, получается, что нужно сделать задержку после появления текста (то есть в (3)).
        Stage gwStage = gw.createStageWithAnimationOnShowing(AnimaTarget.BOTH, AnimaTarget.NO_ANIMATION, new int[] {0, 0, 3000});

        // Укажем, какой метод, который необходимо вызвать после завершения анимаций.
        // В данном случае в коллекцию прослушивателей нужно добавить реализацию интерфейса IAnimationWatcher в виде лямда-выражения.
        gw.observersList.add(() -> Example2.callableFunction(gwStage));

        // Второй способ добавления прослушивателя - анонимный класс.
        gw.observersList.add(new IAnimationWatcher() {
            public void invokeAfterAnimation() {
                // DoSomeStuff or call methods
            }
        });

        // Так как мы отключили автозакрытие Stage, то закрыть его придется самостоятельно в одном из методов-наблюдателей.
        gwStage.showAndWait();
    }

    public static void callableFunction(Stage stage) {
        System.out.println("Animation end!");
        stage.close();
    };
}
