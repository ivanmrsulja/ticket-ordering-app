package beans;

public class Karta {
	private String id;
	private String idManifestacije;
	private int brojMesta;
	private double cena;
	private String idKupca;
	private String status;
	private String tip;
	
	public Karta() {}

	public Karta(String id, int brojMesta, String kupac, String status, double cena,
			String type, String m) {
		this.id = id;
		this.brojMesta = brojMesta;
		this.tip = type;
		this.cena = cena;
		this.idKupca = kupac;
		this.status = status;
		this.idManifestacije = m;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public int getBrojMesta() {
		return brojMesta;
	}

	public void setBrojMesta(int brojMesta) {
		this.brojMesta = brojMesta;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getIdManifestacije() {
		return idManifestacije;
	}

	public void setIdManifestacije(String idManifestacije) {
		this.idManifestacije = idManifestacije;
	}

	public String getIdKupca() {
		return idKupca;
	}

	public void setIdKupca(String idKupca) {
		this.idKupca = idKupca;
	}

	@Override
	public String toString() {
		return "Karta [id=" + id + ", idManifestacije=" + idManifestacije + ", brojMesta=" + brojMesta + ", cena="
				+ cena + ", idKupca=" + idKupca + ", status=" + status + ", tip=" + tip + "]";
	}

}
