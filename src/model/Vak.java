package model;

public class Vak {
	private String vakCode;
	private String vakNaam;
	
	public Vak(String vC, String vN) {
		vakCode = vC;
		vakNaam = vN;
	}
	
	public String getVakNaam() {
		return vakNaam;
	}
	
	public String getVakCode() {
		return vakCode;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = obj instanceof Vak;

		result = result && (((Vak)obj).getVakCode().equals(vakCode));

		return result;
	}
}
