package model;

/**
 * Created by Gundraub on 30/03/16.
 */

public class Presentie {

    private Student deStudent;
    private Les deLes;

    public enum Present {
        AANWEZIG, AFWEZIG, ZIEK, ONBEKEND, LEEG
    }

    private Present status;

    public Presentie(Student s, Les l ) {
        deStudent = s;
        deLes = l;
    }

    public Student getDeStudent() {
        return deStudent;
    }

    public Les getDeLes() {
        return deLes;
    }

    public void setStatus(Present s) {
        status = s;
    }

    public Present getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = obj instanceof Presentie;

        result = result && ((Presentie)obj).getDeStudent().equals(deStudent);
        result = result && ((Presentie)obj).getDeLes().equals(deLes);

        return result;
    }

}
