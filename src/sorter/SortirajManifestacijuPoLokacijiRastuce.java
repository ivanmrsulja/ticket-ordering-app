package sorter;

import java.util.Comparator;

import beans.Manifestacija;
import beans.Lokacija;
public class SortirajManifestacijuPoLokacijiRastuce implements Comparator<Manifestacija> {

	@Override
	public int compare(Manifestacija o1, Manifestacija o2) {
		// TODO Auto-generated method stub
		return o1.getLokacija().getAdresa().compareTo(o2.getLokacija().getAdresa());
	}

}
