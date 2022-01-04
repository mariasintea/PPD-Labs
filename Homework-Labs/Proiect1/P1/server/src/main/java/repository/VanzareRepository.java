package repository;

import domain.Sala;
import domain.Spectacol;
import domain.Vanzare;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class VanzareRepository {
    private SpectacolRepository spectacolRepository = new SpectacolRepository();
    private JDBCUtils jdbcUtils;

    public VanzareRepository() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("C:\\Users\\Maria\\Desktop\\P1\\server\\src\\main\\resources\\database.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbcUtils = new JDBCUtils(properties);
    }

    public Vanzare save(Vanzare vanzare, Sala sala){
        Spectacol spectacol = spectacolRepository.findOne(vanzare.getID_spectacol());
        if (sala.getNr_locuri() - spectacol.getLista_locuri_vandute().size() < vanzare.getNr_bilete_vandute())
            return null;
        int id = -1;
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Vanzari (data_vanzare, ID_spectacol) VALUES (?, ?)")) {
            preparedStatement.setString(1, vanzare.getData_vanzare());
            preparedStatement.setInt(2, vanzare.getID_spectacol());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM Vanzari")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt("id") > id)
                    id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Integer loc : vanzare.getLista_locuri_vandute()){
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO VanzariLocuri (id_vanzare,nr_loc,id_spectacol) VALUES (?, ?, ?)")) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, loc);
                preparedStatement.setInt(3, vanzare.getID_spectacol());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        vanzare.setId(id);

        List<Vanzare> vanzari = sala.getLista_vanzari();
        vanzari.add(vanzare);
        sala.setLista_vanzari(vanzari);

        Spectacol spectacol2 = spectacolRepository.findOne(vanzare.getID_spectacol());
        for (Spectacol s : sala.getLista_spectacole()){
            if (s.getID_spectacol() == spectacol2.getID_spectacol())
            {
                s.setLista_locuri_vandute(spectacol2.getLista_locuri_vandute());
                s.setSold(spectacol2.getSold());
            }
        }

        return vanzare;
    }

    public List<Vanzare> findAll(){
        Connection connection = jdbcUtils.getConnection();
        List<Vanzare> vanzari = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Vanzari")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Vanzare vanzare = new Vanzare();
                vanzare.setId(resultSet.getInt("id"));
                vanzare.setID_spectacol(resultSet.getInt("ID_spectacol"));
                vanzare.setData_vanzare(resultSet.getString("data_vanzare"));

                List<Integer> locuri = new ArrayList<>();
                try (PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT nr_loc FROM VanzariLocuri WHERE id_vanzare = ?")) {
                    preparedStatement2.setInt(1, resultSet.getInt("id"));
                    ResultSet resultSet2 = preparedStatement2.executeQuery();
                    while (resultSet2.next()) {
                        locuri.add(resultSet2.getInt("nr_loc"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                vanzare.setLista_locuri_vandute(locuri);
                vanzare.setNr_bilete_vandute(locuri.size());
                Spectacol spectacol = spectacolRepository.findOne(vanzare.getID_spectacol());
                vanzare.setSuma(spectacol.getPret_bilet() * locuri.size());

                vanzari.add(vanzare);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vanzari;
    }

    public void deleteAll(){
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM VanzariLocuri")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Vanzari")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
