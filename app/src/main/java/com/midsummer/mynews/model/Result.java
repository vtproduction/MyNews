
package com.midsummer.mynews.model;

import org.parceler.Generated;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Generated("org.jsonschema2pojo")
@Parcel(analyze = {Result.class})
public class Result{

    public String id;
    public String type;
    public String sectionId;
    public String webTitle;
    public String webPublicationDate;
    public Fields fields;
    public String webUrl;
    public String apiUrl;
    public String sectionName;



    /**
     * No args constructor for use in serialization
     * 
     */
    public Result() {
    }

    /**
     * 
     * @param id
     * @param webUrl
     * @param sectionId
     * @param apiUrl
     * @param sectionName
     * @param webTitle
     * @param type
     * @param webPublicationDate
     * @param fields
     */
    public Result(String id, String type, String sectionId, String webTitle, String webPublicationDate, Fields fields, String webUrl, String apiUrl, String sectionName) {
        this.id = id;
        this.type = type;
        this.sectionId = sectionId;
        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.fields = fields;
        this.webUrl = webUrl;
        this.apiUrl = apiUrl;
        this.sectionName = sectionName;


    }

    public Date getPostDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(this.webPublicationDate);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Calendar.getInstance().getTime();
        }
    }


}
