package beans;

public class Komentar {
	private Korisnik kupac;
	private Manifestacija manifestacija;
	private String tekst;
	private double ocena;
	private boolean odobren;
	private boolean obrisan;
	
	public Komentar() {}

	public Komentar(Korisnik kupac, Manifestacija manifestacija, String tekst, double ocena) {
		super();
		this.kupac = kupac;
		this.manifestacija = manifestacija;
		this.tekst = tekst;
		this.ocena = ocena;
		odobren = false;
		obrisan = false;
	}
	
	public boolean isObrisan() {
		return obrisan;
	}

	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}

	public boolean isOdobren() {
		return odobren;
	}
	
	public void setOdobren(boolean o) {
		odobren = o;
	}
	
	public Korisnik getKupac() {
		return kupac;
	}

	public void setKupac(Korisnik kupac) {
		this.kupac = kupac;
	}

	public Manifestacija getManifestacija() {
		return manifestacija;
	}

	public void setManifestacija(Manifestacija manifestacija) {
		this.manifestacija = manifestacija;
	}

	public String getTekst() {
		return tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public double getOcena() {
		return ocena;
	}

	public void setOcena(double ocena) {
		this.ocena = ocena;
	}

	@Override
	public String toString() {
		return "Komentar [kupac=" + kupac.getUsername() + ", manifestacija=" + manifestacija.getNaziv() + ", tekst=" + tekst + ", ocena=" + ocena
				+ " odobren="+ odobren+"]";
	}
	
}
