module br.unijui.edu.prog.avaliacao.pokemonapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires jdk.crypto.ec;

    opens com.meuprojeto.pokedexfx to javafx.fxml;
    exports com.meuprojeto.pokedexfx;
}
