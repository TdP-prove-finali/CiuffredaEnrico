package it.polito.tdp.model;


public class Circuito {
	private Integer id;
	private String ref;
	private String nome;
	private String location;
	private String paese;
	private Double lat;
	private Double lng;
	
	/**
	 * @param id
	 * @param ref
	 * @param nome
	 * @param location
	 * @param paese
	 * @param lat
	 * @param lng
	 */
	public Circuito(Integer id, String ref, String nome, String location, String paese, Double lat, Double lng) {
		super();
		this.id = id;
		this.ref = ref;
		this.nome = nome;
		this.location = location;
		this.paese = paese;
		this.lat = lat;
		this.lng = lng;
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
		Circuito other = (Circuito) obj;
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


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getPaese() {
		return paese;
	}


	public void setPaese(String paese) {
		this.paese = paese;
	}


	public Double getLat() {
		return lat;
	}


	public void setLat(Double lat) {
		this.lat = lat;
	}


	public Double getLng() {
		return lng;
	}


	public void setLng(Double lng) {
		this.lng = lng;
	}


	@Override
	public String toString() {
		if(nome.equals("null")) {
			return location+ " in " +paese;
		}
		return nome + " di " + location + " in " + paese;
	}
	
	
}
