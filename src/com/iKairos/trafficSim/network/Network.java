package com.iKairos.trafficSim.network;

import java.util.ArrayList;

public class Network {

	TwoLaneOneWayEdge[] edges;
	private ArrayList<TwoLaneOneWayEdge> edgesAL = new ArrayList<TwoLaneOneWayEdge>();

    public void addEdge(TwoLaneOneWayEdge edge) {
		edgesAL.add(edge);
	}

	public TwoLaneOneWayEdge getEdge(int edgeId) {
		return this.edges[edgeId];
	}

	public void optimise() {

		//Convert edges ArrayList into an array for quicker access during vehicle translations
		edges = new TwoLaneOneWayEdge[edgesAL.size()];
		
		for (int i = 0; i < edgesAL.size(); i++) {
			edges[i] = edgesAL.get(i);
		}
	}
}
