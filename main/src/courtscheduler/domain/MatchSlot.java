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

/**
 * Created with IntelliJ IDEA.
 * User: CJ
 * Date: 10/28/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchSlot extends AbstractPersistable {
    private Integer day;
    private Integer time;
    private Integer court;

    public MatchSlot(int day, int time, int court) {
        this.day = new Integer(day);
        this.time = new Integer(time);
        this.court = new Integer(court);
    }

    public Integer getDay(){
        return this.day;
    }
    public Integer getTime(){
        return this.time;
    }
    public Integer getCourt(){
        return this.court;
    }
    public void setDay(Integer day){
        this.day=day;
    }
    public void setTime(Integer time){
        this.time=time;
    }
    public void setCourt(Integer court){
        this.court=court;
    }

    public String toString() {
        return "(" + day + ":" + time + ":" + court + ")";
    }

	public boolean equals(Object other) {
		if (other == null || other.getClass() != MatchSlot.class) {
			return false;
		}
		MatchSlot o = (MatchSlot) other;
		return this.getTime().equals(o.getTime()) && this.getDay().equals(o.getDay()) && this.getCourt().equals(o.getCourt());
	}

	public static int getDateDistance(MatchSlot m1, MatchSlot m2) {
		return Math.abs(m1.getDay() - m2.getDay());
	}
}
