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
public class StudentWork implements Serializable {

    private String studentName;
    private Vector<Answer> answerList;
    private Topic topic;
    private int timeFinish;
    private double mark;

    public StudentWork(String studentName, Vector<Answer> answerList, int timeFinish, double mark, Topic topic) {
        this.studentName = studentName;
        this.answerList = answerList;
        this.timeFinish = timeFinish;
        this.mark = mark;
        this.topic = topic;
    }

    public Topic getTopic() {
        return topic;
    }

    public String getStudentName() {
        return studentName;
    }

    public Vector<Answer> getAnswerList() {
        return answerList;
    }

    public int getTimeFinish() {
        return timeFinish;
    }

    public double getMark() {
        return mark;
    }

}
