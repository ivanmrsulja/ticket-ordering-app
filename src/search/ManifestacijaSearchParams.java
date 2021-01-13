package search;

public class ManifestacijaSearchParams {
	private String naziv;
	private long startDate;
	private long endDate;
	private String lokacija;
	private double startPrice;
	private double endPrice;
	private String tip;
	private boolean rasprodata;
	private String kriterijumSortiranja;
	private boolean opadajuce;
	
	public ManifestacijaSearchParams() {
		this.naziv = "";
		this.startDate = -4103432590000l;
		this.endDate = 4103432590000l;
		this.lokacija = "";
		this.startPrice = 0;
		this.endPrice = 10000000;
		this.tip = "SVE";
		this.rasprodata = false;
		this.kriterijumSortiranja = "NAZIV";
		this.opadajuce = false;
	}

	public ManifestacijaSearchParams(String naziv, long startDate, long endDate, String lokacija, double startPrice,
			double endPrice, String tip, boolean rasprodata, String kriterijumSortiranja, boolean opadajuce) {
		super();
		this.naziv = naziv;
		this.startDate = startDate;
		this.endDate = endDate;
		this.lokacija = lokacija;
		this.startPrice = startPrice;
		this.endPrice = endPrice;
		this.tip = tip;
		this.rasprodata = rasprodata;
		this.kriterijumSortiranja = kriterijumSortiranja;
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

	public String getLokacija() {
		return lokacija;
	}

	public void setLokacija(String lokacija) {
		this.lokacija = lokacija;
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

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public boolean isRasprodata() {
		return rasprodata;
	}

	public void setRasprodata(boolean rasprodata) {
		this.rasprodata = rasprodata;
	}

	public String getKriterijumSortiranja() {
		return kriterijumSortiranja;
	}

	public void setKriterijumSortiranja(String kriterijumSortiranja) {
		this.kriterijumSortiranja = kriterijumSortiranja;
	}

	public boolean isOpadajuce() {
		return opadajuce;
	}

	public void setOpadajuce(boolean opadajuce) {
		this.opadajuce = opadajuce;
	}

	@Override
	public String toString() {
		return "ManifestacijaSearchParams [naziv=" + naziv + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", lokacija=" + lokacija + ", startPrice=" + startPrice + ", endPrice=" + endPrice + ", tip=" + tip
				+ ", rasprodata=" + rasprodata + ", kriterijumSortiranja=" + kriterijumSortiranja + ", opadajuce="
				+ opadajuce + "]";
	}
	
}
