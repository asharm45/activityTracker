/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author home
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHandler extends Configs {

    protected Connection dbconnection;

    public Connection getConnection() {
        final String dbURL = "jdbc:mysql://" + Configs.dbhost + ":" + Configs.dbport + "/" + Configs.dbname;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        try {
            dbconnection = DriverManager.getConnection(dbURL, Configs.dbuser, Configs.dbpass);
            
            

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return dbconnection;
    }

}
