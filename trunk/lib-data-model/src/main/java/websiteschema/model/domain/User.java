/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package websiteschema.model.domain;

/**
 *
 * @author ray
 */
public class User implements java.io.Serializable {

    long userId;
    String name;
    String passwd;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
