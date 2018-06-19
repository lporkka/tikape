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
public class DrinkkiohjeDao {
    
    public String findOne(Integer key) throws SQLException {    
        try (Connection conn = getConnection();
                ResultSet rslts = conn.prepareStatement("SELECT nimi FROM DrinkkiRaakaAine WHERE id = " + key).executeQuery()) {
            String nimi = rslts.toString();
            rslts.close();
            conn.close();
            return nimi;
            }                  
    }

    public ArrayList<String> findAll() throws SQLException {
        ArrayList<String> lista = new ArrayList<>();
        try (Connection conn = getConnection();
                ResultSet rslts = conn.prepareStatement("SELECT nimi FROM DrinkkiRaakaAine").executeQuery()) {
            
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
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM DrinkkiRaakaAine WHERE nimi = ?");
            stmt.setString(1, nimi);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }         
        
    }
    
    public void saveOrUpdate(String uusi) throws SQLException {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT nimi FROM DrinkkiRaakaAine WHERE nimi = '" + uusi + "'");
            ResultSet rslt = stmt.executeQuery();
            int i = 0;
            while (rslt.next()) {
                i++;
            }
            rslt.close();
            if (i == 0) {
                PreparedStatement add = conn.prepareStatement("INSERT INTO DrinkkiRaakaAine (nimi) VALUES (?)");
                add.setString(1, uusi);
                add.executeUpdate();                
                add.close();                
            }
            conn.close();
        }
    }
    
    public void yhdista(Integer drinkkiKey, Integer raakaAineKey, Integer jarjestys, String maara, String ohje) throws SQLException {
        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO DrinkkiRaakaAine (drinkki_id, raakaAine_id, jarjestys, maara, ohje) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, drinkkiKey);
            stmt.setInt(2, raakaAineKey);
            stmt.setInt(3, jarjestys);
            stmt.setString(4, maara);
            stmt.setString(5, ohje);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
        }
    }
    
    public ArrayList<String>

    public Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if (dbUrl != null && dbUrl.length() > 0) {
            return DriverManager.getConnection(dbUrl);
        }

        return DriverManager.getConnection("jdbc:sqlite:harjoitus.db");
    }
}