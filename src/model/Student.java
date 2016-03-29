package model;

public class Student {
	private String gebruikersNaam;
	private String wachtwoord;
	private Klas mijnKlas;
	private String studentNummer;

	private String voornaam;
	private String tussenvoegsel;
	private String achternaam;

	public Student(String gbNm, String ww) {
		gebruikersNaam = gbNm;
		wachtwoord = ww;
	}

	public void setVoornaam(String vN) {
		voornaam = vN;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public void setTussenvoegsel(String tsv) {
		tussenvoegsel = tsv;
	}

	public String getTussenvoegsel() {
		return tussenvoegsel;
	}

	public void setAchternaam(String aN) {
		achternaam = aN;
	}

	public String getAchternaam() {
		return achternaam;
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

	@Override
	public boolean equals(Object obj) {
		boolean result = obj instanceof Student;
		Student stObj = (Student)obj;

		result = result && stObj.getGebruikersNaam().equals(gebruikersNaam);
		result = result && stObj.controleerWachtwoord(wachtwoord);
		result = result && stObj.getMijnKlas().equals(mijnKlas);
		result = result && stObj.getStudentNummer().equals(studentNummer);
		result = result && stObj.getVoornaam().equals(voornaam);
		result = result && stObj.getTussenvoegsel().equals(tussenvoegsel);
		result = result && stObj.getAchternaam().equals(achternaam);

		return result;
	}

	public String toString() {
		String naam = voornaam + " ";
		if(!tussenvoegsel.equals("")) naam += tussenvoegsel + " ";
		naam += achternaam;
		String result = naam + " - " + studentNummer + " - " + mijnKlas.getKlasCode();

		return result;
	}
}
