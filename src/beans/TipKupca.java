package beans;

public class TipKupca {
	private String imeTipa;
	private double popust;
	private int bodovi;
	
	public TipKupca() {}
	public TipKupca(String it, double p, int b) {
		imeTipa = it;
		popust = p;
		bodovi = b;
	}
	
	public String getImeTipa() {
		return imeTipa;
	}
	public void setImeTipa(String imeTipa) {
		this.imeTipa = imeTipa;
	}
	public double getPopust() {
		return popust;
	}
	public void setPopust(double popust) {
		this.popust = popust;
	}
	public int getBodovi() {
		return bodovi;
	}
	public void setBodovi(int bodovi) {
		this.bodovi = bodovi;
	}
	
	@Override
	public String toString() {
		return "TipKupca [imeTipa=" + imeTipa + ", popust=" + popust + ", bodovi=" + bodovi + "]";
	}
	
	
	
}
