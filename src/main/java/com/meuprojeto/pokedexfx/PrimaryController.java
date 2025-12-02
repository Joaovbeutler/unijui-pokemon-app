package com.meuprojeto.pokedexfx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class PrimaryController {

    // Vínculos com o FXML (note os nomes iguais aos fx:id)
    @FXML private ListView<String> listaLateral;
    @FXML private TextField txtBusca;
    @FXML private ImageView imgPokemon;
    @FXML private Label lblNome;
    @FXML private Label lblId;
    @FXML private Label lblTypes;

    private PokeService service = new PokeService();
    private int currentId = 1;

    // Este método roda automaticamente quando o FXML é carregado
    @FXML
    public void initialize() {
        // Configura o clique na lista
        listaLateral.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarPokemon(newVal);
            }
        });

        // Carrega os dados iniciais em background
        new Thread(() -> {
            try {
                List<String> nomes = service.buscarListaNomes();
                Platform.runLater(() -> {
                    listaLateral.getItems().addAll(nomes);
                    listaLateral.getSelectionModel().select(0);
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblNome.setText("Erro API Lista"));
            }
        }).start();
    }

    @FXML
    private void onBuscar() {
        carregarPokemon(txtBusca.getText());
    }

    @FXML
    private void onProximo() {
        navegar(1);
    }

    @FXML
    private void onAnterior() {
        navegar(-1);
    }

    private void navegar(int direcao) {
        int novoId = currentId + direcao;
        if (novoId < 1) novoId = 1;
        carregarPokemon(String.valueOf(novoId));
    }

    private void carregarPokemon(String termo) {
        if (termo == null || termo.trim().isEmpty()) return;

        lblNome.setText("...");
        imgPokemon.setImage(null);

        new Thread(() -> {
            try {
                Pokemon p = service.buscarPokemon(termo);

                Platform.runLater(() -> {
                    currentId = p.getId();
                    String nomeFormatado = p.getName().substring(0, 1).toUpperCase() + p.getName().substring(1);
                    
                    lblNome.setText(nomeFormatado);
                    lblId.setText("ID: #" + p.getId());
                    
                    if (p.getSpriteUrl() != null) {
                        imgPokemon.setImage(new Image(p.getSpriteUrl()));
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> {
                    lblNome.setText("Não encontrado");
                    lblId.setText("");
                });
            }
        }).start();
    }
}