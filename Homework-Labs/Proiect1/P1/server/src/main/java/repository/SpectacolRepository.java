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

public class SpectacolRepository {
    private JDBCUtils jdbcUtils;

    public SpectacolRepository() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("C:\\Users\\Maria\\Desktop\\P1\\server\\src\\main\\resources\\database.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jdbcUtils = new JDBCUtils(properties);
    }

    public Spectacol findOne(Integer idSpectacol){
        Spectacol spectacol = new Spectacol();
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Spectacole WHERE ID_spectacol = ?")) {
            preparedStatement.setInt(1, idSpectacol);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            else {
                spectacol.setID_spectacol(resultSet.getInt("ID_spectacol"));
                spectacol.setData_spectacol(resultSet.getString("data_spectacol"));
                spectacol.setTitlu(resultSet.getString("titlu"));
                spectacol.setPret_bilet(resultSet.getInt("pret_bilet"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Integer> locuri = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT nr_loc FROM VanzariLocuri WHERE id_spectacol = ?")) {
            preparedStatement.setInt(1, idSpectacol);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                locuri.add(resultSet.getInt("nr_loc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        spectacol.setLista_locuri_vandute(locuri);
        spectacol.setSold(spectacol.getPret_bilet() * spectacol.getLista_locuri_vandute().size());
        return spectacol;
    }

    public Spectacol save(Spectacol spectacol, Sala sala){
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Spectacole (data_spectacol, titlu, pret_bilet) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, spectacol.getData_spectacol());
            preparedStatement.setString(2, spectacol.getTitlu());
            preparedStatement.setInt(3, spectacol.getPret_bilet());
            preparedStatement.executeUpdate();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Spectacol> spectacole = sala.getLista_spectacole();
        spectacole.add(spectacol);
        sala.setLista_spectacole(spectacole);
        return spectacol;
    }

    public boolean isLocOcupat(Integer loc, Integer id_spectacol){
        Spectacol spectacol = findOne(id_spectacol);
        for (Integer locSpectacol: spectacol.getLista_locuri_vandute()){
            if (locSpectacol.equals(loc))
                return true;
        }
        return false;
    }

    public List<Spectacol> findAll(){
        Connection connection = jdbcUtils.getConnection();
        List<Spectacol> spectacole = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Spectacole")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Spectacol spectacol = new Spectacol();
                spectacol.setID_spectacol(resultSet.getInt("ID_spectacol"));
                spectacol.setData_spectacol(resultSet.getString("data_spectacol"));
                spectacol.setTitlu(resultSet.getString("titlu"));
                spectacol.setPret_bilet(resultSet.getInt("pret_bilet"));

                List<Integer> locuri = new ArrayList<>();
                try (PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT nr_loc FROM VanzariLocuri WHERE id_spectacol = ?")) {
                    preparedStatement2.setInt(1, resultSet.getInt("ID_spectacol"));
                    ResultSet resultSet2 = preparedStatement2.executeQuery();
                    while (resultSet2.next()) {
                        locuri.add(resultSet2.getInt("nr_loc"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                spectacol.setLista_locuri_vandute(locuri);
                spectacol.setSold(spectacol.getPret_bilet() * locuri.size());

                spectacole.add(spectacol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return spectacole;
    }
}
