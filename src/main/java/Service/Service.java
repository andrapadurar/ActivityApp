package Service;

import Domain.Activitate;
import Repository.FileRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
    private final FileRepository repository;

    public Service(FileRepository repository) {
        this.repository = repository;
    }

    public void addOrUpdateActivitate(int minute, String descriere, int oresomn, int pasi, LocalDate zi) {
        boolean updated = false;
        for (Activitate activitate : repository.getActivitati()) {
            if (activitate.getZi().equals(zi)) {
                activitate.setOresomn(oresomn);
                activitate.setPasi(pasi);
                updated = true;
                break;
            }
        }
        if (!updated) {
            Activitate newActivitate = new Activitate(minute, descriere, oresomn, pasi, zi);
            repository.addActivitate(newActivitate);
        }

        repository.saveToFile();
    }


    public List<Activitate> getAllActivitati() {
        return repository.getActivitati();
    }

    public List<Activitate> filterActivitatiByMinutes(Integer minMinutes, Integer maxMinutes) {
        return repository.getActivitati().stream()
                .filter(activitate -> (minMinutes == null || activitate.getMinute() >= minMinutes) &&
                        (maxMinutes == null || activitate.getMinute() <= maxMinutes))
                .collect(Collectors.toList());
    }
}
