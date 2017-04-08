/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yyl.common.utils.excel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import com.yyl.common.utils.json.JacksonUtil;
import com.yyl.common.utils.string.StringUtil;

/**
 *
 * @author yangyl
 * @date 2017-1-7
 */
public class ExcelTools extends FileTools {
    
    public static final String OFFICE_EXCEL_2003_SUFFIX = "xls";
    
    public static final String OFFICE_EXCEL_2010_SUFFIX = "xlsx";
    
    /*--------------------------Read Excel Start------------------------------------------*/
    /**
     * 解决思路：采用Apache的POI的API来操作Excel，读取内容后保存到List中，再将List转Json（推荐Linked，增删快，与Excel表顺序保持一致）
     * @param inputStream 为了便于解析本地url和网络url，统一使用input流
     * @param FileName 文件名，通过文件名的后缀来判断是什么格式的excel
     * @return Map  一个线性HashMap，以Excel的sheet表顺序，并以sheet表明作为key，sheet表转换json后的字符串作为value
     * @throws IOException
     */
    public static Map<String,String> excel2jsonWithHeaders(InputStream inputStream, String FileName) throws IOException {

        System.out.println("excel2json方法执行....");

        // 返回的map
        Map<String,String> excelMap = new LinkedHashMap<>();

        // Excel列的样式，主要是为了解决Excel数字科学计数的问题
        CellStyle cellStyle;
        // 根据Excel构成的对象
        Workbook wb;
        // 如果是2007及以上版本，则使用想要的Workbook以及CellStyle
        if(FileName.endsWith("xlsx")){
            System.out.println("是2007及以上版本  xlsx");
            wb = new XSSFWorkbook(inputStream);
            XSSFDataFormat dataFormat = (XSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        }else{
            System.out.println("是2007以下版本  xls");
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            wb = new HSSFWorkbook(fs);
            HSSFDataFormat dataFormat = (HSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        }

        // sheet表个数
        int sheetsCounts = wb.getNumberOfSheets();
        // 遍历每一个sheet
        for (int i = 0; i < sheetsCounts; i++) {
            Sheet sheet = wb.getSheetAt(i);
            System.out.println("第" + i + "个sheet:" + sheet.toString());

            // 一个sheet表对于一个List
            List list = new LinkedList();

            // 将第一行的列值作为正个json的key
            String[] cellNames;
            // 取第一行列的值作为key
            Row fisrtRow = sheet.getRow(0);
            // 如果第一行就为空，则是空sheet表，该表跳过
            if(null == fisrtRow){
                continue;
            }
            // 得到第一行有多少列
            int curCellNum = fisrtRow.getLastCellNum();
            System.out.println("第一行的列数：" + curCellNum);
            // 根据第一行的列数来生成列头数组
            cellNames = new String[curCellNum];
            // 单独处理第一行，取出第一行的每个列值放在数组中，就得到了整张表的JSON的key
            for (int m = 0; m < curCellNum; m++) {
                Cell cell = fisrtRow.getCell(m);
                // 设置该列的样式是字符串
                cell.setCellStyle(cellStyle);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                // 取得该列的字符串值
                cellNames[m] = getCellValue(cell);
            }
            for (String s : cellNames) {
                System.out.print("得到第" + i +" 张sheet表的列头： " + s + ",");
            }
            System.out.println();

            // 从第二行起遍历每一行
            int rowNum = sheet.getLastRowNum();
            System.out.println("总共有 " + rowNum + " 行");
            for (int j = 1; j < rowNum; j++) {
                // 一行数据对于一个Map
                LinkedHashMap rowMap = new LinkedHashMap();
                // 取得某一行
                Row row = sheet.getRow(j);
                int cellNum = row.getLastCellNum();
                // 遍历每一列
                for (int k = 0; k < cellNum; k++) {
                    Cell cell = row.getCell(k);

                    cell.setCellStyle(cellStyle);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    // 保存该单元格的数据到该行中
                    rowMap.put(cellNames[k],getCellValue(cell));
                }
                // 保存该行的数据到该表的List中
                list.add(rowMap);
            }
            // 将该sheet表的表名为key，List转为json后的字符串为Value进行存储
            excelMap.put(sheet.getSheetName(),JacksonUtil.bean2Json(list));
        }

        System.out.println("excel2json方法结束....");

        return excelMap;
    }
    
    /*--------------------------Read Excel Start------------------------------------------*/
    /**
     * 解决思路：采用Apache的POI的API来操作Excel，读取内容后保存到List中，再将List转Json（推荐Linked，增删快，与Excel表顺序保持一致）
     * @param inputStream 为了便于解析本地url和网络url，统一使用input流
     * @param FileName 文件名，通过文件名的后缀来判断是什么格式的excel
     * @param headers 给定表格的头部。。list,如果时String数组-->Arrays.asList();
     * @return Map  一个线性HashMap，以Excel的sheet表顺序，并以sheet表明作为key，sheet表转换json后的字符串作为value
     * @throws IOException
     */
    public static Map<String,String> excel2json(InputStream inputStream, String fileName, List<String> headers) throws IOException {

        System.out.println("excel2json方法执行....");

        // 返回的map
        Map<String,String> excelMap = new LinkedHashMap<>();

        // Excel列的样式，主要是为了解决Excel数字科学计数的问题
        CellStyle cellStyle;
        // 根据Excel构成的对象
        Workbook wb;
        // 如果是2007及以上版本，则使用想要的Workbook以及CellStyle
        if(fileName.endsWith("xlsx")){
            System.out.println("是2007及以上版本  xlsx");
            wb = new XSSFWorkbook(inputStream);
            XSSFDataFormat dataFormat = (XSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        }else{
            System.out.println("是2007以下版本  xls");
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            wb = new HSSFWorkbook(fs);
            HSSFDataFormat dataFormat = (HSSFDataFormat) wb.createDataFormat();
            cellStyle = wb.createCellStyle();
            // 设置Excel列的样式为文本
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
        }

        // sheet表个数
        int sheetsCounts = wb.getNumberOfSheets();
        // 遍历每一个sheet
        for (int i = 0; i < sheetsCounts; i++) {
            Sheet sheet = wb.getSheetAt(i);
            System.out.println("第" + i + "个sheet:" + sheet.toString());

            // 一个sheet表对于一个List
            List list = new LinkedList();

            // 取第一行列的值作为key
            Row fisrtRow = sheet.getRow(0);
            // 如果第一行就为空，则是空sheet表，该表跳过
            if(null == fisrtRow){
                continue;
            }
            // 得到第一行有多少列
            int curCellNum = fisrtRow.getLastCellNum();
            System.out.println("第一行的列数：" + curCellNum);
            
            // 从第二行起遍历每一行
            int rowNum = sheet.getLastRowNum();
            System.out.println("总共有 " + rowNum + " 行");
            for (int j = 1; j < rowNum; j++) {
                // 一行数据对于一个Map
                LinkedHashMap rowMap = new LinkedHashMap();
                // 取得某一行
                Row row = sheet.getRow(j);
                int cellNum = row.getLastCellNum();
                // 遍历每一列
                for (int k = 0; k < cellNum; k++) {
                    Cell cell = row.getCell(k);
                    // 保存该单元格的数据到该行中
                    rowMap.put(headers.get(k),getCellValue(cell));
                }
                // 保存该行的数据到该表的List中
                list.add(rowMap);
            }
            // 将该sheet表的表名为key，List转为json后的字符串为Value进行存储
            excelMap.put(sheet.getSheetName(),JacksonUtil.bean2Json(list));
        }

        System.out.println("excel2json方法结束....");

        return excelMap;
    }
	
    /**
     * 给定指定的头，读取OFFICE Excel表格所有数据，不包含表第一行
     * @param file 文件
     * @param originFileName 文件名
     * @param keyMaps 给定的Map的key.<String,Integer>--<名字，对应的位置>
     * @return List<Map<String, String>>
     * @throws FileException
     * @throws IOException 
     */
    public static List<Map<String, String>> readExcel (InputStream inputStream, String originFileName, Map<String, Integer> keyMaps) throws Exception {
        if (StringUtil.isBlank(originFileName)) {
            throw new FileException("path is empty!", FileException.PARAMETER_INVALID_ERROR);
        } else {
            String suffix = getSuffix(originFileName);
            if (OFFICE_EXCEL_2003_SUFFIX.equals(suffix)) {
                return readXLS(inputStream, keyMaps);
            } else if (OFFICE_EXCEL_2010_SUFFIX.equals(suffix)) {
                return readXLSX(inputStream, keyMaps);
            } else {
                throw new FileException("it is not a excel file!", FileException.FILE_TYPE_MISMATCHED_ERROR);
            }
        }
    }
    
	/** 获取Excel中所有数据，包含表第一行
	 *  
	 * @param file
	 * @param originFileName 文件名
	 * @return List<List<Object>>
	 * @throws FileException
	 * @throws IOException
	 */
    public static List<List<Object>> readExcelWithHeader (InputStream inputStream, String originFileName) throws FileException, IOException {
        if (StringUtil.isBlank(originFileName)) {
            throw new FileException("path is empty!", FileException.PARAMETER_INVALID_ERROR);
        } else {
            String suffix = getSuffix(originFileName);
            if (OFFICE_EXCEL_2003_SUFFIX.equals(suffix)) {
                return readXLSWithHeader(inputStream);
            } else if (OFFICE_EXCEL_2010_SUFFIX.equals(suffix)) {
                return readXLSXWithHeader(inputStream);
            } else {
                throw new FileException("it is not a excel file!", FileException.FILE_TYPE_MISMATCHED_ERROR);
            }
        }
    }
    
    /**
     * 读取OFFICE Excel表格所有数据，不包含表第一行
     * @param file
     * @param originFileName Excel文件名
     * @return List<List<Object>>
     * @throws FileException
     * @throws IOException 
     */
    public static List<List<Object>> readExcel (InputStream inputStream, String originFileName) throws FileException, IOException {
        if (StringUtil.isBlank(originFileName)) {
            throw new FileException("path is empty!", FileException.PARAMETER_INVALID_ERROR);
        } else {
            String suffix = getSuffix(originFileName);
            if (OFFICE_EXCEL_2003_SUFFIX.equals(suffix)) {
                return readXLS(inputStream);
            } else if (OFFICE_EXCEL_2010_SUFFIX.equals(suffix)) {
                return readXLSX(inputStream);
            } else {
                throw new FileException("it is not a excel file!", FileException.FILE_TYPE_MISMATCHED_ERROR);
            }
        }
    }
    /*--------------------------Read Excel End------------------------------------------*/
    /*--------------------------Write Excel Start------------------------------------------*/
    public static void writeToXLS(ExcelRow heads, ExcelData data, String sheetName, ByteArrayOutputStream out) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet(sheetName);
        HSSFRow row = sheet1.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFCell cell;

        for (int i = 0; i < heads.size(); i++) {
            cell = row.createCell(i);
            cell.setCellValue(heads.get(i));
            cell.setCellStyle(style);
        }
        if (data != null && data.size() != 0) {
        	for (int n = 0; n < data.size(); n++) {
                row = sheet1.createRow(n + 1); 
                ExcelRow datarow = data.get(n);
                for (int m = 0; m < datarow.size(); m++) {
                    cell = row.createCell(m);
                    cell.setCellValue(datarow.get(m));
                    cell.setCellStyle(style);
                }
            }
        }
        wb.write(out);
}
    
    /**
     * 将给定的表头和数据写入xlsx结尾的Excel
     * @param heads 表头
     * @param data 数据内容
     * @param sheetName Excel栏
     * @param out 输出的位置
     * @return ByteArrayOutputStream
     * @throws IOException
     */
    public static void writeToXLSX(ExcelRow heads, ExcelData data, String sheetName, ByteArrayOutputStream out) throws IOException {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet1 = wb.createSheet(sheetName);
            XSSFRow row = sheet1.createRow(0);
            XSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            XSSFCell cell;
            for (int i = 0; i < heads.size(); i++) {
                cell = row.createCell(i);
                cell.setCellValue(heads.get(i));
                cell.setCellStyle(style);
            }

            if (data != null && data.size() != 0) {
            	for (int n = 0; n < data.size(); n++) {
                    row = sheet1.createRow(n + 1); 
                    ExcelRow datarow = data.get(n);
                    for (int m = 0; m < datarow.size(); m++) {
                        cell = row.createCell(m);
                        cell.setCellValue(datarow.get(m));
                        cell.setCellStyle(style);
                    }
                }
            }
            wb.write(out);
	}

    /*--------------------------Write Excel End------------------------------------------*/
    private static List<List<Object>> readXLSWithHeader (InputStream inputStream) throws IOException {
//        InputStream is = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = wb.getSheetAt(0);
        
        List<List<Object>> o = new ArrayList();
        List<Object> temp = null;
        if (sheet == null) {
            return null;
        }
        
        for (int row_index = 0; row_index <= sheet.getLastRowNum(); row_index++) {
            HSSFRow row = sheet.getRow(row_index);
            if (row == null) {
                continue ;
            }
            temp = new ArrayList();
            for (int col_index = 0; col_index <= row.getLastCellNum(); col_index++) {
                temp.add(getCellValue(row.getCell(col_index)));
            }
            o.add(temp);
        }
        
        return o;
    }
    
    
	
	private static final String trim(String str) {
		return str == null ? "" : str.trim();
	}
    
    private static List<List<Object>> readXLSXWithHeader (InputStream inputStream) throws IOException {
//        InputStream is = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = wb.getSheetAt(0);
        
        List<List<Object>> o = new ArrayList();
        List<Object> temp = null;
        if (sheet == null) {
            return null;
        }
        
        for (int row_index = 0; row_index <= sheet.getLastRowNum(); row_index++) {
            XSSFRow row = sheet.getRow(row_index);
            if (row == null) {
                continue ;
            }
            temp = new ArrayList();
            for (int col_index = 0; col_index <= row.getLastCellNum(); col_index++) {
                temp.add(getCellValue(row.getCell(col_index)));
            }
            o.add(temp);
        }
        
        return o;
    }
    
   
    
    
    private static List<List<Object>> readXLS (InputStream inputStream) throws IOException {
//        InputStream is = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = wb.getSheetAt(0);
        
        List<List<Object>> o = new ArrayList();
        List<Object> temp = null;
        if (sheet == null) {
            return null;
        }
        
        for (int row_index = 1; row_index <= sheet.getLastRowNum(); row_index++) {
            HSSFRow row = sheet.getRow(row_index);
            if (row == null) {
                continue ;
            }
            temp = new ArrayList();
            for (int col_index = 0; col_index <= row.getLastCellNum(); col_index++) {
                temp.add(getCellValue(row.getCell(col_index)));
            }
            o.add(temp);
        }
        
        return o;
    }
    
    private static List<List<Object>> readXLSX (InputStream inputStream) throws IOException {
//        InputStream is = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = wb.getSheetAt(0);
        
        List<List<Object>> o = new ArrayList();
        List<Object> temp = null;
        if (sheet == null) {
            return null;
        }
        
        for (int row_index = 1; row_index <= sheet.getLastRowNum(); row_index++) {
            XSSFRow row = sheet.getRow(row_index);
            if (row == null) {
                continue ;
            }
            temp = new ArrayList();
            for (int col_index = 0; col_index <= row.getLastCellNum(); col_index++) {
                temp.add(getCellValue(row.getCell(col_index)));
            }
            o.add(temp);
        }
        
        return o;
    }
    
    
    
    private static List<Map<String, String>> readXLS (InputStream inputStream, Map<String, Integer> keyMaps) throws IOException {
//        InputStream is = new FileInputStream(file);
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        
        List<Map<String, String>> list = new ArrayList();
        Map<String, String> temp = null;
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            HSSFSheet sheet = wb.getSheetAt(sheetIndex);
            if (sheet == null) {
                continue;
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                HSSFRow row = sheet.getRow(rowIndex);
                temp = new HashMap();
                if (row != null) {
                    for (Entry<String, Integer> entry : keyMaps.entrySet()) {
                        int index = entry.getValue();
                        HSSFCell cell = row.getCell(index);
                        temp.put(entry.getKey(), getCellValue(cell));
                    }
                }
                list.add(temp);
            }
        }
        return list;
    }
    
    private static List<Map<String, String>> readXLSX (InputStream inputStream, Map<String, Integer> keyMaps) throws IOException {
//        InputStream is = new FileInputStream(file);
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        
        List<Map<String, String>> list = new ArrayList();
        Map<String, String> temp = null;
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            XSSFSheet sheet = wb.getSheetAt(sheetIndex);
            if (sheet == null) {
                continue;
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                XSSFRow row = sheet.getRow(rowIndex);
                temp = new HashMap();
                if (row != null) {
                    for (Entry<String, Integer> entry : keyMaps.entrySet()) {
                        int index = entry.getValue();
                        XSSFCell cell = row.getCell(index);
                        temp.put(entry.getKey(), getCellValue(cell));
                    }
                }
                list.add(temp);
            }
        }
        return list;
    }
    /**
     * 	获取cell值
     * @param cell
     * @return
     */
    private static String getCellValue(HSSFCell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC: {
                DecimalFormat df = new DecimalFormat("0");  
                return df.format(cell.getNumericCellValue());
            }
            default: return String.valueOf(cell.getStringCellValue());
        }
    }
    
    /**
     * 处理掉了excel空格的问题
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {   
        String cellValue = "";   
        DecimalFormat df = new DecimalFormat("#");   
        if(cell==null||cell.equals("")||cell.getCellType() ==Cell.CELL_TYPE_BLANK) {
        System.out.println(cellValue);
        return cellValue;
        }
        switch (cell.getCellType()) {   
            case Cell.CELL_TYPE_STRING:   
                cellValue =cell.getRichStringCellValue().getString().trim();   
                break;   
            case Cell.CELL_TYPE_NUMERIC:  
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue().toString();
                } else {
                    cellValue = df.format(cell.getNumericCellValue()).toString();
                }  
                break;   
            case Cell.CELL_TYPE_BOOLEAN:   
                cellValue =String.valueOf(cell.getBooleanCellValue()).trim();   
                break;     
            default:   
                cellValue = "";   
        }   
        return cellValue;   
    } 
    
//    /**
//	 * 从excel文件中读取内容 以第一行为key，从第二行开始为正式数据
//	 * 
//	 * @param file
//	 * @return
//	 * @throws BiffException
//	 * @throws IOException
//	 */
//	public static final List<Map<String, String>> readSheet(File file) {
//		Workbook workbook = null;
//		List<Map<String, String>> result = null;
//		try {
//			workbook = Workbook.getWorkbook(file);
//			jxl.Sheet sheet = workbook.getSheet(0);
//			int rowSize = sheet.getRows();
//			Cell[] headCell = sheet.getRow(0);
//			result = new ArrayList<Map<String, String>>(rowSize - 1);
//			Cell[] cells;
//			for (int i = 1; i < rowSize; i++) {
//				cells = sheet.getRow(i);
//				Map<String, String> data = new HashMap<String, String>();
//				for (int j = 0; j < headCell.length; j++) {
//					if (cells.length > j) {
//						if (cells[j].getType() == CellType.DATE) { // TODO:时间类型读取
//							DateCell dc = (DateCell) cells[j];
//							Date date = dc.getDate();
//							TimeZone zone = TimeZone.getTimeZone("GMT");
//							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							sdf.setTimeZone(zone);
//							String dateStr = sdf.format(date);
//							data.put(trim(headCell[j].getContents()), dateStr);
//						} else if (cells[j].getType() == CellType.NUMBER) { // 数字类型读取
//							if (cells[j].getContents().indexOf(".") > 0) {
//								NumberCell nc = (NumberCell) cells[j];
//								double value = nc.getValue();
//								data.put(trim(headCell[j].getContents()), trim(String.valueOf(value)));
//							} else {
//								data.put(trim(headCell[j].getContents()), trim(cells[j].getContents()));
//							}
//						} else {
//							data.put(trim(headCell[j].getContents()), trim(cells[j].getContents()));
//						}
//
//					}
//				}
//				result.add(data);
//			}
//		} catch (BiffException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }  finally {
//			workbook.close();
//		}
//
//		return result;
//	}
	
public static class ExcelException extends FileTools.FileException {
        
        private int row_index = -1;
        
        private int col_index = -1;
        
        public ExcelException (int row, int col, String ex, int code) {
            super(ex, code);
            this.row_index = row;
            this.col_index = col;
        }
        
        public ExcelException (int row, int col, Throwable ex, int code) {
            super(ex, code);
            this.row_index = row;
            this.col_index = col;
        }
        
        public static final int CELL_DATA_INVALID_ERROR = 75200;
        
        public static final int CELL_DATA_FORMAT_ERROR = 75201;
        
        public static final int CELL_DATA_MISMATCH_ERROR = 75202;
        
    }


	public static ExcelRow excelHeaders(String... headers) {
		ExcelRow row = new ExcelRow();
		for(int i = 0; i < headers.length; ++i)
			row.add(headers[i]);
		return row;
	}
}
