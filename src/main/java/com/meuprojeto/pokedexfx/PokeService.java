package com.meuprojeto.pokedexfx;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class PokeService {
    
    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";

    // Cliente HTTP reutilizável (melhor performance)
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public Pokemon buscarPokemon(String nomeOuId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + nomeOuId.toLowerCase().trim()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Pokémon não encontrado!");
        }

        return gson.fromJson(response.body(), Pokemon.class);
    }

    // NOVO: Busca a lista de nomes para o menu
    public List<String> buscarListaNomes() throws Exception {
        // limit=1000 pega a geração clássica até a atual, evitando formas estranhas
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://pokeapi.co/api/v2/pokemon?limit=1000")) 
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        PokeListResponse listResponse = gson.fromJson(response.body(), PokeListResponse.class);
        
        // Extrai apenas os nomes da lista de objetos
        return listResponse.results.stream()
                .map(r -> r.name)
                .collect(Collectors.toList());
    }

    // Classes internas auxiliares para mapear a lista JSON
    private static class PokeListResponse {
        List<PokeResult> results;
    }
    
    private static class PokeResult {
        String name;
    }
}