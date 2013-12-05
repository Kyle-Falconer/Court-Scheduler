'''
Author: Stephen Kaysen
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
            def pr1Add():
                btnPr1Add.destroy()
                optPr1Add.grid(row = 0, column = 3)
                prim.set("Select Day")
            def pr2Add():
                btnPr2Add.destroy()
                optPr2Add.grid(row = 1, column = 3)
                prim1.set("Select Day")
            def pr3Add():
                btnPr3Add.destroy()
                optPr3Add.grid(row = 2, column = 3)
                prim2.set("Select Day")
            def pr4Add():
                btnPr4Add.destroy()
                optPr4Add.grid(row = 3, column = 3)
                prim3.set("Select Day")
            def pr5Add():
                btnPr5Add.destroy()
                optPr5Add.grid(row = 4, column = 3)
                prim4.set("Select Day")
            def pr6Add():
                btnPr6Add.destroy()
                optPr6Add.grid(row = 5, column = 3)
                prim5.set("Select Day")
            def pr7Add():
                btnPr7Add.destroy()
                optPr7Add.grid(row = 6, column = 3)
                prim6.set("Select Day")
            def pr8Add():
                btnPr8Add.destroy()
                optPr8Add.grid(row = 7, column = 3)
                prim7.set("Select Day")
            def pr9Add():
                btnPr9Add.destroy()
                optPr9Add.grid(row = 8, column = 3)
                prim8.set("Select Day")
            def pr10Add():
                btnPr10Add.destroy()
                optPr10Add.grid(row = 9, column = 3)
                prim9.set("Select Day")
            
            def passData():
                days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
                dayDict = {}
                dayDict["1"] = (var.get(), ntrySecondary.get().split(", "))
                pr1.append(prim.get())
                dayDict["2"] = (var1.get(), ntrySecondary1.get().split(", "))
                pr1.append(prim1.get())
                dayDict["3"] = (var2.get(), ntrySecondary2.get().split(", "))
                pr1.append(prim2.get())
                dayDict["4"] = (var3.get(), ntrySecondary3.get().split(", "))
                pr1.append(prim3.get())
                dayDict["5"] = (var4.get(), ntrySecondary4.get().split(", "))
                pr1.append(prim4.get())
                dayDict["6"] = (var5.get(), ntrySecondary5.get().split(", "))
                pr1.append(prim5.get())
                dayDict["7"] = (var6.get(), ntrySecondary6.get().split(", "))
                pr1.append(prim6.get())
                dayDict["8"] = (var7.get(), ntrySecondary7.get().split(", "))
                pr1.append(prim7.get())
                dayDict["9"] = (var8.get(), ntrySecondary8.get().split(", "))
                pr1.append(prim8.get())
                dayDict["10"] = (var9.get(), ntrySecondary9.get().split(", "))
                pr1.append(prim9.get())
                checkList =[]
                save = 0
                counter= 0
                for day in dayDict:
                    if type(dayDict[day][1]) == list:
                        checkList.append(dayDict[day][0])
                        checkList.extend(dayDict[day][1])
                    else:
                        checkList.append(dayDict[day][0])
                        checkList.append(dayDict[day][1])
                for day in checkList:
                    if day not in days:
                        save += 1
                if save == 0:
                    currFile = open("Config2.txt", 'w')
                    for x in xrange(0, 10):
                        if pr1[x] == "Thursday":
                            pr1[x] = "R"
                        elif pr1[x] == "Sunday":
                            pr1[x] = "U"
                        elif pr1[x] == "":
                            pr1[x] = ""
                        else:
                            pr1[x] = pr1[x][0]
                    for day in dayDict:
                        if dayDict[day][0] == "Thursday":
                            currFile.write("conference=" + day + "-" + "R" + pr1[counter] +":")
                        elif dayDict[day][0] == "Sunday":
                            currFile.write("conference=" + day + "-" + "U" + pr1[counter] + ":")
                            
                        else:
                            currFile.write("conference=" + day + "-" + dayDict[day][0][0] + pr1[counter] +":")
                        for x in range(len(dayDict[day][1])):
                            if dayDict[day][1][x] == "Thursday":
                                currFile.write("R")
                            elif dayDict[day][1][x] == "Sunday":
                                currFile.write("U")
                            else:
                                currFile.write(dayDict[day][1][x][0])
                        counter += 1
                        currFile.write("\n")
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

            #Create input
            pr1 = []
            
            days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
            var = StringVar(Primary)
            var.set("Select Day")
            var1 = StringVar(Primary)
            var1.set("Select Day")
            var2 = StringVar(Primary)
            var2.set("Select Day")
            var3 = StringVar(Primary)
            var3.set("Select Day")
            var4 = StringVar(Primary)
            var4.set("Select Day")
            var5 = StringVar(Primary)
            var5.set("Select Day")
            var6 = StringVar(Primary)
            var6.set("Select Day")
            var7 = StringVar(Primary)
            var7.set("Select Day")
            var8 = StringVar(Primary)
            var8.set("Select Day")
            var9 = StringVar(Primary)
            var9.set("Select Day")

            prim = StringVar(Primary)
            prim.set("")
            prim1 = StringVar(Primary)
            prim1.set("")
            prim2 = StringVar(Primary)
            prim2.set("")
            prim3 = StringVar(Primary)
            prim3.set("")
            prim4 = StringVar(Primary)
            prim4.set("")
            prim5 = StringVar(Primary)
            prim5.set("")
            prim6 = StringVar(Primary)
            prim6.set("")
            prim7 = StringVar(Primary)
            prim7.set("")
            prim8 = StringVar(Primary)
            prim8.set("")
            prim9 = StringVar(Primary)
            prim9.set("")
            
            lblYoungGrades = Label(Primary, text = "K-1 ")
            lblSecGrade = Label(Primary, text = "2 ")
            lblThirdGrade = Label(Primary, text = "3 ")
            lblFourthGrade = Label(Primary, text = "4 ")
            lblFifthGrade = Label(Primary, text = "5 ")
            lblSixthGrade = Label(Primary, text = "6 ")
            lblSevGrades = Label(Primary, text = "7 ")
            lblEigthGrade= Label(Primary, text = "8 ")
            lblJVGrade = Label(Primary, text = "Junior Varsity ")
            lblVGrade = Label(Primary, text ="Varsity")
            
            lblPrimary = Label(Primary, text = "Primary Day:  ")
            ntryPrimary = OptionMenu(Primary, var, *days)
            ntryPrimary.config(bg="WHITE", width = 11)
            ntryPrimary["menu"].config(bg="WHITE")
            lblSecondary = Label(Primary, text = "Secondary Days: ")
            ntrySecondary = Entry(Primary, width = 35)
            ntrySecondary.insert(0, "Wednesday, Sunday, Saturday")
            lblPrimary1 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary1 = OptionMenu(Primary, var1, *days)
            ntryPrimary1.config(bg="WHITE", width = 11)
            ntryPrimary1["menu"].config(bg="WHITE")
            lblSecondary1 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary1 = Entry(Primary, width = 35)
            ntrySecondary1.insert(0, "Wednesday, Sunday, Saturday")
            lblPrimary2 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary2 = OptionMenu(Primary, var2, *days)
            ntryPrimary2.config(bg="WHITE", width = 11)
            ntryPrimary2["menu"].config(bg="WHITE")
            lblSecondary2 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary2 = Entry(Primary, width = 35)
            ntrySecondary2.insert(0, "Wednesday, Sunday, Saturday")
            lblPrimary3 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary3 = OptionMenu(Primary, var3, *days)
            ntryPrimary3.config(bg="WHITE", width = 11)
            ntryPrimary3["menu"].config(bg="WHITE")
            lblSecondary3 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary3 = Entry(Primary, width = 35)
            ntrySecondary3.insert(0, "Wednesday, Sunday, Saturday")
            lblPrimary4 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary4 = OptionMenu(Primary, var4, *days)
            ntryPrimary4.config(bg="WHITE", width = 11)
            ntryPrimary4["menu"].config(bg="WHITE")
            lblSecondary4 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary4 = Entry(Primary, width = 35)
            ntrySecondary4.insert(0, "Wednesday, Sunday, Saturday")
            lblPrimary5 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary5 = OptionMenu(Primary, var5, *days)
            ntryPrimary5.config(bg="WHITE", width = 11)
            ntryPrimary5["menu"].config(bg="WHITE")
            lblSecondary5 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary5 = Entry(Primary, width = 35)
            ntrySecondary5.insert(0, "Wednesday, Sunday, Saturday")
            lblPrimary6 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary6 = OptionMenu(Primary, var6, *days)
            ntryPrimary6.config(bg="WHITE", width = 11)
            ntryPrimary6["menu"].config(bg="WHITE")
            lblSecondary6 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary6 = Entry(Primary, width = 35)
            ntrySecondary6.insert(0, "Saturday")
            lblPrimary7 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary7 = OptionMenu(Primary, var7, *days)
            ntryPrimary7.config(bg="WHITE", width = 11)
            ntryPrimary7["menu"].config(bg="WHITE")
            lblSecondary7 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary7 = Entry(Primary, width = 35)
            ntrySecondary7.insert(0, "Saturday")
            lblPrimary8 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary8 = OptionMenu(Primary, var8, *days)
            ntryPrimary8.config(bg="WHITE", width = 11)
            ntryPrimary8["menu"].config(bg="WHITE")
            lblSecondary8 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary8 = Entry(Primary, width = 35)
            ntrySecondary8.insert(0, "Wednesday, Sunday, Saturday")
            lblPrimary9 = Label(Primary, text = "Primary Day:  ")
            ntryPrimary9 = OptionMenu(Primary, var9, *days)
            ntryPrimary9.config(bg="WHITE", width = 11)
            ntryPrimary9["menu"].config(bg="WHITE")
            lblSecondary9 = Label(Primary, text = "Secondary Days: ")
            ntrySecondary9 = Entry(Primary, width = 35)
            ntrySecondary9.insert(0, "Wednesday, Sunday, Saturday")

            btnPr1Add = Button(Primary, text ="+", command = pr1Add)
            btnPr1Add.grid(row =0, column = 3, sticky = W)
            btnPr2Add = Button(Primary, text ="+", command = pr2Add)
            btnPr2Add.grid(row =1, column = 3, sticky = W)
            btnPr3Add = Button(Primary, text ="+", command = pr3Add)
            btnPr3Add.grid(row =2, column = 3, sticky = W)
            btnPr4Add = Button(Primary, text ="+", command = pr4Add)
            btnPr4Add.grid(row =3, column = 3, sticky = W)
            btnPr5Add = Button(Primary, text ="+", command = pr5Add)
            btnPr5Add.grid(row =4, column = 3, sticky = W)
            btnPr6Add = Button(Primary, text ="+", command = pr6Add)
            btnPr6Add.grid(row =5, column = 3, sticky = W)
            btnPr7Add = Button(Primary, text ="+", command = pr7Add)
            btnPr7Add.grid(row =6, column = 3, sticky = W)
            btnPr8Add = Button(Primary, text ="+", command = pr8Add)
            btnPr8Add.grid(row =7, column = 3, sticky = W)
            btnPr9Add = Button(Primary, text ="+", command = pr9Add)
            btnPr9Add.grid(row =8, column = 3, sticky = W)
            btnPr10Add = Button(Primary, text ="+", command = pr10Add)
            btnPr10Add.grid(row =9, column = 3, sticky = W)

            optPr1Add = OptionMenu(Primary, prim, *days)
            optPr1Add.config(bg="WHITE", width = 11)
            optPr1Add["menu"].config(bg="WHITE")
            optPr2Add = OptionMenu(Primary, prim1, *days)
            optPr2Add.config(bg="WHITE", width = 11)
            optPr2Add["menu"].config(bg="WHITE")
            optPr3Add = OptionMenu(Primary, prim2, *days)
            optPr3Add.config(bg="WHITE", width = 11)
            optPr3Add["menu"].config(bg="WHITE")
            optPr4Add = OptionMenu(Primary, prim3, *days)
            optPr4Add.config(bg="WHITE", width = 11)
            optPr4Add["menu"].config(bg="WHITE")
            optPr5Add = OptionMenu(Primary, prim4, *days)
            optPr5Add.config(bg="WHITE", width = 11)
            optPr5Add["menu"].config(bg="WHITE")
            optPr6Add = OptionMenu(Primary, prim5, *days)
            optPr6Add.config(bg="WHITE", width = 11)
            optPr6Add["menu"].config(bg="WHITE")
            optPr7Add = OptionMenu(Primary, prim6, *days)
            optPr7Add.config(bg="WHITE", width = 11)
            optPr7Add["menu"].config(bg="WHITE")
            optPr8Add = OptionMenu(Primary, prim7, *days)
            optPr8Add.config(bg="WHITE", width = 11)
            optPr8Add["menu"].config(bg="WHITE")
            optPr9Add = OptionMenu(Primary, prim8, *days)
            optPr9Add.config(bg="WHITE", width = 11)
            optPr9Add["menu"].config(bg="WHITE")
            optPr10Add = OptionMenu(Primary, prim9, *days)
            optPr10Add.config(bg="WHITE", width = 11)
            optPr10Add["menu"].config(bg="WHITE")
            
            btnDaysSubmit = Button(self, text = "Next", width = 25, command = passData)
            btnBack = Button(self, text = "Back", width = 25, command = daysBack)
            btnBack.grid(row = 6, columnspan = 2, column=0, sticky = S, padx = 5)
            btnDaysSubmit.grid(row = 6, columnspan = 2, column = 1, sticky = S, padx = 5)

            #assign input location
            lblYoungGrades.grid(row = 0, column = 0, sticky = W, padx = 5, pady = 4)
            lblSecGrade.grid(row = 1, column = 0, sticky = W, padx = 5, pady = 4)
            lblThirdGrade.grid(row = 2, column = 0, sticky = W, padx = 5, pady = 4)
            lblFourthGrade.grid(row = 3, column = 0, sticky = W, padx = 5, pady = 4)
            lblFifthGrade.grid(row = 4, column = 0, sticky = W, padx = 5, pady = 4)
            lblSixthGrade.grid(row = 5, column = 0, sticky = W, padx = 5, pady = 4)
            lblSevGrades.grid(row = 6, column = 0, sticky = W, padx = 5, pady = 4)
            lblEigthGrade.grid(row = 7, column = 0, sticky = W, padx = 5, pady = 4)
            lblJVGrade.grid(row = 8, column = 0, sticky = W, padx = 5, pady = 4)
            lblVGrade.grid(row = 9, column = 0, sticky = W, padx = 5, pady = 4)
            
            lblPrimary.grid(row = 0, column = 1, padx= 5, pady =4)
            ntryPrimary.grid(row = 0, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary.grid(row = 0, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary.grid(row = 0, column = 5, padx = 5, pady = 4, sticky = W)
            lblPrimary1.grid(row = 1, column = 1, padx= 5, pady =4)
            ntryPrimary1.grid(row = 1, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary1.grid(row = 1, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary1.grid(row = 1, column = 5, padx = 5, pady = 4, sticky = W)
            lblPrimary2.grid(row = 2, column = 1, padx= 5, pady =4)
            ntryPrimary2.grid(row = 2, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary2.grid(row = 2, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary2.grid(row = 2, column = 5, padx = 5, pady = 4, sticky= W)
            lblPrimary3.grid(row = 3, column = 1, padx= 5, pady =4)
            ntryPrimary3.grid(row = 3, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary3.grid(row = 3, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary3.grid(row = 3, column = 5, padx = 5, pady = 4, sticky= W)
            lblPrimary4.grid(row = 4, column = 1, padx= 5, pady =4)
            ntryPrimary4.grid(row = 4, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary4.grid(row = 4, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary4.grid(row = 4, column = 5, padx = 5, pady = 4, sticky= W)
            lblPrimary5.grid(row = 5, column = 1, padx= 5, pady =4)
            ntryPrimary5.grid(row = 5, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary5.grid(row = 5, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary5.grid(row = 5, column = 5, padx = 5, pady = 4, sticky= W)
            lblPrimary6.grid(row = 6, column = 1, padx= 5, pady =4)
            ntryPrimary6.grid(row = 6, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary6.grid(row = 6, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary6.grid(row = 6, column = 5, padx = 5, pady = 4, sticky= W)
            lblPrimary7.grid(row = 7, column = 1, padx= 5, pady =4)
            ntryPrimary7.grid(row = 7, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary7.grid(row = 7, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary7.grid(row = 7, column = 5, padx = 5, pady = 4, sticky= W)
            lblPrimary8.grid(row = 8, column = 1, padx= 5, pady =4)
            ntryPrimary8.grid(row = 8, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary8.grid(row = 8, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary8.grid(row = 8, column = 5, padx = 5, pady = 4, sticky= W)
            lblPrimary9.grid(row = 9, column = 1, padx= 5, pady =4)
            ntryPrimary9.grid(row = 9, column = 2, padx = 5, pady = 4, sticky = W)
            lblSecondary9.grid(row = 9, column = 4, padx = 5, pady = 4, sticky = W)
            ntrySecondary9.grid(row = 9, column = 5, padx = 5, pady = 4, sticky= W)



        


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
                    final = open("Config.ini", 'w')
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
            #btnSubmit = Button(self, text = "Submit", width = 25, command = submitInitial)
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
            timeList = ['1:00am', '1:00pm', '1:10am', '1:10pm', '1:15am', '1:15pm', '1:20am', '1:20pm', '1:30am', '1:30pm', '1:40am', '1:40pm', '1:45am', '1:45pm', '1:50am', '1:50pm',
                     '2:00am', '2:00pm', '2:10am', '2:10pm', '2:15am', '2:15pm', '2:20am', '2:20pm', '2:30am', '2:30pm', '2:40am', '2:40pm', '2:45am', '2:45pm', '2:50am', '2:50pm',
                     '3:00am', '3:00pm', '3:10am', '3:10pm', '3:15am', '3:15pm', '3:20am', '3:20pm', '3:30am', '3:30pm', '3:40am', '3:40pm', '3:45am', '3:45pm', '3:50am', '3:50pm',
                     '4:00am', '4:00pm', '4:10am', '4:10pm', '4:15am', '4:15pm', '4:20am', '4:20pm', '4:30am', '4:30pm', '4:40am', '4:40pm', '4:45am', '4:45pm', '4:50am', '4:50pm',
                     '5:00am', '5:00pm', '5:10am', '5:10pm', '5:15am', '5:15pm', '5:20am', '5:20pm', '5:30am', '5:30pm', '5:40am', '5:40pm', '5:45am', '5:45pm', '5:50am', '5:50pm',
                     '6:00am', '6:00pm', '6:10am', '6:10pm', '6:15am', '6:15pm', '6:20am', '6:20pm', '6:30am', '6:30pm', '6:40am', '6:40pm', '6:45am', '6:45pm', '6:50am', '6:50pm',
                     '7:00am', '7:00pm', '7:10am', '7:10pm', '7:15am', '7:15pm', '7:20am', '7:20pm', '7:30am', '7:30pm', '7:40am', '7:40pm', '7:45am', '7:45pm', '7:50am', '7:50pm',
                     '8:00am', '8:00pm', '8:10am', '8:10pm', '8:15am', '8:15pm', '8:20am', '8:20pm', '8:30am', '8:30pm', '8:40am', '8:40pm', '8:45am', '8:45pm', '8:50am', '8:50pm',
                     '9:00am', '9:00pm', '9:10am', '9:10pm', '9:15am', '9:15pm', '9:20am', '9:20pm', '9:30am', '9:30pm', '9:40am', '9:40pm', '9:45am', '9:45pm', '9:50am', '9:50pm',
                     '10:00am', '10:00pm', '10:10am', '10:10pm', '10:15am', '10:15pm', '10:20am', '10:20pm', '10:30am', '10:30pm', '10:40am', '10:40pm', '10:45am', '10:45pm', '10:50am', '10:50pm',
                     '11:00am', '11:00pm', '11:10am', '11:10pm', '11:15am', '11:15pm', '11:20am', '11:20pm', '11:30am', '11:30pm', '11:40am', '11:40pm', '11:45am', '11:45pm', '11:50am', '11:50pm',
                     '12:00am', '12:00pm', '12:10am', '12:10pm', '12:15am', '12:15pm', '12:20am', '12:20pm', '12:30am', '12:30pm', '12:40am', '12:40pm', '12:45am', '12:45pm', '12:50am', '12:50pm']
                     

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
    root.quit()
    os._exit(0)

def main():
    global root
    root = schedulerConfig()
    root.master.resizable(0,0)
    root.mainloop()



main()
