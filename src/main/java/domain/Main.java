/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import classes.AineDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 *
 * @author tapiiri
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        AineDao raakaAineet = new AineDao();

        Spark.get("/raaka-aineet", (req, res) -> {
            HashMap map = new HashMap<>();
            ArrayList<String> aineet = new ArrayList<>();
            try {
                aineet = raakaAineet.findAll();
            } catch (SQLException ex) {
            }
            
            map.put("aineet", aineet);
            return new ModelAndView(map,"aineet");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/raaka-aineet", (req, res) -> {
            String uusi = req.queryParams("name");
            raakaAineet.saveOrUpdate(uusi);
            
            res.redirect("/raaka-aineet");
            return "";
        });
        
        Spark.post("delete/:nimi", (req, res) -> {
            raakaAineet.delete(":nimi");            
            res.redirect("/raaka-aineet");
            return "";
        });
    }

    

}
