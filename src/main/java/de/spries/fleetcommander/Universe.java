package de.spries.fleetcommander;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class Universe {

	private final List<Planet> planets;

	protected Universe(List<Planet> planets) {
		if (CollectionUtils.isEmpty(planets)) {
			throw new IllegalArgumentException("List of planets required");
		}
		if (!planetsHaveDistinctLocations(planets)) {
			throw new IllegalArgumentException("planets must all have a distinct position: " + planets);
		}
		this.planets = Collections.unmodifiableList(planets);
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public Planet getHomePlanet(Player player) {
		// TODO improve performance
		for (Planet planet : planets) {
			if (planet.isHomePlanetOf(player)) {
				return planet;
			}
		}
		throw new IllegalStateException("player " + player + " has no home planet");
	}

	private static boolean planetsHaveDistinctLocations(List<Planet> planets) {
		for (int i = 0; i < planets.size() - 1; i++) {
			for (int j = i + 1; j < planets.size(); j++) {
				Planet p1 = planets.get(i);
				Planet p2 = planets.get(j);
				if (p1.distanceTo(p2) == 0) {
					return false;
				}
			}
		}
		return true;
	}

}