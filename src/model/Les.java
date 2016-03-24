package model;

/**
 * Created by Gundraub on 24/03/16.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Les {
    SimpleDateFormat datumFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date datum;

    SimpleDateFormat tijdFormat = new SimpleDateFormat("HH:mm");
    Date beginTijd;
    Date eindTijd;

    private Vak hetVak; //?

    private Docent deDocent;

    private String hetLokaal;

    private Klas deKlas;

    public Les(String dat, String bT, String eT, Vak v, Docent d, String l, Klas k) throws ParseException {
        datum = datumFormat.parse(dat);
        beginTijd = tijdFormat.parse(bT);
        eindTijd = tijdFormat.parse(eT);
        hetVak = v;
        deDocent = d;
        hetLokaal = l;
        deKlas = k;
    }

    public String toString() {
        String result = "";

        result += "Op " + datum + " van " + beginTijd + " tot " + eindTijd + " in lokaal " + hetLokaal + ",\n";
        result += "wordt het vak " + hetVak.getVakNaam() + " met cursuscode " + hetVak.getVakCode() + " gegeven door ";
        result += deDocent.getGebruikersNaam() + " aan klas " + deKlas.getKlasCode();

        return result;
    }

}
