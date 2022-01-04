package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sala implements Serializable {
    private int nr_locuri;
    private List<Vanzare> lista_vanzari = new ArrayList<>();
    private List<Spectacol> lista_spectacole = new ArrayList<>();

    public Sala() {
    }

    public Sala(int nr_locuri) {
        this.nr_locuri = nr_locuri;
    }

    public int getNr_locuri() {
        return nr_locuri;
    }

    public void setNr_locuri(int nr_locuri) {
        this.nr_locuri = nr_locuri;
    }

    public List<Vanzare> getLista_vanzari() {
        return lista_vanzari;
    }

    public void setLista_vanzari(List<Vanzare> lista_vanzari) {
        this.lista_vanzari = lista_vanzari;
    }

    public List<Spectacol> getLista_spectacole() {
        return lista_spectacole;
    }

    public void setLista_spectacole(List<Spectacol> lista_spectacole) {
        this.lista_spectacole = lista_spectacole;
    }

    @Override
    public String toString() {
        return "Sala{" +
                "nr_locuri=" + nr_locuri +
                ", lista_vanzari=" + lista_vanzari +
                ", lista_spectacole=" + lista_spectacole +
                '}';
    }
}
