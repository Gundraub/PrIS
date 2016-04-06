package controller;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import model.Les;
import model.PrIS;
import model.Student;
import model.Presentie;
import server.Conversation;
import server.Handler;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StudentController implements Handler {
	DateFormat dateFormat = new SimpleDateFormat("dd/MM");
	DateFormat timeFormat = new SimpleDateFormat("HH:mm");
	DateFormat ziekmeldFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	//aantal milliseconde vanaf 1 januari 1970 tot aan 18-02-2016
	// word veranderd naar huidige datum door 14557.... te vervangen met:
	//System.currentTimeMillis()
	String gebruikersnaam;
	long mmS;
	
	
	
	private PrIS informatieSysteem;
	/**
	 * De StudentController klasse moet alle student-gerelateerde aanvragen
	 * afhandelen. Methode handle() kijkt welke URI is opgevraagd en laat
	 * dan de juiste methode het werk doen. Je kunt voor elke nieuwe URI
	 * een nieuwe methode schrijven.
	 * 
	 * @param infoSys - het toegangspunt tot het domeinmodel
	 */
	public StudentController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/student/mijnmedestudenten")) {
			mijnMedestudenten(conversation);
		}
		
		else if (conversation.getRequestedURI().startsWith("/student/mijnrooster"))	{
			mijnRooster(conversation);
		}
		
		else if (conversation.getRequestedURI().startsWith("/student/wijzigrooster")) {
			wijzigRooster(conversation);
		}

		else if (conversation.getRequestedURI().startsWith("/student/studentafmelden")) {
			studentAfmelden(conversation);
		}

		else if (conversation.getRequestedURI().startsWith("/student/datumopvragen")) {
			huidigeDatum(conversation);
		}
	}
	

	/**
	 * Deze methode haalt eerst de opgestuurde JSON-data op. Daarna worden
	 * de benodigde gegevens uit het domeinmodel gehaald. Deze gegevens worden
	 * dan weer omgezet naar JSON en teruggestuurd naar de Polymer-GUI!
	 * 
	 * @param conversation - alle informatie over het request
	 */
	private void huidigeDatum(Conversation conversation)	{
		mmS = System.currentTimeMillis();
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
				.add("afmeldbericht","afgemeld")
				.build();
		conversation.sendJSONMessage(value.toString());

	}

	private void studentAfmelden(Conversation conversation)  {
		Date date = new Date(mmS);
		JsonObject jsonObjectIn;
		jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String tijd = jsonObjectIn.getString("tijd");
		String gebruikersnaam = jsonObjectIn.getString("username");
		informatieSysteem.meldZiek(gebruikersnaam, tijd, ziekmeldFormat.format(date));
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
				.add("afmeldbericht","afgemeld")
				.build();
		conversation.sendJSONMessage(value.toString());
	}

	private void mijnRooster(Conversation conversation) {

		Date date = new Date(mmS);
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		gebruikersnaam = jsonObjectIn.getString("username");
		
		Student student = informatieSysteem.getStudent(gebruikersnaam);
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonArrayBuilder lessen = Json.createArrayBuilder();
		//maakt lijst lessen, met alle lessen die voldoen aan datum en studentennummer.
		for (Les l : informatieSysteem.getDeLessenVanStudent(date, gebruikersnaam)) {
			String stat = "null";

			Presentie p = informatieSysteem.getPresentie(gebruikersnaam, timeFormat.format(l.getBeginTijd()), ziekmeldFormat.format(date));

			if (p != null) {
				switch (p.getStatus()) {
					case AANWEZIG:
						stat = "aanwezig";
						break;
					case AFWEZIG:
						stat = "afwezig";
						break;
					case ZIEK:
						stat = "afgemeld";
						break;
					case ONBEKEND:
						stat = "afmelden";
						break;
					case LEEG:
						stat = "afmelden";
						break;
				}
			}
			System.out.println(stat);
			lessen.add(factory.createObjectBuilder()
					.add("status", stat)
					.add("tijd", timeFormat.format(l.getBeginTijd()))
					.add("vak",  l.getHetVak().getVakNaam())
					.add("docent", l.getDeDocent().getGebruikersNaam())
					.add("lokaal", l.getHetLokaal()));
		}

		 
	//Maakt lijst met twee lijsten, lijst lessen en lijst datum.
	//Lijst datum geeft de huidige ingestelde datum naar polymer
	//Lijst lessen geeft variabele tijd, vak, docent, lokaal
	//Beide lijsten komen samen in object "job"
	//job wordt in geheel opgestuurd naar polymer rooster element
		JsonObject job = factory.createObjectBuilder()
				.add("datum", dateFormat.format(date))
				.add("lessen", lessen)
 				.build();
		System.out.println(job.toString());
		conversation.sendJSONMessage(job.toString());
		System.out.println(conversation);
		
	}
	

		
	public void wijzigRooster(Conversation conversation){
		
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		int offset = Integer.parseInt(jsonObjectIn.getString("offset"));
		System.out.println(offset);
		long mS = offset * 24 * 60 * 60 * 1000;
		mmS += mS;
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonObject value = factory.createObjectBuilder()
				.add("wijzig","datum gewijzigd")
				.build();
		conversation.sendJSONMessage(value.toString());
		
	}
	
	private void mijnMedestudenten(Conversation conversation) {
		JsonObject jsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String gebruikersnaam = jsonObjectIn.getString("username");
		
		Student student = informatieSysteem.getStudent(gebruikersnaam);			// Student-object opzoeken
		String klasCode = student.getMijnKlas().getKlasCode();					// klascode van de student opzoeken
		ArrayList<Student> studentenVanKlas = informatieSysteem.getStudentenVanKlas(klasCode);	// medestudenten opzoeken
		
		JsonArrayBuilder jab = Json.createArrayBuilder();						// Uiteindelijk gaat er een array...
		
		for (Student s : studentenVanKlas) {									// met daarin voor elke medestudent een JSON-object... 
			if (s.getGebruikersNaam().equals(gebruikersnaam)) 					// behalve de student zelf...
				continue;
			else {
				jab.add(Json.createObjectBuilder()
						.add("naam", s.toString()));
			}
		}
		conversation.sendJSONMessage(jab.build().toString());	// terug naar de Polymer-GUI!
	}
}
