package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Komentar;
import beans.Korisnik;
import beans.Manifestacija;

public class KomentarDAO {

	
	private List<Komentar> komentariList;
	private Map komentariMap;
	private KorisnikDAO korisnici;
	private ManifestacijaDAO manifestacije;
	public KomentarDAO() {
		komentariList=  new ArrayList<Komentar>();
		komentariMap = new HashMap<String, Komentar>();
		
	}
	
	public KomentarDAO(KorisnikDAO korisnici, ManifestacijaDAO manifestacije) {
		this();
		this.korisnici = korisnici;
		this.manifestacije = manifestacije;
		
	}
	
	
	
	public void load() {
		String path = "data//komentari.csv";
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(path));
			String currentLine;
			while((currentLine = bf.readLine()) != null) {
				
				if(currentLine.trim().equals(""))
					continue;
				
				String tokens[] = currentLine.split(";");
				Komentar k = new Komentar((Korisnik)korisnici.getKorisniciMap().get(tokens[0]),(Manifestacija) manifestacije.getManifestacijaMap().get(tokens[1]), tokens[2], Double.parseDouble(tokens[3]));
				if(tokens[4].contentEquals("true"))
					k.setOdobren(true);
				if(tokens[5].contentEquals("true"))
					k.setObrisan(true);
				komentariList.add(k);
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void save() {
		String path = "data//komentari.csv";
//		Komentar k1 = new Komentar((Kupac) korisnici.getKorisniciMap().get("Jovana"),(Manifestacija) manifestacije.getManifestacijaMap().get(0), "Jovanin prvi komentar", 6);
//		Komentar k2 = new Komentar((Kupac) korisnici.getKorisniciMap().get("Jovana"),(Manifestacija) manifestacije.getManifestacijaMap().get(0), "Jovanin drugi komentar", 10);
//		k2.setOdobren(true);
//		Komentar k3 = new Komentar((Kupac) korisnici.getKorisniciMap().get("bojan"),(Manifestacija) manifestacije.getManifestacijaMap().get(0), "Bojnov prvi komentar- tj tekst komentaraa", 6);
//		komentariList.add(k1);
//		komentariList.add(k2);
//		komentariList.add(k3);
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
			for(Komentar k: komentariList) {
				out.print(k.getKupac().getUsername());
				out.print(";");
				out.print(k.getManifestacija().getId());
				out.print(";");
				out.print(k.getTekst());
				out.print(";");
				out.print(k.getOcena());
				out.print(";");
				out.print(k.isOdobren());
				out.print(";");
				out.println(k.isObrisan());
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public List<Komentar> getKomentariList() {
		return komentariList;
	}

	public void setKomentariList(List<Komentar> komentariList) {
		this.komentariList = komentariList;
	}

	public Map getKomentariMap() {
		return komentariMap;
	}

	public void setKomentariMap(Map komentariMap) {
		this.komentariMap = komentariMap;
	}

	public KorisnikDAO getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(KorisnikDAO korisnici) {
		this.korisnici = korisnici;
	}

	public ManifestacijaDAO getManifestacije() {
		return manifestacije;
	}

	public void setManifestacije(ManifestacijaDAO manifestacije) {
		this.manifestacije = manifestacije;
	}

	@Override
	public String toString() {
		return "KomentarDAO [komentariList=" + komentariList + ", komentariMap=" + komentariMap + ", korisnici="
				+ korisnici + ", manifestacije=" + manifestacije + "]";
	}
	
	
}
