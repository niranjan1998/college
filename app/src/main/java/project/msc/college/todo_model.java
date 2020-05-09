package project.msc.college;

public class todo_model {

    String title;
    String desc;
    String date;
    String po_date;
    String id;
    String tech_name;
    String grp_class;
    String status;


    todo_model(String title, String desc, String date, String po_date, String id, String tech_name, String grp_class, String status) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.po_date = po_date;
        this.id = id;
        this.tech_name = tech_name;
        this.grp_class = grp_class;
        this.status = status;
    }

    public todo_model() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getPo_date() {
        return po_date;
    }

    public void setPo_date(String po_date) {
        this.po_date = po_date;
    }

    public String getTech_name() {
        return tech_name;
    }

    public void setTech_name(String tech_name) {
        this.tech_name = tech_name;
    }

    public String getGrp_class() {
        return grp_class;
    }

    public void setGrp_class(String grp_class) {
        this.grp_class = grp_class;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
