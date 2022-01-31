module greetingwindow {
    requires javafx.controls;
    requires javafx.graphics;
    opens greetingwindow to javafx.graphics;
    exports greetingwindow;
}
