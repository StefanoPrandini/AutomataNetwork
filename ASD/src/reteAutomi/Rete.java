package reteAutomi;

import java.util.ArrayList;

public class Rete {
	ArrayList<Automa> automi;
	ArrayList<Link> links;
	
	public Rete(ArrayList<Automa> automi, ArrayList<Link> links) {
		this.automi = automi;
		this.links = links;
	}

}
