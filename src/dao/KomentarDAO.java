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
import beans.Prodavac;

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
	
	public int getNewId() {
		int max = 0;
		for(Komentar k : komentariList) {
			if(k.getId() >= max) {
				max = k.getId();
			}
		}
		max++;
		return max;
	}
	
	public ArrayList<Komentar> vratiNeobrisaneKomentare(){
		
		ArrayList<Komentar> neobrisaniKomentari = new ArrayList<Komentar>();
		for(Komentar k: komentariList) {
			if(!k.isObrisan())
				neobrisaniKomentari.add(k);
				
		}
		return neobrisaniKomentari;
	}
	
	public ArrayList<Komentar> vratiKomentareManifestacija(int idMan, boolean odobreni){
		ArrayList<Komentar> komentari = new ArrayList<Komentar>();
		for(Komentar k: komentariList) {
			if(odobreni) {
				if(k.getManifestacija().getId() == idMan && !k.isObrisan() && k.isOdobren() == true) {
					komentari.add(k);
				}
			}else {
				if(k.getManifestacija().getId() == idMan && !k.isObrisan()) {
					komentari.add(k);
				}
			}
		}
		return komentari;
	}
	
	public ArrayList<Komentar> vratiKomentareZaProdavca(String username){
		ArrayList<Komentar> komentari = new ArrayList<Komentar>();
		Prodavac p = korisnici.getProdavciMap().get(username);
		for(Manifestacija m : p.getManifestacije()) {
			ArrayList<Komentar> temp = new ArrayList<Komentar>();
			temp = vratiKomentareManifestacija(m.getId(), false);
			for(Komentar kom : temp) {
				komentari.add(kom);
			}
		}
		return komentari;
	}
	
	public ArrayList<Komentar> approveAndReturn(int id, String username){
		for(Komentar kom : komentariList) {
			if(kom.getId() == id) {
				kom.setOdobren(true);
				break;
			}
		}
		save();
		return vratiKomentareZaProdavca(username);
	}
	
	
	public ArrayList<Komentar> deleteAndReturn(int id){
		for(Komentar kom : komentariList) {
			if(kom.getId() == id) {
				kom.setObrisan(true);
				break;
			}
		}
		save();
		return vratiNeobrisaneKomentare();
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
				Komentar k = new Komentar((Korisnik)korisnici.getKorisniciMap().get(tokens[0]),(Manifestacija) manifestacije.getManifestacijaMap().get(Integer.parseInt(tokens[1])), tokens[2], Double.parseDouble(tokens[3]));
				if(tokens[4].contentEquals("true"))
					k.setOdobren(true);
				if(tokens[5].contentEquals("true"))
					k.setObrisan(true);
				k.setId(Integer.parseInt(tokens[6]));
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
				out.print(k.isObrisan());
				out.print(";");
				out.println(k.getId());
			}
			out.flush();
			out.close();
		} catch (IOException e) {
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
