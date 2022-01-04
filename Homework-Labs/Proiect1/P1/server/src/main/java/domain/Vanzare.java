package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Vanzare implements Serializable {
    private int id;
    private int ID_spectacol;
    private String data_vanzare;
    private int nr_bilete_vandute;
    private List<Integer> lista_locuri_vandute = new ArrayList<>();
    private int suma;

    public Vanzare() {
    }

    public Vanzare(int ID_spectacol, String data_vanzare, int nr_bilete_vandute, List<Integer> lista_locuri_vandute, int suma) {
        this.ID_spectacol = ID_spectacol;
        this.data_vanzare = data_vanzare;
        this.nr_bilete_vandute = nr_bilete_vandute;
        this.lista_locuri_vandute = lista_locuri_vandute;
        this.suma = suma;
    }

    public Vanzare(int id, int ID_spectacol, String data_vanzare, int nr_bilete_vandute, List<Integer> lista_locuri_vandute, int suma) {
        this.id = id;
        this.ID_spectacol = ID_spectacol;
        this.data_vanzare = data_vanzare;
        this.nr_bilete_vandute = nr_bilete_vandute;
        this.lista_locuri_vandute = lista_locuri_vandute;
        this.suma = suma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getID_spectacol() {
        return ID_spectacol;
    }

    public void setID_spectacol(int ID_spectacol) {
        this.ID_spectacol = ID_spectacol;
    }

    public String getData_vanzare() {
        return data_vanzare;
    }

    public void setData_vanzare(String data_vanzare) {
        this.data_vanzare = data_vanzare;
    }

    public int getNr_bilete_vandute() {
        return nr_bilete_vandute;
    }

    public void setNr_bilete_vandute(int nr_bilete_vandute) {
        this.nr_bilete_vandute = nr_bilete_vandute;
    }

    public List<Integer> getLista_locuri_vandute() {
        return lista_locuri_vandute;
    }

    public void setLista_locuri_vandute(List<Integer> lista_locuri_vandute) {
        this.lista_locuri_vandute = lista_locuri_vandute;
    }

    public int getSuma() {
        return suma;
    }

    public void setSuma(int suma) {
        this.suma = suma;
    }

    @Override
    public String toString() {
        return "Vanzare{" +
                "id=" + id +
                ", ID_spectacol=" + ID_spectacol +
                ", data_vanzare='" + data_vanzare + '\'' +
                ", nr_bilete_vandute=" + nr_bilete_vandute +
                ", lista_locuri_vandute=" + lista_locuri_vandute +
                ", suma=" + suma +
                '}';
    }
}
