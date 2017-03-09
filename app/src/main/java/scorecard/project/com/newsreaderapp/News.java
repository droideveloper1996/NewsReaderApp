package scorecard.project.com.newsreaderapp;

/**
 * Created by Abhishek on 06/03/2017.
 */
public class News {

    private String mTitle;
    private String mType;
    private String mSection;
    private String mDate;
    private String mwebUrl;

    public News(String title, String section, String type, String date,String url) {
        mTitle = title;
        mDate = date;
        mSection = section;
        mType = type;
        mwebUrl=url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getType() {
        return mType;
    }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mDate;
    }
    public  String getMwebUrl(){return  mwebUrl;}
}
