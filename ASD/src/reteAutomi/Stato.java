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
	
	public String getNome() {
		return this.nome;
	}

	@Override
	public String toString() {
		return "Stato{" +
				"nome=" + nome +
				'}';
	}
	
	public boolean equals(Stato s) {
		return this.nome.equals(s.nome);
	}
	
	@Override
	public boolean equals(Object o) {
		System.out.println("bbb");
        if (this == o) return true;
        if (!(o instanceof Stato)) return false;
        Stato that = (Stato) o;
        return this.nome == that.getNome();
    }
}
