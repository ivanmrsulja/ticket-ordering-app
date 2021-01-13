package sorter;

import java.util.Comparator;

import beans.Manifestacija;

public class SortirajManifestacijuPoDatumuRastuce implements Comparator<Manifestacija> {

	
	@Override
	public int compare(Manifestacija o1, Manifestacija o2) {
		// TODO Auto-generated method stub
		return Long.compare(o1.getDatumOdrzavanja(), o2.getDatumOdrzavanja());
	}
}
