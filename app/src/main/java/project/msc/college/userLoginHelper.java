package project.msc.college;

public class userLoginHelper {
    String name,roll,phone,email,password,stream,role,pic;
// just for practice
    public userLoginHelper() {
    }
    public userLoginHelper(String name, String roll, String phone, String email, String password, String stream, String role, String pic) {
        this.name = name;
        this.roll = roll;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.stream = stream;
        this.role = role;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
