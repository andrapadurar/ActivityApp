package Domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Activitate {
    private LocalDate zi;
    private int pasi;
    private int oresomn;
    private String descriere;
    private int minute;

    public Activitate(int minute, String descriere, int oresomn, int pasi, LocalDate zi) {
        this.minute = minute;
        this.descriere = descriere;
        this.oresomn = oresomn;
        this.pasi = pasi;
        this.zi = zi;
    }

    public LocalDate getZi() {
        return zi;
    }

    public int getPasi() {
        return pasi;
    }

    public int getOresomn() {
        return oresomn;
    }

    public String getDescriere() {
        return descriere;
    }

    public int getMinute() {
        return minute;
    }

    public void setZi(LocalDate zi) {
        this.zi = zi;
    }

    public void setPasi(int pasi) {
        this.pasi = pasi;
    }

    public void setOresomn(int oresomn) {
        this.oresomn = oresomn;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = zi.format(formatter);
        return formattedDate + "; " + pasi + "; " + oresomn + "; " + (descriere != null && !descriere.isEmpty() ? descriere : "") + (minute > 0 ? ", " + minute : "");
    }

}
