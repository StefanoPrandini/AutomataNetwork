package model;

import java.io.Serializable;

public class Indice implements Serializable {
	private static final long serialVersionUID = -3562299391009560371L;
	
	private Stato stato;
	private boolean marked;
	
	public Indice(Stato stato) {
		this.setStato(stato);
		this.setMarked(false);
	}

	public Stato getStato() {
		return stato;
	}

	public void setStato(Stato stato) {
		this.stato = stato;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	@Override
//	quando controllo se indice e' gia' presente in un stato del dizionario, non importa se marked o no
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if (!(obj instanceof Indice)) return false;
        Indice that = (Indice) obj;
        return this.stato.equals(that.stato);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stato == null) ? 0 : stato.hashCode());
		return result;
	}

}
