package de.spries.fleetcommander.model.core;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import de.spries.fleetcommander.model.core.universe.Planet;
import de.spries.fleetcommander.model.core.universe.Universe;

public class ComputerPlayerTest {

	private Player player;
	private Game game;
	private Universe universe;
	private Planet homePlanet;

	@Before
	public void setUp() {
		player = new ComputerPlayer("Computer");

		game = mock(Game.class);
		universe = mock(Universe.class);
		homePlanet = mock(Planet.class);

		doReturn(universe).when(game).getUniverse();
		doReturn(homePlanet).when(universe).getHomePlanetOf(player);
		doReturn(Arrays.asList(homePlanet)).when(universe).getPlanets();
		doReturn(true).when(homePlanet).isInhabitedBy(player);
	}

	@Test
	public void endsTurnInNewTurn() {
		player.notifyNewTurn(game);
		verify(game).endTurn(player);
	}

	@Test
	public void stillEndsTurnWhenPlayerHasNoMoreHomePlanet() throws Exception {
		doThrow(RuntimeException.class).when(universe).getHomePlanetOf(player);
		player.notifyNewTurn(game);
		verify(game).endTurn(player);
	}

	@Test
	public void buildsNoFactoriesWhenPlayerCannotBuild() throws Exception {
		doReturn(false).when(homePlanet).canBuildFactory(player);
		player.notifyNewTurn(game);
		verify(homePlanet, never()).buildFactory(player);
	}

	@Test
	public void buildsFactoriesWhenPlayerCanBuild() throws Exception {
		doReturn(true).doReturn(true).doReturn(false).when(homePlanet).canBuildFactory(player);
		player.notifyNewTurn(game);
		verify(homePlanet, times(2)).buildFactory(player);
	}

	@Test
	public void stillEndsTurnWhenBuildingFactoryThrowsException() throws Exception {
		doThrow(RuntimeException.class).when(homePlanet).buildFactory(player);
		player.notifyNewTurn(game);
		verify(game).endTurn(player);
	}

}
