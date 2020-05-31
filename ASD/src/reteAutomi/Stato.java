package reteAutomi;

import java.util.concurrent.atomic.AtomicInteger;

public class Stato {

	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;
	private String nome;
	
	public Stato(String nome) {
		this.id = ai.incrementAndGet();
		this.nome = nome;
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
