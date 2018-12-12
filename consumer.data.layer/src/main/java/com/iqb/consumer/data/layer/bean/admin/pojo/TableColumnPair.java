package com.iqb.consumer.data.layer.bean.admin.pojo;

public class TableColumnPair {
    private final String table;
    private final String column;

    private String codeA;
    private String codeB;

    public TableColumnPair(String table, String column) {
        this.table = table;
        this.column = column;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
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

}
