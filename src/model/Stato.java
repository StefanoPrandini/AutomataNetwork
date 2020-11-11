package model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Stato implements Serializable {
	private static final long serialVersionUID = -8342606357729996094L;
	
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
        if (this == o) return true;
        if (!(o instanceof Stato)) return false;
        Stato that = (Stato) o;
        return this.nome == that.getNome();
    }
}
