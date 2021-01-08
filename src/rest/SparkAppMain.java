package rest;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.File;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import beans.Karta;
import beans.Komentar;
import beans.Korisnik;
import beans.Kupac;
import beans.Manifestacija;
import beans.Prodavac;
import beans.ShoppingCartItem;
import beans.TipKupca;
import dao.KarteDAO;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.LokacijaDAO;
import dao.ManifestacijaDAO;
import dao.TipKupcaDAO;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javaxt.utils.Base64;
import ws.WsHandler;


public class SparkAppMain {

	private static Gson g = new Gson();

	/**
	 * Ključ za potpisivanje JWT tokena.
	 * Biblioteka: https://github.com/jwtk/jjwt
	 */
	static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	static TipKupcaDAO tipovi;
	static KorisnikDAO k;
	static LokacijaDAO lokacije;
	static ManifestacijaDAO manifestacije;
	static KarteDAO karte;
	static KomentarDAO komentari;
	
	public static void loadData() {
		tipovi = new TipKupcaDAO();
		tipovi.load();
		k = new KorisnikDAO(tipovi);
		k.load();
		lokacije  = new LokacijaDAO();
		lokacije.load();
		manifestacije = new ManifestacijaDAO(lokacije, k);
		manifestacije.load();
		karte = new KarteDAO(manifestacije, k);
		karte.load();
		komentari = new KomentarDAO(k,manifestacije);
		komentari.load();
	}
	
	public static void saveData() {
		tipovi.save();
		k.save();
		lokacije.save();
		manifestacije.save();
		karte.save();
		komentari.save();
	}

