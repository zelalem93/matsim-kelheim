package org.matsim.analysis.homeWork;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.events.EventsUtils;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
//
public class EventManager {

	public static void main(String[] args) {
		// List of bike-friendly link IDs where speed was reduced
		List<String> bikeFriendlyStreets = List.of(
			"-487455692#0", "487455692#0", "-487455692#2", "487455692#2",
			"-487455692#3", "487455692#3", "-487455692#5", "487455692#5",
			"-487455692#6", "487455692#6", "827847902", "-4712335#0", "4712335#0"
		);

		// Convert to a set of Link IDs
		Set<Id<Link>> targetLinks = bikeFriendlyStreets.stream()
			.map(Id::createLinkId)
			.collect(Collectors.toSet());

		// Setup events manager and custom handler
		var eventsManager = EventsUtils.createEventsManager();
		var handler = new EventHandler();

		eventsManager.addHandler(handler);

		// Read events file

		EventsUtils.readEvents(eventsManager,"output/policy_scenario/policy-scenario-hw.output_events.xml.gz" );


		// results
		var linkModes = handler.getLinkModes();


		linkModes.entrySet().stream()
			.filter(entry -> targetLinks.contains(entry.getKey()))
			.forEach(entry -> {
				var modeCounts = entry.getValue().stream()
					.collect(Collectors.groupingBy(mode -> mode, Collectors.counting()));
				System.out.println("Link: " + entry.getKey() + " → " + modeCounts);
			});
	}
}
