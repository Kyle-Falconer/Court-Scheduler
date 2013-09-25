package courtscheduler.persistence;

public class Team {

   private Integer teamId;
   private String x = "";
   private String teamName;
   private String year = "";
   private String gender;
   private Integer grade;
   private String level;
   private String requests;
   private String notSameTimeAs = "";
   
   public Team() {
   }
   
   public void setTeamId(Integer teamId) {
      this.teamId = teamId;
   }
   
   public Integer getTeamId() {
      return teamId;
   }
   
   public void setX(String x) {
      this.x = x;
   }
   
   public String getX() {
      return x;
   }
   
   public void setTeamName(String teamName) {
      this.teamName = teamName;
   }
   
   public String getTeamName() {
      return teamName;
   }
   
   public void setYear(String year) {
      this.year = year;
   }
   
   public String getYear() {
      return year;
   }
   
   public void setGender(String gender) {
      this.gender = gender;
   }
   
   public String getGender() {
      return gender;
   }
   
   public void setGrade(Integer grade) {
      this.grade = grade;
   }
   
   public Integer getGrade() {
      return grade;
   }
   
   public void setLevel(String level) {
      this.level = level;
   }
   
   public String getLevel() {
      return level;
   }
   
   public void setRequests(String requests) {
      this.requests = requests;
   }
   
   public String getRequests() {
      return requests;
   }
   
   public void setNotSameTimeAs(String notSameTimeAs) {
      this.notSameTimeAs = notSameTimeAs;
   }
   
   public String getNotSameTimeAs() {
      return notSameTimeAs;
   }
   
   @Override
   public String toString() {   
      return teamId.toString() + "~" + x + "~" + teamName + "~" + year + "~" + gender + "~" + grade.toString() + "~" + level + "~" + requests + "~" + notSameTimeAs;
   }                                                      
     
}   