module org.example {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.prefs;
	requires java.net.http;	
	requires javafx.base;
	requires java.desktop;
	requires transitive com.google.gson;
	requires jdk.crypto.ec;
	requires transitive javafx.graphics;


    opens org.example to javafx.fxml;
    exports org.example;
}