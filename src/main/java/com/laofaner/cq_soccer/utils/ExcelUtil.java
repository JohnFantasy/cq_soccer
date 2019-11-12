package com.laofaner.cq_soccer.utils;

/**
 *
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Excel操作工具
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ExcelUtil
{
    /**
     * 日志记录
     */
    private static final Log logger = LogFactory.getLog(ExcelUtil.class);

    /**
     * 读取excel <功能详细描述>
     *
     * @param filePath 文件路径
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static HSSFWorkbook readXls(String filePath)
    {
        InputStream inputStream = null;
        HSSFWorkbook workBook = null;

        try
        {
            inputStream = new FileInputStream(filePath);

            if (null != inputStream)
            {
                workBook = new HSSFWorkbook(inputStream);
            }
        }
        catch (FileNotFoundException e)
        {
            logger.error("readXls fail!", e);
        }
        catch (IOException e)
        {
            logger.error("readXls fail!", e);
        }
        finally
        {

            CloseUtils.close(inputStream);
        }

        return workBook;
    }

    /**
     * 写Excel <功能详细描述>
     *
     * @param workBook excel文档对象
     * @param path 存放路径
     * @see [类、类#方法、类#成员]
     */
    public static void writeXls(HSSFWorkbook workBook, String path)
    {
        FileOutputStream outputStream = null;

        try
        {
            // 修改模板内容导出新模板
            outputStream = new FileOutputStream(path);
            workBook.write(outputStream);
        }
        catch (FileNotFoundException e)
        {
            logger.error("writeXls fail!", e);
        }
        catch (IOException e)
        {
            logger.error("writeXls fail!", e);
        }
        finally
        {
            CloseUtils.close(outputStream);
        }
    }

    /**
     * 获取输入流
     * <功能详细描述>
     * @param workBook
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static InputStream getInputStream(HSSFWorkbook workBook)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;

        try
        {
            workBook.write(byteArrayOutputStream);
            byte[] content = byteArrayOutputStream.toByteArray();
            inputStream = new ByteArrayInputStream(content);
        }
        catch (IOException e)
        {
            logger.error("Write excel fail!" +e.getStackTrace());
        }
        finally
        {
            CloseUtils.close(inputStream);
            CloseUtils.close(byteArrayOutputStream);
        }

        return inputStream;
    }

    /**
     * 获取字节数组输出流
     * <功能详细描述>
     * @param workBook
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static ByteArrayOutputStream getByteArrayOutputStream(HSSFWorkbook workBook)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try
        {
            workBook.write(byteArrayOutputStream);
        }
        catch (IOException e)
        {
            logger.error("Write excel fail!" + e.getStackTrace());
        }
        finally
        {
            CloseUtils.close(byteArrayOutputStream);
        }

        return byteArrayOutputStream;
    }

    /**
     * 获取sheet <功能详细描述>
     *
     * @param workBook Excel工作薄
     * @param sheetName Sheet名称
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static HSSFSheet getSheet(HSSFWorkbook workBook, String sheetName)
    {
        HSSFSheet sheet = workBook.getSheet(sheetName);

        if (null == sheet)
        {
            sheet = workBook.createSheet(sheetName);
        }

        return sheet;
    }

    /**
     * 获取行
     * <功能详细描述>
     * @param rowIndex 行号
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static HSSFRow getRow(HSSFSheet sheet, int rowIndex)
    {
        HSSFRow row = sheet.getRow(rowIndex);

        if (row == null)
        {
            row = sheet.createRow(rowIndex);
        }

        return row;
    }

    /**
     * 获取单元格
     * <功能详细描述>
     * @param row 行对象
     * @param colIndex 列编号
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static HSSFCell getCell(HSSFRow row, int colIndex)
    {
        HSSFCell cell = row.getCell(colIndex);

        if (cell == null)
        {
            cell = row.createCell(colIndex);
        }

        return cell;
    }

    /**
     * 获取单元格
     * <功能详细描述>
     * @param sheet sheet页对象
     * @param rowIndex 行编号
     * @param colIndex 列编号
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static HSSFCell getCell(HSSFSheet sheet, int rowIndex, int colIndex)
    {
        HSSFRow row = getRow(sheet, rowIndex);
        return getCell(row, colIndex);
    }

    /**
     * 填充单元格值 <功能详细描述>
     *
     * @param sheet Sheet名称
     * @param rowIndex 行坐标
     * @param colIndex 列坐标
     * @param value 填充值
     * @param cellType 单元格参数类型
     * @see [类、类#方法、类#成员]
     */
    public static void setSheetCellValue(HSSFSheet sheet, int rowIndex, int colIndex, String value, int cellType)
    {
        HSSFCell cell = getCell(sheet, rowIndex, colIndex);
        setCellValue(cell, value, cellType);
    }

    /**
     * 填充单元格值 <功能详细描述>
     *
     * @param sheet Sheet名称
     * @param rowIndex 行坐标
     * @param colIndex 列坐标
     * @param value 填充值
     * @see [类、类#方法、类#成员]
     */
    public static void setSheetCellValue(HSSFSheet sheet, int rowIndex, int colIndex, int value)
    {
        HSSFCell cell = getCell(sheet, rowIndex, colIndex);
        setCellValue(cell, value);
    }

    /**
     * 填充单元格值 <功能详细描述>
     *
     * @param sheet Sheet名称
     * @param rowIndex 行坐标
     * @param colIndex 列坐标
     * @param value 填充值
     * @see [类、类#方法、类#成员]
     */
    public static void setSheetCellValue(HSSFSheet sheet, int rowIndex, int colIndex, long value)
    {
        HSSFCell cell = getCell(sheet, rowIndex, colIndex);
        setCellValue(cell, value);
    }

    /**
     * 填充单元格值 <功能详细描述>
     *
     * @param sheet Sheet名称
     * @param rowIndex 行坐标
     * @param colIndex 列坐标
     * @param value 填充值
     * @see [类、类#方法、类#成员]
     */
    public static void setSheetCellValue(HSSFSheet sheet, int rowIndex, int colIndex, String value)
    {
        HSSFCell cell = getCell(sheet, rowIndex, colIndex);
        setCellValue(cell, value);
    }

    /**
     * 填充单元格值 <功能详细描述>
     *
     * @param cell 单元格
     * @param value 填充值
     * @param cellType 单元格类型
     * @see [类、类#方法、类#成员]
     */
    public static void setCellValue(HSSFCell cell, String value, int cellType)
    {
        cell.setCellType(cellType);

        switch (cell.getCellType())
        {
            case Cell.CELL_TYPE_STRING:
            {
                cell.setCellValue(value);
                break;
            }
            case Cell.CELL_TYPE_NUMERIC:
            {
                cell.setCellValue(Integer.parseInt(value));
                break;
            }
            case Cell.CELL_TYPE_FORMULA:
            {
                cell.setCellFormula(value);
                break;
            }
            default:
            {
                cell.setCellValue(value);
                break;
            }
        }
    }

    /**
     * 填充单元格值 <功能详细描述>
     *
     * @param cell 单元格
     * @param value 填充值
     * @see [类、类#方法、类#成员]
     */
    public static void setCellValue(HSSFCell cell, int value)
    {
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
    }

    /**
     * 填充单元格值 <功能详细描述>
     *
     * @param cell 单元格
     * @param value 填充值
     * @see [类、类#方法、类#成员]
     */
    public static void setCellValue(HSSFCell cell, long value)
    {
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
    }

    /**
     * 填充单元格值 <功能详细描述>
     *
     * @param cell 单元格
     * @param value 填充值
     * @see [类、类#方法、类#成员]
     */
    public static void setCellValue(HSSFCell cell, String value)
    {
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
    }

    public static String getSheetCellValue(HSSFSheet sheet, int rowIndex, int colIndex)
    {
        HSSFCell cell = getCell(sheet, rowIndex, colIndex);
        return getCellStringValue(cell);
    }

    /**
     * 读取单元格值
     * <功能详细描述>
     * @param cell 单元格
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getCellStringValue(HSSFCell cell)
    {
        String cellValue = "";

        switch (cell.getCellType())
        {
            // 字符串类型
            case HSSFCell.CELL_TYPE_STRING:
            {
                cellValue = cell.getStringCellValue();
                break;
            }
            // 数值类型
            case HSSFCell.CELL_TYPE_NUMERIC:
            {
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            }
            // 公式
            case HSSFCell.CELL_TYPE_FORMULA:
            {
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            }
            case HSSFCell.CELL_TYPE_BLANK:
            {
                break;
            }
            case HSSFCell.CELL_TYPE_BOOLEAN:
            {
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            }
            case HSSFCell.CELL_TYPE_ERROR:
            {
                break;
            }
            default:
            {
                break;
            }
        }

        return cellValue;
    }

    /**
     * 合并单元格
     * <功能详细描述>
     * @param sheet sheet页
     * @param rowFrom 开始行
     * @param rowTo 结束行
     * @param columnFrom 开始列
     * @param columnTo 结束列
     * @see [类、类#方法、类#成员]
     */
    public static void mergeCell(HSSFSheet sheet, int rowFrom, int rowTo, int columnFrom, int columnTo)
    {
        CellRangeAddress crAddress = new CellRangeAddress(rowFrom, rowTo, columnFrom, columnTo);
        sheet.addMergedRegion(crAddress);
    }

    /**
     * 设置单元格格式
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public static void setCellStyle(HSSFWorkbook workBook, HSSFCell cell, short styleType)
    {
        HSSFCellStyle cellStyle = workBook.createCellStyle();
        cellStyle.setAlignment(styleType);
        cell.setCellStyle(cellStyle);
    }

    /**
     * 计算出单元格中公式的值
     * <功能详细描述>
     * @param workBook 工作部
     * @param cell 单元格
     * @see [类、类#方法、类#成员]
     */
    public static double calculateFormulaValue(HSSFWorkbook workBook, HSSFCell cell)
    {
        double retValue = 0.0;

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA)
        {
            HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workBook);
            CellValue cellValue = evaluator.evaluate(cell);
            retValue = cellValue.getNumberValue();
        }

        return retValue;
    }

    /**
     * 重设公式单元格值
     * <功能详细描述>

     * @see [类、类#方法、类#成员]
     */
    public static void resetFormulaCell(HSSFSheet sheet, int rowIndex, int colIndex)
    {
        HSSFCell hssfCell = getCell(sheet, rowIndex, colIndex);

        if (HSSFCell.CELL_TYPE_FORMULA == hssfCell.getCellType())
        {
            //取得公式单元格的公式,重新设置
            hssfCell.setCellFormula(hssfCell.getCellFormula());
        }
    }

    /**
     * 重设公式单元格值
     * <功能详细描述>

     * @see [类、类#方法、类#成员]
     */
    public static void resetFormulaCell(HSSFCell hssfCell)
    {
        if (HSSFCell.CELL_TYPE_FORMULA == hssfCell.getCellType())
        {
            //取得公式单元格的公式,重新设置
            hssfCell.setCellFormula(hssfCell.getCellFormula());
        }
    }

    /**
     * 字母列名转换为矩阵列坐标
     * <pre>
     * A -> [0]
     * B -> [1]
     * C -> [2]
     *  ......
     * IV -> [255]
     * </pre>
     * @param letterName 字母坐标
     * @return
     * @see [类、类#方法、类#成员]
     */
    /*public static int convertToColumnIndex(String letterName)
    {
        int column = -1;
        letterName = StringUtils.upperCase(letterName);

        for (int i = 0; i < letterName.length(); ++i)
        {
            int c = letterName.charAt(i);
            column = (column + 1) * 26 + c - 'A';
        }

        return column;
    }*/

    /**
     * 列座标转换为字母列名
     * <pre>
     * [0] -> A
     * [1] -> B
     * [2] -> C
     *  ......
     * [255] -> IV
     * </pre>
     * @param columnIndex 矩阵坐标
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String convertToLetterName(int columnIndex)
    {
        columnIndex+= 1;
        String strResult = "";
        int intRound = columnIndex / 26;
        int intMod = columnIndex % 26;

        if (intRound != 0)
        {
            strResult = String.valueOf(((char)(intRound + 64)));
        }

        strResult += String.valueOf(((char)(intMod + 64)));

        return strResult;
    }


    /**
     * 矩阵坐标转换为单元格编号
     * <pre>
     * [0][0] -> A1
     * [0][1] -> A2
     * ......
     * [255][65535] -> IV65536
     * </pre>
     * @param columnIndex 列坐标
     * @param rowIndex 行坐标
     * @return String
     * @see [类、类#方法、类#成员]
     */
    public static String convertToCellId(int columnIndex, int rowIndex)
    {
        return convertToLetterName(columnIndex) + (rowIndex + 1);
    }

    /**
     * 删除sheet
     * <功能详细描述>
     * @param workBook
     * @param sheetName
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static void delSheet(HSSFWorkbook workBook, String sheetName)
    {
        int sheetIndex = workBook.getSheetIndex(sheetName);
        workBook.removeSheetAt(sheetIndex);
    }



    /**
     * 将excel写入内存
     * <功能详细描述>
     * @param fileName
     * @param bytes
     * @param response
     * @see [类、类#方法、类#成员]
     */
    public static void writeExcel (String fileName, byte[] bytes, HttpServletResponse response)
    {
        try
        {
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e)
        {

        }
    }

}
