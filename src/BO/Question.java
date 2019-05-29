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
public class Question implements Serializable {

    private String questID;
    private String questQuest;
    private String questPath;
    private String questAnswer1;
    private String questAnswer2;
    private String questAnswer3;
    private String questAnswer4;
    private String questKey;

    public Question(String questID, String questQuest, String questPath, String questAnswer1, String questAnswer2, String questAnswer3, String questAnswer4, String questKey) {
        this.questID = questID;
        this.questQuest = questQuest;
        this.questPath = questPath;
        this.questAnswer1 = questAnswer1;
        this.questAnswer2 = questAnswer2;
        this.questAnswer3 = questAnswer3;
        this.questAnswer4 = questAnswer4;
        this.questKey = questKey;
    }

    public String getQuestID() {
        return questID;
    }

    public String getQuestQuest() {
        return questQuest;
    }

    public String getQuestPath() {
        return questPath;
    }

    public String getQuestAnswer1() {
        return questAnswer1;
    }

    public String getQuestAnswer2() {
        return questAnswer2;
    }

    public String getQuestAnswer3() {
        return questAnswer3;
    }

    public String getQuestAnswer4() {
        return questAnswer4;
    }

    public String getQuestKey() {
        return questKey;
    }

    public void setQuestQuest(String questQuest) {
        this.questQuest = questQuest;
    }

    public void setQuestPath(String questPath) {
        this.questPath = questPath;
    }

    public void setQuestAnswer1(String questAnswer1) {
        this.questAnswer1 = questAnswer1;
    }

    public void setQuestAnswer2(String questAnswer2) {
        this.questAnswer2 = questAnswer2;
    }

    public void setQuestAnswer3(String questAnswer3) {
        this.questAnswer3 = questAnswer3;
    }

    public void setQuestAnswer4(String questAnswer4) {
        this.questAnswer4 = questAnswer4;
    }

    public void setQuestKey(String questKey) {
        this.questKey = questKey;
    }
   
}
