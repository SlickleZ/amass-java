package main;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;

public class DBManager {

    private Connection conn;
    private Object[][] rows;
    private Object[] cols;

    String host, username, password;
    public DBManager(String host, String username, String password){
        this.host = host;
        this.username = username;
        this.password = password;
    }

    public void connect_DB(){
        try{
            conn = DriverManager.getConnection(host, username, password);
            System.out.println("DB Connected!");
        }catch(SQLException e){
            System.err.print(e.getMessage());
        }
    }

    public void close_DB(){
        try {
            conn.close();
            System.out.println("DB disconnected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void query(String query){
        try {
            connect_DB();
            int rowIndex = 0, columnIndex = 0;
            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(query);
//            System.out.println("In method query");
            ResultSetMetaData col = rs.getMetaData();

            cols = new Object[col.getColumnCount()];

            for(;columnIndex < col.getColumnCount(); columnIndex++){
                cols[columnIndex] = col.getColumnName(columnIndex+1);
            }
            System.out.println(Arrays.deepToString(getColumns()));
            rs.last();
            //System.out.println(rs.getRow());
            if(rs.getRow() == 0) {
                rows = new Object[1][5];
                rows[0][1] = "Initial Data";
                rows[0][2] = 0.000;
                rows[0][3] = 0.000;
                rows[0][4] = 0.000;
                close_DB();
                statement.close();
                return;
            }else
                rows = new Object[rs.getRow()][5];
            rs.first();

            rows[rowIndex][0] = rs.getString(1);
            rows[rowIndex][1] = rs.getString(2);
            rows[rowIndex][2] = rs.getBigDecimal(3);
            rows[rowIndex][3] = rs.getBigDecimal(4);
            rows[rowIndex][4] = rs.getBigDecimal(5);
            rowIndex+=1;

            while(rs.next()){
                rows[rowIndex][0] = rs.getString(1);
                rows[rowIndex][1] = rs.getString(2);
                rows[rowIndex][2] = rs.getBigDecimal(3);
                rows[rowIndex][3] = rs.getBigDecimal(4);
                rows[rowIndex][4] = rs.getBigDecimal(5);
                rowIndex+=1;
            }System.out.println(Arrays.deepToString(getRows()));
            close_DB();
            statement.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void Update_Query(String query){
        try {
            connect_DB();
            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.executeUpdate(query);
//            System.out.println("In query update\n");
            query("select * from ledger.ledger");
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "Try again", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public void dropTable() {
        try{
            connect_DB();
            Statement statement = conn.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS ledger");
//            System.out.println("After drop update\n");
    }catch (SQLException e){
        JOptionPane.showMessageDialog(null, e.getMessage(),
                "Try again", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }

    public void export_to_xlsx(String path) throws Exception {
        String FileName = path.concat(".xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Database Export");
            sheet.setColumnWidth(0, 20*256);
            sheet.setColumnWidth(1, 25*256);
            sheet.setColumnWidth(2, 13*256);
            sheet.setColumnWidth(3, 13*256);
            sheet.setColumnWidth(4, 13*256);
            CellStyle cellStyle;
            Font cellFont = workbook.createFont();
            cellFont.setFontName("Comic Sans MS");
            cellFont.setFontHeightInPoints((short)12);
            cellFont.setColor((short) 4);
            XSSFCell cell;

            int rowCount = 0;
            int columnCount = 0;
            XSSFRow sheetRow = sheet.createRow(rowCount++);
            for(Object col : cols){
                cell = sheetRow.createCell(columnCount++);

                cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setBorderBottom(BorderStyle.THICK);
                cellStyle.setBorderLeft(BorderStyle.THICK);
                cellStyle.setBorderRight(BorderStyle.THICK);
                cellStyle.setBorderTop(BorderStyle.THICK);
                cellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setFont(cellFont);

                cell.setCellStyle(cellStyle);
                cell.setCellValue((String) col);
            }

            for(Object[] row : rows){
                sheetRow = sheet.createRow(rowCount++);
                columnCount = 0;
                for(Object rowObj : row){
                    cell = sheetRow.createCell(columnCount++);

                    cellStyle = workbook.createCellStyle();
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    if(rowCount >= rows.length+1)
                        cellStyle.setBorderBottom(BorderStyle.THICK);
                    else
                        cellStyle.setBorderBottom(BorderStyle.THIN);
                    cellStyle.setBorderLeft(BorderStyle.THICK);
                    cellStyle.setBorderRight(BorderStyle.THICK);
                    cellStyle.setBorderTop(BorderStyle.THIN);
                    cellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE1.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellFont.setFontHeightInPoints((short)10);
                    cellStyle.setFont(cellFont);
                    cell.setCellStyle(cellStyle);
                    if(rowObj instanceof String)
                        cell.setCellValue((String) rowObj);
                    if(rowObj instanceof BigDecimal){
                        cell.setCellValue(((BigDecimal) rowObj).floatValue());
                    }
                }
            }
        FileOutputStream outputStream = new FileOutputStream(FileName);
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();
        JOptionPane.showMessageDialog(null, "Write .xlsx successfully.",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
//        System.out.println("Write .xlsx successfully");
    }

    @SuppressWarnings("MalformedFormatString")
    public void export_to_csv(String path) throws Exception {
        String FileName = path.concat(".csv");

            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FileName));

            /* Write Columns */
            String ColumnLine = "";
            for(Object current : cols){
                ColumnLine = ColumnLine.concat((String)current).concat(",");
            }
            fileWriter.write(ColumnLine);
            fileWriter.newLine();

            /* Write rows */
            for (Object[] row : rows) {
                String RowLine = String.format("\"%s\",%s,%.3f,%.3f,%.3f",
                        row[0], row[1], row[2], row[3], row[4]);
                fileWriter.write(RowLine);
                fileWriter.newLine();
            }
            JOptionPane.showMessageDialog(null, "Write .csv successfully.",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
//            System.out.println("Write .csv successfully");
            fileWriter.flush();
            fileWriter.close();

    }

//    public String getFilename(String filename){
//        DateFormat format = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
//        String dateTime = format.format(new Date());
//        return filename.concat("_export").concat(String.format("_%s", dateTime));
//    }

    public BigDecimal getOldBalance(){ return (BigDecimal) rows[rows.length-1][4]; }
    public Object[][] getRows(){ return rows; }
    public Object[] getColumns(){
        return cols;
    }
}
