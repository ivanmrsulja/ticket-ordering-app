package beans;

import java.util.ArrayList;
import java.util.Date;

public class Prodavac extends Korisnik{
	
	private ArrayList<Manifestacija> manifestacije;
	private boolean obrisan;
	private boolean banovan;
	
	public Prodavac() {}
	public Prodavac(String u, String p, String im, String pr, String po, long dr, String ul) {
		super(u, p, im, pr, po, dr, ul);
		manifestacije = new ArrayList<Manifestacija>();
		obrisan = false;
		banovan = false;
	}
	
	public boolean isBanovan() {
		return banovan;
	}
	public void setBanovan(boolean banovan) {
		this.banovan = banovan;
	}
	public void addManifestacija(Manifestacija m) {
		manifestacije.add(m);
	}
	
	public ArrayList<Manifestacija> getManifestacije() {
		return manifestacije;
	}
	
	public void setManifestacije(ArrayList<Manifestacija> manifestacije) {
		this.manifestacije = manifestacije;
	}
	
	public boolean isObrisan() {
		return obrisan;
	}
	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}
	
	@Override
	public String toString() {
		return "Prodavac [manifestacije="+super.toString() + manifestacije + "]";
	}
	
}
