package courtscheduler.domain;

import java.util.List;

public class Team {

    private Integer teamId;
    private String teamName;
    private String year = "";
    private String gender;
    private Integer grade;
    private String level;
    private Integer conference;

    private DoubleHeaderPreference doubleHeaderPreference;
    private BackToBackPreference backToBackPreference;
    private DateConstraint dateConstraint;
    private OffTimes offTimes;
    private SharedTeams sharedTeams;
    private PlayOnceRequests playOnceRequests;
    private PreferredDates preferredDates;

    private MatchAvailability avail;


    public Team() {
		sharedTeams = new SharedTeams();
    }

    public void setTeamId(Integer teamId) {
      this.teamId = teamId;
    }

    public Integer getTeamId() {
      return teamId;
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

    public Integer getConference() {
        return conference;
    }

    public void setConference(Integer conference) {
        this.conference = conference;
    }

    public DoubleHeaderPreference getDoubleHeaderPreference() {
        return doubleHeaderPreference;
    }

    public void setDoubleHeaderPreference(DoubleHeaderPreference doubleHeaderPreference) {
        this.doubleHeaderPreference = doubleHeaderPreference;
    }

    public BackToBackPreference getBackToBackPreference() {
        return backToBackPreference;
    }

    public void setBackToBackPreference(BackToBackPreference backToBackPreference) {
        this.backToBackPreference = backToBackPreference;
    }

    public DateConstraint getDateConstraint() {
        return dateConstraint;
    }

    public void setDateConstraint(DateConstraint dateConstraint) {
        this.dateConstraint = dateConstraint;
    }

    public OffTimes getOffTimes() {
        return offTimes;
    }

    public void setOffTimes(OffTimes offTimes) {
        this.offTimes = offTimes;
    }

    public SharedTeams getSharedTeams() {
        return sharedTeams;
    }

    public void setSharedTeams(SharedTeams sharedTeams) {
        this.sharedTeams = sharedTeams;
    }

    public PlayOnceRequests getPlayOnceRequests() {
        return playOnceRequests;
    }

    public void setPlayOnceRequests(PlayOnceRequests playOnceRequests) {
        this.playOnceRequests = playOnceRequests;
    }

    public PreferredDates getPreferredDates() {
        return preferredDates;
    }

    public void setPreferredDates(PreferredDates preferredDates) {
        this.preferredDates = preferredDates;
    }

    @Override
    public String toString() {
      return teamId.toString() + "~" + teamName + "~" + year + "~" + gender + "~" + grade.toString() + "~" + level;
    }

    public MatchAvailability getAvailability() {
        return this.avail;
    }

    public void compileMatchAvaiilability(){
        this.avail.badDates=this.getDateConstraint();
        this.avail.prefDates=this.getPreferredDates();
        this.avail.dontPlay=this.getSharedTeams();
    }

	public static boolean canPlay(Team t1, Team t2) {
		List<Integer> t1BadTeams = t1.getSharedTeams().getSharedTeamList();
		List<Integer> t2BadTeams = t2.getSharedTeams().getSharedTeamList();
		return !t1BadTeams.contains(t2.getTeamId()) && !t2BadTeams.contains(t1.getTeamId());
	}
}
