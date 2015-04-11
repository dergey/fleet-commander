package de.spries.fleetcommander.model.universe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import de.spries.fleetcommander.model.Player;

public class Universe {

	private final List<Planet> planets;
	private List<ShipFormation> travellingShipFormations;

	protected Universe(List<Planet> planets) {
		if (CollectionUtils.isEmpty(planets)) {
			throw new IllegalArgumentException("List of planets required");
		}
		if (!planetsHaveDistinctLocations(planets)) {
			throw new IllegalArgumentException("planets must all have a distinct position: " + planets);
		}
		this.planets = Collections.unmodifiableList(planets);
		travellingShipFormations = new ArrayList<>();
	}

	public List<Planet> getPlanets() {
		return planets;
	}

	public Collection<Planet> getHomePlanets() {
		return planets.parallelStream().filter((p) -> p.isHomePlanet()).collect(Collectors.toList());
	}

	public Planet getHomePlanetOf(Player player) {
		Optional<Planet> homePlanet = planets.parallelStream().filter((p) -> p.isHomePlanetOf(player)).findFirst();
		if (!homePlanet.isPresent()) {
			throw new IllegalStateException("player " + player + " has no home planet");
		}
		return homePlanet.get();
	}

	public void runFactoryProductionCycle() {
		for (Planet planet : planets) {
			planet.runProductionCycle();
		}
	}

	public void runShipTravellingCycle() {
		for (ShipFormation shipFormation : travellingShipFormations) {
			shipFormation.landOnDestination();
		}
		travellingShipFormations.clear();
	}

	public void sendShips(int shipCount, int originPlanetId, int destinationPlanetId, Player player) {
		Planet origin = getPlanetForId(originPlanetId);
		Planet destination = getPlanetForId(destinationPlanetId);
		sendShips(shipCount, origin, destination, player);
	}

	protected void sendShips(int shipCount, Planet origin, Planet destination, Player player) {
		if (!planets.contains(origin) || !planets.contains(destination)) {
			throw new IllegalArgumentException("origin & destination must be contained in universe");
		}

		if (destination.equals(origin)) {
			return;
		}

		origin.sendShipsAway(shipCount, player);
		destination.addIncomingShips(shipCount);

		ShipFormation newShipFormation = new ShipFormation(shipCount, origin, destination, player);
		ShipFormation joinableFormation = getJoinableShipFormation(newShipFormation);

		if (joinableFormation == null) {
			travellingShipFormations.add(newShipFormation);
		} else {
			newShipFormation.join(joinableFormation);
		}
	}

	public Planet getPlanetForId(int planetId) {
		return planets.parallelStream().filter((p) -> p.getId() == planetId).findFirst().get();
	}

	private ShipFormation getJoinableShipFormation(ShipFormation newShipFormation) {
		for (ShipFormation formation : travellingShipFormations) {
			if (newShipFormation.canJoin(formation)) {
				return formation;
			}
		}
		return null;
	}

	public List<ShipFormation> getTravellingShipFormations() {
		return travellingShipFormations;
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