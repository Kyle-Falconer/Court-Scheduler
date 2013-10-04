import java.util.*;
import java.io.*;
import java.util.zip.*;

/**A class that defines a the data type of a cell
*@author Tan Hong Cheong
*@version 20121004
*/
abstract class DataType
{
    /**@return the XML string that represent this data type
    */
    public abstract String getXMLString();
    
    /**@return the string representation of this cell
    */
    public abstract String getString();
}

/**A class that defines a string in a cell
*@author Tan Hong Cheong
*@version 20121004
*/
class StringType extends DataType
{
    /**constructor
    *@param s the string
    */
    public StringType(String s)
    {
        this.s = s;
        index = 0;
    }
    
    /**@return the XML string that represent this data type
    */
    public String getXMLString()
    {
        return "<c t='s'><v>"+index+"</v></c>";
    }
    
    
    /**
      *Set the string value
      *@param s the s value
      */
    public void setString(String s)
    {
        this.s = s;
    }

    /**
      *@return the string value
      */
    public String getString()
    {
        return s;
    }

    /**
      *the index in the shared string
      *@param index the index value
      */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
      *@return the index value
      */
    public int getIndex()
    {
        return index;
    }

    /**
      *the index in the shared string
      */
    private int index;

    /**
      *The string
      */
    private String s;
}

/**A class that defines a double in a cell
*@author Tan Hong Cheong
*@version 20121004
*/
class DoubleType extends DataType
{
    /**constructor
    *@param d the double value
    */
    public DoubleType(double d)
    {
        this.d = d;
    }
    
    /**@return the XML string that represent this data type
    */
    public String getXMLString()
    {
        return "<c><v>"+d+"</v></c>";
    }
    
    /**
      *Set the double value
      *@param d the d value
      */
    public void setValue(double d)
    {
        this.d = d;
    }

    /**
      *@return the double value
      */
    public double getValue()
    {
        return d;
    }

    /**@return the string representation of this cell
    */
    public String getString()
    {
        return ""+d;
    }
    
    /**
      *the double value
      */
    private double d;
}

/**A class that defines a string in a cell
*@author Tan Hong Cheong
*@version 20121004
*/
class FormulaType extends DataType
{
    /**constructor
    *@param f the formula
    */
    public FormulaType(String f)
    {
        this.f = f;
    }
    
    /**@return the XML string that represent this data type
    */
    public String getXMLString()
    {
        return "<c><f>"+f+"</f></c>";
    }
    
    /**
      *Set the formula
      *@param f the formula
      */
    public void setFormula(String f)
    {
        this.f = f;
    }

    /**
      *@return the formula value
      */
    public String getFormula()
    {
        return f;
    }
    
    /**@return the string representation of this cell
    */
    public String getString()
    {
        return f;
    }
    
    /**
      *the formula
      */
    private String f;
}

/**a class that defines a spread sheet
*@author Tan Hong Cheong
*@version 20101004
*/
class Spreadsheet
{
    /**Constructor
    *@param name the name of the spread sheet
    */
    public Spreadsheet(String name)
    {
        this.name = name;
        rows = new ArrayList<List<DataType>>();
    }
    
    /**add a row of string
    *@param row the row of string
    */
    public void addRow(String ... row)
    {
        ArrayList<String> data = new ArrayList<String>();
        for(String s:row)
        {
            data.add(s);
        }
        addRow(data);
    }
    
    /**add a row of string
    *@param row the row of string
    */
    public void addRow(List<String> row)
    {
        
        if (row.size()==0)
        {
            addRow("");
        }
        else
        {
            ArrayList<DataType> list = new ArrayList<DataType>();
            for(String s:row)
            {
                //check if string is a double
                try
                {
                    double d = Double.parseDouble(s);
                    //check if 1st character is 0
                    //if it is 0xxx, we do not want to save as number
                    if ((s.length()>0)&&(s.charAt(0)=='0')&&(s.length()!=1))//0 should be a double
                    {
                        //not a number, add as string
                        list.add(new StringType(s));
                    }
                    else
                    {
                        list.add(new DoubleType(d));
                    }
                }
                catch(NumberFormatException e)
                {
                    //not a number, add as string
                    list.add(new StringType(s));
                }
            }
            addDataRow(list);
        }
    }
    
    /**add a row of data
    *@param row the row of data
    */
    private void addDataRow(List<DataType> row)
    {
        rows.add(row);
    }
    
    /**
      *the name of the spreadsheet
      *@param name the name value
      */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
      *@return the name value
      */
    public String getName()
    {
        return name;
    }
    
    /**@return the rows of string
    */
    public List<List<DataType>> getRows()
    {
        return rows;
    }
    
    /**set a cell as a formula
    *@param r the row
    *@param c the col
    *@throws ArrayIndexOutOfBoundsException
    */
    public void setCellAsFormula(int r, int c)
    {
        List<DataType> row = rows.get(r);
        DataType data = row.get(c);
        FormulaType formula = new FormulaType(data.getString());
        row.set(c,formula);
    }
    
