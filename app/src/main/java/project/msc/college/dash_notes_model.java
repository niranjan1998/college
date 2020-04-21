package project.msc.college;

public class dash_notes_model {
    public String name;
    public String url;
    private String key;
    private String Stream;
    private String extras;
    private String sem;

    dash_notes_model(String name, String url, String key, String stream, String extras, String sem) {
        this.name = name;
        this.url = url;
        this.key = key;
        this.Stream = stream;
        this.extras = extras;
        this.sem = sem;
    }

    // Default constructor required for calls to
    public dash_notes_model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStream() {
        return Stream;
    }

    public void setStream(String stream) {
        Stream = stream;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }
}