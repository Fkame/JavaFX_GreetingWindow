package greetingwindow;

/**
 * Интерфейс для создания списка наблюдателей и системы подписи на событие "окончание анимации"
 */
@FunctionalInterface
public interface IAnimationWatcher {
    /**
     * Метод, который уведомит наблюдателя(-ей) об окончании анимации в окне.
     */
    void invokeAfterAnimation();
}
