
package com.midsummer.mynews.model;

import org.parceler.Generated;
import org.parceler.Parcel;

@Generated("org.jsonschema2pojo")
@Parcel(analyze = {Fields.class})
public class Fields {

    public String main;
    public String body;
    public String trailText;
    public String thumbnail;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Fields() {
    }

    /**
     * 
     * @param body
     * @param trailText
     * @param main
     * @param thumbnail
     */
    public Fields(String main, String body, String trailText, String thumbnail) {
        this.main = main;
        this.body = body;
        this.trailText = trailText;
        this.thumbnail = thumbnail;
    }

}
