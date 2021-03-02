module com.github.bielmarfran {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.prefs;
	requires java.net.http;	
	requires javafx.base;
	requires java.desktop;
	requires transitive com.google.gson;
	requires jdk.crypto.ec;
	requires transitive javafx.graphics;


    opens com.github.bielmarfran.nameit to javafx.fxml;
    exports com.github.bielmarfran.nameit;
}