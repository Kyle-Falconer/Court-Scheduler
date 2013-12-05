###############################################################################
#    Copyright 2013 Court Scheduler Development Team
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
###############################################################################


User Documentation for Court Scheduler
======================================

Court Scheduler takes an input excel file (see "Samples" folder for example) and creates a schedule
for each conference, and outputs an excel file with the master schedule on the first sheet, and
each conference's schedule on separate sheets. The program also saves an HTML version for web use.

How to run:
    Open the folder containing the program and run CourtScheduler.exe

    A configuration window will open and you will be able to edit various settings
    such as what time games start on each day of the week, how many games are played each day,
    and which dates should not be scheduled due to holidays.

    When the configuration window opens you will see the Initial Setup window.
    Here you can select the starting and ending dates of the season.
    You can also define the number of courts, and the timeslot length.
    Once this information is entered you can click next.

    The next window is the Courts Availability window.
    Here you can enter the Starting and Ending time for each day of the week
    following this format: 10:00am or 11:30pm. The allowed times are every ten minutes
    or each quarter of an hour (ex. 10:15pm, or 8:30am).
    When each text box is filled with information following the correct format,
    you will be able to click next and go to the next page.

    Next you will see the Primary/Secondary day setup page.
    On the left side of the window, you can set the primary day for each
    grade level up to Varsity. If a grade level has 2 primary days, click the
    plus button to bring up another primary day.
    The right side of the window contains the secondary days for each grade.
    This information must be entered as the full name, ex. Monday or Tuesday.
    If there are multiple secondary days for a grade, you can separate
    them with commas (ex. Monday, Wednesday, Saturday).

    The next window allows you to type in the holidays or other days that
    the courts will be closed. You can type in each date into the
    Holidays/Days off text box, and click the "Add" button to add them to
    the list below. Clicking "Clear" will delete the list if you make a mistake.
    The format for single date is: 12/23/2014
    The format for a range of dates is seperating the dates with a dash (no spaces): 12/24/2014-12/30/2014

    When this is completed, click the "Submit" button. This will bring up
    a browse window where you will select your input excel file. The file 
    extension of this Excel file should be ".xlsx"; the older file format 
    for Excel (".xls") will not work with the program. Once a file has been 
    selected, confirm your selection and the program will run.

    Depending on the number of conferences to be scheduled and the scheduling 
    constraints, the program could take several hours to complete.

    Once the program is finished running, it will automatically open the schedule in an excel file.
    The first tab on the excel file is the master schedule, sorted by date and time. This has every
    match from every conference in one sheet.
    Each consecutive tab is labelled by their conference name, and contains the schedule for that
    particular conference.

    Note: If the scheduler could not fit a match into the times the teams are available,
    "WARNING" will appear to the right of the match that violates the constraint.
    This will help you identify which matches may need to be moved around manually.
    
    HOWEVER, note that this will only automatically detect teams who are scheduled when they
    cannot play. It will not automatically warn you about teams scheduled for double-headers or
    back-to-back games, or any other situation.
	



Excel Input Formatting:
    Team ID: The first column should contain the team ID. For example, 101

    Conference: The conference should be entered by each team, and follows the
                format of '1a' or '7b'

    Team Name: The team name should be entered with the team code followed by the name.
                Example: 101-BLACK AND WHITE TIGERS

    Gender: Should be entered as 'B' for boys and 'G' for girls.

    Grade: Enter the number K-8, JV, or V

    Level: Recreation - R
           Intermediate - I
           Competitive - C

    Requests:

        Request to not play on day of the week: No Wednesday
        Request to not play on certain date: No 1/1/14
        Request to not play on date range: No 1/1/14-1/4/14
        Request to not play before a certain time: after 7:00pm
        Request to not play during a time range: No 6:00pm-8:00pm

        Request to play double headers: DH
        Request for back to back games:
        Prefer to play on a certain day: pref Saturday


     Not Same Time As:
         If a team shares players or a coach with another team, and
                 they wish to not play at the same time as them, enter the
                 team name into the "not same time as" column.
                 
                 
Project Source Code
-------------------
The project source code is available open source on [GitHub](https://github.com/netinept/Court-Scheduler) and can be modified and distributed as needed, per the terms of the License.


Court Scheduler Development Team
--------------------------------
Michael Adams (Michael555@live.missouristate.edu)
CJ Done (Done245@live.missouristate.edu)
Charles Eswine (Charles343@live.missouristate.edu)
Kyle Falconer  (kfalconer@gmail.com)
Will Gorman (Gorman1@live.missouristate.edu)
Stephen Kaysen (Kaysen7@live.missouristate.edu)
Pat McCroskey (McCroskey236@live.missouristate.edu)
Matthew Swinney (MatthewGSwinney@gmail.com)



