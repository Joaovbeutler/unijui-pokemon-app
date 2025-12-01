package br.unijui.edu.prog.avaliacao.pokemonapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {

    private PokeService service = new PokeService();
    
    // Componentes de UI
    private TextField txtBusca;
    private ImageView imgPokemon;
    private Label lblNome;
    private Label lblId;
    private Label lblTypes; // Novo: Mostra o tipo
    private ListView<String> listaLateral;
    
    // Estado da Aplicação
    private int currentId = 1; // Começa pelo Bulbasaur

    @Override
    public void start(Stage stage) {
        // --- 1. Menu Lateral (Esquerda) ---
        listaLateral = new ListView<>();
        listaLateral.setPrefWidth(150);
        
        // Evento: Ao clicar em um nome na lista
        listaLateral.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                carregarPokemon(newVal);
            }
        });

        // --- 2. Painel Central (Detalhes) ---
        Label lblTitulo = new Label("Pokedéx");
        lblTitulo.setFont(new Font("Arial Bold", 24));

        txtBusca = new TextField();
        txtBusca.setPromptText("Buscar...");
        txtBusca.setOnAction(e -> carregarPokemon(txtBusca.getText()));

        Button btnBuscar = new Button("Ir");
        btnBuscar.setOnAction(e -> carregarPokemon(txtBusca.getText()));
        
        HBox boxBusca = new HBox(5, txtBusca, btnBuscar);
        boxBusca.setAlignment(Pos.CENTER);

        imgPokemon = new ImageView();
        imgPokemon.setFitHeight(200);
        imgPokemon.setFitWidth(200);
        imgPokemon.setPreserveRatio(true);

        lblNome = new Label("Carregando...");
        lblNome.setFont(new Font("Arial Bold", 20));
        
        lblId = new Label("");
        lblTypes = new Label("");
        lblTypes.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");

        VBox centro = new VBox(15);
        centro.setPadding(new Insets(20));
        centro.setAlignment(Pos.CENTER);
        centro.getChildren().addAll(lblTitulo, boxBusca, imgPokemon, lblNome, lblId, lblTypes);

        // --- 3. Navegação (Baixo) ---
        Button btnVoltar = new Button("<< Anterior");
        Button btnProximo = new Button("Próximo >>");

        btnVoltar.setOnAction(e -> navegar(-1));
        btnProximo.setOnAction(e -> navegar(1));

        HBox rodape = new HBox(20);
        rodape.setPadding(new Insets(15));
        rodape.setAlignment(Pos.CENTER);
        rodape.setStyle("-fx-background-color: #ddd;");
        rodape.getChildren().addAll(btnVoltar, btnProximo);

        // --- 4. Montagem do Layout Principal (BorderPane) ---
        BorderPane root = new BorderPane();
        root.setLeft(listaLateral);
        root.setCenter(centro);
        root.setBottom(rodape);

        // --- 5. Inicialização ---
        Scene scene = new Scene(root, 600, 500); // Janela maior agora
        stage.setTitle("Pokedéx Completa");
        stage.setScene(scene);
        stage.show();

        // Carrega dados iniciais
        inicializarDados();
    }

    private void inicializarDados() {
        // Carrega a lista lateral em uma Thread separada para não travar a janela ao abrir
        new Thread(() -> {
            try {
                List<String> nomes = service.buscarListaNomes();
                
                // Atualiza a UI na Thread do JavaFX
                Platform.runLater(() -> {
                    listaLateral.getItems().addAll(nomes);
                    // Seleciona o primeiro
                    listaLateral.getSelectionModel().select(0);
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblNome.setText("Erro ao conectar API"));
            }
        }).start();
    }

    // Lógica para Next/Prev
    private void navegar(int direcao) {
        int novoId = currentId + direcao;
        if (novoId < 1) novoId = 1; // Não deixa baixar de 1
        
        carregarPokemon(String.valueOf(novoId));
    }

    private void carregarPokemon(String termo) {
        if (termo == null || termo.trim().isEmpty()) return;

        // Feedback visual
        lblNome.setText("...");
        imgPokemon.setImage(null);

        // Executa busca em Thread separada
        new Thread(() -> {
            try {
                Pokemon p = service.buscarPokemon(termo);

                // Atualiza UI
                Platform.runLater(() -> {
                    currentId = p.getId(); // Sincroniza o ID atual
                    
                    String nomeFormatado = p.getName().substring(0, 1).toUpperCase() + p.getName().substring(1);
                    lblNome.setText(nomeFormatado);
                    lblId.setText("ID: #" + p.getId());
                    
                    // Tratamento simples para Tipos (ex: grass, poison)
                    // Como não mapeamos Types no Pokemon.java ainda, deixaremos visualmente simples
                    // ou você pode adicionar o mapeamento se desejar.
                    
                    if (p.getSpriteUrl() != null) {
                        imgPokemon.setImage(new Image(p.getSpriteUrl()));
                    }
                    
                    // Tenta sincronizar a seleção da lista sem disparar evento de loop
                    // (Opcional, apenas visual)
                    txtBusca.setText("");
                });

            } catch (Exception ex) {
                Platform.runLater(() -> {
                    lblNome.setText("Não encontrado");
                    lblId.setText("");
                });
            }
        }).start();
    }

    public static void main(String[] args) {
        launch();
    }
}