package com.santorini;

public class GodCardFactory {
    
    public static GodCard getGodCard(String godCardName) {
        switch (godCardName.toLowerCase()) {
            case "demeter":
                return new Demeter();
            case "hephaestus":
                return new Hephaestus();
            case "minotaur":
                return new Minotaur();
            case "pan":
                return new Pan();
            default:
                throw new IllegalArgumentException("Unknown God Card: " + godCardName);
        }
    }
}
