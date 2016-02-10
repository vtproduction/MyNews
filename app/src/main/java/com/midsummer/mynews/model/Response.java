
package com.midsummer.mynews.model;

import org.parceler.Generated;

import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Response {

    public String status;
    public String userTier;
    public int total;
    public int startIndex;
    public int pageSize;
    public int currentPage;
    public int pages;
    public String orderBy;
    public List<Result> results = new ArrayList<Result>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Response() {
    }

    /**
     * 
     * @param total
     * @param startIndex
     * @param results
     * @param orderBy
     * @param status
     * @param pages
     * @param pageSize
     * @param currentPage
     * @param userTier
     */
    public Response(String status, String userTier, int total, int startIndex, int pageSize, int currentPage, int pages, String orderBy, List<Result> results) {
        this.status = status;
        this.userTier = userTier;
        this.total = total;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.pages = pages;
        this.orderBy = orderBy;
        this.results = results;
    }

}
