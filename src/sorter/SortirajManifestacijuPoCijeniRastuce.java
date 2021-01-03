package sorter;

import java.util.Comparator;

import beans.Manifestacija;

public class SortirajManifestacijuPoCijeniRastuce implements Comparator<Manifestacija> {

	@Override
	public int compare(Manifestacija o1, Manifestacija o2) {
		// TODO Auto-generated method stub
		return Double.compare(o1.getCenaRegular(), o2.getCenaRegular());
	}

}
