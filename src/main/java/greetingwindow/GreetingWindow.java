package greetingwindow;

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

/**
 * Класс предоставляет функционал для создания и некоторой настройки простенького окна приветствия пользователя с анимацией.
 * 
 * <p>Всего доступны 4 анимации, следующие в строгом порядке: появление окна, появление текста, исчезновение текста,
 * исчезновение окна. Какие-либо могут быть пропущены - в таком случае элемент будет видим изначально.
 * Эффект анимации только 1 - изменение прозрачности элемента.
 * 
 * <p>Все анимации создаются в последовательном виде - нет идущих одновременно.
 * В полном виде цепочка анимаций имеет вид: {@code - Stage_int - Text_in - Text_out - Stage_out - }. 
 * На место всех тире могут быть добавлены задержки (в коде - {@code delaysInMills}).
 * 
 * <p>Окно может быть автоматически закрыто с помощью флага {@link GreetingWindow#_needToCloseStageAtEndOfAnimation}, а
 * также класс имеет список наблюдателей, которых может уведовить о завершении анимации.
 */
public class GreetingWindow implements IAnimationWatcher {
    
    /**
     * Константа с путем к иконке по умолчанию (должна лежать в том же пакете).
     */
    public static final String DEFAULT_ICON_PATH = "appIcon.png";

    /**
     * Переменная хранит картинку для иконки.
     */
    private Image icon;

    /**
     * Цвет фона окна.
     */
    private Color _sceneBackgroundColor = Color.valueOf("#303030");

    /**
     * Цвет текста.
     */
    private Color _sceneTextColor = Color.valueOf("#fafafa");

    /**
     * Цвет тени текста.
     */
    private Color _sceneTextShadowColor = Color.web("#ffffff", 0.5);

    /**
     * Текст окна приветствия.
     */
    private String _greetingText = "Greeting";

    /**
     * Время появления окна в миллисекундах.
     */
    private int _timeOfWindowAppearanceInMills = 1000;

    /**
     * Время исчезновения окна в миллисекундах.
     */
    private int _timeOfWindowDisappearanceInMills = 1000;

    /**
     * Время появления текста в миллисекундах.
     */
    private int _timeOfTextAppearanceInMills = 2000;

    /**
     * Время исчезновения текста в миллисекундах.
     */
    private int _timeOfTextDisappearanceInMills = 1000;

    /**
     * Нужно ли закрывать окно после завершения всех анимаций. Если анимаций нет - окно закрыто не будет.
     */
    public boolean _needToCloseStageAtEndOfAnimation = true;

    /**
     * Включает свечение текста.
     * Внимание! Лучше не использовать вместе с анимацией появления/исчезновения текста - анимация будет подвисать!
     */
    public boolean enableShadowOnText = false;

    /**
     * Спискок подписанных наблюдателей, которые будут вызваны, когда анимация закончится. 
     * <p>Можно добавить наблюдателя после создания готового окна приветствия, потому что глубоко внутри к окончанию
     * последней анимации привязывается ссылка на объект {@link GreetingWindow}, а он уже оповещает всех наблюдателей.
     * <p>
     */
    public final ArrayList<IAnimationWatcher> observersList;

    /**
     * Окно приветствия.
     */
    private Stage stage;

    /**
     * Элемент с текстом приветствия.
     */
    private Label greeting;

    private String titleName = "Greeting Window";

    /**
     * Конструктор по умолчанию. Загружает иконку по умолчанию.
     */
    public GreetingWindow() {
        System.setProperty("prism.lcdtext", "false");
        InputStream iconStream = getClass().getResourceAsStream(GreetingWindow.DEFAULT_ICON_PATH);
        if (iconStream != null) this.icon = new Image(iconStream);

        observersList = new ArrayList<IAnimationWatcher>();
    }

