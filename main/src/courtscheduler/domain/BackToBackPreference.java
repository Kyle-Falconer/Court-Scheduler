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
 * User: Michael
 * Date: 10/14/13
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackToBackPreference extends Constraint{

    boolean likesBackToBack = false;

	public BackToBackPreference(boolean likesBackToBack) {
		this.likesBackToBack = likesBackToBack;
	}
    public void toggleBackToBack(){
        this.likesBackToBack=!this.likesBackToBack;
    }
    public boolean isLikesBackToBack() {
        return likesBackToBack;
    }


}