	public static void main(String[] args) throws Exception {
		port(9090);

		webSocket("/ws", WsHandler.class);

		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		loadData();
		
		/////////////// KORISNICI ///////////////
		
		post("/rest/users/registerUser", (req, res) -> {
			//Popraviti
			Korisnik user = g.fromJson(req.body(), Korisnik.class);
			Korisnik kor = k.find(user.getUsername(), user.getPassword());
			if(kor != null) {
				return "Failed";
			}else {
				System.out.println("Prosao");
				Korisnik newUser = new Korisnik(user.getUsername(), user.getPassword(), user.getIme(), user.getPrezime(), user.getPol(), user.getDatumRodjenja(), "KUPAC");
				
				TipKupca tk = (TipKupca)tipovi.getKupciMap().get("NOVI");
				Kupac newBuyer = new Kupac(user.getUsername(), user.getPassword(), user.getIme(), user.getPrezime(), user.getPol(), user.getDatumRodjenja(), "KUPAC", tk, 0);
				
				k.addNewBuyer(newUser, newBuyer);
				return "Done";
			}
		});
		
		post("/rest/users/registerSeller", (req, res) -> {
			//Popraviti
			Korisnik user = g.fromJson(req.body(), Korisnik.class);
			Korisnik kor = k.find(user.getUsername(), user.getPassword());
			if(kor != null) {
				return "Failed";
			}else {
				System.out.println("Prosao");
				Korisnik newUser = new Korisnik(user.getUsername(), user.getPassword(), user.getIme(), user.getPrezime(), user.getPol(), user.getDatumRodjenja(), "PRODAVAC");
				
				Prodavac newSeller = new Prodavac(user.getUsername(), user.getPassword(), user.getIme(), user.getPrezime(), user.getPol(), user.getDatumRodjenja(), "PRODAVAC");
				System.out.println(newUser);
				System.out.println(newSeller);
				k.addNewSeller(newUser, newSeller);
				return "Done";
			}
		});
		
		post("/rest/users/logUser", (req, res) -> {
			//Popraviti
			Korisnik user = g.fromJson(req.body(), Korisnik.class);
			Korisnik kor = k.find(user.getUsername(), user.getPassword());
			if(kor != null) {
				res.type("application/json");
				switch(kor.getUloga()) {
				case "KUPAC":
						System.out.println(k.getKupciMap().get(kor.getUsername()));
						if(k.getKupciMap().get(kor.getUsername()).isObrisan()) {
							return "Nalog vam je obrisan.";
						}
						if(k.getKupciMap().get(kor.getUsername()).isBanovan()) {
							return "Banovani ste.";
						}
					break;
				case "PRODAVAC":
						System.out.println(k.getKupciMap().get(kor.getUsername()));
						if(k.getProdavciMap().get(kor.getUsername()).isObrisan()) {
							return "Nalog vam je obrisan.";
						}
						if(k.getProdavciMap().get(kor.getUsername()).isBanovan()) {
							return "Banovani ste.";
						}
					break;
				}
				ArrayList<ShoppingCartItem> sc = new ArrayList<ShoppingCartItem>();
				req.session().attribute("currentUser", kor);
				req.session().attribute("cart", sc);
				return "Done";
			}else {
				res.type("application/json");
				return "Failed";
			}
		});
		
		get("/rest/users/logout", (req, res) -> {
			req.session().invalidate();
			return "Done";
		});
		
		get("/rest/users/currentUser", (req, res) -> {
			Korisnik user = (Korisnik)req.session().attribute("currentUser");
			res.type("application/json");
			return g.toJson(user);
		});
		
		put("rest/users/updateUser", (req,res) -> {
			Korisnik user = (Korisnik) req.session().attribute("currentUser");
			System.out.println(user);
			Korisnik updatedUser = (Korisnik) g.fromJson(req.body(), Korisnik.class);
			user.setPassword(updatedUser.getPassword());
			user.setIme(updatedUser.getIme());
			user.setPrezime(updatedUser.getPrezime());
			user.setPol(updatedUser.getPol());
			user.setDatumRodjenja(updatedUser.getDatumRodjenja());
			switch(user.getUloga()) {
				case "KUPAC":
						Kupac kup = k.getKupciMap().get(user.getUsername());
						kup.setPassword(updatedUser.getPassword());
						kup.setIme(updatedUser.getIme());
						kup.setPrezime(updatedUser.getPrezime());
						kup.setPol(updatedUser.getPol());
						kup.setDatumRodjenja(updatedUser.getDatumRodjenja());
						System.out.println(kup);
					break;
				case "PRODAVAC":
					Prodavac prod = k.getProdavciMap().get(user.getUsername());
					prod.setPassword(updatedUser.getPassword());
					prod.setIme(updatedUser.getIme());
					prod.setPrezime(updatedUser.getPrezime());
					prod.setPol(updatedUser.getPol());
					prod.setDatumRodjenja(updatedUser.getDatumRodjenja());
					System.out.println(prod);
					break;
			}
			k.save();
			
			return "Done";
		});
		
		get("/rest/users/all", (req, res) -> {
			res.type("application/json");
			ArrayList<Korisnik> ret = k.vratiNeobrisaneKorisnike();
			return g.toJson(ret);
		});
		
		delete("/rest/users/:username", (req, res) -> {
			String username = req.params("username");
			try {
				k.getKupciMap().get(username).setObrisan(true);
			}catch(Exception e) {
				k.getProdavciMap().get(username).setObrisan(true);
			}
			k.save();
			return "Done";
		});
		
		put("/rest/users/:username", (req, res) -> {
			String username = req.params("username");
			try {
				k.getKupciMap().get(username).setBanovan(!k.getKupciMap().get(username).isBanovan());
				k.getKorisniciMap().get(username).setBanovan(!k.getKorisniciMap().get(username).isBanovan());
			}catch(Exception e) {
				try {
					k.getProdavciMap().get(username).setBanovan(!k.getProdavciMap().get(username).isBanovan());
					k.getKorisniciMap().get(username).setBanovan(!k.getKorisniciMap().get(username).isBanovan());
				}catch(Exception ex){
					return "Korisnik nije nu kupac ni prodavac.";
				}
			}
			k.save();
			if(k.getKorisniciMap().get(username).isBanovan()) {
				return "Korisnik banovan.";
			}else {
				return "Korisnik unbanovan.";
			}
		});
		
		/////////////// MANIFESTACIJE ///////////////
		
		get("/rest/manifestations/all", (req, res) -> {
			res.type("application/json");
			ArrayList<Manifestacija> ret = new ArrayList<Manifestacija>();
			for(Manifestacija m : manifestacije.getManifestacijaList()) {
				if(!m.isObrisana()) {
					ret.add(m);
				}
			}
			return g.toJson(ret);
		});
		
		get("/rest/manifestations/seller", (req, res) -> {
			res.type("application/json");
			ArrayList<Manifestacija> ret = new ArrayList<Manifestacija>();
			Korisnik ko = req.session().attribute("currentUser");
			Prodavac pr = k.getProdavciMap().get(ko.getUsername());
			for(Manifestacija m : pr.getManifestacije()) {
				if(!m.isObrisana()) {
					ret.add(m);
				}
			}
			return g.toJson(ret);
		});
		
		get("/rest/manifestations/welcome", (req, res) -> {
			res.type("application/json");
			return g.toJson(manifestacije.vratiAktuelne());
		});
		
		get("/rest/manifestations/odobri/:id", (req, res) -> {
			String id = req.params("id");
			Manifestacija m = (Manifestacija) manifestacije.getManifestacijaMap().get(Integer.parseInt(id));
			m.setStatus("AKTIVNO");
			manifestacije.save();
			return "Done";
		});
		
		delete("/rest/manifestations/obrisi/:id", (req, res) -> {
			String id = req.params("id");
			Manifestacija m = (Manifestacija) manifestacije.getManifestacijaMap().get(Integer.parseInt(id));
			m.setObrisana(true);
			manifestacije.save();
			return "Done";
		});
		
		post("/rest/manifestations/add", (req, res) -> {
			//Popraviti
			Manifestacija man = g.fromJson(req.body(), Manifestacija.class);
			Korisnik ko = req.session().attribute("currentUser");
			boolean ok = manifestacije.checkLocation(man.getLokacija(), man.getDatumOdrzavanja());
			if (ok) {
				
				int id = manifestacije.makeID();
				
				byte[] imgBytes = Base64.decode(man.getSlika());
				FileOutputStream osf = new FileOutputStream(new File("static\\" + id + ".png"));
				osf.write(imgBytes);
				osf.flush();
				osf.close();
				
				Manifestacija nova = new Manifestacija(id, man.getNaziv(), man.getTipManifestacije(), man.getDatumOdrzavanja(), man.getBrojMesta(), man.getCenaRegular(), "NEAKTIVNO", man.getLokacija(), id + ".png");
				nova.setIdProdavca(ko.getUsername());
				ok = manifestacije.dodajNovu(nova);
				System.out.println(man);
				if(ok) {
					return "Done";
				}
				return "Failed";
			}
			return "Failed";
		});
		
		put("/rest/manifestations/update", (req, res) -> {
			Manifestacija man = g.fromJson(req.body(), Manifestacija.class);
			Korisnik ko = req.session().attribute("currentUser");
			boolean ok = manifestacije.checkLocation(man.getLokacija(), man.getDatumOdrzavanja());
			
			int id = man.getId();
			Manifestacija stara = (Manifestacija) manifestacije.getManifestacijaMap().get(id);
			
			System.out.println(man);
			System.out.println(stara);
			
			ok = true;
			
			for (Manifestacija manif : manifestacije.getManifestacijaList()) {
				if(manif.getDatumOdrzavanja() == man.getDatumOdrzavanja() && stara.getLokacija().getGeografskaDuzina() == man.getLokacija().getGeografskaDuzina() && stara.getLokacija().getGeografskaSirina() == man.getLokacija().getGeografskaSirina() && manif.getId() != man.getId()) {
					ok = false;
				}
			}
			
			if (ok) {
				
				if(!man.getSlika().equals("")) {
					byte[] imgBytes = Base64.decode(man.getSlika());
					FileOutputStream osf = new FileOutputStream(new File("static\\" + id + ".png"));
					osf.write(imgBytes);
					osf.flush();
					osf.close();
				}
				
				stara.setBrojMesta(man.getBrojMesta());
				stara.setDatumOdrzavanja(man.getDatumOdrzavanja());
				stara.setCenaRegular(man.getCenaRegular());
				stara.setTipManifestacije(man.getTipManifestacije());
				stara.setLokacija(man.getLokacija());
				lokacije.getLokacijeList().add(man.getLokacija());
				lokacije.getLokacijeMap().put(man.getLokacija().getAdresa(), man.getLokacija());
				
				manifestacije.save();
				lokacije.save();
				karte.save();
				karte.load();
				
				return "Uspesno azurirano.";
			}
			return "Azuriranje nije uspelo, pokusajte ponovo.";
		});
		
		post("/rest/manifestations/setCurrent", (req, res) -> {
			Manifestacija man = g.fromJson(req.body(), Manifestacija.class);
			req.session().attribute("currentManif", man);
			return "Done";
		});
		
		get("/rest/manifestations/getCurrent", (req, res) -> {
			res.type("application/json");
			Manifestacija m = (Manifestacija) req.session().attribute("currentManif");
			return g.toJson(m);
		});
		
		get("/rest/manifestations/commentable", (req,res) -> {
			Manifestacija m = (Manifestacija) req.session().attribute("currentManif");
			Korisnik ko = (Korisnik) req.session().attribute("currentUser");
			res.type("application/json");
			if (ko == null) {
				return false;
			}
			Kupac ku = k.getKupciMap().get(ko.getUsername());
			
			System.out.println(m.getDatumOdrzavanja() < System.currentTimeMillis());
			
			for(Karta ka : ku.getSveKarte()) {
				if (ka.getIdManifestacije().equals(m.getNaziv()) && ka.getStatus().equals("REZERVISANA") && m.getDatumOdrzavanja() < System.currentTimeMillis() ) {
					return true;
				}
			}
			
			return false;
		});
		
		post("/rest/tickets/addToCart", (req, res) -> {
			ShoppingCartItem sci = null;
			try {
				sci = g.fromJson(req.body(), ShoppingCartItem.class);
			}
			catch(Exception e) {
				return "Unesite kolicinu.";
			}
			Manifestacija man = req.session().attribute("currentManif");
			Korisnik current = req.session().attribute("currentUser");
			Kupac kupac = k.getKupciMap().get(current.getUsername());
			ArrayList<ShoppingCartItem> sc = req.session().attribute("cart");
			
			if(sci.getIdManifestacije() != man.getId() || man.getStatus().equals("NEAKTIVNO") || sci.getKolicina() < 0 || sci.getKolicina() > man.getBrojMesta() || sci.getKolicina() == 0) {
				return "Unesite ispravnu kolicinu.";
			}
			double cijena = 0.0;
			int newId = 0;
			for(ShoppingCartItem it : sc) {
				if(it.getId() >= newId) {
					newId = it.getId() + 1;
				}
			}
			sci.setId(newId);
			switch(sci.getTipKarte()) {
			case "REGULAR":
					cijena = sci.getKolicina() * man.getCenaRegular() * kupac.getTip().getPopust();
				break;
			case "VIP":
					cijena = sci.getKolicina() * man.getCenaRegular() * 2 * kupac.getTip().getPopust();
				break;
			case "FAN_PIT":
					cijena = sci.getKolicina() * man.getCenaRegular() * 4 * kupac.getTip().getPopust();
				break;
			}
			sci.setCijena(cijena);
			sc.add(sci);
			req.session().attribute("cart", sc);
			return "Dodato u korpu.";
		});
		
		get("/rest/tickets/getCart", (req, res) -> {
			res.type("application/json");
			ArrayList<ShoppingCartItem> sc = req.session().attribute("cart");
			return g.toJson(sc);
		});
		
		get("/rest/tickets/forUser", (req, res) -> {
			Korisnik current = req.session().attribute("currentUser");
			Kupac kupac = k.getKupciMap().get(current.getUsername());
			List<Karta> karte = kupac.getSveKarte();
			ArrayList<Karta> ret = new ArrayList<Karta>();
			for(Karta ka : karte) {
				if(ka.getStatus().equals("REZERVISANA")) {
					ret.add(ka);
				}
			}
			res.type("application/json");
			return g.toJson(ret);
		});
		
		get("/rest/tickets/removeFromCart/:id", (req, res) -> {
			res.type("application/json");
			int id = Integer.parseInt(req.params("id"));
			ArrayList<ShoppingCartItem> sc = req.session().attribute("cart");
			int index = -1;
			for(ShoppingCartItem it : sc) {
				index++;
				if(it.getId() == id) {
					break;
				}
			}
			sc.remove(index);
			req.session().attribute("cart", sc);
			System.out.println("aaaa");
			return g.toJson(sc);
		});
		
		get("/rest/tickets/checkout", (req, res) -> {
			ArrayList<ShoppingCartItem> sc = req.session().attribute("cart");
			Korisnik ko = req.session().attribute("currentUser");
			karte.makeTickets(sc, k.getKupciMap().get(ko.getUsername()));
			sc.clear();
			req.session().attribute("cart", sc);
			return "Done";
		});
		
		put("/rest/tickets/odustanak/:id", (req, res) -> {
			Karta ka = (Karta) karte.getKarteMap().get(req.params("id"));
			ka.setStatus("ODUSTANAK");
			Kupac ku = k.getKupciMap().get(ka.getIdKupca());
			ku.setBrojBodova((int)(ku.getBrojBodova() - (ka.getCena()/1000)*133*4));
			for(Manifestacija m : manifestacije.getManifestacijaList()) {
				if(m.getNaziv().equals(ka.getIdManifestacije())) {
					m.setBrojMesta(m.getBrojMesta() + 1);
				}
			}
			k.updateType(ku);
			karte.save();
			manifestacije.save();
			k.save();
			return "Done";
		});
		
		get("/rest/tickets/all", (req, res) -> {
			res.type("application/json");
			ArrayList<Karta> ret = karte.getNeobrisaneKarte();
			System.out.println(ret);
			return g.toJson(ret);
		});
		
		get("/rest/tickets/prodavac", (req, res) -> {
			res.type("application/json");
			Korisnik ko = (Korisnik) req.session().attribute("currentUser");
			//Prodavac p = k.getProdavciMap().get(ko.getUsername()); 
			ArrayList<Karta> ret = k.vratiKarteProdavcu(ko.getUsername(), karte);
			System.out.println(ret);
			return g.toJson(ret);
		});
		
		post("/rest/comments/postComment", (req, res) -> {
			Manifestacija m = (Manifestacija) req.session().attribute("currentManif");
			Korisnik ko = (Korisnik) req.session().attribute("currentUser");
			Kupac ku = k.getKupciMap().get(ko.getUsername());
			System.out.println(ko);
			boolean ok = false;
			
			for(Karta ka : ku.getSveKarte()) {
				if (ka.getIdManifestacije().equals(m.getNaziv()) && ka.getStatus().equals("REZERVISANA") && m.getDatumOdrzavanja() < System.currentTimeMillis()) {
					ok = true;
					break;
				}
			}
			
			if(ok) {
				Komentar novi = g.fromJson(req.body(), Komentar.class);
				
				novi.setKupac(ko);
				novi.setManifestacija(m);
				novi.setTekst(novi.getTekst().replace("\n", "_"));
				
				System.out.println(novi);
				
				if(novi.getOcena() < 1 || novi.getOcena() > 5 || novi.getTekst().contains("_") || novi.getTekst().contains(";") || novi.getTekst().trim().equals("")) {
					return "Doslo je do greske.";
				}
				
				komentari.getKomentariList().add(novi);
				komentari.save();
				return "Komentar je poslat na uvid prodavcu.";
			}else {
				return "Doslo je do greske.";
			}
		});
		
		get("/rest/comments/allComments", (req, res) -> {
			res.type("application/json");
			return  g.toJson(komentari);
		});
		

	}
}
