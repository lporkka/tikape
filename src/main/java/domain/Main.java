/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import classes.AineDao;
import classes.DrinkkiDao;
import classes.DrinkkiohjeDao;
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
        DrinkkiDao drinkit = new DrinkkiDao();
        DrinkkiohjeDao ohjeet = new DrinkkiohjeDao();
        
        Spark.get("/drinkkilista", (req, res) -> {
            HashMap map = new HashMap<>();
            ArrayList<String> drinkkilista = new ArrayList<>();
            drinkkilista = drinkit.findAll();
            
            map.put("drinkit", drinkkilista);
            return new ModelAndView(map,"index");
        }, new ThymeleafTemplateEngine());
        
        
        
        Spark.post("/drinkkilista", (req, res) -> {
           drinkit.saveOrUpdate(req.queryParams("name"));
           res.redirect("/drinkkilista");
           return "";
        });

        
        Spark.get("/drinkkilista/:nimi", (req, res) -> {
            HashMap map = new HashMap<>();
            Integer drinkkiKey = drinkit.findKey(req.params("nimi"));
            ArrayList<String> ohjelista = ohjeet.findRaakaAineet(drinkkiKey);
            ArrayList<String> aineet = raakaAineet.findAll();
            map.put("ohjeet", ohjelista);
            map.put("drinkinNimi",req.params("nimi"));
            map.put("aineet", aineet);
                       
            return new ModelAndView(map,"drinkki");
        }, new ThymeleafTemplateEngine());
                
                
        
        Spark.post("add/:nimi", (req, res) -> {
            int drinkkiKey = drinkit.findKey(req.params("nimi"));
            int aineKey = raakaAineet.findKey(req.queryParams("aine"));
            String maara = req.queryParams("maara");
            String ohje = req.queryParams("ohje");
            ohjeet.yhdista(drinkkiKey, aineKey, maara, ohje);
            res.redirect("/drinkkilista/" + req.params("nimi"));
            return "";
        });
                
                
        Spark.get("/raaka-aineet", (req, res) -> {
            HashMap map = new HashMap<>();
            ArrayList<String> aineet = new ArrayList<>();
                aineet = raakaAineet.findAll();
                
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
            raakaAineet.delete(req.params("nimi"));            
            res.redirect("/raaka-aineet");
            return "";
        });
    }

    

}
