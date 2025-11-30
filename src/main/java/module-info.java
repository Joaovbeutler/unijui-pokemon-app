module br.unijui.edu.prog.avaliacao.pokemonapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;

    opens br.unijui.edu.prog.avaliacao.pokemonapp to javafx.fxml;
    exports br.unijui.edu.prog.avaliacao.pokemonapp;
}
