package model;

public class Klas {
	private String klasCode;
	
	public Klas(String kC) {
		klasCode = kC;
	}
	
	public String getKlasCode() {
		return klasCode;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = obj instanceof Klas;
		Klas klObj = (Klas)obj;

		result = result && klObj.getKlasCode().equals(klasCode);

		return result;
	}
}
