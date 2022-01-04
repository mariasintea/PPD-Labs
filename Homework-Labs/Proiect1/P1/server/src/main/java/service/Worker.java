package service;

import domain.Sala;
import domain.Vanzare;
import repository.SpectacolRepository;
import repository.VanzareRepository;

import java.util.concurrent.Callable;

public class Worker implements Callable<Vanzare> {
    private VanzareRepository vanzareRepository;
    private SpectacolRepository spectacolRepository;
    private Vanzare vanzare;
    private Sala sala;

    public Worker(VanzareRepository vanzareRepository,
                  SpectacolRepository spectacolRepository,
                  Vanzare vanzare, Sala sala) {
        this.vanzareRepository = vanzareRepository;
        this.spectacolRepository = spectacolRepository;
        this.vanzare = vanzare;
        this.sala = sala;
    }

    @Override
    public Vanzare call() {
        for (Integer loc : vanzare.getLista_locuri_vandute()) {
            synchronized (spectacolRepository) {
                if (spectacolRepository.isLocOcupat(loc, vanzare.getID_spectacol())) {
                    return null;
                }
            }
        }

        synchronized (vanzareRepository) {
            return vanzareRepository.save(vanzare, sala);
        }
    }
}
