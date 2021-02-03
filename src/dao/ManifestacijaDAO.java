package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Lokacija;
import beans.Manifestacija;
import search.ManifestacijaSearchParams;
import sorter.SortirajManifestacijuPoCijeniRastuce;
import sorter.SortirajManifestacijuPoDatumuRastuce;
import sorter.SortirajManifestacijuPoLokacijiRastuce;
import sorter.SortirajManifestacijuPoNazivuRastuce;

public class ManifestacijaDAO {

	private List<Manifestacija> manifestacijaList;
	private HashMap<Integer, Manifestacija> manifestacijaMap;
	private LokacijaDAO lokacije;
	private KorisnikDAO korisnici;
	
	public ManifestacijaDAO() {
		manifestacijaList = new ArrayList<Manifestacija>();
		manifestacijaMap = new HashMap<Integer, Manifestacija>();
	}
	
	public ManifestacijaDAO(LokacijaDAO l, KorisnikDAO k) {
		this();
		lokacije = l;
		korisnici = k;
	}
	
	
	public void load() {
		String path = "data/manifestacije.csv";
		try {
			SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			BufferedReader bf = new BufferedReader(new FileReader(path));
			String currentLine;
			
			while((currentLine= bf.readLine())!= null) {
				if(currentLine.trim().equals(""))
					continue;
				String[] tokens = currentLine.split(";");
				Lokacija l = (Lokacija) lokacije.getLokacijeMap().get(tokens[7]);
				
				Manifestacija m =  new Manifestacija(Integer.parseInt(tokens[0]), tokens[1],tokens[2],Long.parseLong(tokens[3]),Integer.parseInt(tokens[4]),Double.parseDouble(tokens[5]),tokens[6],(Lokacija) lokacije.getLokacijeMap().get(tokens[7]),tokens[8]);
				m.setIdProdavca(tokens[10]);
				
				korisnici.getProdavciMap().get(m.getIdProdavca()).addManifestacija(m);
				
				if(tokens[9].contentEquals("true")) {
					m.setObrisana(true);
				}
				manifestacijaList.add(m);
				manifestacijaMap.put(m.getId(), m);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public void save() {
	SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//		try {
//			Manifestacija m1 = new Manifestacija(0,"Manifestacija 1", "TIP MAFINESTACIJE 1",formater.parse("22/12/2001 22:21:01"),55, 85,"U PRODAJU", (Lokacija) lokacije.getLokacijeMap().get(""),"putanjaSlike" );
//			Manifestacija m2 = new Manifestacija(1,"Manifestacija 2", "TIP MAFINESTACIJE 2",formater.parse("22/12/2001 22:21:01"),55, 85,"RASPRODANA", (Lokacija) lokacije.getLokacijeMap().get("Bulevar oslobodjenja 96, Novi Sad 21000"),"putanjaSlike" );
//			Manifestacija m3 = new Manifestacija(2,"Manifestacija 3", "TIP MAFINESTACIJE 3",formater.parse("22/12/2001 22:21:01"),55, 85,"PRETPRODAJA", (Lokacija) lokacije.getLokacijeMap().get(""),"putanjaSlike" );
//			manifestacijaList.add(m1);
//			manifestacijaList.add(m2);
//			manifestacijaList.add(m3);
//		
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
		String path = "data/manifestacije.csv";
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
			
			for(Manifestacija m: manifestacijaList) {
				out.print(m.getId());
				out.print(";");
				out.print(m.getNaziv());
				out.print(";");
				out.print(m.getTipManifestacije());
				out.print(";");
				out.print(m.getDatumOdrzavanja());
				out.print(";");
				out.print(m.getBrojMesta());
				out.print(";");
				out.print(m.getCenaRegular());
				out.print(";");
				out.print(m.getStatus());
				
				out.print(";");
				if(m.getLokacija() != null)
					out.print(m.getLokacija().getAdresa());
				else
					out.print(" ");
				out.print(";");
				out.print(m.getSlika());
				out.print(";");
				out.print(m.isObrisana());
				out.print(";");
				out.print(m.getIdProdavca());
				out.println();
			}
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public int makeID() {
		int max = 0;
		for(Manifestacija m : manifestacijaList) {
			if(m.getId() >= max) {
				max = m.getId();
			}
		}
		return max+1;
	}
	
	public boolean checkLocation(Lokacija lo, long date) {
		for(Manifestacija m : manifestacijaList) {
			if(m.getDatumOdrzavanja() == date && ((m.getLokacija().getGeografskaSirina() == lo.getGeografskaSirina() && m.getLokacija().getGeografskaDuzina() == lo.getGeografskaDuzina()) || m.getLokacija().getAdresa().equals(lo.getAdresa()))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean dodajNovu(Manifestacija nova) {
		for(Manifestacija m : manifestacijaList) {
			if(m.getNaziv().equals(nova.getNaziv())) {
				System.out.println(m.getNaziv());
				return false;
			}
		}
		manifestacijaList.add(nova);
		manifestacijaMap.put(nova.getId(), nova);
		lokacije.getLokacijeList().add(nova.getLokacija());
		lokacije.getLokacijeMap().put(nova.getLokacija().getAdresa(), nova.getLokacija());
		lokacije.save();
		save();
		return true;
	}
	
	public List<Manifestacija> vratiAktuelne(){
		ArrayList<Manifestacija> ret = new ArrayList<Manifestacija>();
		for(Manifestacija m : manifestacijaList) {
			if(!m.isObrisana()) {
				ret.add(m);
			}
		}
		Collections.reverse(ret);
		return ret;
	}
	
	public List<Manifestacija> searchFilterSort(ManifestacijaSearchParams msp){
		List<Manifestacija> ret = new ArrayList<Manifestacija>();
		
		for(Manifestacija m : vratiAktuelne()) {
			if(m.getNaziv().toUpperCase().contains(msp.getNaziv().toUpperCase()) && m.getLokacija().getAdresa().toUpperCase().contains(msp.getLokacija().toUpperCase()) && m.getCenaRegular() <= msp.getEndPrice() && m.getCenaRegular() >= msp.getStartPrice() && m.getDatumOdrzavanja() >= msp.getStartDate() && m.getDatumOdrzavanja() <= msp.getEndDate()) {
				if(!msp.isRasprodata()) {
					if(m.getBrojMesta() == 0) {
						continue;
					}
				}
				if(!msp.getTip().equals("SVE")) {
					if(!m.getTipManifestacije().equals(msp.getTip())) {
						continue;
					}
				}
				ret.add(m);
			}
		}
		
		switch(msp.getKriterijumSortiranja()) {
		case "NAZIV":
			Collections.sort(ret, new SortirajManifestacijuPoNazivuRastuce());
			break;
		case "DATUM":
			Collections.sort(ret, new SortirajManifestacijuPoDatumuRastuce());
			break;
		case "CENA":
			Collections.sort(ret, new SortirajManifestacijuPoCijeniRastuce());
			break;
		case "LOKACIJA":
			Collections.sort(ret, new SortirajManifestacijuPoLokacijiRastuce());
			break;
		}
		
		if(msp.isOpadajuce()) {
			Collections.reverse(ret);
		}
		
		return ret;
	}

	public List<Manifestacija> getManifestacijaList() {
		return manifestacijaList;
	}

	public Map getManifestacijaMap() {
		return manifestacijaMap;
	}

	public LokacijaDAO getLokacije() {
		return lokacije;
	}

	public void setManifestacijaList(List<Manifestacija> manifestacijaList) {
		this.manifestacijaList = manifestacijaList;
	}

	public void setManifestacijaMap(HashMap<Integer, Manifestacija> manifestacijaMap) {
		this.manifestacijaMap = manifestacijaMap;
	}

	public void setLokacije(LokacijaDAO lokacije) {
		this.lokacije = lokacije;
	}

	@Override
	public String toString() {
		return "ManifestacijaDAO [manifestacijaList=" + manifestacijaList + ", manifestacijaMap=" + manifestacijaMap
				+ ", lokacije=" + lokacije + "]";
	}
	
	
}
