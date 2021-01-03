package rest;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.File;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import beans.Korisnik;
import beans.Kupac;
import beans.Prodavac;
import beans.TipKupca;
import dao.KarteDAO;
import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.LokacijaDAO;
import dao.ManifestacijaDAO;
import dao.TipKupcaDAO;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
		manifestacije = new ManifestacijaDAO(lokacije);
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
		
		post("/rest/users/logUser", (req, res) -> {
			//Popraviti
			Korisnik user = g.fromJson(req.body(), Korisnik.class);
			Korisnik kor = k.find(user.getUsername(), user.getPassword());
			if(kor != null) {
				System.out.println("Prosao");
				res.type("application/json");
				switch(kor.getUloga()) {
				case "KUPAC":
						System.out.println(k.getKupciMap().get(kor.getUsername()));
						if(k.getKupciMap().get(kor.getUsername()).isObrisan() || k.getKupciMap().get(kor.getUsername()).isBanovan()) {
							return "Failed";
						}
					break;
				case "PRODAVAC":
						System.out.println(k.getKupciMap().get(kor.getUsername()));
						if(k.getProdavciMap().get(kor.getUsername()).isObrisan()) {
							return "Failed";
						}
					break;
				}
				
				req.session().attribute("currentUser", kor);
				return "Done";
			}else {
				System.out.println("Pao");
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
		
		get("/rest/users/allUsers", (req, res) -> {
			res.type("application/json");
			return g.toJson(k.getKorisnici());
		});
		
		get("/rest/users/allByers", (req, res) -> {
			res.type("application/json");
			return g.toJson(k.getKupci());
		});
		
		get("/rest/users/allSellers", (req, res) -> {
			res.type("application/json");
			return g.toJson(k.getProdavci());
		});
		
		get("/rest/comments/allComments", (req, res) -> {
			res.type("application/json");
			return  g.toJson(komentari);
		});
		
		post("/rest/tickets/checkout", (req, res) -> {
			HashMap<String, Integer> cart = (HashMap<String, Integer>) g.fromJson(req.body(), Map.class);
			System.out.println(cart);
			res.type("application/json");
			//TODO: da se nastavi :D
			return  "Done";
		});
//		get("/rest/demo/book/:isbn", (req, res) -> {
//			String isbn = req.params("isbn");
//			return "/rest/demo/book received PathParam 'isbn': " + isbn;
//		});
//
//		get("/rest/demo/books", (req, res) -> {
//			String num = req.queryParams("num");
//			String num2 = req.queryParams("num2");
//			return "/rest/demo/book received QueryParam 'num': " + num + ", and num2: " + num2;
//		});
//		
//		get("/rest/demo/testheader", (req, res) -> {
//			String cookie = req.headers("Cookie");
//			return "/rest/demo/testheader received HeaderParam 'Cookie': " + cookie;
//		});
//		
//		get("/rest/demo/testcookie", (req, res) -> {
//			String cookie = req.cookie("pera");
//			if (cookie == null) {
//				res.cookie("pera", "Perin kolacic");
//				return "/rest/demo/testcookie <b>created</b> CookieParam 'pera': 'Perin kolacic'";  
//			} else {
//				return "/rest/demo/testcookie <i><u>received</u></i> CookieParam 'pera': " + cookie;
//			}
//		});

//		post("/rest/demo/forma", (req, res) -> {
//			res.type("application/json");
//			String ime = req.queryParams("ime");
//			String prezime = req.queryParams("prezime");
//			Student s = new Student(ime, prezime, null);
//			return g.toJson(s);
//		});
	}
}
