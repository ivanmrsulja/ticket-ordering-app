package beans;

import java.util.ArrayList;
import java.util.Date;

public class Manifestacija {
	private int id;
	private String naziv;
	private String tipManifestacije;
	private long datumOdrzavanja;
	private int brojMesta;
	private double cenaRegular;
	private String status;
	private Lokacija lokacija;
	private String slika; //putanja do slike
	private double ocena;
	
	public Manifestacija() {}

	public Manifestacija(int id, String naziv, String tipManifestacije, long datOdr, int brojMesta, double cenaRegular, String status,
			Lokacija lokacija, String slika) {
		this.id = id;
		this.naziv = naziv;
		this.tipManifestacije = tipManifestacije;
		this.datumOdrzavanja = datOdr;
		this.brojMesta = brojMesta;
		this.cenaRegular = cenaRegular;
		this.status = status;
		this.lokacija = lokacija;
		this.slika = slika;
		this.ocena = 0;
	}
	
	public double getOcena() {
		return ocena;
	}

	public void setOcena(double ocena) {
		this.ocena = ocena;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getTipManifestacije() {
		return tipManifestacije;
	}

	public void setTipManifestacije(String tipManifestacije) {
		this.tipManifestacije = tipManifestacije;
	}

	public int getBrojMesta() {
		return brojMesta;
	}

	public void setBrojMesta(int brojMesta) {
		this.brojMesta = brojMesta;
	}

	public double getCenaRegular() {
		return cenaRegular;
	}

	public void setCenaRegular(double cena) {
		this.cenaRegular = cena;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Lokacija getLokacija() {
		return lokacija;
	}
	
	

	public int getId() {
		return id;
	}

	public long getDatumOdrzavanja() {
		return datumOdrzavanja;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDatumOdrzavanja(long datumOdrzavanja) {
		this.datumOdrzavanja = datumOdrzavanja;
	}

	public void setLokacija(Lokacija lokacija) {
		this.lokacija = lokacija;
	}

	public String getSlika() {
		return slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	@Override
	public String toString() {
		return "Manifestacija [ naziv=" + naziv + ", tipManifestacije=" + tipManifestacije
				+ ", brojMesta=" + brojMesta + ", cena=" + cenaRegular + ", status=" + status + ", lokacija=" + lokacija
				+ ", slika=" + slika + "]";
	}
	
}
