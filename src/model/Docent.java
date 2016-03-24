package model;

import java.util.ArrayList;

public class Docent {
	private String gebruikersNaam;
	private String wachtwoord;
	private ArrayList<Vak> mijnVakken;
	
	public Docent(String gbNm, String ww) {
		mijnVakken = new ArrayList<Vak>();
		gebruikersNaam = gbNm;
		wachtwoord = ww;
	}
	
	public String getGebruikersNaam() {
		return gebruikersNaam;
	}
	
	public boolean controleerWachtwoord(String ww) {
		return wachtwoord.equals(ww);
	}
	
	public void voegVakToe(Vak nwV) {
		if(mijnVakken.contains(nwV)) {
			mijnVakken.add(nwV);
		}
	}
	
	public ArrayList<Vak> getVakken() {
		return mijnVakken;
	}
}
