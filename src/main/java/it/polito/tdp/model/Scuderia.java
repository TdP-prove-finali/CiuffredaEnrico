package it.polito.tdp.model;

public class Scuderia {
	private Integer id;
	private String ref;
	private String nome;
	private String nazionalita;
	private Integer importospeso;
	private float punteggio;
	
	/**
	 * @param id
	 * @param ref
	 * @param nome
	 * @param nazionalita
	 * @param importospeso
	 */
	public Scuderia(Integer id, String ref, String nome, String nazionalita, Integer importospeso) {
		super();
		this.id = id;
		this.ref = ref;
		this.nome = nome;
		this.nazionalita = nazionalita;
		this.importospeso = importospeso;
		this.punteggio=0;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scuderia other = (Scuderia) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	public Integer getId() {
		return id;
	}





	public void setId(Integer id) {
		this.id = id;
	}





	public String getRef() {
		return ref;
	}





	public void setRef(String ref) {
		this.ref = ref;
	}





	public String getNome() {
		return nome;
	}





	public void setNome(String nome) {
		this.nome = nome;
	}





	public String getNazionalita() {
		return nazionalita;
	}





	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}





	public Integer getImportospeso() {
		return importospeso;
	}





	public void setImportospeso(Integer importospeso) {
		this.importospeso = importospeso;
	}





	public Float getPunteggio() {
		return punteggio;
	}


	public void setPunteggio(Float punteggio) {
		this.punteggio = punteggio;
	}


	@Override
	public String toString() {
		return "La scuderia " + nome;
	}
	
	
}
