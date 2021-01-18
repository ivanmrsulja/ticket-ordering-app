package dao;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.TipKupca;
public class TipKupcaDAO {

	private List<TipKupca> tipoviKupca;
	private Map kupciMap;
	

	public TipKupcaDAO() {
		tipoviKupca = new ArrayList<TipKupca>();
		kupciMap = new HashMap<String, TipKupca>();
	}
	
	public void load() {
		String path = "data/tipoviKupaca.csv";
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(path));
			
			String currentLine;
			while((currentLine = bf.readLine())!=null) {
				System.out.println(currentLine);
				String[] tokens = currentLine.split(";");
				TipKupca tip = new TipKupca(tokens[0],Double.parseDouble(tokens[2]), Integer.parseInt(tokens[1]));
				tipoviKupca.add(tip);
				kupciMap.put(tip.getImeTipa(), tip);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public List<TipKupca> getTipoviKupca() {
		return tipoviKupca;
	}

	public void setTipoviKupca(List<TipKupca> tipoviKupca) {
		this.tipoviKupca = tipoviKupca;
	}
	
	public void save() {
		String path = "data/tipoviKupaca.csv";
		
//		TipKupca t1 = new TipKupca("Pocetni kupac",0,0);
//		TipKupca t2 = new TipKupca("Redovan kupac",6,30);
//		TipKupca t3 = new TipKupca("Premium kupac",15,80);
//		tipoviKupca.add(t1);
//		tipoviKupca.add(t2);
//		tipoviKupca.add(t3);
		
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
			for(TipKupca t: tipoviKupca) {
				out.print(t.getImeTipa());
				out.print(";");
				out.print(t.getBodovi());
				out.print(";");
				out.print(t.getPopust());
				out.println();
			}
			System.out.println("PROSAOO");
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	public Map getKupciMap() {
		return kupciMap;
	}

	public void setKupciMap(Map kupciMap) {
		this.kupciMap = kupciMap;
	}

	@Override
	public String toString() {
		return "TipKupcaDAO [tipoviKupca=" + tipoviKupca + "]";
	}
	
}
