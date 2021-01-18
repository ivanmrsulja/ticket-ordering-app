package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import beans.Karta;
import beans.Korisnik;
import beans.Kupac;
import beans.Manifestacija;
import beans.Prodavac;
import beans.TipKupca;
import search.KorisniciSearchParams;

public class KorisnikDAO {
	private List<Korisnik> korisnici;
	private List<Kupac> kupci;
	private List<Prodavac> prodavci;
	private HashMap<String, Korisnik> korisniciMap;
	private HashMap<String, Kupac> kupciMap;
	private HashMap<String, Prodavac> prodavciMap;
	private TipKupcaDAO tipovi;
	
	public KorisnikDAO() {
		korisnici = new ArrayList<Korisnik>();
		kupci = new ArrayList<Kupac>();
		prodavci = new ArrayList<Prodavac>();
		korisniciMap = new HashMap<String, Korisnik>();
		kupciMap = new HashMap<String, Kupac>();
		prodavciMap = new HashMap<String, Prodavac>();
	}
	
	public KorisnikDAO(TipKupcaDAO t) {
		this();
		tipovi = t;
	}
	
	public ArrayList<Kupac> vratiNeobrisaneKupce(){
		ArrayList<Kupac> neobrisaniKupci = new ArrayList<Kupac>();
		for(Kupac k: kupci) {
			if(!k.isObrisan())
				neobrisaniKupci.add(k);
		}
		
		return neobrisaniKupci;
	}
	
	
	public ArrayList<Prodavac> vratiNeobrisaneProdavce(){
		ArrayList<Prodavac> neobrisaniProdavci = new ArrayList<Prodavac>();
		for(Prodavac p: prodavci) {
			if(!p.isObrisan())
				neobrisaniProdavci.add(p);
		}
		
		return neobrisaniProdavci;
	}
	
	public void updateType(Kupac ku) {
		TipKupca t = null;
		for(TipKupca tip : tipovi.getTipoviKupca()) {
			if(tip.getBodovi() <= ku.getBrojBodova()) {
				t = tip;
			}
		}
		if(t == null) {
			t = (TipKupca) tipovi.getKupciMap().get("NOVI");
		}
		ku.setTip(t);
	}
	
	public ArrayList<Karta> vratiKarteProdavcu(String username, KarteDAO karteManager){
		ArrayList<Karta> karte = new ArrayList<Karta>();
		Prodavac p = prodavciMap.get(username);
		if(p == null)
			return karte;
		System.out.println(p.getManifestacije());
		for(Manifestacija m: p.getManifestacije()) {
			karte.addAll(karteManager.neobrisaneKarteManifestacija(m.getNaziv()));
		}
		return karte;
		
	}
	
	public ArrayList<Korisnik> vratiNeobrisaneKorisnike(){
		ArrayList<Korisnik> neobrisaniKorisnici = new ArrayList<Korisnik>();
		for(Kupac k : kupci) {
			if(!k.isObrisan()) {
				neobrisaniKorisnici.add(korisniciMap.get(k.getUsername()));
			}
		}
		for(Prodavac p : prodavci) {
			if(!p.isObrisan()) {
				neobrisaniKorisnici.add(korisniciMap.get(p.getUsername()));
			}
		}
		
		return neobrisaniKorisnici;
	}
	
