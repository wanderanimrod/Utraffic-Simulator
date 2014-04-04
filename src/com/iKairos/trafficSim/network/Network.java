package com.iKairos.trafficSim.network;

import java.util.ArrayList;

public class Network {

	TwoLaneOneWayRoad[] edges;
	private ArrayList<TwoLaneOneWayRoad> edgesAL = new ArrayList<TwoLaneOneWayRoad>();

    public void addEdge(TwoLaneOneWayRoad edge) {
		edgesAL.add(edge);
	}

	public TwoLaneOneWayRoad getEdge(int edgeId) {
		return this.edges[edgeId];
	}

	public void optimise() {

		//Convert edges ArrayList into an array for quicker access during vehicle translations
		edges = new TwoLaneOneWayRoad[edgesAL.size()];
		
		for (int i = 0; i < edgesAL.size(); i++) {
			edges[i] = edgesAL.get(i);
		}
	}
}
