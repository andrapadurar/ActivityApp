package Repository;

import Domain.Activitate;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileRepository {

    private List<Activitate> activitati;
    private String fileName;

    public FileRepository(String fileName) {
        this.fileName = fileName;
        this.activitati = new ArrayList<>();
        readFromFile();
    }

    public List<Activitate> getActivitati() {
        return activitati;
    }

    public void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 3) {
                    System.err.println("Linie invalidă: " + line);
                    continue;
                }

                LocalDate zi = LocalDate.parse(parts[0].trim(), formatter);
                int pasi = Integer.parseInt(parts[1].trim());
                int oresomn = Integer.parseInt(parts[2].trim());

                if (parts.length > 3 && !parts[3].isBlank()) {
                    for (int i = 3; i < parts.length; i++) {
                        String[] activitateParts = parts[i].split(",");
                        String descriere = activitateParts.length > 0 ? activitateParts[0].trim() : "";
                        String minuteStr = activitateParts.length > 1 ? activitateParts[1].trim() : "";

                        if (!descriere.isEmpty() || !minuteStr.isEmpty()) {
                            int minute = !minuteStr.isEmpty() ? Integer.parseInt(minuteStr) : 0;
                            Activitate activitate = new Activitate(minute, descriere, oresomn, pasi, zi);
                            activitati.add(activitate);
                        }
                    }
                } else {
                    Activitate activitate = new Activitate(0, "", oresomn, pasi, zi);
                    activitati.add(activitate);
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fișierului: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Eroare la procesarea liniei: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

            for (Activitate activitate : activitati) {
                StringBuilder line = new StringBuilder();
                line.append(activitate.getZi().format(formatter)).append(";")
                        .append(activitate.getPasi()).append(";")
                        .append(activitate.getOresomn());

                if (!activitate.getDescriere().isEmpty() || activitate.getMinute() > 0) {
                    line.append(";").append(activitate.getDescriere());
                    if (activitate.getMinute() > 0) {
                        line.append(", ").append(activitate.getMinute());
                    }
                }

                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Eroare la salvarea fișierului: " + e.getMessage());
        }
    }

    public void addActivitate(Activitate activitate) {
        activitati.add(activitate);
        saveToFile();
    }

}
