/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activitytrackerBL;

/**
 *
 * @author home
 */
public class ActivityClass {

    private String loginId;
    private String fromDate;
    private String toDate;
    private String task;
    private String taskDescription;
    private String action;


    public String getLoginId() {
        return loginId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public String getTask() {
        return task;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getAction() {
        return action;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public void setAction(String action) {
        this.action = action;
    }
    

}
