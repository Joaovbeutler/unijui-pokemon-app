module br.unijui.edu.prog.avaliacao.pokemonapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires jdk.crypto.ec;

    opens br.unijui.edu.prog.avaliacao.pokemonapp to javafx.fxml;
    exports br.unijui.edu.prog.avaliacao.pokemonapp;
}
