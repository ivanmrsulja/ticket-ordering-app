package sorter;
import java.util.Comparator;

import beans.Karta;
public class SortirajKartePoCijeniOpadajuce implements Comparator<Karta>{

	@Override
	public int compare(Karta o1, Karta o2) {
		// TODO Auto-generated method stub
		return Double.compare(o1.getCena(), o2.getCena())* -1;
	}

}
