package model;

/**
 * Created by Gundraub on 24/03/16.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Les {
    Date beginTijd;
    Date eindTijd;

    private Vak hetVak; //?
    private Docent deDocent;
    private String hetLokaal;
    private Klas deKlas;

    public Les(String dat, String bT, String eT, Vak v, Docent d, String l, Klas k) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");

        beginTijd = inputFormat.parse(dat + bT);
        eindTijd = inputFormat.parse(dat + eT);
        hetVak = v;
        deDocent = d;
        hetLokaal = l;
        deKlas = k;
    }

    public Date getBeginTijd() {
        return beginTijd;
    }

    public Date getEindTijd() {
        return eindTijd;
    }

    public Vak getHetVak() {
        return hetVak;
    }

    public void setDeDocent(Docent d) {
        deDocent = d;
    }

    public Docent getDeDocent() {
        return deDocent;
    }

    public void setHetLokaal(String l) {
        hetLokaal = l;
    }

    public String getHetLokaal() {
        return hetLokaal;
    }

    public Klas getDeKlas() {
        return deKlas;
    }

    public String toString() {
        SimpleDateFormat datumFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat tijdFormat = new SimpleDateFormat("HH:mm");

        String result = "";

        result += "Op " + datumFormat.format(beginTijd) + " van " + tijdFormat.format(beginTijd) + " tot " + tijdFormat.format(eindTijd) + " in lokaal " + hetLokaal + ",\n";
        result += "wordt het vak " + hetVak.getVakNaam() + " met cursuscode " + hetVak.getVakCode() + " gegeven door ";
        result += deDocent.getGebruikersNaam() + " aan klas " + deKlas.getKlasCode();

        return result;
    }

}
