import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataAccess {
    private Connection conn = null;
    private Statement stmt;

    
    public DataAccess(){
        makeConnection(); // bruges til at lave forbindelse, når objektet initialiseres. 
        createTable(); //opretter tabellen hvis den ikke eksistere i forvejen
    }
    
    public void makeConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:database.db");
         } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         }
        //System.out.println("Opened database successfully"); - whitebox test?
    }
    
    public void createTable(){
        try{   
            stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM SensorValues");        
        } 
        catch (Exception e) {
            try {
                stmt.executeUpdate("CREATE TABLE SensorValues(id INT PRIMARY KEY AUTO INCREMENT NOT NULL, value INT, type TEXT, time TIMESTAMP)");
            } catch (SQLException ex) {
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
   
    public void setEKG(int value){
        
        try{
        stmt.executeUpdate("INSERT INTO SensorValue(type,value,time) VALUES ('EKG', "+value+", NOW())");
     
        }   catch(Exception e){
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, e);
            }
    }
    
    public void setPulse(int value){
        try{
        stmt.executeUpdate("INSERT INTO SensorValue(type,value,time) VALUES ('pulse', "+value+", NOW())");
     
        }   catch(Exception e){
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, e);
            }
    }
    
    public ArrayList<Integer> getEKG(){
        ArrayList<Integer> data = new ArrayList<Integer>();
        
        try {
            ResultSet r = stmt.executeQuery("SELECT * FROM SensorValues WHERE type = 'EKG' ORDER BY id DESC LIMIT 10");
            
            while(r.next()){ 
                data.add(0,r.getInt("type"));               
            }

        }   catch (Exception e) {
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, e);
            }
           return data;
    }
    
    public ArrayList<Integer> getPulse(){ 
        ArrayList<Integer> data = new ArrayList<Integer>();
        
        try {
            ResultSet r = stmt.executeQuery("SELECT * FROM SensorValues WHERE type = 'pulse' ORDER BY id DESC LIMIT 10");
            
            while(r.next()){ 
                data.add(0,r.getInt("type"));               
            }

        }   catch (Exception e) {
                Logger.getLogger(DataAccess.class.getName()).log(Level.SEVERE, null, e);
            }
           return data;   
    }
}