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
public class Record {
    
    private String loginId;
    private String fromDate;
    private String toDate;
    private String Task;
    private String TaskDescription;

    public Record(String loginId, String fromDate, String toDate, String Task, String TaskDescription) {
        this.loginId = loginId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.Task = Task;
        this.TaskDescription = TaskDescription;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTask() {
        return Task;
    }

    public void setTask(String Task) {
        this.Task = Task;
    }

    public String getTaskDescription() {
        return TaskDescription;
    }

    public void setTaskDescription(String TaskDescription) {
        this.TaskDescription = TaskDescription;
    }

    
}
