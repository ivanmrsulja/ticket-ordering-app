package search;

public class KorisniciSearchParams {
	private String ime;
	private String prezime;
	private String username;
	private String kriterijumSortiranja;
	private String uloga;
	private String tip;
	private boolean opadajuce;
	
	public KorisniciSearchParams() {
		this.ime = "";
		this.prezime = "";
		this.username = "";
		this.kriterijumSortiranja = "IME";
		this.uloga = "SVE";
		this.tip = "SVE";
		this.opadajuce = false;
	}

	public KorisniciSearchParams(String ime, String prezime, String username, String kriterijumSortiranja, String uloga,
			String tip, boolean opadajuce) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.username = username;
		this.kriterijumSortiranja = kriterijumSortiranja;
		this.uloga = uloga;
		this.tip = tip;
		this.opadajuce = opadajuce;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKriterijumSortiranja() {
		return kriterijumSortiranja;
	}

	public void setKriterijumSortiranja(String kriterijumSortiranja) {
		this.kriterijumSortiranja = kriterijumSortiranja;
	}

	public String getUloga() {
		return uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public boolean isOpadajuce() {
		return opadajuce;
	}

	public void setOpadajuce(boolean opadajuce) {
		this.opadajuce = opadajuce;
	}

	@Override
	public String toString() {
		return "KorisniciSearchParams [ime=" + ime + ", prezime=" + prezime + ", username=" + username
				+ ", kriterijumSortiranja=" + kriterijumSortiranja + ", uloga=" + uloga + ", tip=" + tip
				+ ", opadajuce=" + opadajuce + "]";
	}
	
}
