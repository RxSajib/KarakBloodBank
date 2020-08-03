package AllPogoClass;

public class donar_holder {

    String UID, date, donar_bloodgroup, donar_number, donar_profile_imageURL;
    String donar_post, donar_name, donar_location, login_name;

    public donar_holder(){

    }

    public donar_holder(String UID, String date, String donar_bloodgroup, String donar_number, String donar_profile_imageURL, String donar_post, String donar_name, String donar_location, String login_name) {
        this.UID = UID;
        this.date = date;
        this.donar_bloodgroup = donar_bloodgroup;
        this.donar_number = donar_number;
        this.donar_profile_imageURL = donar_profile_imageURL;
        this.donar_post = donar_post;
        this.donar_name = donar_name;
        this.donar_location = donar_location;
        this.login_name = login_name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDonar_bloodgroup() {
        return donar_bloodgroup;
    }

    public void setDonar_bloodgroup(String donar_bloodgroup) {
        this.donar_bloodgroup = donar_bloodgroup;
    }

    public String getDonar_number() {
        return donar_number;
    }

    public void setDonar_number(String donar_number) {
        this.donar_number = donar_number;
    }

    public String getDonar_profile_imageURL() {
        return donar_profile_imageURL;
    }

    public void setDonar_profile_imageURL(String donar_profile_imageURL) {
        this.donar_profile_imageURL = donar_profile_imageURL;
    }

    public String getDonar_post() {
        return donar_post;
    }

    public void setDonar_post(String donar_post) {
        this.donar_post = donar_post;
    }

    public String getDonar_name() {
        return donar_name;
    }

    public void setDonar_name(String donar_name) {
        this.donar_name = donar_name;
    }

    public String getDonar_location() {
        return donar_location;
    }

    public void setDonar_location(String donar_location) {
        this.donar_location = donar_location;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

}
