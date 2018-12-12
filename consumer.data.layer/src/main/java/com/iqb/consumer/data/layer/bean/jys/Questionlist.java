/**
 * Copyright 2017 bejson.com
 */
package com.iqb.consumer.data.layer.bean.jys;

import java.util.List;

/**
 * Auto-generated: 2017-05-27 10:46:49
 * 
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Questionlist {

    private String questionNumber;
    private String question;
    private List<Itemlist> itemList;
    private String questionKind;

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Itemlist> getItemList() {
        return itemList;
    }

    public void setItemList(List<Itemlist> itemList) {
        this.itemList = itemList;
    }

    public String getQuestionKind() {
        return questionKind;
    }

    public void setQuestionKind(String questionKind) {
        this.questionKind = questionKind;
    }

}
