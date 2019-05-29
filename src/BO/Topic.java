/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BO;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author Admin
 */
public class Topic implements Serializable{
    private Vector<Question> questList;
    private int topicTime;
    private String topicCode;

    public Topic(Vector<Question> questList, int timeFinish, String questCode) {
        this.questList = questList;
        this.topicTime = timeFinish;
        this.topicCode = questCode;
    }

    public Vector<Question> getQuestList() {
        return questList;
    }

    public int getTopicTime() {
        return topicTime;
    }

    public String getTopicCode() {
        return topicCode;
    }


    public void setQuestList(Vector<Question> questList) {
        this.questList = questList;
    }

    public void setTopicTime(int topicTime) {
        this.topicTime = topicTime;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }
    
}
