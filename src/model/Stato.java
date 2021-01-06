package model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Stato implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;
	private String nome;
	
	public Stato(String nome) {
		this.setId(ai.incrementAndGet());
		this.nome = nome;
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
        return this.nome.equals(that.getNome());
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
