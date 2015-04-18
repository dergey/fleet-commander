package de.spries.fleetcommander.model.core;

import de.spries.fleetcommander.model.core.universe.Planet;

public class ComputerPlayer extends Player {

	public ComputerPlayer(String name) {
		super(name);
	}

	@Override
	public void notifyNewTurn(Game game) {
		super.notifyNewTurn(game);

		try {
			Planet homePlanet = game.getUniverse().getHomePlanetOf(this);
			while (homePlanet.canBuildFactory(this)) {
				homePlanet.buildFactory(this);
			}
		} catch (Exception e) {
			// Just end the turn
		}

		game.endTurn(this);
	}

}