    public GreetingWindow(String titleName) {
        this.titleName = titleName;
        System.setProperty("prism.lcdtext", "false");
        InputStream iconStream = getClass().getResourceAsStream(GreetingWindow.DEFAULT_ICON_PATH);
        if (iconStream != null) this.icon = new Image(iconStream);

        observersList = new ArrayList<IAnimationWatcher>();
    }

    /** {@inheritDoc} */
    @Override 
    public void invokeAfterAnimation() {
        for (IAnimationWatcher observer : observersList) {
            if (observer == null) continue;
            observer.invokeAfterAnimation();
        }
        if (this._needToCloseStageAtEndOfAnimation & stage.isShowing()) stage.close();
    }

    /**
     * Метод создания окна с вшитимы на событие появления анимациями.
     * @see GreetingWindow#createStageWithAnimationOnShowing(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills)
     * @param appearance - для каких элементов необходимо создать анимации появления.
     * @param disappearance - для каких элементов необходимо создать анимации исчезновения.
     * @return готовое окно, которое осталось только вывести пользователю (через {@link Stage#show()})
    */
    public Stage createStageWithAnimationOnShowing(AnimaTarget appearance, AnimaTarget disappearance) {
        return this.createStageWithAnimationOnShowing(appearance, disappearance, null);
    }

    /**
     * Метод создания окна с вшитимы на событие появления анимациями.
     * 
     * @param appearance - для каких элементов необходимо создать анимации появления.
     * @param disappearance - для каких элементов необходимо создать анимации исчезновения.
     * @param delaysInMills - задержки анимаций. То, к чему будем относиться каждый элемент массива 
     * зависит от значений {@code appearance} и {@code disappearance}.
     * 
     * Полная цепочка анимаций имеет вид: {@code - Stage - Text - Text - Stage - }. На место всех тире могут быть добавлены
     * задержки (в коде - {@code delaysInMills}).
     * 
     * {@code delaysInMills[0]} - задержка перед началом цепочки анимаций. 
     * Каждый следующий элемент будет вставлен после каждой последующей анимации.
     * Чтобы сделать задержку после завершения основных анимаций необходимо заполнить массив до этого места. 
     * 
     * Пример: appearance=BOTH, disappearance=ONLY_TEXT, delays={100, 200, 300, 400}.
     * Задержки: 100мс до анимации, 200мс после появления окна, 300мс - после появления текста, 400мс - после исчезновения текста.
     * Таким образом, задержка в 400мс - последняя анимация.
     * @return готовое окно, которое осталось только вывести пользователю (через {@link Stage#show()}).
     */
    public Stage createStageWithAnimationOnShowing(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills) {
        this.stage = this.createGreetingWindow();
        Animation firstAnimation = this.createAnimation(appearance, disappearance, delaysInMills);
        if (firstAnimation != null)
            this.stage.setOnShowing((event) -> firstAnimation.play());
        return stage;
    }

    /**
     * Метод создания окна с вшитимы на событие появления анимациями и автоматическим закрытием окна после анимаций.
     * @see GreetingWindow#createStageWithAnimationOnShowing(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills)
     * @param appearance - для каких элементов необходимо создать анимации появления.
     * @param disappearance - для каких элементов необходимо создать анимации исчезновения.
     * @param delaysInMills - задержки анимаций.
     * @param needToCloseStage - нужно ли автоматически закрыть окно после завершения всех анимаций.
     * @return готовое окно, которое осталось только вывести пользователю (через {@link Stage#show()}).
     */
    public Stage createStageWithAnimationOnShowing(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills, boolean needToCloseStage) {
        this._needToCloseStageAtEndOfAnimation = needToCloseStage;
        return createStageWithAnimationOnShowing(appearance, disappearance, delaysInMills);
    }

    /**
     * Метод создаёт окно без анимаций.
     * @return окно приветствия.
     */
    public Stage createGreetingWindow() {
        Scene scene = this.prepareScene();
        this.stage = this.prepareStage(scene);
        return this.stage;
    }

