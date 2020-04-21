package project.msc.college;

public class comment_model {

    public comment_model(String cmt_name, String cmt_class, String cmt_comment, String key) {
        this.cmt_name = cmt_name;
        this.cmt_class = cmt_class;
        this.cmt_comment = cmt_comment;
        this.key = key;
    }

    private String cmt_name;
    private String cmt_class;
    private String cmt_comment;
    private String key;

    public comment_model() {
    }

    public String getCmt_name() {
        return cmt_name;
    }

    public void setCmt_name(String cmt_name) {
        this.cmt_name = cmt_name;
    }

    public String getCmt_class() {
        return cmt_class;
    }

    public void setCmt_class(String cmt_class) {
        this.cmt_class = cmt_class;
    }

    public String getCmt_comment() {
        return cmt_comment;
    }

    public void setCmt_comment(String cmt_comment) {
        this.cmt_comment = cmt_comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
