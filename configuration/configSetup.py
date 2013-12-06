'''
    Copyright 2013 Court Scheduler Development Team

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Author: Stephen Kaysen and Kyle Falconer
Purpose: Gui for court scheduler information
'''

from Tkinter import *
from calWidget import *
import tkMessageBox
import os
from os.path import expanduser
from tkFileDialog import askopenfilename
import sys

root = None
DEBUG = True

WEEKDAYS = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
WEEKDAY_ABBR = {'Monday':'M', 'Tuesday':'T', 'Wednesday':'W', 'Thursday':'R', 'Friday':'F', 'Saturday':'S', 'Sunday':'U'}

timeList = []

def populateTimeList():
    hours = range(1,13)
    times = ["00", "10", "15", "20", "30", "40", "45", "50"]
    for m in ["am","pm"]:
        for hour in hours:
            for time in times:
                timeList.append(str(hour)+":"+time+m);


                
class dayPref():
    def __init__(self, abbr, label, pDays, sDays):
        self.abbr = abbr
        self.prettyLabel = label
        self.primaryDays = pDays
        self.secondaryDays = sDays
    
    def toConfigString(self):
        self.primaryDays = filter(None, self.primaryDays)
        self.secondaryDays = filter(None, self.secondaryDays)
        primaryDayAbbrs = ''.join(WEEKDAY_ABBR[d] for d in self.primaryDays)
        secondaryDayAbbrs = ''.join(WEEKDAY_ABBR[d] for d in self.secondaryDays)
        return "conference=" + str(self.abbr) + "-" + primaryDayAbbrs + ":" + secondaryDayAbbrs
        
        
presetDayPrefs = []      
 
def populateDayPrefs():       
    labels = ["K-1", "2", "3", "4", "5", "6", "7", "8", "Junior Varsity", "Varsity"]
    presetDayPrefs.append(dayPref("1", labels[0], [], ["Wednesday","Sunday","Saturday"]))
    presetDayPrefs.append(dayPref("2", labels[1], [], ["Wednesday","Sunday","Saturday"]))
    presetDayPrefs.append(dayPref("3", labels[2], [], ["Wednesday","Sunday","Saturday"]))
    presetDayPrefs.append(dayPref("4", labels[3], [], ["Wednesday","Sunday","Saturday"]))
    presetDayPrefs.append(dayPref("5", labels[4], [], ["Wednesday","Sunday","Saturday"]))
    presetDayPrefs.append(dayPref("6", labels[5], [], ["Wednesday","Sunday","Saturday"]))
    presetDayPrefs.append(dayPref("7", labels[6], [], ["Saturday"]))
    presetDayPrefs.append(dayPref("8", labels[7], [], ["Sunday"]))
    presetDayPrefs.append(dayPref("9", labels[8], [], ["Wednesday","Sunday","Saturday"]))
    presetDayPrefs.append(dayPref("10", labels[9], [], ["Wednesday","Sunday","Saturday"]))

    


