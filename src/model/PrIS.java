package model;

import java.text.ParseException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class PrIS {
	private ArrayList<Docent> deDocenten;
	private ArrayList<Student> deStudenten;

	private ArrayList<Klas> deKlassen;
	private ArrayList<Les> deLessen;

	private ArrayList<Vak> deVakken;

	private ArrayList<Presentie> dePresenties;


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

		dePresenties = new ArrayList<Presentie>();

		init();

		//debug();
	}

	/**
	 * De initialisatie methode leest de .csv bestanden uit en maakt aan de hand van diens inhoud de nodige objecten aan.
	 */
	private void init() {
		//
		BufferedReader fileReader = null;
		String currentLine = "";

		// Code voor inlezen klassen.csv
		try {
			// Creëer de file reader
			fileReader = new BufferedReader(new FileReader("data/klassen.csv"));

			// Lees het bestand regel voor regel
			while((currentLine = fileReader.readLine()) != null) {
				// Haal alle tokens uit deze regel
				String[] tokens = currentLine.split(",");

				// Check of de klas al in deKlassen lijst staat
				Klas deKlas = new Klas(tokens[4]);
				if(deKlassen.contains(deKlas)) {
					deKlas = deKlassen.get(deKlassen.indexOf(deKlas));
				}
				// Zo niet, voeg deze klas dan aan de lijst toe
				else {
					deKlassen.add(deKlas);
				}

				// Check of de student al in deStudenten lijst staat, zo niet, voeg deze dan toe
				Student deStudent = new Student(tokens[0], "geheim");
				deStudent.setStudentNummer(tokens[0]);
				deStudent.setMijnKlas(deKlas);
				deStudent.setVoornaam(tokens[3]);
				deStudent.setTussenvoegsel(tokens[2]);
				deStudent.setAchternaam(tokens[1]);
				if(!deStudenten.contains(deStudent)) {
					deStudenten.add(deStudent);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		// Code voor inlezen rooster_C.csv
		try {
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
				// Zo niet, voeg dit vak dan aan de lijst toe
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
				Klas deKlas = new Klas(tokens[6].replace('_', '-'));
				if(deKlassen.contains(deKlas)) {
					deKlas = deKlassen.get(deKlassen.indexOf(deKlas));
				}
				else {
					deKlassen.add(deKlas);
				}

				// Creëer de Les en voeg deze aan de lijst toe
				Les deLes = new Les(tokens[0], tokens[1], tokens[2], hetVak, deDocent, tokens[5], deKlas);
				deLessen.add(deLes);

				// Creëer Presentie objecten voor deze les
				for (Student deStudent : getStudentenVanKlas(deKlas.getKlasCode())) {
					Presentie dePresentie = new Presentie(deStudent, deLes);
					dePresenties.add(dePresentie);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Sluit de fileReader
		finally {
			try {
				fileReader.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Een set for-loops puur bedoelt om te kunnen debuggen.
	 */
	private void debug() {
		System.out.println("- Dit is de inhoud van de studentenlijst -");
		for (Student deStudent : deStudenten) {
			System.out.println(deStudent);
		}

		System.out.println("\n- Dit is de inhoud van de klassenlijst -");
		for (Klas deKlas : deKlassen) {
			System.out.println(deKlas.getKlasCode());
		}

		System.out.println("\n- Dit is de inhoud van de lessenlijst -");
		for (Les deLes : deLessen) {
			System.out.println(deLes.toString() + "\n");
		}

		System.out.println("\n- Dit zijn de lessen van Brian van Yperen op 2016-03-10 -");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		try {
			d = dateFormat.parse("2016-03-10");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (Les deLes : getDeLessenVanStudent(d, "1679084")) {
			System.out.println(deLes.toString() + "\n");
		}

		System.out.println("\n- Dit zijn de vakken die gegeven worden door Peter van Rooijen -");
		for (Vak hetVak : getDocent("Peter van Rooijen").getVakken()) {
			System.out.println(hetVak.getVakNaam());
		}
	}

	/**
	 * Returns of er een Docent of Student object is met de meegegeven gebruikersnaam en wachtwoord
	 *
	 * @param gebruikersnaam
	 * @param wachtwoord
     * @return
     */
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

	/**
	 * Zoekt en returns de Docent met de meegegeven gebruikersnaam
	 *
	 * @param gebruikersnaam
	 * @return
	 */
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

	/**
	 * Zoekt en returns de Student met de meegegeven gebruikersnaam
	 *
	 * @param gebruikersnaam
	 * @return
     */
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

	/**
	 * Zoekt en returns een lijst met alle Studenten in de Klas met de meegegeven klasCode
	 *
	 * @param klasCode
	 * @return
     */
	public ArrayList<Student> getStudentenVanKlas(String klasCode) {
		ArrayList<Student> resultaat = new ArrayList<Student>();
		
		for (Student s : deStudenten) {
			if (s.getMijnKlas().getKlasCode().equals(klasCode)) {
				resultaat.add(s);
			}
		}
		
		return resultaat;
	}

	/**
	 * Zoekt en returns een lijst van alle Lessen die Student nm op Date d heeft
	 *
	 * @param d
	 * @param nm
     * @return
     */
	public ArrayList<Les> getDeLessenVanStudent(Date d, String nm) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		ArrayList<Les> result = new ArrayList<Les>();

		// Zoek de klas waarin de Student zich bevindt
		Klas k = getStudent(nm).getMijnKlas();

		// Vind alle lessen die beginnen op de meegegeven datum en gegeven worden aan de gevonden klas, en voeg deze toe aan de result lijst
		for (Les l : deLessen) {
			boolean drone = dateFormat.format(d).equals(dateFormat.format(l.getBeginTijd()));
			drone = drone && l.getDeKlas().equals(k);

			// If this is the drone we're looking for: add it to the list
			if (drone) {
				result.add(l);
			}
		}

		return result;
	}

	/**
	 * Zoekt en returns de Les die op Date tijd aan Klas k gegeven wordt
	 *
	 * @param k
	 * @param tijd
     * @return
     */
	public Les getLes(Klas k, String tijd) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");

		Les result = null;

		for (Les l : deLessen) {
			try {
				if (l.getBeginTijd().equals(dateFormat.parse(tijd)) && l.getDeKlas().equals(k)) {
                    result = l;
                    break;
                }
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Zoekt en returns het Presentie object voor de Student met gebruikersnaam nm en de les die als begintijd dat+bT heeft
	 *
	 * @param nm
	 * @param bT
	 * @param dat
     * @return
     */
	public Presentie getPresentie(String nm, String bT, String dat) {
		Presentie result = null;

		Student s = getStudent(nm);
		Les l = getLes(getStudent(nm).getMijnKlas(), dat+bT);

		Presentie dePresentie = new Presentie(s, l);

		if (dePresenties.contains(l)) {
			result = dePresenties.get(dePresenties.indexOf(dePresentie));
		}

		return result;
	}

	/**
	 * Zoekt de Les die om dat+bT aan de Klas van de Student met gebruikersnaam nm en meldt deze daarvoor ziek.
	 *
	 * @param nm
	 * @param bT
	 * @param dat
     */
	public void meldZiek(String nm, String bT, String dat) {
		Presentie p = getPresentie(nm, bT, dat);

		p.setStatus(Presentie.Present.ZIEK);
	}


}
