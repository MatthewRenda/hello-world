package src_files;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.time.*;


public class TMModel implements ITMModel {
	
    public boolean startTask(String taskName) {
        String time = LocalDateTime.now().toString();
        Statement stm = null;
        Connection c = null;


        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);
           
            stm = c.createStatement();
            String sql = "INSERT INTO Logs (name,start)VALUES (?, ? )";
            PreparedStatement pstmt= c.prepareStatement(sql);
            pstmt.setString(1,taskName);
            pstmt.setString(2,time);
            pstmt.executeUpdate();
           
            stm.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        return true;
    }

    public boolean stopTask(String taskName) {

        String time = LocalDateTime.now().toString();
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);

            String sql = "UPDATE Logs set stop = ? where name = ?";

            PreparedStatement pstmt= c.prepareStatement(sql);
            pstmt.setString(2,taskName);
            pstmt.setString(1,time);

            pstmt.executeUpdate();
            c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return true;
    }

    public boolean sizeTask(String taskName, String size) {

        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);

            String sql = "UPDATE Logs set size = ? where name = ?";

            PreparedStatement pstmt= c.prepareStatement(sql);
            pstmt.setString(1, size);
            pstmt.setString(2,taskName);

            pstmt.executeUpdate();
            c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return true;
    }

    public boolean deleteTask (String taskName){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);

            String sql = "DELETE FROM Logs WHERE  name = ?";

            PreparedStatement pstmt= c.prepareStatement(sql);
            pstmt.setString(1, taskName);

            pstmt.executeUpdate();
            c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return true;
    }
    
    
    public boolean describeTask(String taskName, String description) {
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);

            String sql = "UPDATE Logs set description = ? where name = ?";

            PreparedStatement pstmt= c.prepareStatement(sql);
            pstmt.setString(1,description);
            pstmt.setString(2,taskName);

            pstmt.executeUpdate();
            c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    	
    	return true;
    }
    
    public boolean renameTask (String oldName, String newName){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);

            String sql = "UPDATE Logs set name = ? where name = ?";

            PreparedStatement pstmt= c.prepareStatement(sql);
            pstmt.setString(1,newName);
            pstmt.setString(2,oldName);

            pstmt.executeUpdate();
            c.commit();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return true;
       
    }
    
    //SQL implemented and fully functional
    //Should not be constantly printing. It should only print the taskElapsedTime
    public String taskElapsedTime(String taskName) {
    	/*
    	 * Credit:
    	 * Retrieving Task Information from SQLDatabase:
    	 * https://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html
    	 */
    	
    	//ArrayList to hold the time values?
    	List<String> arr = new ArrayList<>();
    	
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);
            
            stmt = c.createStatement();
            
            String sql = "select name, start, stop from Logs";

            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	//Get the information from the SQL database for the task
            	String name = rs.getString("name");
            	String start = rs.getString("start");
            	String stop = rs.getString("stop");

            	if (name.equals(taskName)) {
            		arr.add(start);
            		arr.add(stop);
            	}
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        /*
        System.out.println("ElapsedTimeForTask: " + taskName);
        for (int l = 0; l < arr.size(); l = l + 2) {
            System.out.println("Start" + arr.get(l));
            System.out.println("Stop " + arr.get(l + 1));
        }
        */
        
        long seconds = 0;
        
        for (int j = 0; j < arr.size(); j = j + 2) {
            LocalDateTime startDate = LocalDateTime.parse(arr.get(j));
            LocalDateTime stopDate = LocalDateTime.parse(arr.get(j+1));
            seconds += Duration.between(startDate, stopDate).getSeconds();
        }
        
        long diffSec = seconds % 60;
        long diffMin = seconds / 60 % 60;
        long diffH = seconds / (60 * 60) % 24;
        long diffD = seconds / (24 * 60 * 60);
        /*
        System.out.println("The total time spent on " + taskName + " is:");
        System.out.print(diffD + "D:");
        System.out.print(diffH + "h:");
        System.out.print(diffMin + "m:");
        System.out.println(diffSec + "s");
        */
        String time =  diffD + "D:" + diffH + "h:" + diffMin + "m:" + diffSec + "s";
        
        return time;
    }

    public String taskSize(String taskName) {
       
    	 Connection c = null;
         Statement stmt = null;
         try {
             Class.forName("org.sqlite.JDBC");
             c = DriverManager.getConnection("jdbc:sqlite:Log.db");
             c.setAutoCommit(false);
             
             stmt = c.createStatement();
             
             String sql = "SELECT name, size from Logs";

             ResultSet rs = stmt.executeQuery(sql);
             
             while (rs.next()) {
             	//Get the information from the SQL database for the task
            	 String name = rs.getString("name");
            	 String size = rs.getString("size");

             	if (name.equals(taskName)) {
             		
             		/*
             		 * return size;
             		 * or
             		 * System.ou.println("Task size for " + taskName + " is " + size\n";
             		 */
             		
             		
             	}
             }
         }
         catch (Exception e) {
             System.err.println(e.getClass().getName() + ": " + e.getMessage());
         }
        return null;
    }
    
    public String taskDescription(String taskName){
    	 Connection c = null;
         Statement stmt = null;
         try {
             Class.forName("org.sqlite.JDBC");
             c = DriverManager.getConnection("jdbc:sqlite:Log.db");
             c.setAutoCommit(false);
             
             stmt = c.createStatement();
             
             String sql = "SELECT name, description from Logs";

             ResultSet rs = stmt.executeQuery(sql);
             
             while (rs.next()) {
             	//Get the information from the SQL database for the task
            	 String name = rs.getString("name");
            	 String description = rs.getString("description");

             	if (name.equals(taskName)) {
             		
             		/*
             		 * return description;
             		 * or
             		 * System.ou.println(" Task name: " + taskName + "\n" + " Description: " + description\n";
             		 */     		
             	}
             }
         }
         catch (Exception e) {
             System.err.println(e.getClass().getName() + ": " + e.getMessage());
         }
        return null;
    }
    
    //Does not need SQL implementation
    public String minTimeForSize(String size) {
        TreeSet<String> set = new TreeSet<>();
        
        for (String s: taskNamesForSize(size)){
            set.add(taskElapsedTime(s));
        }
        return  set.first().toString();
    }
    
    //Does not need SQL implmentation
    public String maxTimeForSize(String size){
        TreeSet <String> set = new TreeSet<>();
        for (String s: taskNamesForSize(size)){
            set.add(taskElapsedTime(s));
        }
        return  set.last().toString();
    }
    
    //Implemented but Needs Testing
    public String avgTimeForSize(String size){
    	String average = "";
    	
    	//Parse does not work correctly
    	ArrayList<Integer> totalTimeInSeconds = new ArrayList<>();
    	
    	for (String taskName: taskNamesForSize(size)) {
    		//split the taskElapsedTime format(#D:#h:#m:#s);
    		String[] token = taskElapsedTime(taskName).split("D:|h:|m:|s");

    		//Converts the string time tokens into seconds and add to the arraylist
    		totalTimeInSeconds.add(Integer.parseInt(token[0]) * 24 * 60 * 60 + 
    							   Integer.parseInt(token[1]) * 60 * 60 +
    							   Integer.parseInt(token[2]) * 60 +
    							   Integer.parseInt(token[3])); //Not sure if this works
    	}
    	
    	//Caculate the average;
    	int avg = 0;
    	for (int i : totalTimeInSeconds) {
    		avg += i;
    	}
    	avg /= totalTimeInSeconds.size();
    	
    	//Does not include day
    	average = avg / 3600 + "h:" + avg % 60 / 60 + "m:" + avg % 60 + "s";
    	
    	return average;
    }
    
    // SQL implemented
    // Needs testing
    public Set<String> taskNamesForSize(String size){
    	//Set to obtain and hold unique values of taskNames
    	Set<String> set = new TreeSet<>();
    	
    	//Connection to Database
    	Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);
            
            stmt = c.createStatement();
            
            String sql = "select name, size from Logs";

            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	if (rs.getString("size").equals(size)) {
            		set.add(rs.getString("name"));
            	}
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return set;
    }
    
    //Implemented
    public String elapsedTimeForAllTasks(){
    
    ArrayList<String> elapseTime = new ArrayList<>();
    
    //Connection to Database
	Connection c = null;
    Statement stmt = null;
    try {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:Log.db");
        c.setAutoCommit(false);
        
        stmt = c.createStatement();
        
        String sql = "select name from Logs";

        ResultSet rs = stmt.executeQuery(sql);
        
        //get the elapse time for all tasks
        while (rs.next()) {
        	elapseTime.add(taskElapsedTime(rs.getString("name")));
        }
    } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
    }
    
    //Sum the time of all tasks
    int totalTimeSeconds = 0;
    for (String i: elapseTime) {
    	String token[] = i.split("D:|h:|m:|s");
    	
    	//Convert to seconds to properly add the elapsed time of all seconds
    	totalTimeSeconds += Integer.parseInt(token[0])*24*60*60;
    	totalTimeSeconds += Integer.parseInt(token[1])*60*60;
    	totalTimeSeconds += Integer.parseInt(token[2])*60;
    	totalTimeSeconds += Integer.parseInt(token[3]);
    }
    String totalTime = Integer.toString(totalTimeSeconds/60/60) + "h:"
    				 + Integer.toString(totalTimeSeconds/60%60) + "m:"
    				 + Integer.toString(totalTimeSeconds%3600)  + "s";
    
    //StringFormat: #h:#m:#s
    return totalTime;
    }
    
    //Finished implementation
    public Set<String> taskNames(){
    	//Set to hold unique taskNames
        Set <String> set = new TreeSet<>();
        
    	//Connection to Database
    	Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);
            
            stmt = c.createStatement();
            
            String sql = "select name from Logs";

            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	//names are never null so it doesnt need to be checked
            	set.add(rs.getString("name"));
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
                
        return set;
    }

    //Finished implementation
    public Set<String> taskSizes(){
    	
    	//Set to obtain and hold unique values of size for each task
    	Set<String> set = new TreeSet<>();
    	
    	//Connection to Database
    	Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Log.db");
            c.setAutoCommit(false);
            
            stmt = c.createStatement();
            
            String sql = "select size from Logs";

            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
            	if (rs.getString("size") != null) {
            		set.add(rs.getString("size"));
            	}
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return set;
    }
}