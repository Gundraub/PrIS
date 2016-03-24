package model;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PrIS {
	private ArrayList<Docent> deDocenten;
	private ArrayList<Student> deStudenten;

	private ArrayList<Klas> deKlassen;
	private ArrayList<Les> deLessen;

	private ArrayList<Vak> deVakken;


	//TODO: Deze commentaar voorzien van actueel commentaar...
	/**
	 * De constructor maakt een set met standaard-data aan. Deze data
	 * moet nog vervangen worden door gegevens die uit een bestand worden
	 * ingelezen, maar dat is geen onderdeel van deze demo-applicatie!
	 * 
	 * De klasse PrIS (PresentieInformatieSysteem) heeft nu een meervoudige
	 * associatie met de klassen Docent en Student. Uiteraard kan dit nog veel
	 * verder uitgebreid en aangepast worden! 
	 * 
	 * De klasse fungeert min of meer als ingangspunt voor het domeinmodel. Op
	 * dit moment zijn de volgende methoden aanroepbaar:
	 * 
	 * String login(String gebruikersnaam, String wachtwoord)
	 * Docent getDocent(String gebruikersnaam)
	 * Student getStudent(String gebruikersnaam)
	 * ArrayList<Student> getStudentenVanKlas(String klasCode)
	 * 
	 * Methode login geeft de rol van de gebruiker die probeert in te loggen,
	 * dat kan 'student', 'docent' of 'undefined' zijn! Die informatie kan gebruikt 
	 * worden om in de Polymer-GUI te bepalen wat het volgende scherm is dat getoond 
	 * moet worden.
	 * 
	 */
	public PrIS() {
		deDocenten = new ArrayList<Docent>();
		deStudenten = new ArrayList<Student>();

		deKlassen = new ArrayList<Klas>();
		deLessen = new ArrayList<Les>();

		deVakken = new ArrayList<Vak>();

		deVakken.add(new Vak("TCIF-V1AUI-15", "Analyse en User Interfaces"));
		deVakken.add(new Vak("TICT-V1GP-15", "Group Project"));
		deVakken.add(new Vak("TICT-V1OODC-15", "Object Oriented Design & Construction"));

		/*
		Docent d1 = new Docent("Wim", "geheim");
		Docent d2 = new Docent("Hans", "geheim");
		Docent d3 = new Docent("Jan", "geheim");
		
		d1.voegVakToe(new Vak("TCIF-V1AUI-15", "Analyse en User Interfaces"));
		d1.voegVakToe(new Vak("TICT-V1GP-15", "Group Project"));
		d1.voegVakToe(new Vak("TICT-V1OODC-15", "Object Oriented Design & Construction"));
		
		deDocenten.add(d1);
		deDocenten.add(d2);
		deDocenten.add(d3);

		Student s1 = new Student("Roel", "geheim");
		Student s2 = new Student("Frans", "geheim");
		Student s3 = new Student("Daphne", "geheim");
		Student s4 = new Student("Jeroen", "geheim");
		
		Klas k1 = new Klas("SIE-V1X");
		
		s1.setMijnKlas(k1);
		s2.setMijnKlas(k1);
		s3.setMijnKlas(k1);
		s4.setMijnKlas(k1);
		
		deStudenten.add(s1);
		deStudenten.add(s2);
		deStudenten.add(s3);
		deStudenten.add(s4);
		*/

		// Code voor inlezen rooster
		BufferedReader fileReader = null;

		try {
			String currentLine = "";
			// Creëer de file reader
			fileReader = new BufferedReader(new FileReader("data/rooster_C.csv"));

			// Lees het bestand regel voor regel
			while((currentLine = fileReader.readLine()) != null) {
				// Haal alle tokens uit deze regel
				String[] tokens = currentLine.split(",");

				// Check of het vak al in deVakken lijst staat
				Vak hetVak = new Vak(tokens[3], "Cursusnaam onbekend");
				if(deVakken.contains(hetVak)) {
					hetVak = deVakken.get(deVakken.indexOf(hetVak));
				}
				// Zo niet, maak dit vak dan aan en voeg hem aan de lijst toe
				else {
					deVakken.add(hetVak);
				}

				// Same voor de Docent
				Docent deDocent = new Docent(tokens[4], "geheim");
				if(deDocenten.contains(deDocent)) {
					deDocent = deDocenten.get(deDocenten.indexOf(deDocent));
				}
				else {
					deDocenten.add(deDocent);
				}
				deDocent.voegVakToe(hetVak);

				// Same voor de Klas
				Klas deKlas = new Klas(tokens[6]);
				if(deKlassen.contains(deKlas)) {
					deKlas = deKlassen.get(deKlassen.indexOf(deKlas));
				}
				else {
					deKlassen.add(deKlas);
				}

				// Creëer de Les en voeg deze aan de lijst toe
				Les deLes = new Les(tokens[0], tokens[1], tokens[2], hetVak, deDocent, tokens[5], deKlas);
				deLessen.add(deLes);
			}

			// Print de klassen uit om te checken of het gewerkt heeft
			for(Les deLes : deLessen) {
				System.out.println(deLes + "\n");
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				fileReader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public String login(String gebruikersnaam, String wachtwoord) {
		for (Docent d : deDocenten) {
			if (d.getGebruikersNaam().equals(gebruikersnaam)) {
				if (d.controleerWachtwoord(wachtwoord)) {
					return "docent";
				}
			}
		}
		
		for (Student s : deStudenten) {
			if (s.getGebruikersNaam().equals(gebruikersnaam)) {
				if (s.controleerWachtwoord(wachtwoord)) {
					return "student";
				}
			}
		}
		
		return "undefined";
	}
	
	public Docent getDocent(String gebruikersnaam) {
		Docent resultaat = null;
		
		for (Docent d : deDocenten) {
			if (d.getGebruikersNaam().equals(gebruikersnaam)) {
				resultaat = d;
				break;
			}
		}
		
		return resultaat;
	}
	
	public Student getStudent(String gebruikersnaam) {
		Student resultaat = null;
		
		for (Student s : deStudenten) {
			if (s.getGebruikersNaam().equals(gebruikersnaam)) {
				resultaat = s;
				break;
			}
		}
		
		return resultaat;
	}
	
	public ArrayList<Student> getStudentenVanKlas(String klasCode) {
		ArrayList<Student> resultaat = new ArrayList<Student>();
		
		for (Student s : deStudenten) {
			if (s.getMijnKlas().getKlasCode().equals(klasCode)) {
				resultaat.add(s);
			}
		}
		
		return resultaat;
	}
}
