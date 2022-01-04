package service;

import domain.Vanzare;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ServiceInterface extends Remote {
    boolean saveVanzare(Integer idSpectacol, Integer numarBilete, List<Integer> locuri) throws RemoteException, ExecutionException, InterruptedException;
}
