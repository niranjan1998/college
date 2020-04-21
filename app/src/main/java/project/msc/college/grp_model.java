package project.msc.college;

public class grp_model {

    public static  int send_text = 0;
    public static  int send_image = 1;
    public static  int rec_text = 3;
    public static  int rec_image = 2;


    private String user_name;
    private String user_msg;

    private String user_msg_type;
    private String user_key;

    public grp_model(String user_name, String user_msg, String user_msg_type, String user_key) {
        this.user_name = user_name;
        this.user_msg = user_msg;
        this.user_msg_type = user_msg_type;
        this.user_key = user_key;
    }

    public grp_model() {
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_msg() {
        return user_msg;
    }

    public void setUser_msg(String user_msg) {
        this.user_msg = user_msg;
    }

    public String getUser_msg_type() {
        return user_msg_type;
    }

    public void setUser_msg_type(String user_msg_type) {
        this.user_msg_type = user_msg_type;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }
}
