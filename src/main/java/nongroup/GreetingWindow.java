package nongroup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    public static final int TIME_OF_WINDOW_ANIMATION_IN_MILLS = 2000;
    public static final int TIME_OF_TEXT_ANIMATION_IN_MILLS = 2000;

    private boolean closeStageAtEndOfAnimation = false;

    /**
     * Включает свечение текста.
     * Внимание! Лучше не использовать вместе с анимацией появления/исчезновения текста - анимация будет подвисать!
     */
    public boolean enableShadowOnText = false;

    public final ArrayList<Consumer<Stage>> listenersOfAnimationEnd;

    private Stage stage;
    private Scene scene;
    private Label greeting;

    //private Timeline windowAppearance;
    //private FadeTransition labelAppearance;
    private LinkedList<Animation> animationsSequence;

    public GreetingWindow() {
        System.out.println("GreetingWindows default constructor called!");
        InputStream iconStream = getClass().getResourceAsStream(GreetingWindow.DEFAULT_ICON_PATH);
        if (iconStream != null) this.ICON = new Image(iconStream);
        listenersOfAnimationEnd = new ArrayList<Consumer<Stage>>();
        animationsSequence = new LinkedList<Animation>();
    }

    public Stage createGreetingWindow() {
        this.scene = this.prepareScene();
        this.stage = this.prepareStage(scene);
        return this.stage;
    }

    public Animation createAnimation(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills, boolean closeWindowAtEnd) {
        if (appearance == null & disappearance == null) return null;
        if (appearance == AnimaTarget.NO_ANIMATION & disappearance == AnimaTarget.NO_ANIMATION) return null;

        appearance = appearance == null ? AnimaTarget.NO_ANIMATION : appearance;
        disappearance = disappearance == null ? AnimaTarget.NO_ANIMATION : disappearance;

        this.closeStageAtEndOfAnimation = closeWindowAtEnd;

        switch (appearance) {
            case NO_ANIMATION: break;
            case ONLY_WINDOW: 
                hideStage();
                animationsSequence.add(createOppacityAnimationForStage(0, 1));
                break;
            case ONLY_TEXT:
                animationsSequence.add(createOppacityAnimationForNode(0, 1));
                break;
            case BOTH:
                hideStage();
                hideText();
                animationsSequence.add(createOppacityAnimationForStage(0, 1));
                animationsSequence.add(createOppacityAnimationForNode(0, 1));
                break;
            default: 
                throw new IllegalArgumentException("Unexpected appearance variable value.");
        }

        switch (disappearance) {
            case NO_ANIMATION: break;
            case ONLY_WINDOW: 
                animationsSequence.add(createOppacityAnimationForStage(1, 0));
                break;
            case ONLY_TEXT:
                animationsSequence.add(createOppacityAnimationForNode(1, 0));
                break;
            case BOTH:
                animationsSequence.add(createOppacityAnimationForNode(1, 0));
                animationsSequence.add(createOppacityAnimationForStage(1, 0));
                break;
            default: 
                throw new IllegalArgumentException("Unexpected appearance variable value.");
        }

        this.insertDelaysInSequence(this.animationsSequence, delaysInMills);
        Animation startAnimation = createLinkedSequence(this.animationsSequence);
        return startAnimation;
    }

    private void hideText() {
        greeting.setOpacity(0);
    }

    private void hideStage() {
        stage.setOpacity(0);
    }

    private void doEndAnimationThings() {
        for (Consumer<Stage> listener : listenersOfAnimationEnd) {
            listener.accept(this.stage);
        }
        if (this.closeStageAtEndOfAnimation & this.stage.isShowing()) {
            this.stage.close();  
        } 
    }

    private Timeline createOppacityAnimationForStage(int startValue, int endValue) {
        Timeline animation = new Timeline();
        animation.setCycleCount(1);
        animation.setAutoReverse(false);
        
        KeyValue kv0 = new KeyValue(this.stage.opacityProperty(), startValue);
        KeyValue kv = new KeyValue(this.stage.opacityProperty(), endValue);
        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf = new KeyFrame(Duration.millis(TIME_OF_WINDOW_ANIMATION_IN_MILLS), kv);
        animation.getKeyFrames().add(kf0);
        animation.getKeyFrames().add(kf);

        return animation;
    }

    private FadeTransition createOppacityAnimationForNode(int startValue, int endValue) {
        FadeTransition animation = new FadeTransition();
        animation.setCycleCount(1);
        animation.setAutoReverse(false);
        animation.setDuration(Duration.millis(TIME_OF_TEXT_ANIMATION_IN_MILLS));
        animation.setFromValue(startValue);
        animation.setToValue(endValue);
        animation.setNode(greeting);
        
        return animation;
    }

    private void insertDelaysInSequence(List<Animation> animationSequence, int[] delaysInMills) {
        if (delaysInMills == null) return;

        // Задержка до анимации + по 1 задержке после каждой анимации
        int limitOfDelaysAmount = 1 + animationSequence.size(); 
        limitOfDelaysAmount = limitOfDelaysAmount > delaysInMills.length ? delaysInMills.length : limitOfDelaysAmount;

        int insertIdx = 0;
        for (int idx = 0; idx < limitOfDelaysAmount; idx++) {
            if (delaysInMills[idx] <= 0) {
                insertIdx += 1;
                continue;
            }
            Animation delay = createDelay(delaysInMills[idx]);
            animationSequence.add(insertIdx, delay);
            insertIdx += 2;
        }
    }

    public List<Animation> getAnimationSequence() {
        return this.animationsSequence;
    }

    private Animation createLinkedSequence(List<Animation> animationsSequence) {
        for (int i = 1; i < animationsSequence.size(); i++) {
            Animation prev =  animationsSequence.get(i - 1);
            Animation next = animationsSequence.get(i);
            prev.setOnFinished((event) -> next.play());
        }
        Animation last = animationsSequence.get(animationsSequence.size() - 1);
        last.setOnFinished((event) -> this.doEndAnimationThings());
        return animationsSequence.get(0);
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
        ds.setColor(Color.web(GREETING_SCENE_LABEL_SHADOW_HEX, 0.5));
        ds.setOffsetX(0);
        ds.setOffsetY(0);
        ds.setRadius(20);
        ds.setSpread(0.3);

        label.setEffect(ds);
    }

    public Animation createDelay(int timeInMills) {
        Timeline delay = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(timeInMills), new KeyValue[] { });
        delay.getKeyFrames().add(frame);
        return delay;
    }

    /*

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
    */

    /*

    public FadeTransition createLabelAppearanceAnimation() {
        this.labelAppearance = new FadeTransition();

        int count = this.needToReverseLabelAnimation ? 2 : 1;
        this.labelAppearance.setCycleCount(count);
        this.labelAppearance.setAutoReverse(needToReverseLabelAnimation);
        this.labelAppearance.setDuration(Duration.millis(TIME_OF_TEXT_ANIMATION_IN_MILLS));

        this.labelAppearance.setFromValue(0);
        this.labelAppearance.setToValue(1);
        this.labelAppearance.setNode(greeting);
        
        return this.labelAppearance;
    }

    */







}