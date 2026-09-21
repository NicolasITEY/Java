/**
 * Module declaration for the Projet_POO application, defining the required modules and exported packages.
 * This module requires JavaFX controls and graphics for the user interface, as well as Gson for JSON serialization and deserialization of game state. 
 * It opens specific packages to JavaFX and Gson to allow for reflection-based access to classes and members,
 * while exporting the main application packages for use by other modules or libraries.
 */
module Projet_POO {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires transitive com.google.gson;
    requires gson.extras;

    opens view to javafx.graphics;
    opens app to javafx.graphics;
    opens core.pieces to com.google.gson;
    opens core to com.google.gson;
    
    exports app;
    exports view;
    exports controller;
    exports model;

    exports core;
    exports core.pieces;
}