package service;

import domain.Pair;
import domain.Sala;
import domain.Spectacol;
import domain.Vanzare;
import repository.SpectacolRepository;
import repository.VanzareRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Checker extends Thread {
    VanzareRepository vanzareRepository = new VanzareRepository();
    SpectacolRepository spectacolRepository = new SpectacolRepository();

    Sala sala;
    List<Vanzare> vanzari;

    List<Vanzare> vanzariVerificate;
    List<Pair<Integer, Integer>> sumeIncasateVerificate;
    int soldTotalVerificat;

    Writer writer = new Writer();

    Checker(Sala sala, List<Vanzare> vanzari) {
        this.sala = sala;
        this.vanzari = vanzari;
        this.vanzariVerificate = new ArrayList<>();
        this.sumeIncasateVerificate = new ArrayList<>();
        this.soldTotalVerificat = 0;
    }

    @Override
    public void run() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.YYYY  HH:mm");
        LocalDateTime now = LocalDateTime.now();
        writer.write(dtf.format(now)+"\n\n");
        while (true) {
            String output = "";
            List<Vanzare> vanzariDB;

            synchronized (vanzareRepository){
                vanzariDB = vanzareRepository.findAll();
            }

            for (Spectacol s : sala.getLista_spectacole()){
                output += "Spectacolul "+ s.getTitlu()+" are vandute locurile: "+s.getLista_locuri_vandute()+" si soldul total: "+s.getSold()+"\n";
                for (Vanzare v : vanzari) {
                    if (v.getID_spectacol() == s.getID_spectacol()) {
                        if (!vanzariVerificate.contains(v)) {
                            int sem = 0;
                            for (Vanzare v2: vanzariDB)
                                if (v2.getId() == v.getId())
                                {
                                    sem = 1;
                                    break;
                                }
                            if (sem == 0)
                                output += "       Vanzarea locurilor "+ v.getLista_locuri_vandute() + " cu suma totala de "+ v.getSuma() + " este incorecta\n";
                            else
                                output += "       Vanzarea locurilor "+ v.getLista_locuri_vandute() + " cu suma totala de "+ v.getSuma() + " este corecta\n";
                            vanzariVerificate.add(v);
                        }
                    }
                }
            }

            writer.write(output+"\n");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
