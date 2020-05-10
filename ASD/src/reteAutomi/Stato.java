package reteAutomi;

import java.util.concurrent.atomic.AtomicInteger;

public class Stato {

	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;
	
	public Stato() {
		this.id = ai.incrementAndGet();
	}


	public int getId(){
		return this.id;
		
	}

	@Override
	public String toString() {
		return "Stato{" +
				"id=" + id +
				'}';
	}
}
