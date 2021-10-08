package nongroup;

import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GreetingWindow {
    
    public static final String DEFAULT_ICON_PATH = "appIcon.png";
    public Image ICON;

    public static final String GREETING_SCENE_BACKGROUND_HEX = "#303030";
    public static final String GREETING_SCENE_LABEL_HEX = "#fafafa";
    public static final String GREETING_SCENE_LABEL_SHADOW_HEX = "#ffffff";

    public static final String GREETING_TEXT = "Greeting";

    public static final int TIME_OF_WINDOW_ANIMATION_IN_MILLS = 1000;
    public static final int TIME_OF_TEXT_ANIMATION_IN_MILLS = 2000;
    public static final int DELAY_AFTER_ANIMATION = 500;

    public boolean needToReverseLabelAnimation = false;
    public boolean needToReverseWindowAnimation = false;

    //public boolean closeStageAfterAnimationEnd = false;

    /**
     * Включает свечение текста.
     * Внимание! Лучше не использовать вместе с анимацией появления/исчезновения текста - анимация будет подвисать!
     */
    public boolean enableShadowOnText = false;

    public final ArrayList<Consumer<Stage>> listenersOfAnimationEnd;

    private Stage stage;
    private Scene scene;
    private Label greeting;

    private Timeline windowAppearance;
    private FadeTransition labelAppearance;

    public GreetingWindow() {
        System.out.println("GreetingWindows constructor () called!");
        this.ICON = new Image(getClass().getResourceAsStream(GreetingWindow.DEFAULT_ICON_PATH));
        listenersOfAnimationEnd = new ArrayList<Consumer<Stage>>();
    }

    public Stage createGreetingWindow() {
        this.scene = this.prepareScene();
        this.stage = this.prepareStage(scene);
        return this.stage;
    }

    private Stage prepareStage(Scene scene) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setMinWidth(200);
        stage.setMinHeight(100);
        stage.setWidth(800);
        stage.setHeight(500);
        stage.centerOnScreen();

        if (this.ICON != null) stage.getIcons().add(this.ICON);

        stage.setScene(scene);
        return stage;
    }

    private Scene prepareScene() {
        Scene scene = new Scene(this.configureGreetingText());
        scene.setFill(Color.valueOf(GREETING_SCENE_BACKGROUND_HEX));
        return scene;
    }

    private Label configureGreetingText() {
        this.greeting = new Label(GREETING_TEXT);
        greeting.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        greeting.setTextFill(Color.valueOf(GREETING_SCENE_LABEL_HEX));
        greeting.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        greeting.setAlignment(Pos.CENTER);
        if (enableShadowOnText) this.configureLabelShadow(greeting);

        return greeting;
    }

    private void configureLabelShadow(Label label) {
        DropShadow ds = new DropShadow();
        ds.setColor(Color.web(this.GREETING_SCENE_LABEL_SHADOW_HEX, 0.5));
        ds.setOffsetX(0);
        ds.setOffsetY(0);
        ds.setRadius(20);
        ds.setSpread(0.3);

        label.setEffect(ds);
    }

    public Animation createDelay(int timeInMills) {
        Timeline delay = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(timeInMills), null);
        delay.getKeyFrames().add(frame);
        return delay;
    }

    public Timeline createWindowAppearanceAnimation() {
        this.windowAppearance = new Timeline();

        // Количество кадров
        int countAmount = this.needToReverseWindowAnimation ? 2 : 1;
        windowAppearance.setCycleCount(countAmount);

        // Выполнение анимации в обратную сторону
        windowAppearance.setAutoReverse(this.needToReverseWindowAnimation);
        
        KeyValue kv0 = new KeyValue(stage.opacityProperty(), 0);
        KeyValue kv = new KeyValue(stage.opacityProperty(), 1);
        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf = new KeyFrame(Duration.millis(TIME_OF_WINDOW_ANIMATION_IN_MILLS), kv);
        windowAppearance.getKeyFrames().add(kf0);
        windowAppearance.getKeyFrames().add(kf);

        return this.windowAppearance;
    }

    public FadeTransition createLabelAppearanceAnimation() {
        this.labelAppearance = new FadeTransition();

        int count = this.needToReverseLabelAnimation ? 2 : 1;
        this.labelAppearance.setCycleCount(count);
        this.labelAppearance.setAutoReverse(needToReverseLabelAnimation);
        this.labelAppearance.setDuration(Duration.millis(TIME_OF_TEXT_ANIMATION_IN_MILLS));

        this.labelAppearance.setFromValue(0);
        this.labelAppearance.setToValue(10);
        this.labelAppearance.setNode(greeting);
        
        return this.labelAppearance;
    }

}