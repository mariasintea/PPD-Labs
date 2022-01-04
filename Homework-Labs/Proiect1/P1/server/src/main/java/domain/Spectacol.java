package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Spectacol implements Serializable {
    private int ID_spectacol;
    private String data_spectacol;
    private String titlu;
    private int pret_bilet;
    private List<Integer> lista_locuri_vandute = new ArrayList<>();
    private int sold;

    public Spectacol() {
    }

    public Spectacol(int ID_spectacol, String data_spectacol, String titlu, int pret_bilet) {
        this.ID_spectacol = ID_spectacol;
        this.data_spectacol = data_spectacol;
        this.titlu = titlu;
        this.pret_bilet = pret_bilet;
    }

    public Spectacol(int ID_spectacol, String data_spectacol, String titlu, int pret_bilet, List<Integer> lista_locuri_vandute, int sold) {
        this.ID_spectacol = ID_spectacol;
        this.data_spectacol = data_spectacol;
        this.titlu = titlu;
        this.pret_bilet = pret_bilet;
        this.lista_locuri_vandute = lista_locuri_vandute;
        this.sold = sold;
    }

    public int getID_spectacol() {
        return ID_spectacol;
    }

    public void setID_spectacol(int ID_spectacol) {
        this.ID_spectacol = ID_spectacol;
    }

    public String getData_spectacol() {
        return data_spectacol;
    }

    public void setData_spectacol(String data_spectacol) {
        this.data_spectacol = data_spectacol;
    }

    public String getTitlu() {
        return titlu;
    }

    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }

    public int getPret_bilet() {
        return pret_bilet;
    }

    public void setPret_bilet(int pret_bilet) {
        this.pret_bilet = pret_bilet;
    }

    public List<Integer> getLista_locuri_vandute() {
        return lista_locuri_vandute;
    }

    public void setLista_locuri_vandute(List<Integer> lista_locuri_vandute) {
        this.lista_locuri_vandute = lista_locuri_vandute;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return "Spectacol{" +
                "ID_spectacol=" + ID_spectacol +
                ", data_spectacol='" + data_spectacol + '\'' +
                ", titlu='" + titlu + '\'' +
                ", pret_bilet=" + pret_bilet +
                ", lista_locuri_vandute=" + lista_locuri_vandute +
                ", sold=" + sold +
                '}';
    }
}
