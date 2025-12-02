module com.meuprojeto.pokedexfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires jdk.crypto.ec;

    exports com.meuprojeto.pokedexfx;
    
    // "opens" permite que bibliotecas externas (Gson e JavaFX) mexam nas suas classes via reflexão
    opens com.meuprojeto.pokedexfx to com.google.gson, javafx.fxml;
}