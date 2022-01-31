package greetingwindow;

import java.util.LinkedList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Вспомогательный класс ля создания анимации для окна приветствия {@link GreetingWindow}.
 * Умеет создавать анимации, связывать их, а также способен подписать наблюдателя на событие заверешения анимации.
 * 
 * Анимации, помимо деления по элементам, также разделены на {@code appearance} и {@code disappearance} - то есть 
 * появление и исчезнование. Подобные аргументы можно увидеть у метода {@link GreetingWindowAnimation#createAnimationSequence}.
 * 
 * Все анимации создаются в последовательном виде - нет идущих одновременно. 
 * В полном виде цепочка анимаций имеет вид: {@code - Stage - Text - Test - Stage - }. На место всех тире могут быть добавлены
 * задержки (в коде - {@code delaysInMills}).
 */
class GreetingWindowAnimation {

    /**
     * Окно, для которого может быть создана анимация появления/исчезновения
     */
    private Stage stage;

     /**
     * Узел (элемент) с текстом, для которого может быть создана анимация появления/исчезновения.
     */
    private Node text;

    /**
     * Основной класс, из которого можно достать настройки анимации
     */
    private GreetingWindow settingsContainer;

    /**
     * Наблюдатель, который будет информирован о наступлении события.
     */
    public IAnimationWatcher observer;

    /**
     * Конструктор
     * @param settingsContainer - ссылка на вызывающий класс, из которого можно получить настройки анимации.
     * @param stage - окно, для которого нужно создать анимацию.
     * @param text - узел с текстом, для которого нужно создать анимацию.
     * @param observer - наблюдатель. В данном случае это класс {@link GreetingWindow}.
     * @throws NullPointerException если любой входящий аргумент, кроме observer == null
     */
    public GreetingWindowAnimation(GreetingWindow settingsContainer, Stage stage, Node text, IAnimationWatcher observer) {
        if (settingsContainer == null | stage == null | text == null) 
            throw new NullPointerException("Null argument (except observer) in constructor!");
        this.settingsContainer = settingsContainer;
        this.stage = stage;
        this.text = text;
        this.observer = observer;
    }

    /**
     * Конструктор
     * @param settingsContainer - ссылка на вызывающий класс, из которого можно получить настройки анимации.
     * @param stage - окно, для которого нужно создать анимацию.
     * @param text - узел с текстом, для которого нужно создать анимацию.
     * @throws NullPointerException если один из входящих аргументов == null
     */
    public GreetingWindowAnimation(GreetingWindow settingsContainer, Stage stage, Node text) {
        this(settingsContainer, stage, text, null);
    }

    /**
     * Метод для создания цепочки анимаций.
     * @param appearance - для каких элементов необходимо создать анимации появления.
     * @param disappearance - для каких элементов необходимо создать анимации исчезновения.
     * @param delaysInMills - задержки анимаций. То, к чему будем относиться каждый элемент массива 
     * зависит от значений {@code appearance} и {@code disappearance}.
     * 
     * <p>Все анимации создаются в последовательном виде - нет идущих одновременно.
     * В полном виде цепочка анимаций имеет вид: {@code - Stage_int - Text_in - Text_out - Stage_out - }. 
     * На место всех тире могут быть добавлены задержки (в коде - {@code delaysInMills}).
     * 
     * <p>{@code delaysInMills[0]} - задержка перед началом цепочки анимаций. 
     * Каждый следующий элемент будет вставлен после каждой последующей анимации.
     * Чтобы сделать задержку после завершения основных анимаций необходимо заполнить массив до этого места. 
     * 
     * <p>Пример: appearance=BOTH, disappearance=ONLY_TEXT, delays={100, 200, 300, 400}.
     * Задержки: 100мс до анимации, 200мс после появления окна, 300мс - после появления текста, 400мс - после исчезновения текста.
     * Таким образом, задержка в 400мс - последняя анимация.
     * 
     * @return последовательность анимаций в виде списка.
     * @throws IllegalArgumentException если передаётся неучтённое значение {@link AnimaTarget}.
     */
    public List<Animation> createAnimationSequence(AnimaTarget appearance, AnimaTarget disappearance, int[] delaysInMills) {
        if (appearance == null & disappearance == null) return null;
        if (appearance == AnimaTarget.NO_ANIMATION & disappearance == AnimaTarget.NO_ANIMATION) return null;

        appearance = appearance == null ? AnimaTarget.NO_ANIMATION : appearance;
        disappearance = disappearance == null ? AnimaTarget.NO_ANIMATION : disappearance;

        LinkedList<Animation> animationsSequence = new LinkedList<Animation>();

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

        this.insertDelaysInSequence(animationsSequence, delaysInMills);
        return animationsSequence;
    }

    private void hideText() {
        text.setOpacity(0);
    }

    private void hideStage() {
        stage.setOpacity(0);
    }

    /**
     * Метод, который будет вызван, когда анимация закончится.
     * Передаёт сигнал наблюдателю (вызывает метод по контракту).
     */
    private void doEndAnimationThings() {
        if (observer != null) observer.invokeAfterAnimation(); 
    }

    /**
     * Склеивает анимации из списка между собой, на выходе получается последовательность включающихся друг за другом анимаций.
     * @param animationsSequence - последовательность анимаций в виде списка.
     * @return самая первая анимация, к которой приклеены друг за другом остальные анимации.
     */
    public Animation connectAnimationsEachAfterPrev(List<Animation> animationsSequence) {
        if (animationsSequence == null) return null;
        if (animationsSequence.size() == 0) return null;
        
        for (int i = 1; i < animationsSequence.size(); i++) {
            Animation prev =  animationsSequence.get(i - 1);
            Animation next = animationsSequence.get(i);
            prev.setOnFinished((event) -> next.play());
        }
        Animation last = animationsSequence.get(animationsSequence.size() - 1);
        last.setOnFinished((event) -> this.doEndAnimationThings());
        return animationsSequence.get(0);
    }

    /**
     * Метод создаёт задержку в виде анимации. Суть задержки в том, что это просто пустакя задержка на определённое время.
     * @param timeInMills - время задержки в миллисекундах.
     * @return анимация задержки.
     */
    public static Animation createDelay(int timeInMills) {
        Timeline delay = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(timeInMills), new KeyValue[] { });
        delay.getKeyFrames().add(frame);
        return delay;
    }

    /**
     * Метод создаёт анимацию изменения прозрачности от {@code startValue} до {@code endValue} для элемента 
     * {@link GreetingWindowAnimation#stage},
     * который хранится в виде переменной класса.
     * @param startValue - начальное значение прозрачности.
     * @param endValue - конечное значение прозрачности.
     * @return анимация изменения прозрачности {@code Stage}.
     * @throws IllegalArgumentException если один из аргументов меньше 0.
     */
    private Timeline createOppacityAnimationForStage(int startValue, int endValue) {
        if (startValue < 0 | endValue < 0) throw new IllegalArgumentException("One of arguments < 0");
        Timeline animation = new Timeline();
        animation.setCycleCount(1);
        animation.setAutoReverse(false);
        
        int duration = this.settingsContainer.getTimeOfWindowAppearanceInMills();
        if (startValue > endValue) 
            duration = this.settingsContainer.getTimeOfWindowDisappearanceInMills();
        
        KeyValue kv0 = new KeyValue(this.stage.opacityProperty(), startValue);
        KeyValue kv = new KeyValue(this.stage.opacityProperty(), endValue);
        KeyFrame kf0 = new KeyFrame(Duration.ZERO, kv0);
        KeyFrame kf = new KeyFrame(Duration.millis(duration), kv);
        animation.getKeyFrames().add(kf0);
        animation.getKeyFrames().add(kf);

        return animation;
    }

    /**
     * Метод создаёт анимацию изменения прозрачности от {@code startValue} до {@code endValue} для элемента 
     * {@link GreetingWindowAnimation#text},
     * который хранится в виде переменной класса.
     * @param startValue - начальное значение прозрачности.
     * @param endValue - конечное значение прозрачности.
     * @return анимация изменения прозрачности {@code Node}.
     * @throws IllegalArgumentException если один из аргументов меньше 0.
     */
    private FadeTransition createOppacityAnimationForNode(int startValue, int endValue) {
        if (startValue < 0 | endValue < 0) throw new IllegalArgumentException("One of arguments < 0");
        FadeTransition animation = new FadeTransition();
        animation.setCycleCount(1);
        animation.setAutoReverse(false);
        int duration = this.settingsContainer.getTimeOfTextAppearanceInMills();
        if (startValue > endValue) 
            duration = this.settingsContainer.getTimeOfTextDisappearanceInMills();
        animation.setDuration(Duration.millis(duration));
        animation.setFromValue(startValue);
        animation.setToValue(endValue);
        animation.setNode(this.text);
        
        return animation;
    }

    /**
     * Метод последовательно создаёт и вставляет задержки между анимациями основных элементов графического интерфейса.
     * Подробнее про {@code delaysInMills} в документации к методу {@link GreetingWindowAnimation#createAnimationSequence}
     * @param animationSequence - последовательность анимаций в виде списка.
     * @param delaysInMills - массив задержек.
     */
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
            Animation delay = GreetingWindowAnimation.createDelay(delaysInMills[idx]);
            animationSequence.add(insertIdx, delay);
            insertIdx += 2;
        }
    }
}
