package nongroup;

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

class GreetingWindowAnimation {

    private Stage stage;
    private Node text;
    private GreetingWindow settingsContainer;
    public IAnimationWatcher observer;

    public GreetingWindowAnimation(GreetingWindow settingsContainer, Stage stage, Node text, IAnimationWatcher observer) {
        this.settingsContainer = settingsContainer;
        this.stage = stage;
        this.text = text;
        this.observer = observer;
    }

    public GreetingWindowAnimation(GreetingWindow settingsContainer, Stage stage, Node text) {
        this(settingsContainer, stage, text, null);
    }

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

    public void doEndAnimationThings() {
        if (observer != null) observer.invokeAfterAnimation(); 
    }

    public Animation connectAnimationsEachAfterPrev(List<Animation> animationsSequence) {
        for (int i = 1; i < animationsSequence.size(); i++) {
            Animation prev =  animationsSequence.get(i - 1);
            Animation next = animationsSequence.get(i);
            prev.setOnFinished((event) -> next.play());
        }
        Animation last = animationsSequence.get(animationsSequence.size() - 1);
        last.setOnFinished((event) -> this.doEndAnimationThings());
        return animationsSequence.get(0);
    }

    public static Animation createDelay(int timeInMills) {
        Timeline delay = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.millis(timeInMills), new KeyValue[] { });
        delay.getKeyFrames().add(frame);
        return delay;
    }

    private Timeline createOppacityAnimationForStage(int startValue, int endValue) {
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

    private FadeTransition createOppacityAnimationForNode(int startValue, int endValue) {
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
