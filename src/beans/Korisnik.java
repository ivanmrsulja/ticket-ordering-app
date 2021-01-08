package beans;

import java.time.LocalDateTime;
import java.util.Date;

public class Korisnik {
	private String username;
	private String password;
	private String ime;
	private String prezime;
	private String pol;
	private long datumRodjenja;
	private String uloga;
	private boolean banovan;
	
	public Korisnik() {}
	public Korisnik(String us, String pa, String im, String pr, String po, long dr, String ul) {
		username = us;
		password = pa;
		ime = im;
		prezime = pr;
		pol = po;
		datumRodjenja = dr;
		uloga = ul;
		banovan = false;
	}
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPol() {
		return pol;
	}
	public void setPol(String pol) {
		this.pol = pol;
	}
	
	public long getDatumRodjenja() {
		return datumRodjenja;
	}
	public void setDatumRodjenja(long datumRodjenja) {
		this.datumRodjenja = datumRodjenja;
	}
	public String getUloga() {
		return uloga;
	}
	public void setUloga(String uloga) {
		this.uloga = uloga;
	}
	
	public boolean isBanovan() {
		return banovan;
	}
	public void setBanovan(boolean banovan) {
		this.banovan = banovan;
	}
	@Override
	public String toString() {
		return "Korisnik [username=" + username + ", password=" + password + ", ime=" + ime + ", prezime=" + prezime
				+ ", pol=" + pol + ", datumRodjenja=" + (new Date(datumRodjenja)).toString() + ", uloga=" + uloga + "]";
	}
	
}
