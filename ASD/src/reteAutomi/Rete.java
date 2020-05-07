package reteAutomi;

import java.util.ArrayList;

public class Rete {
	private ArrayList<Automa> automi;
	private ArrayList<Link> links;
	
	public Rete(ArrayList<Automa> automi, ArrayList<Link> links) {
		this.automi = automi;
		this.links = links;
	}

}
