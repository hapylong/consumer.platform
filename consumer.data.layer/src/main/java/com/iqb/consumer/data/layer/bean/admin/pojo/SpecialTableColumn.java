package com.iqb.consumer.data.layer.bean.admin.pojo;

public class SpecialTableColumn {
    private final String table;
    private final String column;
    private final String knownColumn;
    private final String knownValue;

    private String codeA;
    private String codeB;

    public SpecialTableColumn(String table, String column, String knownColumn, String knownValue) {
        this.table = table;
        this.column = column;
        this.knownColumn = knownColumn;
        this.knownValue = knownValue;
    }

    public String getCodeA() {
        return codeA;
    }

    public void setCodeA(String codeA) {
        this.codeA = codeA;
    }

    public String getCodeB() {
        return codeB;
    }

    public void setCodeB(String codeB) {
        this.codeB = codeB;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    public String getKnownColumn() {
        return knownColumn;
    }

    public String getKnownValue() {
        return knownValue;
    }

}