	public void load(){
		String path = "data/korisnici.csv";
		
		BufferedReader bf = null;
		
		String currentLine = null;
			try {
				bf = new BufferedReader(new FileReader(path));
				while((currentLine = bf.readLine()) != null) {
					String[] tokens = currentLine.split(";");
					switch(tokens[6]) {
					case "ADMIN":{
						Korisnik k;
						k = new Korisnik(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Long.parseLong(tokens[5]),tokens[6]);
						korisnici.add(k);
						korisniciMap.put(k.getUsername(),k);
						
						break;
					}
					case "PRODAVAC":{
						Korisnik k;
						Prodavac pr;
						pr = new Prodavac(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Long.parseLong(tokens[5]),tokens[6]);
						k = new Korisnik(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Long.parseLong(tokens[5]),tokens[6]);
						if (tokens[7].contentEquals("true")) {
							pr.setObrisan(true);
							k.setObrisan(true);
						}
						if (tokens[8].contentEquals("true")) {
							pr.setBanovan(true);
							k.setBanovan(true);
						}
						korisnici.add(k);
						prodavci.add(pr);
						korisniciMap.put(k.getUsername(),k);
						prodavciMap.put(k.getUsername(), pr);
						break;
					}
					case "KUPAC":{
						String[] members = tokens[8].split("-");
						
						System.out.println(members.length);
						Kupac k = null;
						Korisnik ko;
						k = new Kupac(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Long.parseLong(tokens[5]), tokens[6],(TipKupca) tipovi.getKupciMap().get(tokens[9]),Integer.parseInt(tokens[8]));
						ko = new Korisnik(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Long.parseLong(tokens[5]),tokens[6]);
						if(!tokens[7].equals("false")) {
							k.setBanovan(true);
							ko.setBanovan(true);
						}
						if(tokens[10].contentEquals("true")) {
							k.setObrisan(true);
							ko.setObrisan(true);
						}
						
						korisnici.add(ko);
						kupci.add((Kupac) k);
						korisniciMap.put(ko.getUsername(),ko);
						kupciMap.put(k.getUsername(), k);
						
						break;
					}
					default:
						break;
					
					}
					
				}
				bf.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			currentLine = null;
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream("data/infractions.csv")));
				try {
					while((currentLine = br.readLine()) != null) {
						String[] tokens = currentLine.split(";");
						long date = Long.parseLong(tokens[1]);
						if(date > (new Date()).getTime() - 2629800000l) {
							korisniciMap.get(tokens[0]).setBrojOtkazivanja(korisniciMap.get(tokens[0]).getBrojOtkazivanja() + 1);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			
			for(Korisnik k: korisnici)
				System.out.println(k);
		
	}
	
	public void save() {
		String path = "data/korisnici.csv";
		
		try {
			
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
			
			for(Korisnik k: korisnici) {
				out.print(k.getUsername());
				out.print(";");
				out.print(k.getPassword());
				out.print(";");
				out.print(k.getIme());
				out.print(";");
				out.print(k.getPrezime());
				out.print(";");
				out.print(k.getPol());
				out.print(";");
				out.print(k.getDatumRodjenja());
				out.print(";");
				out.print(k.getUloga());
				switch(k.getUloga()) {
					case "ADMIN":{
						out.println();
						break;
					}
						
					case "PRODAVAC":
					{
						out.print(";");
						out.print(prodavciMap.get(k.getUsername()).isObrisan());
						out.print(";");
						out.print(prodavciMap.get(k.getUsername()).isBanovan());
						out.println();
						break;
					}
					case "KUPAC":{
						out.print(";");
						out.print(kupciMap.get(k.getUsername()).isBanovan());
						out.print(";");
						out.print(kupciMap.get(k.getUsername()).getBrojBodova());
						if(kupciMap.get(k.getUsername()).getTip() == null) {
							out.print(";");
							out.print("NOVI");
							out.print(";");
							out.print(kupciMap.get(k.getUsername()).isObrisan());
							out.println();
							break;
						}
						out.print(";");
						out.print(kupciMap.get(k.getUsername()).getTip().getImeTipa());
						out.print(";");
						out.print(kupciMap.get(k.getUsername()).isObrisan());
						out.println();
						break;
						
					}
						
					default: 
						break;
				}
			}
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addNewBuyer(Korisnik ko, Kupac ku) {
		korisnici.add(ko);
		kupci.add(ku);
		korisniciMap.put(ko.getUsername(), ko);
		kupciMap.put(ku.getUsername(), ku);
		save();
	}
	
	public void addNewSeller(Korisnik ko, Prodavac p) {
		korisnici.add(ko);
		prodavci.add(p);
		korisniciMap.put(ko.getUsername(), ko);
		prodavciMap.put(p.getUsername(), p);
		save();
	}

	public List<Korisnik> getKorisnici() {
		return korisnici;
	}

	public List<Kupac> getKupci() {
		return kupci;
	}

	public List<Prodavac> getProdavci() {
		return prodavci;
	}

	public TipKupcaDAO getTipovi() {
		return tipovi;
	}

	public void setKorisnici(List<Korisnik> korisnici) {
		this.korisnici = korisnici;
	}

	public void setKupci(List<Kupac> kupci) {
		this.kupci = kupci;
	}
	
	public HashMap<String, Kupac> getKupciMap(){
		return this.kupciMap;
	}

	public HashMap<String, Korisnik> getKorisniciMap() {
		return korisniciMap;
	}

	public void setKorisniciMap(HashMap<String, Korisnik> korisniciMap) {
		this.korisniciMap = korisniciMap;
	}

	public void setProdavci(List<Prodavac> prodavci) {
		this.prodavci = prodavci;
	}

	public void setTipovi(TipKupcaDAO tipovi) {
		this.tipovi = tipovi;
	}

	public HashMap<String, Prodavac> getProdavciMap() {
		return prodavciMap;
	}

	public void setProdavciMap(HashMap<String, Prodavac> prodavciMap) {
		this.prodavciMap = prodavciMap;
	}

	public void setKupciMap(HashMap<String, Kupac> kupciMap) {
		this.kupciMap = kupciMap;
	}
	
	public List<Korisnik> searchFilterSort(KorisniciSearchParams ksp) {
		List<Korisnik> ret = new ArrayList<Korisnik>();
		
		for(Korisnik k : korisnici) {
			if(k.getIme().toUpperCase().contains(ksp.getIme().toUpperCase()) && k.getPrezime().toUpperCase().contains(ksp.getPrezime().toUpperCase()) && k.getUsername().toUpperCase().contains(ksp.getUsername().toUpperCase())) {
				if(!ksp.getUloga().equals("SVE")) {
					if(!k.getUloga().equals(ksp.getUloga())) {
						continue;
					}
				}
				
				if(!ksp.getTip().equals("SVE")) {
					if(!k.getUloga().equals("KUPAC")) {
						continue;
					}
					Kupac kupac = getKupciMap().get(k.getUsername());
					if(!kupac.getTip().getImeTipa().equals(ksp.getTip())) {
						continue;
					}
				}
				ret.add(k);
			}
		}
		
		switch(ksp.getKriterijumSortiranja()) {
		case "IME":
			Collections.sort(ret, new Comparator<Korisnik>() {
							
				@Override
				public int compare(Korisnik k1, Korisnik k2) {
					return k1.getIme().compareTo(k2.getIme());
				}
			});
			break;
		case "PREZIME":
			Collections.sort(ret, new Comparator<Korisnik>() {
				
				@Override
				public int compare(Korisnik k1, Korisnik k2) {
					return k1.getPrezime().compareTo(k2.getPrezime());
				}
			});
			break;
		case "USERNAME":
			Collections.sort(ret, new Comparator<Korisnik>() {
				
				@Override
				public int compare(Korisnik k1, Korisnik k2) {
					return k1.getUsername().compareTo(k2.getUsername());
				}
			});
			break;
		case "BODOVI":
			List<Kupac> temp = new ArrayList<Kupac>();
			Comparator<Kupac> comparator = new Comparator<Kupac>() {
				@Override
				public int compare(Kupac k1, Kupac k2) {
					return Integer.compare(k1.getBrojBodova(), k2.getBrojBodova());
				}
			};
			for(String username : ret.stream().filter(k -> k.getUloga().equals("KUPAC")).map(k -> k.getUsername()).collect(Collectors.toList())) {
				temp.add(getKupciMap().get(username));
			}
			Collections.sort(temp, comparator);
			ret.clear();
			for(Kupac ku : temp) {
				ret.add(getKorisniciMap().get(ku.getUsername()));
			}
			break;
		}
		
		if(ksp.isOpadajuce()) {
			Collections.reverse(ret);
		}
		System.out.println(ret.size());
		return ret;
	}

	public Korisnik find(String username, String password) {
		try {
			Korisnik k = (Korisnik) korisniciMap.get(username);
			if(k.getPassword().equals(password)) {
				return k;
			}else {
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}
	
}