class schedulerConfig(Frame):

    def __init__(self):
        "Sets up window and widgets"
        Frame.__init__(self)
        self.master.title("Court Scheduler Configuration")
        saveOffDates = []
        saveOffCourts = []
        self.grid()
                
        #Creating Interface
        frame1 = Frame(self)
            
        def daySetup():

            def daysBack():
                Primary.grid_forget()
                btnBack.grid_forget()
                btnDaysSubmit.grid_forget()
                time()
                
            def primaryButtonAddCallback(index):
                primaryDaysAddEntryButtons[index].destroy()
                optionalPrimaryDaysDropdown[index].grid(row = index, column = 3)
                optionalPrimaryDaysEntry[index].set("Select Day")
            
            primaryButtonAddCallbacks = [None]*len(presetDayPrefs)
            for entryNumber in range(len(presetDayPrefs)):
                primaryButtonAddCallbacks[entryNumber] = lambda index=entryNumber : primaryButtonAddCallback(index)
                
            
            def passData():
            
                valid = True
                
                
                
                for entryNum in range(len(presetDayPrefs)):
                    primarydays = [primaryDaysEntry[entryNum].get(), optionalPrimaryDaysEntry[entryNum].get()]
                    presetDayPrefs[entryNum].primaryDays = primarydays
                    
                    secondarydays = secondaryDaysEntry[entryNum].get().split(", ")
                    for sday in secondarydays:
                        if sday not in WEEKDAYS:
                            valid = False
                    presetDayPrefs[entryNum].secondaryDays = secondarydays
                    
                if valid:
                    currFile = open("Config2.txt", 'w')
                    for daypref in presetDayPrefs:
                        currFile.write(daypref.toConfigString()+"\n")
                    currFile.close()
                    Primary.grid_forget()
                    btnDaysSubmit.grid_forget()
                    btnBack.grid_forget()
                    sessionUnavail()
                else:
                    tkMessageBox.showinfo("Incorrect Days", "Make sure all days are spelled correctly, and all Primary Days have been selected")
                        
            #create new Frame
            Primary = LabelFrame(self, text = "Primary/Secondary Setup", bd = 6)
            Primary.grid(row = 1, column = 0, columnspan = 3, padx = 10, pady = 6, sticky = W)

            
            primaryDaysEntry = [None]*len(presetDayPrefs)
            primaryDaysEntryDropdown = [None]*len(presetDayPrefs)
            optionalPrimaryDaysEntry = [None]*len(presetDayPrefs)
            optionalPrimaryDaysDropdown = [None]*len(presetDayPrefs)
            primaryDaysAddEntryButtons = [None]*len(presetDayPrefs)
            secondaryDaysEntry = [None]*len(presetDayPrefs)
            
            for entryNumber in range(len(presetDayPrefs)):
                prettyLabel = Label(Primary, text = presetDayPrefs[entryNumber].prettyLabel + " ")
                prettyLabel.grid(row = entryNumber, column = 0, padx = 5, pady = 4, sticky= W)
                primaryDaysLabel = Label(Primary, text = "Primary Day:  ")
                primaryDaysLabel.grid(row = entryNumber, column = 1, padx = 5, pady = 4, sticky= W)
                
                primaryDaysEntry[entryNumber] = StringVar(Primary)
                primaryDaysEntry[entryNumber].set("Select Day")
                primaryDaysEntryDropdown[entryNumber] = OptionMenu(Primary, primaryDaysEntry[entryNumber], *WEEKDAYS)
                primaryDaysEntryDropdown[entryNumber].config(bg="WHITE", width = 11)
                primaryDaysEntryDropdown[entryNumber]["menu"].config(bg="WHITE")
                primaryDaysEntryDropdown[entryNumber].grid(row = entryNumber, column = 2, sticky = W)
                
                optionalPrimaryDaysEntry[entryNumber] = StringVar(Primary)
                optionalPrimaryDaysEntry[entryNumber].set("")
                optionalPrimaryDaysDropdown[entryNumber] = OptionMenu(Primary, optionalPrimaryDaysEntry[entryNumber], *WEEKDAYS)
                optionalPrimaryDaysDropdown[entryNumber].config(bg="WHITE", width = 11)
                optionalPrimaryDaysDropdown[entryNumber]["menu"].config(bg="WHITE")
                
                primaryDaysAddEntryButtons[entryNumber] = Button(Primary, text ="+", command=primaryButtonAddCallbacks[entryNumber])
                primaryDaysAddEntryButtons[entryNumber].grid(row = entryNumber, column = 3, sticky = W)
                
                secondaryDaysLabel = Label(Primary, text = "Secondary Days: ")
                secondaryDaysLabel.grid(row = entryNumber, column = 4, padx = 5, pady = 4, sticky= W)
                secondaryDaysEntry[entryNumber] = Entry(Primary, width = 35)
                secondaryDaysString = ', '.join(presetDayPrefs[entryNumber].secondaryDays)
                secondaryDaysEntry[entryNumber].insert(0, secondaryDaysString)
                secondaryDaysEntry[entryNumber].grid(row = entryNumber, column = 5, padx = 5, pady = 4, sticky= W)
                
            
            
            btnDaysSubmit = Button(self, text = "Next", width = 25, command = passData)
            btnBack = Button(self, text = "Back", width = 25, command = daysBack)
            btnBack.grid(row = 6, columnspan = 2, column=0, sticky = S, padx = 5)
            btnDaysSubmit.grid(row = 6, columnspan = 2, column = 1, sticky = S, padx = 5)

        


        def sessionUnavail():

            def sessionBack():
                Session.grid_forget()
                btnSessSubmit.grid_forget()
                btnSessBack.grid_forget()
                daySetup()

                
            def holidaySubmit():
                config = open("Config3.txt", 'w')
                for dates in saveOffDates:
                            config.write( "holiday=" + dates + "\n")
                tkMessageBox.showinfo("Completion", "You have completed the setup, please pick the File to be scheduled")
                config.close()
                config=0
                Tk().withdraw()
                
                filename = askopenfilename(initialdir =expanduser("~"))
                save = 0
                if filename[-5:] != ".xlsx":
                    tkMessageBox.showinfo("Incorrect File Type", "Please pick an excel file (with extension .xlsx)")
                    save += 1
                if save != 0:
                    pass
                else:
                    if DEBUG:
                        print("writing the config.ini")
                    final = open("config.ini", 'w')
                    initial = open("config1.txt", 'r').read()
                    time = open("config5.txt", 'r').read()
                    days = open("config2.txt", 'r').read()
                    session = open("config3.txt", 'r').read()
                    final.write(initial + "\n" + time + '\n' + days + "\n" + session)
                    os.remove("Config1.txt")
                    os.remove("Config2.txt")
                    os.remove("Config3.txt")
                    os.remove("Config5.txt")
                    final.write("input_file="+ filename)
                    final.close()
                    stop()
                
            def clearSessionUnavail():
                while len(saveOffDates) > 0:
                    saveOffDates.pop(0)
                displayDate.config(state = NORMAL)
                displayDate.delete(1.0, END)
                displayDate.config(state= DISABLED)
                
            def dates():
                months = list(range(1, 13))
                days = list(range(1, 32))
                year = list(range(2013, 2070))
                date = ntryDaysOff.get()
                newdate = date.split("-")

                try:
                    for x in xrange(len(newdate)):
                        dates = newdate[x].split("/")
                        if int(dates[0]) in months and int(dates[1]) in days and int(dates[2]) in year:
                            if x == 0:
                                displayDate.config(state = NORMAL)
                                displayDate.insert(1.0, date + "\n")
                                ntryDaysOff.delete(0, END)
                                displayDate.config(state = DISABLED)
                                saveOffDates.append(date)
                                
                        
                        else:
                            tkMessageBox.showinfo("Holidays/Days Off Error", "The format of your input is incorrect. Make sure it is of the format '1/24/2013' or '1/24/2013-1/25/2013' and try again.")
                        

                except:
                    tkMessageBox.showinfo("Holidays/Days Off Error", "The format of your input is incorrect. Make sure it is of the format '1/24/2013' or '1/24/2013-1/25/2013' and try again.")


            Session = LabelFrame(self, text = "Session Unavailability", bd = 6)
            Session.grid(row = 1, column = 0, columnspan = 3, padx = 10, pady = 6, sticky = W)
            lblDaysOff = Label(Session, text = "Holidays/Days Off: ")
            ntryDaysOff = Entry(Session)
            btnDaysOff = Button(Session, text = "Add", command = dates, width= 13)
            displayDate = Text(Session, width = 43, height = 3)
            scrollbar = Scrollbar(Session, command =displayDate.yview)
            scrollbar.grid(row= 5,column = 3, sticky = E)
            displayDate['yscrollcommand'] = scrollbar.set
            displayDate.config(state= DISABLED)
            btnClearSess = Button(Session, text = "Clear", width = 13, command = clearSessionUnavail)
            lblDaysOff.grid(row = 4, column = 0, pady=4, padx=5, sticky=W)
            ntryDaysOff.grid(row = 4, column = 1, pady=4, padx=5, sticky=W)
            btnDaysOff.grid(row = 4, column = 2, columnspan = 2, pady=4, padx=5, sticky = W)
            displayDate.grid(row = 5, column= 0, pady=4, padx=5, columnspan = 3)
            btnClearSess.grid(row = 6, column = 2, columnspan = 2, sticky =W, padx = 5,pady=4)

            btnSessSubmit = Button(self, text = "Next", width = 20, command = holidaySubmit)
            btnSessBack = Button(self, text = "Back", width = 20, command = sessionBack)
            btnSessBack.grid(row = 6, column = 0, sticky = E, padx = 5)
            btnSessSubmit.grid(row = 6, column = 2, sticky = W, padx = 5)


        
            
        def config():

            def calStart():
                global filewin
                global ttkcal
                filewin = Toplevel(self)
                ttkcal = Calendar(filewin, firstweekday=calendar.SUNDAY)
                ttkcal.pack(expand=1, fill='both')
                accept = Button(filewin, width = 20, text='Submit', command = closeStart)
                accept.pack()
            def calEnd():
                global filewin
                global ttkcal
                filewin = Toplevel(self)
                ttkcal = Calendar(filewin, firstweekday=calendar.SUNDAY)
                ttkcal.pack(expand=1, fill='both')
                accept = Button(filewin, width = 20, text='Submit', command = closeEnd)
                accept.pack()
            def closeEnd():
                global enddate
                date = ttkcal.selection
                filewin.destroy()
                date = str(date)
                enddate = date.split(" ")
                ntryEndDate.config(text=enddate[0])
            def closeStart():
                global startdate
                date = ttkcal.selection
                filewin.destroy()
                date = str(date)
                startdate = date.split(" ")
                ntryStartDate.config(text=startdate[0])
            def submitInitial():
                if ntryCourts.get() != "":
                    save = 0
                    months = list(range(1, 13))
                    days = list(range(1, 32))
                    year = list(range(2013, 2070))
                    try:
                        strtdate = startdate[0].split("-")
                    except:
                        save += 1
                        tkMessageBox.showinfo("Start date", "Pick a start date")
                    try:
                        endate = enddate[0].split("-")
                    except:
                        save += 1
                        tkMessageBox.showinfo("End date", "Pick a end date")
                    try:
                        x = int(ntryCourts.get())
                        y = int(ntryMatchLength.get())
                    except:
                        save += 1
                        tkMessageBox.showinfo("Integer", "Make sure entry values are integers")
                        
                    try:
                        if len(strtdate) != 3 or len(endate) != 3:
                            tkMessageBox.showinfo("Failure to Initialize", "Make sure values are in the proper format")
                        else:
                            config = open("Config1.txt", "w")
                            
                            if int(strtdate[0]) in year and int(strtdate[1]) in months and int(strtdate[2]) in days:
                                config.write( "conference_start=" + strtdate[1] + '/' + strtdate[2] + '/' + strtdate[0] +"\n")
                            else:
                                save += 1
                                tkMessageBox.showinfo("Failure to Initialize", "Start Date not in the proper format")
                            if int(endate[0]) in year and int(endate[1]) in months and int(endate[2]) in days:
                                config.write( "conference_end=" +endate[1] + '/' + endate[2] + '/' + endate[0] + "\n")
                            else:
                                save += 1
                                tkMessageBox.showinfo("Failure to Initialize", "End Date not in the proper format")
                            
                            if save == 0:
                                config.write( "court_count=" + ntryCourts.get() + "\n" + "\n")
                                config.write("timeslots_start=5:00am" + "\n")
                                config.write("timeslots_count=20" + "\n")
                                config.write( "timeslot_duration_minutes=" + ntryMatchLength.get() + "\n")
                                Initial.grid_forget()
                                btnSubmit.grid_forget()
                                time()
                    except:
                            pass

                else:
                    tkMessageBox.showinfo("Failure to Initialize", "Make sure you have inserted values in the Initial Setup and they are in the proper format")

            endcount =0
            Initial = LabelFrame(self, text = "Initial Setup", bd = 6)
            Initial.grid(row = 0,column = 0, columnspan =3, padx=10, pady = 6, sticky = W)
            #Creating the Labels, Buttons, and Entries
            lblStartDate = Label(Initial, text = "Starting Date: ")
            ntryStartDate = Button(Initial, text = "Select Start Date", bg = "WHITE", width = 15, command = calStart)
            lblEndDate = Label(Initial, text = "Ending Date: ")
            ntryEndDate = Button(Initial, text = "Select End Date", bg = "WHITE", width = 15, command = calEnd)
            lblCourts = Label(Initial, text = "Number of Courts: ")
            ntryCourts = Entry(Initial, width = 4)
            ntryCourts.insert(0, "3")
            lblStartDate.grid(row = 0, column = 0, pady=4, padx=5)
            ntryStartDate.grid(row =0, column = 1, pady=4,padx=5)
            lblEndDate.grid(row = 1, column = 0,pady=4, padx=5)
            ntryEndDate.grid(row = 1, column = 1, pady=4, padx=5)
            lblCourts.grid(row = 2, column = 0, pady=4, padx=5)
            ntryCourts.grid(row =2 , column = 1, pady=4, padx=5, sticky=W)

            #Assigning grid location and day of week labels
            lblMatchLength = Label(Initial, text = "Timeslot Length (min): ")
            lblMatchLength.grid(row = 3, column = 0, pady=4,padx=5)
            ntryMatchLength = Entry(Initial, width = 4)
            ntryMatchLength.insert(0, "50")
            ntryMatchLength.grid(row = 3, column = 1, pady=4, padx=5, sticky=W)

            #Submit
            btnSubmit = Button(self, text = "Submit", width = 25, command = submitInitial)
            btnSubmit.grid(row = 4, columnspan = 1, column = 0, sticky = E, padx = 5)
            
        def time():

            def daysBack():
                Time.grid_forget()
                btnBack.grid_forget()
                btnTimeSubmit.grid_forget()
                config()

            def passTime():
                save = 0
                count = 0
                checkTimes = [sunStart.get(), sunEnd.get(), monStart.get(), monEnd.get(),
                              tueStart.get(), tueEnd.get(), wedStart.get(), wedEnd.get(),
                              thuStart.get(), thuEnd.get(), friStart.get(), friEnd.get(),
                              satStart.get(), satEnd.get()]
                
                for time in checkTimes:
                    if time not in timeList:
                        save += 1

                if save == 0:
                    timeDict = {}
                    timeDict["sunday"] = (sunStart.get(), sunEnd.get())
                    timeDict["monday"] =(monStart.get(), monEnd.get())
                    timeDict['tuesday'] = (tueStart.get(), tueEnd.get())
                    timeDict['wednesday'] = (wedStart.get(), wedEnd.get())
                    timeDict['thursday'] = (thuStart.get(), thuEnd.get())
                    timeDict['friday'] = (friStart.get(), friEnd.get())
                    timeDict['saturday'] = (satStart.get(), satEnd.get())
                    
                    config = open("Config5.txt",'w')
                    config.write("schedule_description=")
                    for time in timeDict:
                        if count != 6:
                            config.write(time + ' after ' + timeDict[time][0] + ", " + time + " before " + timeDict[time][1] + ", ")
                            count += 1
                        else:
                            config.write(time + ' after ' + timeDict[time][0] + ", " + time + " before " + timeDict[time][1])
                    config.write("\n")
                    config.close()
                    Time.grid_forget()
                    btnBack.grid_forget()
                    btnTimeSubmit.grid_forget()
                    daySetup()
                else:
                    tkMessageBox.showinfo("Time Selection", "Make sure all times are spelled correctly (ex. 10:00am)")
                
                
            Time = LabelFrame(self, text = "The Courts Availability", bd = 6)
            Time.grid(row = 0, column = 0, columnspan = 3, padx=10, pady = 6, sticky = W)
            
            
                     

            btnBack = Button(Time, text = "Back", width = 20, command = daysBack)
            btnTimeSubmit = Button(Time, text ="Next", width =20, command = passTime)
            btnBack.grid(row = 8, columnspan = 2, column = 0, padx = 5, pady = 6)
            btnTimeSubmit.grid(row = 8, column = 2, columnspan = 2, padx = 5, pady = 6)
            
            lblStart = Label(Time, text = 'Starting Time: ')
            lblEnd = Label(Time, text = 'Ending Time: ')
            lblSun = Label(Time, text = "Sunday: ")
            lblMon = Label(Time, text= "Monday: ")
            lblTue = Label(Time, text = "Tuesday: ")
            lblWed = Label(Time, text = "Wednesday: ")
            lblThu = Label(Time, text = "Thursday: ")
            lblFri = Label(Time, text = "Friday: ")
            lblSat = Label(Time, text = "Saturday: ")

            lblStart.grid(row = 0, column = 1, padx = 5, pady = 6)
            lblEnd.grid(row = 0, column = 2, padx = 5, pady = 6)
            lblSun.grid(row = 1, column = 0, padx= 5, pady = 6)
            lblMon.grid(row =2, column = 0, padx = 5, pady = 6)
            lblTue.grid(row =3, column = 0, padx = 5, pady = 6)
            lblWed.grid(row =4, column = 0, padx = 5, pady = 6)
            lblThu.grid(row =5, column = 0, padx = 5, pady = 6)
            lblFri.grid(row =6, column = 0, padx = 5, pady = 6)
            lblSat.grid(row =7, column = 0, padx = 5, pady = 6)
            
        
            sunStart = Entry(Time,width = 10)
            sunStart.grid(row =1, column = 1)
            sunEnd = Entry(Time, width = 10)
            sunEnd.grid(row =1, column = 2, padx = 5)
            monStart = Entry(Time, width = 10)
            monStart.grid(row =2, column = 1)
            monEnd = Entry(Time, width = 10)
            monEnd.grid(row =2, column = 2, padx = 5)
            tueStart = Entry(Time, width = 10)
            tueStart.grid(row =3, column = 1)
            tueEnd = Entry(Time, width = 10)
            tueEnd.grid(row =3, column = 2, padx = 5)
            wedStart = Entry(Time, width = 10)
            wedStart.grid(row =4, column = 1)
            wedEnd = Entry(Time, width =10)
            wedEnd.grid(row =4, column = 2, padx = 5)
            thuStart = Entry(Time, width = 10)
            thuStart.grid(row =5, column = 1)
            thuEnd = Entry(Time, width = 10)
            thuEnd.grid(row =5, column = 2, padx = 5)
            friStart = Entry(Time, width = 10)
            friStart.grid(row =6, column = 1)
            friEnd = Entry(Time, width = 10)
            friEnd.grid(row =6, column = 2, padx = 5)
            satStart = Entry(Time, width = 10)
            satStart.grid(row =7, column = 1)
            satEnd = Entry(Time, width = 10)
            satEnd.grid(row =7, column = 2, padx = 5)

            
         
        config()

def stop():
    if DEBUG:
        print("Closing the configuration utility.")
    root.quit()
    os._exit(0)

def main():
    global root
    if DEBUG:
        print("Starting the configuration utility for Court Scheduler")
    populateTimeList()
    populateDayPrefs()
    root = schedulerConfig()
    root.master.resizable(0,0)
    root.mainloop()



main()
