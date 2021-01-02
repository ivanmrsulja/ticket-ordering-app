package dao;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Karta;
import beans.Kupac;
import beans.Manifestacija;
public class KarteDAO {

	
	private List<Karta> karteList;
	private Map karteMap;
	private ManifestacijaDAO manifestacije;
	private KorisnikDAO korisnici;
	public KarteDAO() {
		karteList =  new ArrayList<Karta>();
		karteMap = new HashMap<String, Karta>();
	}
	
	
	public KarteDAO(ManifestacijaDAO manifestacije, KorisnikDAO korisnici) {
		this();
		this.manifestacije = manifestacije;
		this.korisnici = korisnici;
	}
	
	
	public void save() {
//		Karta k1 = new Karta("000",80,(Kupac) korisnici.getKorisniciMap().get("bojan"),"U PRODAJI",1000, "TIP KARTE 1",(Manifestacija) manifestacije.getManifestacijaMap().get(2));
//		Karta k2 = new Karta("001",110,(Kupac) korisnici.getKorisniciMap().get("Jovana"),"U PRODAJI",1000, "TIP KARTE 1",(Manifestacija) manifestacije.getManifestacijaMap().get(1));
//		Karta k3 = new Karta("002",60,(Kupac) korisnici.getKorisniciMap().get("Jovana"),"U PRODAJI",1000, "TIP KARTE 1",(Manifestacija) manifestacije.getManifestacijaMap().get(0));
		String path = "data//karte.csv";
//		karteList.add(k1);
//		karteList.add(k2);
//		karteList.add(k3);
		PrintWriter out;
	
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
			
			for(Karta k: karteList) {
				out.print(k.getId());
				out.print(";");
				out.print(k.getBrojMesta());
				out.print(";");
				out.print(k.getIdKupca());
				out.print(";");
				out.print(k.getStatus());
				out.print(";");
				out.print(k.getCena());
				out.print(";");
				out.print(k.getTip());
				out.print(";");
				out.print(k.getIdManifestacije());
				out.print(";");
				out.print(k.isObrisana());
				out.println();

			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void load() {
		String path = "data//karte.csv";
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(path));
			String currentLine;
			
			while((currentLine= bf.readLine())!= null) {
				if(currentLine.trim().equals(""))
					continue;
				String[] tokens = currentLine.split(";");
				Kupac kupac = korisnici.getKupciMap().get(tokens[2]);
				Karta k  = new Karta(tokens[0],Integer.parseInt(tokens[1]), kupac.getUsername(), tokens[3], Double.parseDouble(tokens[4]), tokens[5], tokens[6]);
				if(tokens[7].contentEquals("true")) {
					k.setObrisana(true);
				}
				kupac.addKarta(k);
				karteList.add(k);
				karteMap.put(k.getId(),k);
				
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public Collection<Karta> getKarteList() {
		return karteList;
	}


	public void setKarteList(List<Karta> karteList) {
		this.karteList = karteList;
	}


	public Map getKarteMap() {
		return karteMap;
	}


	public void setKarteMap(Map karteMap) {
		this.karteMap = karteMap;
	}


	public ManifestacijaDAO getManifestacije() {
		return manifestacije;
	}


	public void setManifestacije(ManifestacijaDAO manifestacije) {
		this.manifestacije = manifestacije;
	}


	public KorisnikDAO getKorisnici() {
		return korisnici;
	}


	public void setKorisnici(KorisnikDAO korisnici) {
		this.korisnici = korisnici;
	}


	@Override
	public String toString() {
		return "KarteDAO [karteList=" + karteList + ", karteMap=" + karteMap + ", manifestacije=" + manifestacije
				+ ", korisnici=" + korisnici + "]";
	}
	
	
}
