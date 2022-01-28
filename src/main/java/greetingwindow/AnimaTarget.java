package greetingwindow;

/**
 * Перечисление информирует о том, для какого элемента необходимо сделать анимацию.
 * NO_ANIMATION - не делать вовсе;
 * ONLY_WINDOW - только для окна;
 * ONLY_TEXT - только текст;
 * BOTH - для окна и для текста.
 */
public enum AnimaTarget {
    NO_ANIMATION,
    ONLY_WINDOW,
    ONLY_TEXT,
    BOTH;
}
