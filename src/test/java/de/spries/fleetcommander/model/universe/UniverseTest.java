package de.spries.fleetcommander.model.universe;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.spries.fleetcommander.model.Player;

public class UniverseTest {

	private Player john;
	private Player jack;
	private Planet johnsHomePlanet;
	private Planet jacksHomePlanet;
	private Universe universe;

	//TODO refactor tests to use mocks

	@Before
	public void setUp() {
		john = mock(Player.class);
		jack = mock(Player.class);
		johnsHomePlanet = new Planet(1, 1, john);
		jacksHomePlanet = new Planet(5, 1, jack);
		johnsHomePlanet.setId(0);
		jacksHomePlanet.setId(1);
		universe = new Universe(Arrays.asList(johnsHomePlanet, jacksHomePlanet));
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void requiresPlanetList() {
		new Universe(null);
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void requiresNonEmptyPlanetList() {
		new Universe(Collections.emptyList());
	}

	@Test
	public void newUniverseHasPlanets() {
		assertThat(universe.getPlanets(), is(not(empty())));
	}

	@Test
	public void universeHasHomePlanets() throws Exception {
		Collection<Planet> homePlanets = universe.getHomePlanets();
		assertThat(homePlanets, hasItem(johnsHomePlanet));
		assertThat(homePlanets, hasItem(jacksHomePlanet));
		assertThat(homePlanets, hasSize(2));
	}

	@SuppressWarnings("unused")
	@Test(expected = IllegalArgumentException.class)
	public void planetsMustHaveDifferentPosition() {
		new Universe(Arrays.asList(new Planet(0, 0), new Planet(0, 0)));
	}

	@Test
	public void universeHasNoTravellingShips() throws Exception {
		assertThat(universe.getTravellingShipFormations(), hasSize(0));
	}
}
