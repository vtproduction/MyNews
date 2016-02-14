
package com.midsummer.mynews.model.topic;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Result {

    private String id;
    private String webTitle;
    private List<Edition> editions = new ArrayList<Edition>();
    private String webUrl;
    private String apiUrl;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The webTitle
     */
    public String getWebTitle() {
        return webTitle;
    }

    /**
     * 
     * @param webTitle
     *     The webTitle
     */
    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    /**
     * 
     * @return
     *     The editions
     */
    public List<Edition> getEditions() {
        return editions;
    }

    /**
     * 
     * @param editions
     *     The editions
     */
    public void setEditions(List<Edition> editions) {
        this.editions = editions;
    }

    /**
     * 
     * @return
     *     The webUrl
     */
    public String getWebUrl() {
        return webUrl;
    }

    /**
     * 
     * @param webUrl
     *     The webUrl
     */
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    /**
     * 
     * @return
     *     The apiUrl
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * 
     * @param apiUrl
     *     The apiUrl
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }



}
