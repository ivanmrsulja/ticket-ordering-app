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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Karta;
import beans.Kupac;
import beans.Manifestacija;
import beans.ShoppingCartItem;
import search.KarteSearchParams;
import sorter.SortirajKartePoCijeniRastuce;
import sorter.SortirajKartePoManifestacijiRastuce;
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
	
	public  ArrayList<Karta> vratiKarteZaManifestaciju(String idMan){
		ArrayList<Karta> karteMenifestacija = new ArrayList<Karta>();
		for(Karta k: karteList) {
			if(k.getIdManifestacije().equals(idMan)) {
				karteMenifestacija.add(k);
		}		
			}
		
		return karteMenifestacija;
	}
	
	public  ArrayList<Karta> neobrisaneKarteManifestacija(String idMan){
		ArrayList<Karta> karteMenifestacija = new ArrayList<Karta>();
		for(Karta k: karteList) {
			if(k.getIdManifestacije().equals(idMan) && (!k.isObrisana()) && k.getStatus().equals("REZERVISANA")) {
				karteMenifestacija.add(k);
		}		
			}
		
		return karteMenifestacija;
	}
	
	
	public ArrayList<Karta> getNeobrisaneKarte(){
		ArrayList<Karta> karte = new ArrayList<Karta>();
		for(Karta k: karteList) {
			if(!k.isObrisana()) {
				karte.add(k);
			}
		}
		return karte;
	}
	
	public void save() {
		String path = "data/karte.csv";
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
		karteList.clear();
		karteMap.clear();
		
		for(Kupac k : korisnici.getKupci()) {
			k.getSveKarte().clear();
		}
		
		String path = "data/karte.csv";
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
				k.setImePrezime(kupac.getIme() + " " + kupac.getPrezime());
				if(tokens[7].contentEquals("true")) {
					k.setObrisana(true);
				}
				k.setImePrezime(kupac.getIme()+" "+kupac.getPrezime());
				kupac.addKarta(k);
				karteList.add(k);
				karteMap.put(k.getId(),k);
				for(Manifestacija m : manifestacije.getManifestacijaList()) {
					if(m.getNaziv().equals(k.getIdManifestacije())) {
						k.setDatum(m.getDatumOdrzavanja());
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public List<Karta> searchFilterSort(KarteSearchParams ksp){
		List<Karta> ret = new ArrayList<Karta>();
		
		for(Karta k : getNeobrisaneKarte()) {
			if(k.getIdManifestacije().toUpperCase().contains(ksp.getNaziv().toUpperCase()) && k.getCena() <= ksp.getEndPrice() && k.getCena() >= ksp.getStartPrice() && k.getDatum() >= ksp.getStartDate() && k.getDatum() <= ksp.getEndDate()) {
				
				if(!ksp.getTip().equals("SVE")) {
					if(!k.getTip().equals(ksp.getTip())) {
						continue;
					}
				}
				
				if(!ksp.getStatus().equals("SVE")) {
					if(!k.getStatus().equals(ksp.getStatus())) {
						continue;
					}
				}
				ret.add(k);
			}
		}
		
		switch(ksp.getKriterijumSortiranja()) {
		case "NAZIV":
			Collections.sort(ret, new SortirajKartePoManifestacijiRastuce());
			break;
		case "DATUM":
			Collections.sort(ret, new Comparator<Karta>() {
				
				@Override
				public int compare(Karta k1, Karta k2) {
					return Long.compare(k1.getDatum(), k2.getDatum());
				}
			});
			
			break;
		case "CENA":
			Collections.sort(ret, new SortirajKartePoCijeniRastuce());
			break;
		}
		
		if(ksp.isOpadajuce()) {
			Collections.reverse(ret);
		}
		
		return ret;
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

	public String getNewID() {
		boolean go = true;
		String guess = null;
		while(go) {
			guess = getAlphaNumericID(10);
			go = false;
			for(Karta k : karteList) {
				if (k.getId().equals(guess)) {
					go = true;
				}
			}
		}
		return guess;
	}
	
	public void makeTickets(ArrayList<ShoppingCartItem> sc, Kupac kupac) {
		for(ShoppingCartItem item : sc) {
			Manifestacija m = (Manifestacija) manifestacije.getManifestacijaMap().get(item.getIdManifestacije());
			for(int i = 0; i < item.getKolicina(); i++) {
				if(m.getBrojMesta() == 0) {
					break;
				}
				Karta nova = new Karta(getNewID(), m.getBrojMesta(), kupac.getUsername(), "REZERVISANA", item.getCijena(), item.getTipKarte(), m.getNaziv());
				nova.setDatum(m.getDatumOdrzavanja());
				nova.setImePrezime(kupac.getIme() + " " + kupac.getPrezime());
				kupac.setBrojBodova((int)(kupac.getBrojBodova() + ((item.getCijena()/1000)*133)));
				m.setBrojMesta(m.getBrojMesta() - 1);
				karteMap.put(nova.getId(), nova);
				karteList.add(nova);
				kupac.addKarta(nova);
			}
		}
		korisnici.updateType(kupac);
		save();
		manifestacije.save();
		korisnici.save();
	}
	
	public ArrayList<Karta> vratiKarte(){
		ArrayList<Karta> karte = new ArrayList<Karta>();
		for(Karta k: karteList) {
			
			if(k.isObrisana())
				continue;
			else
				karte.add(k);
		}
		
	
		return karte;
	}

	public String getAlphaNumericID(int n) 
    { 
  
        // chose a Character random from this String 
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz"; 
  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
  
        for (int i = 0; i < n; i++) { 
  
            // generate a random number between 
            // 0 to AlphaNumericString variable length 
            int index 
                = (int)(AlphaNumericString.length() 
                        * Math.random()); 
  
            // add Character one by one in end of sb 
            sb.append(AlphaNumericString 
                          .charAt(index)); 
        } 
  
        return sb.toString(); 
    } 
	
	
	@Override
	public String toString() {
		return "KarteDAO [karteList=" + karteList + ", karteMap=" + karteMap + ", manifestacije=" + manifestacije
				+ ", korisnici=" + korisnici + "]";
	}
	
	
}
