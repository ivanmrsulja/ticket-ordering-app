package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import beans.Lokacija;

public class LokacijaDAO {
	
	public List<Lokacija> lokacijeList;
	public Map lokacijeMap;
	
	public LokacijaDAO() {
		lokacijeList = new ArrayList<Lokacija>();
		lokacijeMap = new HashMap<String, Lokacija>();
	}
	
	
	public void load() {
		String path = "data//lokacije.csv";
		
		BufferedReader bf;
		String currentLine;
		try {
			bf = new BufferedReader(new FileReader(path));
			while((currentLine = bf.readLine()) != null) {
				if(currentLine.trim().equals(""))
					continue;
				
				String[] tokens = currentLine.split(";");
				Lokacija l = new Lokacija(Double.parseDouble(tokens[0]),Double.parseDouble(tokens[1]),tokens[2]);
				lokacijeList.add(l);
				lokacijeMap.put(l.getAdresa(), l);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void save() {
		String path = "data//lokacije.csv";
//		Lokacija l1 = new Lokacija(45.24740, 19.85112, "Bulevar cara Lazara 3, Novi Sad 21000");
//		Lokacija l2 = new Lokacija(45.24935, 19.84623, "Sutjeska 3 Novi Sad 21000");
//		Lokacija l3 = new Lokacija(45.24648,19.83968,"Bulevar oslobodjenja 96, Novi Sad 21000");
//		lokacijeList.add(l1);
//		lokacijeList.add(l2);
//		lokacijeList.add(l3);
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
			for(Lokacija l: lokacijeList) {
				
				out.print(l.getGeografskaSirina());
				out.print(";");
				out.print(l.getGeografskaDuzina());
				out.print(";");
				out.print(l.getAdresa());
				out.println();
				
			}
			
			out.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	

	public Map getLokacijeMap() {
		return lokacijeMap;
	}


	public void setLokacijeMap(Map lokacijeMap) {
		this.lokacijeMap = lokacijeMap;
	}


	public List<Lokacija> getLokacijeList() {
		return lokacijeList;
	}


	public void setLokacijeList(List<Lokacija> lokacijeList) {
		this.lokacijeList = lokacijeList;
	}


	@Override
	public String toString() {
		return "LokacijaDAO [lokacijeList=" + lokacijeList + ", lokacijeMap=" + lokacijeMap + "]";
	}
	
	
	
	
	
	
	
	
	
}
