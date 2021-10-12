package nongroup;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
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

public class GreetingWindow implements IAnimationWatcher {
    
    public static final String DEFAULT_ICON_PATH = "appIcon.png";
    private Image icon;

    private Color _sceneBackgroundColor = Color.valueOf("#303030");
    private Color _sceneTextColor = Color.valueOf("#fafafa");
    private Color _sceneTextShadowColor = Color.web("#ffffff", 0.5);

    private String _greetingText = "Greeting";

    private int _timeOfWindowAppearanceInMills = 1000;
    private int _timeOfWindowDisappearanceInMills = 1000;
    private int _timeOfTextAppearanceInMills = 2000;
    private int _timeOfTextDisappearanceInMills = 1000;

    public boolean _needToCloseStageAtEndOfAnimation = true;

    /**
     * Включает свечение текста.
     * Внимание! Лучше не использовать вместе с анимацией появления/исчезновения текста - анимация будет подвисать!
     */
    public boolean enableShadowOnText = false;

    /**
     * Спискок подписанных наблюдателей, которые будут вызваны, когда анимация закончится.
     */
    public final ArrayList<IAnimationWatcher> observersList;

    private Stage stage;
    private Label greeting;

    public GreetingWindow() {
        System.out.println("GreetingWindows default constructor called!");

        InputStream iconStream = getClass().getResourceAsStream(GreetingWindow.DEFAULT_ICON_PATH);
        if (iconStream != null) this.icon = new Image(iconStream);

        observersList = new ArrayList<IAnimationWatcher>();
    }

    @Override 
    public void invokeAfterAnimation() {
        for (IAnimationWatcher observer : observersList) {
            if (observer == null) continue;
            observer.invokeAfterAnimation();
        }
        if (this._needToCloseStageAtEndOfAnimation & stage.isShowing()) stage.close();
    }

    public Stage createStageWithAnimationOnShowing(AnimaTarget appearance, AnimaTarget disappearance) {
        return this.createStageWithAnimationOnShowing(appearance, disappearance, null);
    }

    public Stage createStageWithAnimationOnShowing(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills) {
        this.stage = this.createGreetingWindow();
        Animation firstAnimation = this.createAnimation(appearance, disappearance, delaysInMills);
        this.stage.setOnShowing((event) -> firstAnimation.play());
        return stage;
    }

    public Stage createStageWithAnimationOnShowing(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills, boolean needToCloseStage) {
        this._needToCloseStageAtEndOfAnimation = needToCloseStage;
        return createStageWithAnimationOnShowing(appearance, disappearance, delaysInMills);
    }

    public Stage createGreetingWindow() {
        Scene scene = this.prepareScene();
        this.stage = this.prepareStage(scene);
        return this.stage;
    }

    public Animation createAnimation(AnimaTarget appearance, AnimaTarget disappearance) {
        return this.createAnimation(appearance, disappearance, null);
    }

    public Animation createAnimation(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills) {
        if (stage == null) createGreetingWindow();
        GreetingWindowAnimation animaAssistant = new GreetingWindowAnimation(this, this.stage, this.greeting, this);
        List<Animation> animaSeq = animaAssistant.createAnimationSequence(appearance, disappearance, delaysInMills);
        Animation firstAnimation = animaAssistant.connectAnimationsEachAfterPrev(animaSeq);
        return firstAnimation;

    }

    public Animation createAnimation(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills, boolean needToCloseStage) {
        this._needToCloseStageAtEndOfAnimation = needToCloseStage;
        return createAnimation(appearance, disappearance, delaysInMills);
    }

    private Stage prepareStage(Scene scene) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setMinWidth(200);
        stage.setMinHeight(100);
        stage.setWidth(800);
        stage.setHeight(500);
        stage.centerOnScreen();

        if (this.icon != null) stage.getIcons().add(this.icon);

