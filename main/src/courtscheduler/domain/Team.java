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

    //depricated, replaced by Constraint Types.
    private boolean likesDoubleHeaders = false;
    private boolean likesBackToBack = false;
    private List<MatchDate> offDateList;
    private List<MatchTime> offTimeList;
    private List<Integer> sharedTeamList;
    private List<Integer> playOnceTeamList;
    private List<MatchDate> preferredDateList;
    //end deprication

    public Team() {
    }

    public void setLikesDoubleHeaders(boolean likesDoubleHeaders) {
        this.likesDoubleHeaders = likesDoubleHeaders;
    }

    public boolean getLikesDoubleHeaders() {
        return likesDoubleHeaders;
    }

    public void setLikesBackToBack(boolean likesBackToBack) {
        this.likesBackToBack = likesBackToBack;
    }

    public boolean getLikesBackToBack() {
        return likesBackToBack;
    }

    public void setPlayOnceTeamList(List<Integer> playOnceTeamList) {
        this.playOnceTeamList = playOnceTeamList;
    }

    public List<Integer> getPlayOnceTeamList() {
        return playOnceTeamList;
    }

    public void setPreferredDateList(List<MatchDate> preferredDateList) {
        this.preferredDateList = preferredDateList;
    }

    public List<MatchDate> getPreferredDateList() {
        return preferredDateList;
    }

    public void setOffDateList(List<MatchDate> offDateList) {
        this.offDateList = offDateList;
    }

    public List<MatchDate> getOffDateList() {
        return offDateList;
    }

    public void setOffTimeList(List<MatchTime> offTimeList) {
        this.offTimeList = offTimeList;
    }

    public List<MatchTime> getOffTimeList() {
        return offTimeList;
    }

    public void setSharedTeamList(List<Integer> sharedTeamList) {
        this.sharedTeamList = sharedTeamList;
    }

    public List<Integer> getSharedTeamList() {
        return sharedTeamList;
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
     
}   