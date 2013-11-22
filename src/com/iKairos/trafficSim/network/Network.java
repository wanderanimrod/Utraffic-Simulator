package com.iKairos.trafficSim.network;

import java.util.ArrayList;

public class Network {

	Edge[] edges;
	private ArrayList<Edge> edgesAL = new ArrayList<Edge>();

    public void addEdge(Edge edge) {
		edgesAL.add(edge);
	}

	public Edge getEdge(int edgeId) {
		return this.edges[edgeId];
	}

	public void optimise() {

		//Convert edges ArrayList into an array for quicker access during vehicle translations
		edges = new Edge[edgesAL.size()];
		
		for (int i = 0; i < edgesAL.size(); i++) {
			edges[i] = edgesAL.get(i);
		}
	}
}
