package sorter;

import java.util.Comparator;

import beans.Karta;

public class SortirajKartePoManifestacijiOpadajuce implements Comparator<Karta> {

	@Override
	public int compare(Karta o1, Karta o2) {
		// TODO Auto-generated method stub
		return o1.getIdManifestacije().compareTo(o2.getIdManifestacije()) * -1;
	}

}
