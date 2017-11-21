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
public class UsersClass {
    
    private String loginId;
    private String password;
    private String role;
    private String reviewOfficer;

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getReviewOfficer() {
        return reviewOfficer;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setReviewOfficer(String reviewOfficer) {
        this.reviewOfficer = reviewOfficer;
    }
    
    
    
}
