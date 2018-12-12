package com.iqb.consumer.common.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * Description: 生成xlsx
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月15日    adam       1.0        1.0 Version 
 * </pre>
 */
public class XlsxUtil {

    private HSSFWorkbook hwb;
    private HSSFCellStyle timeFormat = null;
    private HSSFCellStyle coinFormat = null;
    private HSSFCellStyle centerFormat = null;

    public XlsxUtil(HSSFWorkbook hwb) {
        this.hwb = hwb;
        getTimeFormat();
        getCoinFormat();
        getCenterFormat();
    }

    public HSSFCellStyle getTimeFormat() {
        if (timeFormat == null) {
            timeFormat = hwb.createCellStyle();
            timeFormat.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        }
        return timeFormat;
    }

    public HSSFCellStyle getCoinFormat() {
        if (coinFormat == null) {
            coinFormat = hwb.createCellStyle();
            coinFormat.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        }
        return coinFormat;
    }

    public HSSFCellStyle getCenterFormat() {
        if (centerFormat == null) {
            centerFormat = hwb.createCellStyle();
            centerFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        }
        return centerFormat;
    }

    public HSSFRow append(HSSFRow hr, int index, Object value) {
        if (value != null) {
            HSSFCell hc = hr.createCell(index);
            hc.setCellStyle(getCenterFormat());
            if (value instanceof String) {
                hc.setCellValue((String) value);
            } else if (value instanceof Date) {
                hc.setCellValue((Date) value);
                hc.setCellStyle(getTimeFormat());
            } else if (value instanceof BigDecimal) {
                hc.setCellValue(((BigDecimal) value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                hc.setCellStyle(getCoinFormat());
            } else if (value instanceof Integer) {
                hc.setCellValue(((Integer) value));
            } else if (value instanceof Double) {
                hc.setCellValue(((Double) value));
            } else if (value instanceof Long) {
                hc.setCellValue(((Long) value));
            } else if (value instanceof Calendar) {
                hc.setCellValue((Calendar) value);
                hc.setCellStyle(getTimeFormat());
            } else {
                throw new RuntimeException("No type to convert xlsx.Please set up XlsxUtil.append method.");
            }
        }
        return hr;
    }
}
