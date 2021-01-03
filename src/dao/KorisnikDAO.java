package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Karta;
import beans.Korisnik;
import beans.Kupac;
import beans.Manifestacija;
import beans.Prodavac;
import beans.TipKupca;

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
	
	
	
	public ArrayList<Karta> vratiKarteProdavcu(String username, KarteDAO karteManager){
		ArrayList<Karta> karte = new ArrayList<Karta>();
		Prodavac p = prodavciMap.get(username);
		if(p == null)
			return karte;
		for(Manifestacija m: p.getManifestacije()) {
			karte.addAll(karteManager.NeobrisaneKarteManifestacija(m.getNaziv()));
		}
		return karte;
		
	}
	
	public ArrayList<Karta> vratiKarteKupcu(String username, KarteDAO karteManager){
		ArrayList<Karta> karte = new ArrayList<Karta>();
		
		
		
		return karte;
		
	}
	
	public ArrayList<Korisnik> vratiNeobrisaneKorisnike(){
		ArrayList<Korisnik> neobrisaniKorisnici = new ArrayList<Korisnik>();
		for(Korisnik k : korisnici) {
			if(k instanceof Kupac) {
				if(!((Kupac) k).isObrisan())
					neobrisaniKorisnici.add(k);	
			}else if(k instanceof Prodavac) {
				if(!((Prodavac) k).isObrisan())
					neobrisaniKorisnici.add(k);				
				
			}else {
				neobrisaniKorisnici.add(k);
			}
		}
		
		
		return neobrisaniKorisnici;
	}
	
	public void load() {
		//putanja je navedena rucno, kasnije vidjeti sa mrsuljom gdje i kako
		String path = "data\\korisnici.csv";
		
		BufferedReader bf = null;
		
		
		String currentLine = null;
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
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
						}
						if(tokens[10].contentEquals("true")) {
							k.setObrisan(true);
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
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(Korisnik k: korisnici)
				System.out.println(k);
			
			
			
		
	}
	
	public void save() {
		String path = "data\\korisnici.csv";
		//rucno dodavanje korisnika
		//pitanje: da li LocalDate treba da bude ili LocalDateTime -> ja sam stavio na localDate
		String input = "01/07/2020 11:15:02";
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//		Korisnik k1;
//		try {
//			k1 = new Korisnik("admin","admin","marko","markovic","MUSKI",formater.parse("22/12/2001 22:21:01"),"ADMIN");
//			Korisnik k2 = new Prodavac("nikolas","nidzo","nikola","stevanovic","MUSKI",formater.parse("10/10/1999 22:21:01"),"PRODAVAC");
//			Korisnik k3 = new Prodavac("andrija","andro","andrija","vojnvoic","MUSKI",formater.parse("15/09/1995 22:21:01"),"PRODAVAC");
//			Korisnik k4 = new Kupac("Jovana","jovanica1999","jovana","jevtic","ZENSKI",formater.parse("22/12/2001 22:21:01"),"KUPAC", (TipKupca) tipovi.getKupciMap().get("Pocetni kupac"),24);
//			Korisnik k5 = new Kupac("bojan","caki","bojan","Cakar","MUSKI",formater.parse("22/12/2001 22:21:01"),"KUPAC",(TipKupca) tipovi.getKupciMap().get("Nepostojeci kupac"), 0);
//			korisnici.add(k1);
//			korisnici.add(k2);
//			korisnici.add(k3);
//			korisnici.add(k4);
//			korisnici.add(k5);
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		
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
