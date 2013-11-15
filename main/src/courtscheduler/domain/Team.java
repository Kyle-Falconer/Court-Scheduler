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
    private OffTimes offTimes;
    private PlayOnceRequests playOnceRequests;
	private SharedTeams dontPlay;

    private MatchAvailability avail;


    public Team() {
        doubleHeaderPreference = new DoubleHeaderPreference(false);
        backToBackPreference = new BackToBackPreference(false);
        offTimes = new OffTimes();
        playOnceRequests = new PlayOnceRequests();
		this.avail = new MatchAvailability();
		dontPlay = new SharedTeams();
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

    public String getConferenceString(){
        String confStr= Integer.toString(conference);
        return confStr;
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

    public OffTimes getOffTimes() {
        return offTimes;
    }

    public void setOffTimes(OffTimes offTimes) {
        this.offTimes = offTimes;
    }

    public SharedTeams getDontPlay() {
        return dontPlay;
    }

    public PlayOnceRequests getPlayOnceRequests() {
        return playOnceRequests;
    }

    public void setPlayOnceRequests(PlayOnceRequests playOnceRequests) {
        this.playOnceRequests = playOnceRequests;
    }

    public DateConstraint getPreferredDates() {
        return this.avail.prefDates;
    }
    public void setPreferredDates(DateConstraint preferredDates) {
        this.avail.prefDates = preferredDates;
    }

    @Override
    public String toString() {
        String teamId_ = teamId != null ?  teamId.toString()  : "";
        String teamName_ = teamName != null ?  teamName  : "";
        String year_ = year != null ?  year  : "";
        String gender_ = gender != null ?  gender  : "";
        String grade_ = grade != null ?  grade.toString()  : "";
        String level_ = level != null ?  level  : "";

      return teamId_ + "~" + teamName_ + "~" + year_ + "~" + gender_ + "~" + grade_ + "~" + level_;
    }

    public MatchAvailability getAvailability() {
        return this.avail;
    }

	public static boolean cannotPlay(Team t1, Team t2) {
		List<Integer> t1BadTeams = t1.getDontPlay().getSharedTeamList();
		List<Integer> t2BadTeams = t2.getDontPlay().getSharedTeamList();
		return t1BadTeams.contains(t2.getTeamId()) || t2BadTeams.contains(t1.getTeamId());
	}
    public boolean getDoubleHeader(){
        return this.getDoubleHeaderPreference().likesDoubleHeaders();

    }
    public boolean getBackToBack(){
        return this.getBackToBackPreference().isLikesBackToBack();
    }
	public DateConstraint getBadDates() {
		return this.avail.badDates;
	}
    public void setBadDates(DateConstraint badDates) {
        this.avail.badDates = badDates;
    }
    public DateConstraint getOnlyDates() {
        return this.avail.onlyDates;
    }
	public void setOnlyDates(DateConstraint onlyDates) {
		if (onlyDates != null) {
			this.avail.onlyDates = onlyDates;
		}
	}
}
