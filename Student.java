package model;

public class Student {
	private String gebruikersNaam;
	private String wachtwoord;
	private Klas mijnKlas;
	private String studentNummer;
	
	public Student(String gbNm, String ww) {
		gebruikersNaam = gbNm;
		wachtwoord = ww;
	}
	
	public String getGebruikersNaam() {
		return gebruikersNaam;
	}
	
	public boolean controleerWachtwoord(String ww) {
		return ww.equals(wachtwoord);
	}
	
	public void setMijnKlas(Klas k) {
		mijnKlas = k;
	}
	
	public Klas getMijnKlas() {
		return mijnKlas;
	}

	public void setStudentNummer(String studNum) {
		studentNummer = studNum;
	}
	
	public String getStudentNummer() {
		return studentNummer;
	}
}
