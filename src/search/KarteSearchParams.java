package search;

public class KarteSearchParams {
	
	private String naziv;
	private long startDate;
	private long endDate;
	private double startPrice;
	private double endPrice;
	private String kriterijumSortiranja;
	private String tip;
	private String status;
	private boolean opadajuce;
	
	public KarteSearchParams() {
		this.naziv = "";
		this.startDate = -4103432590000l;
		this.endDate = 4103432590000l;
		this.startPrice = 0;
		this.endPrice = 10000000;
		this.tip = "SVE";
		this.kriterijumSortiranja = "NAZIV";
		this.status = "SVE";
		this.opadajuce = false;
	}

	public KarteSearchParams(String naziv, long startDate, long endDate, double startPrice, double endPrice,
			String kriterijumSortiranja, String tip, String status, boolean opadajuce) {
		super();
		this.naziv = naziv;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startPrice = startPrice;
		this.endPrice = endPrice;
		this.kriterijumSortiranja = kriterijumSortiranja;
		this.tip = tip;
		this.status = status;
		this.opadajuce = opadajuce;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

	public double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(double startPrice) {
		this.startPrice = startPrice;
	}

	public double getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(double endPrice) {
		this.endPrice = endPrice;
	}

	public String getKriterijumSortiranja() {
		return kriterijumSortiranja;
	}

	public void setKriterijumSortiranja(String kriterijumSortiranja) {
		this.kriterijumSortiranja = kriterijumSortiranja;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isOpadajuce() {
		return opadajuce;
	}

	public void setOpadajuce(boolean opadajuce) {
		this.opadajuce = opadajuce;
	}

	@Override
	public String toString() {
		return "KarteSearchParams [naziv=" + naziv + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", startPrice=" + startPrice + ", endPrice=" + endPrice + ", kriterijumSortiranja="
				+ kriterijumSortiranja + ", tip=" + tip + ", status=" + status + ", opadajuce=" + opadajuce + "]";
	}
	
}
