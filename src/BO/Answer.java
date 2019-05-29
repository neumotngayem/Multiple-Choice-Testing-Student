/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BO;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class Answer implements Serializable{

    private String answerID;
    private String studentAnswer = null;

    public Answer(String answerID) {
        this.answerID = answerID;
    }

    public void setAnswerID(String anwserID) {
        this.answerID = anwserID;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getAnswerID() {
        return answerID;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

}
