package interface_adapter.switch_pokemon;

import use_case.switch_pokemon.SwitchPokemonOutputBoundary;
import use_case.switch_pokemon.SwitchPokemonOutputData;

public class SwitchPokemonPresenter implements SwitchPokemonOutputBoundary {
    private final SwitchPokemonViewModel viewModel;

    public SwitchPokemonPresenter(SwitchPokemonViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SwitchPokemonOutputData outputData) {
        viewModel.setBattle(outputData.getBattle());
        viewModel.setOldPokemonName(outputData.getOldPokemonName());
        viewModel.setNewPokemonName(outputData.getNewPokemonName());
        viewModel.setMessage(outputData.getMessage());
        viewModel.setSwitchSuccessful(true);

        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setMessage(error);
        viewModel.setSwitchSuccessful(false);
        viewModel.firePropertyChanged();
    }
}