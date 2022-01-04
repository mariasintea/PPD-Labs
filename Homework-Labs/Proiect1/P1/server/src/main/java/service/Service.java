package service;

import domain.Sala;
import domain.Spectacol;
import domain.Vanzare;
import repository.SpectacolRepository;
import repository.VanzareRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Service implements ServiceInterface {
    private Sala sala;
    private SpectacolRepository spectacolRepository = new SpectacolRepository();
    private VanzareRepository vanzareRepository = new VanzareRepository();

    private ExecutorService executor = Executors.newFixedThreadPool(8);
    List<Vanzare> vanzari = new ArrayList<>();

    public Service() {
        vanzareRepository.deleteAll();
        sala = new Sala(100);
        sala.setLista_vanzari(vanzareRepository.findAll());
        sala.setLista_spectacole(spectacolRepository.findAll());
    }

    public void startChecker() {
        Thread checker = new Checker(sala,vanzari);
        checker.start();
    }

    @Override
    public boolean saveVanzare(Integer idSpectacol, Integer numarBilete, List<Integer> locuri) throws ExecutionException, InterruptedException {
        Spectacol spectacol = spectacolRepository.findOne(idSpectacol);
        if (spectacol == null) {
            return false;
        }

        LocalDate localDate = LocalDate.now();
        String s = localDate.format(DateTimeFormatter.ofPattern("dd-MM-YYYY"));
        Vanzare vanzare = new Vanzare(idSpectacol,s,numarBilete,locuri,spectacol.getPret_bilet() * numarBilete);

        Callable<Vanzare> worker = new Worker(vanzareRepository,spectacolRepository,vanzare,sala);
        Future<Vanzare> submit = executor.submit(worker);

        if (submit.get() != null)
            vanzare.setId(submit.get().getId());
        else
            vanzare.setId(-1);

        synchronized (vanzari) {
            vanzari.add(vanzare);
        }

        boolean isAdded = submit.get() != null;

        return isAdded;
    }
}
