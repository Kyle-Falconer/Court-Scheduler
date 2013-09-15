Court-Scheduler
===============

Vision Statement
----------------
Created for Shane Friebe, the Sports Director at The Courts, the Court Scheduler was designed to automate the scheduling process for the staff at The Courts, dramatically cutting the time spent on scheduling basketball leagues.

Setting itself apart from other scheduling programs, the Court Scheduler is designed to allow as many constraints as possible, a feature that is directly beneficial to The Courts where they pride themselves on accommodating for any requests a team may have.

The core feature of the Court Scheduler is that it allows the user to input many different conferences of different ages and skill levels, and receive a master chart with every conference scheduled. Another feature displayed by Group 3 is the "Web Output" feature, which outputs a schedule that is already formatted to be inserted on a webpage. 


**It will save you and us a lot of time by setting up your development environment correctly.**
It solves all known pitfalls that can disrupt your development.
It also describes all guidelines, tips and tricks.
If you want your pull requests (or patches) to be merged into master, please respect those guidelines.


To import the project using IntelliJ
------------------------------------

1. Clone the repository using whatever method you use (command-line or SourceTree).
2. Open IntelliJ and choose "Import Project" and select the parent directory of the project (example: "C:\git\Court-Scheduler")
3. Choose the option which says "Import project from external model", then select Maven and hit "Next".
4. Check the boxes at the bottom for "Automatically download: Sources"
5. Click Next all the way through to the end, you can change the "Project name" if you like.
6. If you get the prompt asking if you want "to create a project with no SDK assigned" then you must hit cancel and then click on the "+" symbol and locate the JDK installation on your computer (download and install from Oracle if needed).

To run one of the examples using IntelliJ
-----------------------------------------

1. With the project open in IntelliJ, make the project by going to the Build menu, then selecting Make. This may take some time since Maven has to download all the dependencies. If you see errors during this process, just be patient and wait for it to finish. You may have to "make" the project twice.
2. Open the Edit Configurations dialog via the Run menu, then click on the "+" button at the top and choose "Application"
3. In the Configuration tab, under Main Class, put: "org.optaplanner.examples.nurserostering.app.NurseRosteringApp"
4. Under "Working directory", select the "optaplanner-examples" folder under the project's main directory, example: "C:\git\Court-Scheduler\optaplanner-examples"
5. Under "Use classpath of module" choose "optaplanner examples"
6. Click OK, and then "Run" under the Run menu to run the example.