    /**
      *the name of the spreadsheet
      */
    private String name;
    
    /**the array of rows of data
    */
    private List<List<DataType>> rows;
}

/**A class that defines a simple XLXS file
*@author Tan Hong Cheong
*@version 20101004
*/
public class SimpleXLSXFile
{
    /**constructor
    *@param filename the filename of the file
    */
    public SimpleXLSXFile(String filename)
    {
        this.filename = filename;
        sheets = new ArrayList<Spreadsheet>();
        sharesStrings = new ArrayList<String>();
    }
    
    /**add a spreadsheet
    */
    public void addSheet(Spreadsheet sheet)
    {
        sheets.add(sheet);
    }
    
    /**
      *the filename of the file
      *@param filename the filename value
      */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }
    
    /**create worksheet
    *@param sheet the sheet to be saved
    *@param index the pos of the spreadsheet
    *@throws IOException when there is an io error
    */
    private void createWorksheet(Spreadsheet sheet,int pos) throws IOException
    {
        //ZipEntry entry = new ZipEntry("xl/worksheets/"+sheet.getName()+".xml");
        ZipEntry entry = new ZipEntry("xl/worksheets/sheet"+pos+".xml");
        zos.putNextEntry(entry);                    
        writer.write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");writer.newLine();
        writer.write("<worksheet xmlns='http://schemas.openxmlformats.org/spreadsheetml/2006/main' xmlns:r='http://schemas.openxmlformats.org/officeDocument/2006/relationships'>");writer.newLine();
        writer.write("<sheetData>");writer.newLine();
        List<List<DataType>> rows = sheet.getRows();
        int rowNo = 1;
        for(List<DataType> row:rows)
        {
            writer.write("\t<row r='"+rowNo+"' spans='1:"+row.size()+"'>");writer.newLine();
            for(DataType data:row)
            {
                writer.write("\t\t"+data.getXMLString());writer.newLine();
            }
            writer.write("\t</row>");writer.newLine();
            rowNo++;
        }
        
        writer.write("</sheetData>");writer.newLine();
        writer.write("</worksheet>");writer.newLine();
        writer.flush();
        zos.closeEntry();
    }
    
    /**create the xl/sharedStrings.xml
    *@throws IOException when there is an io error
    */
    private void createSharedStringsXML() throws IOException
    {
        ZipEntry entry = new ZipEntry("xl/sharedStrings.xml");
        zos.putNextEntry(entry);                    
        int total = 0;
        int currentIndex = 0;
        //search through the sheets for all strings
        for(Spreadsheet sheet:sheets)
        {
            List<List<DataType>> rows = sheet.getRows();
            for(List<DataType> row:rows)
            {
                for(DataType data:row)
                {
                    if (data instanceof StringType)
                    {
                        StringType s = (StringType)data;
                        total++;
                        int index = sharesStrings.indexOf(s.getString());
                        if (index==-1)
                        {
                            s.setIndex(currentIndex);
                            currentIndex++;
                            sharesStrings.add(s.getString());
                        }
                        else
                        {
                            s.setIndex(index);
                        }
                    }
                }
            }
        }
        
        writer.write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");writer.newLine();
        writer.write("<sst xmlns='http://schemas.openxmlformats.org/spreadsheetml/2006/main' count='"+total+"' uniqueCount='"+sharesStrings.size()+"'>");writer.newLine();
        
        for(int i=0;i<sharesStrings.size();i++)
        {
            String s = sharesStrings.get(i);
            //replace all & with &amp;
            s = s.replace("&","&amp;");
            //replace all < with &lt;
            s = s.replace("<","&lt;");
            //replace all > with &gt;
            s = s.replace(">","&gt;");
            writer.write("\t<si><t>"+s+"</t></si>");writer.newLine();
        }
        writer.write("</sst>");writer.newLine();
        writer.flush();
        zos.closeEntry();
    }
    
    /**create the xl/workbook.xml
    *@throws IOException when there is an io error
    */
    private void createWorkbookXML() throws IOException
    {
        ZipEntry entry = new ZipEntry("xl/workbook.xml");
        zos.putNextEntry(entry);                    
        writer.write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");writer.newLine();        
        writer.write("<workbook xmlns='http://schemas.openxmlformats.org/spreadsheetml/2006/main' xmlns:r='http://schemas.openxmlformats.org/officeDocument/2006/relationships'>");writer.newLine();
        writer.write("\t<sheets>");writer.newLine();
        int id=1;
        for(Spreadsheet sheet:sheets)
        {
            String data = "<sheet name='"+sheet.getName()+"' sheetId='"+id+"' r:id='rId"+id+"'/>";
            writer.write("\t\t"+data);writer.newLine();
            id++;
        }
        writer.write("\t</sheets>");writer.newLine();
        writer.write("</workbook>");writer.newLine();
        writer.flush();
        zos.closeEntry();
    }
    
    /**create the xl/_rels/workbook.xml.rels
    *@throws IOException when there is an io error
    */
    private void createXL_rel() throws IOException
    {        
        ZipEntry entry = new ZipEntry("xl/_rels/workbook.xml.rels");
        zos.putNextEntry(entry);                    
        writer.write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");writer.newLine();
        writer.write("<Relationships xmlns='http://schemas.openxmlformats.org/package/2006/relationships'>");writer.newLine();
        int id=1;
        for(Spreadsheet sheet:sheets)
        {
            String data = "<Relationship Id='rId"+id+"' Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet' Target='worksheets/sheet"+id+".xml'/>";
            writer.write("\t"+data);writer.newLine();
            id++;
        }
        {
            String data = "<Relationship Id='rId"+id+"' Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/sharedStrings' Target='sharedStrings.xml'/>";
            writer.write("\t"+data);writer.newLine();
        }
        writer.write("</Relationships>");writer.newLine();
        writer.flush();
        zos.closeEntry();
    }
    
    /**create the [Content_Types].xml
    *@throws IOException when there is an io error
    */
    private void creatContentType() throws IOException
    {
        ZipEntry entry = new ZipEntry("[Content_Types].xml");
        zos.putNextEntry(entry);                    
        writer.write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");writer.newLine();
        writer.write("<Types xmlns='http://schemas.openxmlformats.org/package/2006/content-types'>");writer.newLine();
        writer.write("\t<Default Extension='rels' ContentType='application/vnd.openxmlformats-package.relationships+xml'/>");writer.newLine();
        writer.write("\t<Default Extension='xml' ContentType='application/xml'/>");writer.newLine();
        writer.write("\t<Override PartName='/xl/workbook.xml' ContentType='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml'/>");writer.newLine();
        int id = 1;
        for(Spreadsheet sheet:sheets)
        {
            String data = "<Override PartName='/xl/worksheets/sheet"+id+".xml' ContentType='application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml'/>";
            writer.write('\t'+data);writer.newLine();
            id++;
        }
        writer.write("\t<Override PartName='/xl/sharedStrings.xml' ContentType='application/vnd.openxmlformats-officedocument.spreadsheetml.sharedStrings+xml'/>");writer.newLine();
        writer.write("</Types>");writer.newLine();
        writer.flush();
        zos.closeEntry();
    }
    
    /**create the .rels file
    *@throws IOException when there is an io error
    */
    private void createRels() throws IOException
    {
        ZipEntry entry = new ZipEntry("_rels/.rels");
        zos.putNextEntry(entry);
        writer.write("<?xml version='1.0' encoding='UTF-8' standalone='yes'?>");writer.newLine();
        writer.write("<Relationships xmlns='http://schemas.openxmlformats.org/package/2006/relationships'>");writer.newLine();
        writer.write("\t<Relationship Id='rId1' Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument' Target='xl/workbook.xml'/>");writer.newLine();
        writer.write("</Relationships>");writer.newLine();
        writer.flush();
        zos.closeEntry();
    }
    
    /**save the file
    *@throws IOException when there is an IOerror
    */
    public void save() throws IOException
    {
        
        FileOutputStream outputFile = new FileOutputStream(filename);
        zos = new ZipOutputStream(outputFile);
        writer = new BufferedWriter(new OutputStreamWriter(zos));
        createRels();
        
        creatContentType();        
        
        createXL_rel();
        createWorkbookXML();
        createSharedStringsXML();
        for(int i=0;i<sheets.size();i++)
        {
            Spreadsheet sheet = sheets.get(i);
            createWorksheet(sheet,i+1);
        }
        zos.close();
    }
    
    /**
      *@return the filename value
      */
    public String getFilename()
    {
        return filename;
    }
    
    /**the list of spreadsheet
    */
    private List<Spreadsheet> sheets;
    
    /**
      *the filename of the file
      */
    private String filename;
    
    /**the array of strings used in the workbook
    */
    private ArrayList<String> sharesStrings;
    
    /**the zipped output stream
    */
    private ZipOutputStream zos;
    
    /**the root of the zip file
    */
    private ZipEntry root;
    
    /**the buffered writer
    */
    private BufferedWriter writer;
    
    public static void main(String[] args)
    {
        try
        {
            Spreadsheet sheet1 = new Spreadsheet("a1");
            
            ArrayList<String> row = new ArrayList<String>();
            row.add("abc");
            row.add(""+37.5);
            sheet1.addRow(row);
            sheet1.addRow("a","d","c");
            sheet1.addRow("1.1","1","$A$3+$B$3");
            sheet1.setCellAsFormula(2,2);
            SimpleXLSXFile xlsxFile = new SimpleXLSXFile("testXLSX.xlsx");
            xlsxFile.addSheet(sheet1);
            xlsxFile.save();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}