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

package courtscheduler.domain;

import java.util.List;

public class Team {

    private Integer teamId;
    private String teamName;
    private String year = "";
    private String gender;
    private Integer grade;
    private String level;
    private String conference;

    private DoubleHeaderPreference doubleHeaderPreference;
    private BackToBackPreference backToBackPreference;
    private PlayOnceRequests playOnceRequests;
	private SharedTeams dontPlay;

    private MatchAvailability avail;

    public Integer getGameCount() {
        return gameCount;
    }

    public void setGameCount(Integer gameCount) {
        this.gameCount = gameCount;
    }

    private Integer gameCount = 10;


    public Team() {
        doubleHeaderPreference = new DoubleHeaderPreference(false);
        backToBackPreference = new BackToBackPreference(false);
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

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
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
		if (t1 == t2)
            return true;
        if (!t1.getConference().equals(t2.getConference()))
			return true;
        if (t1.getGameCount() == 0 || t2.getGameCount() == 0)
            return true;
        MatchAvailability avail= new MatchAvailability(t1.getAvailability(),t2.getAvailability());
        if(avail.notScheduleable())
            return true;
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
