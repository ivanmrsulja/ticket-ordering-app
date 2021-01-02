package beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Kupac extends Korisnik {
	
	private ArrayList<Karta> sveKarte;
	private int brojBodova;
	private TipKupca tip;
	private boolean banovan;
	private boolean obrisan;
	
	public Kupac() {}
	public Kupac(String u, String p, String im, String pr, String po, long dr, String ul, TipKupca t, int bodovi) {
		super(u, p, im, pr, po, dr, ul);
		sveKarte = new ArrayList<Karta>();
		brojBodova = bodovi;
		tip = t;
		banovan = false;
		obrisan = false;
	}
	
	public boolean isObrisan() {
		return obrisan;
	}
	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}
	public boolean isBanovan() {
		return banovan;
	}
	
	public void setBanovan(boolean b) {
		banovan = b;
	}
	
	public List<Karta> getSveKarte() {
		return sveKarte;
	}
	public void setSveKarte(ArrayList<Karta> sveKarte) {
		this.sveKarte = sveKarte;
	}
	public int getBrojBodova() {
		return brojBodova;
	}
	public void addKarta(Karta k) {
		sveKarte.add(k);
	}
	public void setBrojBodova(int brojBodova) {
		this.brojBodova = brojBodova;
	}
	public TipKupca getTip() {
		return tip;
	}
	public void setTip(TipKupca tip) {
		this.tip = tip;
	}
	
	@Override
	public String toString() {
		return "Kupac [sveKarte=" + sveKarte + ", brojBodova=" + brojBodova + ", tip=" + tip + ", banovan=" + banovan
				+ ", obrisan=" + obrisan + "]";
	}
	
	
	
}
