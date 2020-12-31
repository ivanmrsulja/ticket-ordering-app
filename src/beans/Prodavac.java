package beans;

import java.util.ArrayList;
import java.util.Date;

public class Prodavac extends Korisnik{
	
	ArrayList<Manifestacija> manifestacije;
	
	public Prodavac() {}
	public Prodavac(String u, String p, String im, String pr, String po, long dr, String ul) {
		super(u, p, im, pr, po, dr, ul);
		manifestacije = new ArrayList<Manifestacija>();
	}
	
	public ArrayList<Manifestacija> getManifestacije() {
		return manifestacije;
	}
	
	public void setManifestacije(ArrayList<Manifestacija> manifestacije) {
		this.manifestacije = manifestacije;
	}
	
	@Override
	public String toString() {
		return "Prodavac [manifestacije="+super.toString() + manifestacije + "]";
	}
	
}
