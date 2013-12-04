/*
 * Copyright 2013 Michael Adams, CJ Done, Charles Eswine, Kyle Falconer,
 *  Will Gorman, Stephen Kaysen, Pat McCroskey and Matthew Swinney
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package courtscheduler.persistence;

import courtscheduler.domain.Team;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvReader {
   
   public CsvReader(String filename) {
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
               // team.setX(x);
               team.setTeamName(teamName);
               team.setYear(year);
               team.setGender(gender);
               team.setGrade(grade);
               team.setLevel(level);
               // team.setRequests(requests);
               // team.setNotSameTimeAs(notSameTimeAs);
            
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