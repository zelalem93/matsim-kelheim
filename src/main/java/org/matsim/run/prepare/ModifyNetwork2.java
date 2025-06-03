package org.matsim.run.prepare;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.network.io.NetworkWriter;

import java.util.List;

public class ModifyNetwork2 {


	public static void main(String[] args) {

		var network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile("https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/kelheim/kelheim-v3.0/input/kelheim-v3.0-network-with-pt.xml.gz");

		// list of list that are going to be modified

		List<String> bikeFriendlyStreets = List.of("-487455692#0","487455692#0","-487455692#2",
			"487455692#2","-487455692#3","487455692#3","-487455692#5","487455692#5",
			"-487455692#6","487455692#6","827847902","-4712335#0","4712335#0");


		for(String linkid : bikeFriendlyStreets){
			Link link = network.getLinks().get(Id.createLinkId(linkid));

			if(link != null){
				// reduce speed
				link.setFreespeed(5.5);
				System.out.println("Updated link" + linkid);
			}
		}

		// Save to new file
		new NetworkWriter(network).write("input/modified_network2.xml");

	}
}
