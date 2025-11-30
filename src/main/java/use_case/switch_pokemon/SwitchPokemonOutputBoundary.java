package use_case.switch_pokemon;

public interface SwitchPokemonOutputBoundary {
    void prepareSuccessView(SwitchPokemonOutputData outputData);
    void prepareFailView(String error);
}