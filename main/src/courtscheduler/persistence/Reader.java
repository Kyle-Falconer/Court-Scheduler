package courtscheduler.persistence;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import courtscheduler.persistence.Team;

public class Reader {
   
   public Reader(String filename) {
      BufferedReader br = null;
      InputStreamReader isr = null;
      FileInputStream fis = null;
      
      try {
         File file = new File(filename);
         System.out.println("File found...");
         
         fis = new FileInputStream(file);
         isr = new InputStreamReader(fis);
         br = new BufferedReader(isr);
         
         ArrayList<Team> teamList = new ArrayList<Team>();
         String line = null;
         
         System.out.println("Processing file...");
         br.readLine();

         while((line = br.readLine()) != null) {
            String[] splitLine = line.split(",",-1);
            
            Integer teamId = null;
            String x = "";
            String teamName = "";
            String year = "";
            String gender = "";
            Integer grade = null;
            String level = "";
            String requests = "";
            String notSameTimeAs = "";
            
            try {
               x = splitLine[1];
               
               String[] teamNameArray = splitLine[2].split("-", 2);               
               teamId = Integer.parseInt(teamNameArray[0].replace("*",""));
               teamName = teamNameArray[1];
                              
               year = splitLine[3];
               gender = splitLine[4];
               grade = Integer.parseInt(splitLine[5]); 
               level = splitLine[6];      
               requests = splitLine[7];
               notSameTimeAs = splitLine[8];
               
               Team team = new Team();
               team.setTeamId(teamId);
               team.setX(x);
               team.setTeamName(teamName);
               team.setYear(year);
               team.setGender(gender);
               team.setGrade(grade);
               team.setLevel(level);
               team.setRequests(requests);
               team.setNotSameTimeAs(notSameTimeAs);
            
               teamList.add(team);
              
            } catch (Exception e) {
               e.printStackTrace();   
               System.out.println("Line: " + line + " - NOT PROCESSED");
            }
            
         }
         
         System.out.println("Finished processing\n\n");
         
         for (Team team : teamList) {
            System.out.println(team.toString());
         }
         
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if(br != null)
               br.close();
            
            if(isr != null)
               isr.close();
            
            if(fis != null)   
               fis.close();
         } catch (Exception e) {
            // do nothing
         }         
      }      
   }

}