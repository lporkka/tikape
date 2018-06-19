/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tapiiri
 */
public class AineDao {
    
    public String findOne(Integer key) throws SQLException {    
        try (Connection conn = getConnection();
                ResultSet rslts = conn.prepareStatement("SELECT nimi FROM RaakaAine WHERE id = " + key).executeQuery()) {
            String nimi = rslts.toString();
            rslts.close();
            conn.close();
            return nimi;
            }                  
    }

    public ArrayList<String> findAll() throws SQLException {
        ArrayList<String> lista = new ArrayList<>();
        try (Connection conn = getConnection();
                ResultSet rslts = conn.prepareStatement("SELECT nimi FROM RaakaAine").executeQuery()) {
            
            while (rslts.next()) {
                lista.add(rslts.getString("nimi"));
            }
            rslts.close();
            conn.close();
        }
        return lista;
    }
    
    public void delete(String nimi) throws SQLException {
        try (Connection conn = getConnection()) {    
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM RaakaAine WHERE nimi = ?");
            stmt.setString(1, nimi);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }         
        
    }
    
    public void saveOrUpdate(String uusi) throws SQLException {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT nimi FROM RaakaAine WHERE nimi = '" + uusi + "'");
            ResultSet rslt = stmt.executeQuery();
            int i = 0;
            while (rslt.next()) {
                i++;
            }
            rslt.close();
            if (i == 0) {
                PreparedStatement add = conn.prepareStatement("INSERT INTO RaakaAine (nimi) VALUES (?)");
                add.setString(1, uusi);
                add.executeUpdate();                
                add.close();                
            }
            conn.close();
        }
    }

    public Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:sqlite:harjoitus.db");
    }
}
