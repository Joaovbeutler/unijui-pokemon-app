module br.unijui.edu.prog.avaliacao.pokemonapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens br.unijui.edu.prog.avaliacao.pokemonapp to javafx.fxml;
    exports br.unijui.edu.prog.avaliacao.pokemonapp;
}
