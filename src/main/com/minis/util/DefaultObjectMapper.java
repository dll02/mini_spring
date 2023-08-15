package com.minis.util;

import com.minis.util.ObjectMapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DefaultObjectMapper implements ObjectMapper {
    String dateFormat = "yyyy-MM-dd";
    DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(dateFormat);

    String decimalFormat = "#,##0.00";
    DecimalFormat decimalFormatter = new DecimalFormat(decimalFormat);

    public DefaultObjectMapper() {
    }

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.datetimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public void setDecimalFormat(String decimalFormat) {
        this.decimalFormat = decimalFormat;
        this.decimalFormatter = new DecimalFormat(decimalFormat);
    }
    public String writeValuesAsString(Object obj) {
        String sJsonStr = "{";
        Class<?> clz = obj.getClass();

        Field[] fields = clz.getDeclaredFields();
        //对返回对象中的每一个属性进行格式转换
        for (Field field : fields) {
            String sField = "";
            Object value = null;
            Class<?> type = null;
            String name = field.getName();
            String strValue = "";
            field.setAccessible(true);
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            type = field.getType();

            //针对不同的数据类型进行格式转换
            if (value instanceof Date) {
                LocalDate localDate = ((Date)value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                strValue = localDate.format(this.datetimeFormatter);
            }
            else if (value instanceof BigDecimal || value instanceof Double || value instanceof Float){
                strValue = this.decimalFormatter.format(value);
            }
            else {
                strValue = value.toString();
            }

            //拼接Json串
            if (sJsonStr.equals("{")) {
                sField = "\"" + name + "\":\"" + strValue + "\"";
            }
            else {
                sField = ",\"" + name + "\":\"" + strValue + "\"";
            }

            sJsonStr += sField;
        }
        sJsonStr += "}";
        return sJsonStr;
    }
}