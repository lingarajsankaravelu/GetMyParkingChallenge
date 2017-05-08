package lingaraj.hourglass.in.getmyparkingchallenge.cloud.service.Response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lingaraj on 5/7/17.
 */

public class KickStarterResponse implements Serializable {
    @SerializedName("s.no")
    private int sNo;

    @SerializedName("amt.pledged")
    private int amtPledged;

    private String blurb;

    private String by;

    private String country;

    private String currency;

    @SerializedName("end.time")
    private String date;

    private String location;

    @SerializedName("percentage.funded")
    private int percentageFunded;

    @SerializedName("num.backers")
    private String backers;

    private String state;

    private String title;

    private String type;

    private String url;

    public int getsNo() {
        return sNo;
    }

    public void setsNo(int sNo) {
        this.sNo = sNo;
    }

    public int getAmtPledged() {
        return amtPledged;
    }

    public void setAmtPledged(int amtPledged) {
        this.amtPledged = amtPledged;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        location = location;
    }

    public int getPercentageFunded() {
        return percentageFunded;
    }

    public void setPercentageFunded(int percentageFunded) {
        this.percentageFunded = percentageFunded;
    }

    public String getBackers() {
        return backers;
    }

    public void setBackers(String backers) {
        this.backers = backers;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
