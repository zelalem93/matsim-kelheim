package org.matsim.run.prepare;

import org.matsim.api.core.v01.Id;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.network.io.NetworkWriter;
import org.matsim.core.utils.geometry.CoordUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ModifyNetwrok {

	public static void main(String[] args) {

		// read in the network

		var network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile("https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/kelheim/kelheim-v3.0/input/kelheim-v3.0-network-with-pt.xml.gz");

		//  5 existing nodes from kelheim city center to  the industrial zones to create links
		List<String> nodeIds = List.of("cluster_294729181_4289309782","301468928",
			"304054295","260080642","2540199201");

		// create bidirectional links

		for(int i=0; i< nodeIds.size()-1; i++){
			var fromId = nodeIds.get(i);
			var toId = nodeIds.get(i+1);

			var fromNode = network.getNodes().get(Id.createNodeId(fromId));
			var toNode  = network.getNodes().get(Id.createNodeId(toId));

			/// calculate euclidean distance from coordinates
			double length = CoordUtils.calcEuclideanDistance(fromNode.getCoord(),toNode.getCoord());

			// added some adjustment factors of 5%
			double adj_length = length * 0.05;

			/// forward link
			var linkIdFwd = "bike_highway_" + fromId + "_to_" + toId;
			var linkFwd  = network.getFactory().createLink(Id.createLinkId(linkIdFwd), fromNode, toNode);
			linkFwd.setCapacity(5000);
			linkFwd.setLength(adj_length);
			linkFwd.setAllowedModes(Set.of("bike"));
			linkFwd.setFreespeed(7);
			linkFwd.setNumberOfLanes(1);
			network.addLink(linkFwd);

			/// backward link
			var linkIdBwd = "bike_highway_" + toId + "_to_" + fromId;
			var linkBwd  = network.getFactory().createLink(Id.createLinkId(linkIdBwd), toNode, fromNode);
			linkBwd.setCapacity(5000);
			linkBwd.setLength(adj_length);
			linkBwd.setAllowedModes(Set.of("bike"));
			linkBwd.setFreespeed(7);
			linkBwd.setNumberOfLanes(1);
			network.addLink(linkBwd);
		}

		new NetworkWriter(network).write("input/modified_network.xml");
	}
}