        stage.setScene(scene);
        return stage;
    }

    private Scene prepareScene() {
        Scene scene = new Scene(this.configureGreetingText());
        scene.setFill(this._sceneBackgroundColor);
        return scene;
    }

    private Label configureGreetingText() {
        this.greeting = new Label(this._greetingText);
        greeting.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        greeting.setTextFill(this._sceneTextColor);
        greeting.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        greeting.setAlignment(Pos.CENTER);
        if (this.enableShadowOnText) this.configureLabelShadow(greeting);

        return greeting;
    }

    private void configureLabelShadow(Label label) {
        DropShadow ds = new DropShadow();
        ds.setColor(this._sceneTextShadowColor);
        ds.setOffsetX(0);
        ds.setOffsetY(0);
        ds.setRadius(20);
        ds.setSpread(0.3);

        label.setEffect(ds);
    }

    public Animation createDelay(int timeInMills) {
        return GreetingWindowAnimation.createDelay(timeInMills);
    }

    public GreetingWindow setIcon(Image icon) {
        if (icon == null) throw new IllegalArgumentException("Icon image == null");
        this.icon = icon;
        return this;
    }

    public Image getIcon() { 
        return this.icon; 
    }

    public GreetingWindow setSceneBackground(String backColor) {
        if (backColor == null) throw new IllegalArgumentException("backColor == null");
        this._sceneBackgroundColor = Color.web(backColor);
        return this;
    }

    public GreetingWindow setSceneBackground(Color backColor) {
        if (backColor == null) throw new IllegalArgumentException("backColor == null");
        this._sceneBackgroundColor = backColor;
        return this;
    }

    public Color getSceneBackground() { 
        return this._sceneBackgroundColor; 
    }

    public GreetingWindow setTextColor(String textColor) {
        if (textColor == null) throw new IllegalArgumentException("textColor == null");
        this._sceneTextColor = Color.web(textColor);
        return this;
    }

    public GreetingWindow setTextColor(Color textColor) {
        if (textColor == null) throw new IllegalArgumentException("textColor == null");
        this._sceneTextColor = textColor;
        return this;
    }

    public Color getTextColor() { 
        return this._sceneTextColor; 
    }

    public GreetingWindow setTextShadowColor(String shadowColor, double opacity) {
        if (opacity < 0) throw new IllegalArgumentException("Opacity value < 0");
        if (shadowColor == null) throw new IllegalArgumentException("shadowColor == null");
        this._sceneTextShadowColor = Color.web(shadowColor, opacity);
        return this;
    }

    public Color getTextShadowColor() { 
        return this._sceneTextShadowColor; 
    }

    public GreetingWindow setText(String text) {
        if (text == null) throw new IllegalArgumentException("text == null");
        this._greetingText = text;
        return this;
    }

    public String getText() {
        return this._greetingText;
    }

    public GreetingWindow setTimeOfWindowAppearanceInMills(int time) {
        if (time <= 0) throw new IllegalArgumentException("time <= 0");
        this._timeOfWindowAppearanceInMills = time;
        return this;
    }

    public int getTimeOfWindowAppearanceInMills() {
        return this._timeOfWindowAppearanceInMills;
    }

    public GreetingWindow setTimeOfWindowDisappearanceInMills(int time) {
        if (time <= 0) throw new IllegalArgumentException("time <= 0");
        this._timeOfWindowDisappearanceInMills = time;
        return this;
    }

    public int getTimeOfWindowDisappearanceInMills() {
        return this._timeOfWindowDisappearanceInMills;
    }

    public GreetingWindow setTimeOfTextAppearanceInMills(int time) {
        if (time <= 0) throw new IllegalArgumentException("time <= 0");
        this._timeOfTextAppearanceInMills = time;
        return this;
    }

    public int getTimeOfTextAppearanceInMills() {
        return this._timeOfTextAppearanceInMills;
    }

    public GreetingWindow setTimeOfTextDisappearanceInMills(int time) {
        if (time <= 0) throw new IllegalArgumentException("time <= 0");
        this._timeOfTextDisappearanceInMills = time;
        return this;
    }

    public int getTimeOfTextDisappearanceInMills() {
        return this._timeOfTextDisappearanceInMills;
    }
}