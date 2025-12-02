package com.meuprojeto.pokedexfx;

public class Pokemon {
    private String name;
    private int id;
    private Sprites sprites;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getSpriteUrl() {
        // Retorna a URL da imagem frontal
        return (sprites != null) ? sprites.front_default : null;
    }

    // Classe interna para mapear o objeto "sprites" do JSON
    private static class Sprites {
        String front_default;
    }
}