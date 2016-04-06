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

		debug();
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
		System.out.println("[DEBUG START]\n");


		System.out.println("- Dit is de inhoud van de studentenlijst -");
		for (Student deStudent : deStudenten) {
			System.out.println(deStudent);
		}

		System.out.println("\n- Dit is de inhoud van de klassenlijst -");
		for (Klas deKlas : deKlassen) {
			System.out.println(deKlas.getKlasCode());
		}

		System.out.println("\n- Dit zijn de verschillende klassen met inhoud -");
		for (Klas deKlas : deKlassen) {
			System.out.println("\n" + deKlas.getKlasCode());
			for (Student s : getStudentenVanKlas(deKlas.getKlasCode())) {
				System.out.println(s);
			}
		}

		/*
		System.out.println("\n- Dit is de inhoud van de lessenlijst -");
		for (Les deLes : deLessen) {
			System.out.println(deLes.toString() + "\n");
		}

		/*
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

		/*
		System.out.println("\n- Dit zijn de vakken die gegeven worden door Peter van Rooijen -");
		for (Vak hetVak : getDocent("Peter van Rooijen").getVakken()) {
			System.out.println(hetVak.getVakNaam());
		}

		Student ali = getStudent("1691834");
		Klas v1a = ali.getMijnKlas();

		System.out.println("- Dit zijn alle lessen van klas " + v1a.getKlasCode() + " -");
		for (Les l : getDeLessenVanKlas(v1a)) {
			System.out.println("\n" + l.toString());
		}

		System.out.println("\n- -");

		int[] presentielijst = getStatistiek(ali.getGebruikersNaam(), "TICT-V1OODC-15");

		String result = "\n" + ali.getVoornaam() + "'s presentielijst voor het vak TICT-V1OODC-15 vóór het ziekmelden:\n";

		for (int i : presentielijst) {
			result += i + " ";
		}

		System.out.println(result);

		meldZiek(ali.getGebruikersNaam(), "09:30", "2016-03-08");
		meldZiek(ali.getGebruikersNaam(), "10:00", "2016-03-10");

		presentielijst = getStatistiek(ali.getGebruikersNaam(), "TICT-V1OODC-15");

		result = "\n" + ali.getVoornaam() + "'s presentielijst voor het vak TICT-V1OODC-15 na het ziekmelden:\n";

		for (int i : presentielijst) {
			result += i + " ";
		}

		System.out.println(result);

		Student s = getStudent("1691834");

		System.out.println("\n" + s.getVoornaam() + " zit in klas " + s.getMijnKlas().getKlasCode());

		Docent bart = getDocent("Bart van Eijkelenburg");
		Date day = new Date();
		try {
			day = dateFormat.parse("2016-03-10");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("\n" + bart.getGebruikersNaam() + " geeft op " + dateFormat.format(day) + " de volgende lessen:");
		for (Les l : getDeLessenVanDocent(day, "Bart van Eijkelenburg")) {
			System.out.println(l);
		}
		*/

		System.out.println("\n[DEBUG END]\n");

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
	 *
	 *
	 * @param k
	 * @return
	 */
	public ArrayList<Les> getDeLessenVanKlas(Klas k) {
		ArrayList<Les> result = new ArrayList<Les>();

		for (Les l : deLessen) {
			if (l.getDeKlas().equals(k)) {
				result.add(l);
			}
		}

		return result;
	}

	/**
	 * Zoekt en returns een lijst van alle Lessen die Student nm op Date d heeft
	 *
	 * @param datum
	 * @param naam
     * @return
     */
	public ArrayList<Les> getDeLessenVanStudent(Date datum, String naam) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		ArrayList<Les> result = new ArrayList<Les>();

		// Zoek de klas waarin de Student zich bevindt
		Klas k = getStudent(naam).getMijnKlas();

		// Vind alle lessen die beginnen op de meegegeven datum en gegeven worden aan de gevonden klas, en voeg deze toe aan de result lijst
		for (Les l : getDeLessenVanKlas(k)) {
			// Als deze les op de aangegeven dag wordt gehouden: voeg hem dan toe aan de return lijst
			if (dateFormat.format(datum).equals(dateFormat.format(l.getBeginTijd()))) {
				result.add(l);
			}
		}

		return result;
	}

	/**
	 *
	 *
	 * @param datum
	 * @param naam
	 * @return
	 */
	public ArrayList<Les> getDeLessenVanDocent(Date datum, String naam) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		ArrayList<Les> result = new ArrayList<Les>();

		Docent d = getDocent(naam);

		for (Les l : deLessen) {
			if (l.getDeDocent().equals(d) && dateFormat.format(datum).equals(dateFormat.format(l.getBeginTijd()))) {
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

		if (dePresenties.contains(dePresentie)) {
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
		System.out.println(getStudent(nm).getVoornaam() + " is succesvol ziekgemeld.");
	}

	/**
	 * Returnt de presenties van een Student voor een bepaald Vak als een int array
	 *
	 * @param naam
	 * @param vakCode
     * @return
     */
	public int[] getStatistiek(String naam, String vakCode) {
		int[] result = new int[5];

		Student s = getStudent(naam);
		Vak v = null;

		for (Vak i : deVakken) {
			if (i.getVakCode().equals(vakCode)) {
				v = i;
				break;
			}
		}

		for (Les l : getDeLessenVanKlas(s.getMijnKlas())) {
			for (Presentie p : dePresenties) {
				if (p.getDeLes().equals(l) && p.getDeStudent().equals(s)) {
					switch (p.getStatus()) {
						case AANWEZIG:
							result[0]++;
							break;
						case AFWEZIG:
							result[1]++;
							break;
						case ZIEK:
							result[2]++;
							break;
						case ONBEKEND:
							result[3]++;
							break;
						case LEEG:
							result[4]++;
							break;
					}
					break;
				}
			}
		}

		return result;
	}

}
