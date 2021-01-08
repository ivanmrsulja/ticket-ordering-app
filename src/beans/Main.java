package beans;


import dao.KarteDAO;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.LokacijaDAO;
import dao.ManifestacijaDAO;
import dao.TipKupcaDAO;


public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
		
		
		TipKupcaDAO tipovi = new TipKupcaDAO();
		tipovi.load();
		
		KorisnikDAO k = new KorisnikDAO(tipovi);
		k.load();
		System.out.println(k.getKorisnici());
		
		
		LokacijaDAO lokacije  = new LokacijaDAO();
		lokacije.load();
		
		ManifestacijaDAO manifestacije = new ManifestacijaDAO(lokacije, k);
		manifestacije.load();
		
		for(Manifestacija m : manifestacije.getManifestacijaList()) {
			System.out.println(m);
		}
		
		System.out.println(manifestacije);
		
		
		KarteDAO karte = new KarteDAO(manifestacije, k);
		karte.load();
		
		KomentarDAO komentari = new KomentarDAO(k,manifestacije);
		komentari.load();
		komentari.save();
		
		tipovi.save();
		k.save();
		manifestacije.save();
		karte.save();
		komentari.save();
		
	}

}
