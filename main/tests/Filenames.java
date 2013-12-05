import courtscheduler.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Kyle
 * Date: 12/4/13
 * Time: 6:04 PM
 */
@RunWith(JUnit4.class)
public class Filenames {

    @Test
    public void parentDirectory() {
        String[][] tests = {
                {"C:\\court-scheduler\\smallBook.xlsx", "C:\\court-scheduler"},
                {"../court-scheduler/smallBook.xlsx", "..\\court-scheduler"},
                {"./smallBook.xlsx", "."+File.separator},
                {"smallBook.xlsx", "."+ File.separator}
        };
        for (String[] test : tests){
            assertEquals("\"" + test[0] + "\" must be \"" + test[1] + "\"", test[1], Main.parentDirectory(test[0]));
        }
    }

    @Test
    public void folderNames() {
        String[][] tests = {
                {"C:\\court-scheduler", "C:\\court-scheduler\\"},
                {"../court-scheduler", "..\\court-scheduler\\"},
                {"C:\\court-scheduler\\", "C:\\court-scheduler\\"},
                {"../court-scheduler\\", "..\\court-scheduler"+File.separator},
        };
        for (String[] test : tests){
            assertEquals("\""+test[0]+"\" must be \""+test[1]+"\"", test[1], Main.parseFolder(test[0]));
        }
    }
}
