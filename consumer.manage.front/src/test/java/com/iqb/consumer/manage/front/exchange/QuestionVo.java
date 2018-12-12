package com.iqb.consumer.manage.front.exchange;

import java.util.List;

/**
 * 风险适当性测试问题 Created by ckq.
 */
public class QuestionVo {

    private String questionNumber;

    private String question;

    private List<ItemListVo> itemList;

    private Integer questionKind;

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

    public List<ItemListVo> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemListVo> itemList) {
        this.itemList = itemList;
    }

    public Integer getQuestionKind() {
        return questionKind;
    }

    public void setQuestionKind(Integer questionKind) {
        this.questionKind = questionKind;
    }
}
