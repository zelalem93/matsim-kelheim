package org.matsim.analysis.homeWork;

import org.matsim.api.core.v01.events.LinkEnterEvent;

import org.matsim.api.core.v01.events.VehicleEntersTrafficEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.handler.VehicleEntersTrafficEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.vehicles.Vehicle;

import java.util.*;

public class EventHandler implements LinkEnterEventHandler, VehicleEntersTrafficEventHandler {
	public final Map<Id<Vehicle>, Id<Person>> vehicleToPerson = new HashMap<Id<org.matsim.vehicles.Vehicle>, Id<Person>>();
	public final Map<Id<Vehicle>, String> vehicleToMode = new HashMap<Id<org.matsim.vehicles.Vehicle>, String>();
	/// store link usage
	private final Map<Id<Link>, List<String>> linkToModes = new HashMap<>();

	@Override
	public void handleEvent(LinkEnterEvent linkEnterEvent) {
		Id<Vehicle> vehicleId = linkEnterEvent.getVehicleId();
		String mode = vehicleToMode.get(vehicleId);

		// store
		linkToModes.computeIfAbsent(linkEnterEvent.getLinkId(),k -> new ArrayList<>()).add(mode);



	}
	//

	@Override
	public void handleEvent(VehicleEntersTrafficEvent vehicleEntersTrafficEvent) {
		vehicleToPerson.put(vehicleEntersTrafficEvent.getVehicleId(),vehicleEntersTrafficEvent.getPersonId());
		vehicleToMode.put(vehicleEntersTrafficEvent.getVehicleId(),vehicleEntersTrafficEvent.getNetworkMode());


	}
	public Map<Id<Link>, List<String>> getLinkModes(){
		return linkToModes;

	}
}
