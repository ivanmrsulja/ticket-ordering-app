package beans;

public class ShoppingCartItem {
	private int id;
	private int idManifestacije;
	private int kolicina;
	private String tipKarte;
	private String naziv;
	private double cijena;
	

	public ShoppingCartItem() {}

	public ShoppingCartItem(int idManifestacije, int kolicina, String tipKarte) {
		super();
		this.idManifestacije = idManifestacije;
		this.kolicina = kolicina;
		this.tipKarte = tipKarte;
	}

	public int getIdManifestacije() {
		return idManifestacije;
	}

	public void setIdManifestacije(int idManifestacije) {
		this.idManifestacije = idManifestacije;
	}

	public int getKolicina() {
		return kolicina;
	}

	public void setKolicina(int kolicina) {
		this.kolicina = kolicina;
	}

	public String getTipKarte() {
		return tipKarte;
	}

	public void setTipKarte(String tipKarte) {
		this.tipKarte = tipKarte;
	}

	public double getCijena() {
		return cijena;
	}

	public void setCijena(double cijena) {
		this.cijena = cijena;
	}
	
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ShoppingCartItem [id=" + id + ", idManifestacije=" + idManifestacije + ", kolicina=" + kolicina
				+ ", tipKarte=" + tipKarte + ", naziv=" + naziv + ", cijena=" + cijena + "]";
	}
	
}
