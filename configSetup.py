'''
Author: Stephen Kaysen
Purpose: Gui for court scheduler information
'''

from Tkinter import *
import tkMessageBox
import sys


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
        Initial = LabelFrame(self, text = "Initial Setup", bd = 6)
        Initial.grid(row = 0,column = 0, columnspan =3, padx=10, pady = 6, sticky = W)

        def daySetup():

            def passData():
                days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
                dayDict = {}
                dayDict["K-1"] = (ntryPrimary.get(), ntrySecondary.get().split(", "))
                dayDict["2"] = (ntryPrimary1.get(), ntrySecondary1.get().split(", "))
                dayDict["3"] = (ntryPrimary2.get(), ntrySecondary2.get().split(", "))
                dayDict["4"] = (ntryPrimary3.get(), ntrySecondary3.get().split(", "))
                dayDict["5"] = (ntryPrimary4.get(), ntrySecondary4.get().split(", "))
                dayDict["6"] = (ntryPrimary5.get(), ntrySecondary5.get().split(", "))
                dayDict["7-8"] = (ntryPrimary6.get(), ntrySecondary6.get().split(", ")) 
                checkList =[]
                save = 0
                for day in dayDict:
                    if type(dayDict[day][1]) == list:
                        checkList.append(dayDict[day][0])
                        checkList.extend(dayDict[day][1])
                    else:
                        checkList.append(dayDict[day][0])
                        checkList.append(dayDict[day][0])
                for day in checkList:
                    if day not in days:
                        save += 1
                if save == 0:
                    config = open("Config.txt", 'a')
                    for day in dayDict:
                        config.write(day + "_Primary=" + dayDict[day][0] + "\n")
                        for x in range(len(dayDict[day][1])):
                            config.write(day + "_Secondary=" + dayDict[day][1][x] + "\n")
                    config.close()
                    Primary.grid_forget()
                    btnDaysSubmit.grid_forget()
                    sessionUnavail()
                else:
                    tkMessageBox.showinfo("Incorrect Days", "Make sure all days are spelled correctly")
                        
            #create new Frame
            Primary = LabelFrame(self, text = "Primary/Secondary Setup", bd = 6)
            Primary.grid(row = 1, column = 0, columnspan = 3, padx = 10, pady = 6, sticky = W)

            #Create input
            lblYoungGrades = Label(Primary, text = "K-1 ")
            lblSecGrade = Label(Primary, text = "2 ")
            lblThirdGrade = Label(Primary, text = "3 ")
            lblFourthGrade = Label(Primary, text = "4 ")
            lblFifthGrade = Label(Primary, text = "5 ")
            lblSixthGrade = Label(Primary, text = "6 ")
            lblMidGrades = Label(Primary, text = "7-8 ")
            lblPrimary = Label(Primary, text = "Primary Day:  ")
            ntryPrimary = Entry(Primary)
            lblSecondary = Label(Primary, text = "Secondary Days: ")
            ntrySecondary = Entry(Primary, width = 35)
            ntrySecondary.insert(0, "Wednesday, Friday, Saturday")
            lblPrimary1 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary1 = Entry(Primary)
            lblSecondary1 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary1 = Entry(Primary, width = 35)
            ntrySecondary1.insert(0, "Wednesday, Friday, Saturday")
            lblPrimary2 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary2 = Entry(Primary)
            lblSecondary2 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary2 = Entry(Primary, width = 35)
            ntrySecondary2.insert(0, "Wednesday, Friday, Saturday")
            lblPrimary3 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary3 = Entry(Primary)
            lblSecondary3 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary3 = Entry(Primary, width = 35)
            ntrySecondary3.insert(0, "Wednesday, Friday, Saturday")
            lblPrimary4 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary4 = Entry(Primary)
            lblSecondary4 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary4 = Entry(Primary, width = 35)
            ntrySecondary4.insert(0, "Wednesday, Friday, Saturday")
            lblPrimary5 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary5 = Entry(Primary)
            lblSecondary5 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary5 = Entry(Primary, width = 35)
            ntrySecondary5.insert(0, "Wednesday, Friday, Saturday")
            lblPrimary6 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary6 = Entry(Primary)
            lblSecondary6 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary6 = Entry(Primary, width = 35)
            ntrySecondary6.insert(0, "Wednesday, Friday, Saturday")

            btnDaysSubmit = Button(self, text = "Submit", width = 25, command = passData)
            btnDaysSubmit.grid(row = 6, columnspan = 2, column = 0, sticky = S+E, padx = 5)

            #assign input location
            lblYoungGrades.grid(row = 0, column = 0, sticky = W, padx = 5, pady = 4)
            lblSecGrade.grid(row = 1, column = 0, sticky = W, padx = 5, pady = 4)
            lblThirdGrade.grid(row = 2, column = 0, sticky = W, padx = 5, pady = 4)
            lblFourthGrade.grid(row = 3, column = 0, sticky = W, padx = 5, pady = 4)
            lblFifthGrade.grid(row = 4, column = 0, sticky = W, padx = 5, pady = 4)
            lblSixthGrade.grid(row = 5, column = 0, sticky = W, padx = 5, pady = 4)
            lblMidGrades.grid(row = 6, column = 0, sticky = W, padx = 5, pady = 4)
            lblPrimary.grid(row = 0, column = 1, padx= 5, pady =4)
            ntryPrimary.grid(row = 0, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary.grid(row = 0, column = 3, padx = 5, pady = 4, sticky = W)
            ntrySecondary.grid(row = 0, column = 4, padx = 5, pady = 4, sticky = W)
            lblPrimary1.grid(row = 1, column = 1, padx= 5, pady =4)
            ntryPrimary1.grid(row = 1, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary1.grid(row = 1, column = 3, padx = 5, pady = 4, sticky = W)
            ntrySecondary1.grid(row = 1, column = 4, padx = 5, pady = 4, sticky = W)
            lblPrimary2.grid(row = 2, column = 1, padx= 5, pady =4)
            ntryPrimary2.grid(row = 2, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary2.grid(row = 2, column = 3, padx = 5, pady = 4, sticky = W)
            ntrySecondary2.grid(row = 2, column = 4, padx = 5, pady = 4, sticky= W)
            lblPrimary3.grid(row = 3, column = 1, padx= 5, pady =4)
            ntryPrimary3.grid(row = 3, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary3.grid(row = 3, column = 3, padx = 5, pady = 4, sticky = W)
            ntrySecondary3.grid(row = 3, column = 4, padx = 5, pady = 4, sticky= W)
            lblPrimary4.grid(row = 4, column = 1, padx= 5, pady =4)
            ntryPrimary4.grid(row = 4, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary4.grid(row = 4, column = 3, padx = 5, pady = 4, sticky = W)
            ntrySecondary4.grid(row = 4, column = 4, padx = 5, pady = 4, sticky= W)
            lblPrimary5.grid(row = 5, column = 1, padx= 5, pady =4)
            ntryPrimary5.grid(row = 5, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary5.grid(row = 5, column = 3, padx = 5, pady = 4, sticky = W)
            ntrySecondary5.grid(row = 5, column = 4, padx = 5, pady = 4, sticky= W)
            lblPrimary6.grid(row = 6, column = 1, padx= 5, pady =4)
            ntryPrimary6.grid(row = 6, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary6.grid(row = 6, column = 3, padx = 5, pady = 4, sticky = W)
            ntrySecondary6.grid(row = 6, column = 4, padx = 5, pady = 4, sticky= W)



        def submitInitial():
            if ntryStartDate.get() != "" and ntryEndDate.get() != "" and ntryCourts.get() != "": 
                months = list(range(1, 13))
                days = list(range(1, 32))
                year = list(range(2013, 2070))
                startdate = ntryStartDate.get()
                enddate = ntryEndDate.get()
                startdate = startdate.split("/")
                enddate = enddate.split("/")
                save = 0
                if len(startdate) != 3 or len(enddate) != 3:
                    tkMessageBox.showinfo("Failure to Initialize", "Make sure values are in the proper format")
                else:
                    config = open("Config.txt", "w")
                    if int(startdate[0]) in months and int(startdate[1]) in days and int(startdate[2]) in year:
                        config.write( "conference_start=" + ntryStartDate.get() +"\n")
                    else:
                        save += 1
                        tkMessageBox.showinfo("Failure to Initialize", "Start Date not in the proper format")
                    if int(enddate[0]) in months and int(enddate[1]) in days and int(enddate[2]) in year:
                        config.write( "conference_end=" +ntryEndDate.get() + "\n")
                    else:
                        save += 1
                        tkMessageBox.showinfo("Failure to Initialize", "End Date not in the proper format")
                    if save == 0:
                        config.write( "court_count=" + ntryCourts.get() + "\n")
                        config.write( "timeslot_duration_minutes=" + ntryMatchLength.get() + "\n")
                        config.write( "timeslot_start_time=" + ntryMatchStart.get() + "\n" + "\n")
                        config.write( "Monday: " + ntryMon.get() + "\n")
                        config.write( "Tuesday: " + ntryTues.get() + "\n")
                        config.write( "Wednesday: " + ntryWed.get() + "\n")
                        config.write( "Thursday: " + ntryThur.get() + "\n")
                        config.write( "Friday: " + ntryFri.get() + "\n")
                        config.write( "Saturday: " + ntrySat.get() + "\n")
                        config.write( "Sunday: " + ntrySun.get() + "\n" + "\n")
                        config.close()

                        Initial.grid_forget()
                        btnSubmit.grid_forget()
                        daySetup()

            else:
                tkMessageBox.showinfo("Failure to Initialize", "Make sure you have inserted values in the Initial Setup and they are in the proper format")


        def sessionUnavail():

            def holidaySubmit():
                config = open("Config.txt", 'a')
                for dates in saveOffDates:
                            config.write( "holiday=" + dates + "\n")
                config.close()
                Session.grid_forget()
                btnSessSubmit.grid_forget()
                courtUnavail()
                
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
                                displayDate.insert(END,  "-"+date + "\n")
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
            btnDaysOff = Button(Session, text = "Add", command = dates, width= 10)
            displayDate = Text(Session, width = 50, height = 2)
            displayDate.config(state= DISABLED)
            btnClearSess = Button(Session, text = "Clear", width = 10, command = clearSessionUnavail)
            lblDaysOff.grid(row = 4, column = 0, pady=4, padx=5, sticky=W)
            ntryDaysOff.grid(row = 4, column = 1, pady=4, padx=5, sticky=W)
            btnDaysOff.grid(row = 4, column = 2, pady=4, padx=5, sticky = W)
            displayDate.grid(row = 5, column= 0, pady=4, padx=5, columnspan = 3)
            btnClearSess.grid(row = 6, column = 2, sticky =W, padx = 5,pady=4)

            btnSessSubmit = Button(self, text = "Submit", width = 25, command = holidaySubmit)
            btnSessSubmit.grid(row = 6, columnspan = 2, column = 0, sticky = S+E, padx = 5)


        def courtUnavail():

            def courtSubmit():
                config = open("Config.txt", 'a')
                for courts in saveOffCourts:
                            config.write( "NO_courts: " + courts[0] +", " + courts[1] + "\n")
                config.close()
                tkMessageBox.showinfo("Completion", "You have completed the setup")
                quit()
            def clearCourtUnavail():
                while len(saveOffCourts) > 0:
                    saveOffCourts.pop(0)
                displayCourtDate.config(state= NORMAL)
                displayCourtDate.delete(1.0, END)
                displayCourtDate.config(state=DISABLED)

            def courtOff():
                months = list(range(1, 13))
                days = list(range(1, 32))
                year = list(range(2013, 2070))
                date = ntryCourtsOff.get()
                numCourts = ntryCourt.get()

                try:
                    dates = date.split("/")
                    if int(dates[0]) in months and int(dates[1]) in days and int(dates[2]) in year and int(numCourts) < 10:
                        displayCourtDate.config(state=NORMAL)
                        displayCourtDate.insert(END, "-"+date+", "+numCourts + "\n")
                        displayCourtDate.config(state=DISABLED)
                        saveOffCourts.append((date,numCourts))
                    else:
                        tkMessageBox.showinfo("Courts Off Error", "The format of your input is incorrect. Make sure it is of the format '1/24/2013' and courts is a number") 
                except:
                    tkMessageBox.showinfo("Courts Off Error", "The format of your input is incorrect. Make sure it is of the format '1/24/2013' and courts is a number")

            court = LabelFrame(self, text = "Court Unavailability", bd = 6)
            court.grid(row = 3, column = 0, columnspan = 4, padx=10, pady=6, sticky= W)

            lblCourtsOff = Label(court, text = "Date: ")
            ntryCourtsOff = Entry(court)
            lblCourt = Label(court, text = "Courts: ")
            ntryCourt = Entry(court)
            btnCourtOff = Button(court, text = "Add", command = courtOff, width = 10)
            displayCourtDate = Text(court, width = 60, height = 2, wrap = NONE)
            displayCourtDate.config(state= DISABLED)
            btnClearCourt = Button(court, text = "Clear", width = 10, command = clearCourtUnavail)
            lblCourtsOff.grid(row = 5, column = 0, pady=4, padx=5, sticky=W)
            ntryCourtsOff.grid(row = 5, column = 1, pady=4, padx=5)
            lblCourt.grid(row = 5, column = 2, pady=4, padx=5, sticky=W)
            ntryCourt.grid(row = 5, column = 3, pady=4,padx=5, sticky=W)
            btnCourtOff.grid(row = 5, column = 4, pady=4, padx=5, sticky=W)
            displayCourtDate.grid(row=6, column = 0, pady=4, padx = 5, columnspan = 5)
            btnClearCourt.grid(row = 7, column = 4, sticky =W, padx = 5,pady=4)

            btnSessSubmit = Button(self, text = "Submit", width = 25, command = courtSubmit)
            btnSessSubmit.grid(row = 6, columnspan = 2, column = 0, sticky = S+E, padx = 5)
            
        #Creating the Labels, Buttons, and Entries
        lblStartDate = Label(Initial, text = "Starting Date: ")
        ntryStartDate = Entry(Initial)
        lblEndDate = Label(Initial, text = "Ending Date: ")
        ntryEndDate = Entry(Initial)
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
        lblMatchStart = Label(Initial, text = "Start Time for Timeslots: ")
        lblMatchStart.grid(row= 4, column = 0, padx = 5, pady = 4)
        ntryMatchStart = Entry(Initial, width = 10)
        ntryMatchStart.grid(row = 4, column = 1, padx = 5, pady = 4, sticky = W)
        ntryMatchStart.insert(0, "8:00pm")
        lblTimeslots =Label(Initial, bd = 3, width = 35, relief = RIDGE, text = "Number of Timeslots on Specific Days")
        lblTimeslots.grid(row = 0, column = 2, pady=4, padx=5, columnspan = 4, sticky=W)
        ntryMatchLength = Entry(Initial, width = 4)
        ntryMatchLength.insert(0, "50")
        ntryMatchLength.grid(row = 3, column = 1, pady=4, padx=5, sticky=W)
        lblMon = Label(Initial, text = "Monday:")
        lblMon.grid(row=1, column = 2, pady=4,padx=5, sticky=W)
        ntryMon = Entry(Initial, width = 4)
        ntryMon.insert(0, "10")
        ntryMon.grid(row=1, column = 3, pady=4,padx=5, sticky=W)
        lblTues = Label(Initial, text = "Tuesday:")
        lblTues.grid(row=2, column = 2, pady=4,padx=5, sticky=W)
        ntryTues = Entry(Initial, width = 4)
        ntryTues.insert(0, "10")
        ntryTues.grid(row=2, column = 3, pady=4,padx=5, sticky=W)
        lblWed = Label(Initial, text = "Wednesday:")
        lblWed.grid(row=3, column = 2, pady=4,padx=5, sticky=W)
        ntryWed = Entry(Initial, width = 4)
        ntryWed.insert(0, "10")
        ntryWed.grid(row=3, column = 3, pady=4,padx=5, sticky=W)
        lblThur = Label(Initial, text = "Thursday:")
        lblThur.grid(row=4, column = 2, pady=4,padx=5, sticky=W)
        ntryThur = Entry(Initial, width = 4)
        ntryThur.insert(0, "10")
        ntryThur.grid(row=4, column = 3, pady=4,padx=5, sticky=W)
        lblFri = Label(Initial, text = "Friday:")
        lblFri.grid(row=1, column = 4, pady=4,padx=5, sticky=W)
        ntryFri = Entry(Initial, width = 4)
        ntryFri.insert(0, "10")
        ntryFri.grid(row=1, column = 5, pady=4,padx=5, sticky=W)
        lblSat = Label(Initial, text = "Saturday:")
        lblSat.grid(row=2, column = 4, pady=4,padx=5, sticky=W)
        ntrySat = Entry(Initial, width = 4)
        ntrySat.insert(0, "10")
        ntrySat.grid(row=2, column = 5, pady=4,padx=5, sticky=W)
        lblSun = Label(Initial, text = "Sunday:")
        lblSun.grid(row=3, column = 4, pady=4,padx=5, sticky=W)
        ntrySun = Entry(Initial, width = 4)
        ntrySun.insert(0, "10")
        ntrySun.grid(row=3, column = 5, pady=4,padx=5, sticky=W)

        #Submit
        btnSubmit = Button(self, text = "Submit", width = 25, command = submitInitial)
        btnSubmit.grid(row = 6, columnspan = 2, column = 0, sticky = S+E, padx = 5)


def main():
    schedulerConfig().mainloop()



main()