    /**
     * Метод создаёт цепочку из анимаций для окна и возвращает первую анимацию, так как она запустит все другие.
     * @param appearance - для каких элементов необходимо создать анимации появления.
     * @param disappearance - для каких элементов необходимо создать анимации исчезновения.
     * @return первая анимация, которая запустит другие.
     */
    public Animation createAnimation(AnimaTarget appearance, AnimaTarget disappearance) {
        return this.createAnimation(appearance, disappearance, null);
    }

    /**
     * Метод создаёт цепочку из анимаций для окна и возвращает первую анимацию, так как она запустит все другие.
     * @param appearance - для каких элементов необходимо создать анимации появления.
     * @param disappearance - для каких элементов необходимо создать анимации исчезновения.
     * @param delaysInMills - задержки анимаций.
     * @return первая анимация, которая запустит другие.
     */
    public Animation createAnimation(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills) {
        if (stage == null) createGreetingWindow();
        GreetingWindowAnimation animaAssistant = new GreetingWindowAnimation(this, this.stage, this.greeting, this);
        List<Animation> animaSeq = animaAssistant.createAnimationSequence(appearance, disappearance, delaysInMills);
        Animation firstAnimation = animaAssistant.connectAnimationsEachAfterPrev(animaSeq);
        return firstAnimation;
    }

    /**
     * Метод создаёт цепочку из анимаций для окна и возвращает первую анимацию, так как она запустит все другие.
     * @param appearance - для каких элементов необходимо создать анимации появления.
     * @param disappearance - для каких элементов необходимо создать анимации исчезновения.
     * @param delaysInMills - задержки анимаций.
     * @param needToCloseStage - нужно ли закрыть окно после завершения всех анимаций.
     * @return первая анимация, которая запустит другие.
     */
    public Animation createAnimation(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills, boolean needToCloseStage) {
        this._needToCloseStageAtEndOfAnimation = needToCloseStage;
        return createAnimation(appearance, disappearance, delaysInMills);
    }

    /**
     * Метод настроивает {@code Stage}.
     * @param scene - сцена, которая будет отображаться в окне.
     * @return настроенный объект {@code Stage}
     */
    private Stage prepareStage(Scene scene) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setMinWidth(200);
        stage.setMinHeight(100);
        stage.setWidth(800);
        stage.setHeight(500);
        stage.centerOnScreen();

        if (this.icon != null) stage.getIcons().add(this.icon);
        stage.setTitle(this.titleName);

        stage.setScene(scene);
        return stage;
    }

    /**
     * Метод создаёт сцену и настраивает её.
     * @return настроенный объект {@code Scene}
     */
    private Scene prepareScene() {
        Scene scene = new Scene(this.configureGreetingText());
        scene.setFill(this._sceneBackgroundColor);
        return scene;
    }

    /**
     * Метод создаёт и настраивает выводимый текст, по необходимости добавляет тень.
     * @return настроенный объект {@code Label}
     */
    private Label configureGreetingText() {
        this.greeting = new Label(this._greetingText);
        greeting.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        greeting.setTextFill(this._sceneTextColor);
        greeting.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        greeting.setAlignment(Pos.CENTER);
        if (this.enableShadowOnText) this.configureLabelShadow(greeting);

        return greeting;
    }

    /**
     * Метод создаёт и настраивает тень для текста.
     * @param label - объект UI, содержащий текст.
     */
    private void configureLabelShadow(Label label) {
        DropShadow ds = new DropShadow();
        ds.setColor(this._sceneTextShadowColor);
        ds.setOffsetX(0);
        ds.setOffsetY(0);
        ds.setRadius(20);
        ds.setSpread(0.3);

        label.setEffect(ds);
    }

    /**
     * Метод создаёт задержку в виде анимации. Суть задержки в том, что это просто пустакя задержка на определённое время.
     * @param timeInMills - время задержки в миллисекундах.
     * @return анимация задержки.
     */
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