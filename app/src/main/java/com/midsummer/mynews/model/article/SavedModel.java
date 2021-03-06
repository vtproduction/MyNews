package com.midsummer.mynews.model.article;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hugo.weaving.DebugLog;

/**
 * Created by nienb on 10/2/16.
 */
@Table(database = AppDatabase.class, name = "SavedArticle")
@Parcel(analyze = {SavedModel.class})
public class SavedModel extends BaseModel{

    @PrimaryKey
    public String id;
    @Column
    public String type;
    @Column
    public String sectionId;
    @Column
    public String webTitle;
    @Column
    public String webPublicationDate;
    @Column
    public String webUrl;
    @Column
    public String apiUrl;
    @Column
    public String sectionName;
    @Column
    public String main;
    @Column
    public String body;
    @Column
    public String trailText;
    @Column
    public String thumbnail;

    public SavedModel() {
    }

    /**
     * Constructor
     * @param id
     * @param type
     * @param sectionId
     * @param webTitle
     * @param webPublicationDate
     * @param fields
     * @param webUrl
     * @param apiUrl
     * @param sectionName
     */
    public SavedModel(String id, String type, String sectionId, String webTitle, String webPublicationDate, Fields fields, String webUrl, String apiUrl, String sectionName) {
        this.id = id;
        this.type = type;
        this.sectionId = sectionId;
        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
        this.apiUrl = apiUrl;
        this.sectionName = sectionName;
        this.main = extractImagelinkFromRawString(fields.main);
        this.body = fields.body;
        this.trailText = fields.trailText;
        this.thumbnail = fields.thumbnail;
    }

    /**
     *
     * @param raw
     * @return
     */
    public static String extractImagelinkFromRawString(String raw){
        try{
            Matcher m = Pattern.compile("(https?:\\/\\/.*\\.(?:png|jpg|jpeg))").matcher(raw);
            return m.find() ? m.group(1) : "";
        }catch (Exception e){
            return "";
        }
    }

    /**
     *
     * @param result
     * @return
     */
    @DebugLog
    public static SavedModel ViewModel2SavedModel(com.midsummer.mynews.model.article.Result result){
        return new SavedModel(
                result.id,
                result.type,
                result.sectionId,
                result.webTitle,
                result.webPublicationDate,
                result.fields,
                result.webUrl,
                result.apiUrl,
                result.sectionName
        );
    }

    /**
     *
     * @return
     */
    public com.midsummer.mynews.model.article.Result SaveModel2ViewModel(){
        return new Result(
                this.id,
                this.type,
                this.sectionId,
                this.webTitle,
                this.webPublicationDate,
                new Fields(this.main, this.body, this.trailText, this.thumbnail),
                this.webUrl,
                this.apiUrl,
                this.sectionName
        );
    }

    public static List<Result> getAll(){
        List<Result> results = new ArrayList<>();
        List<SavedModel> sm = SQLite.select().from(SavedModel.class).queryList();
        for (SavedModel s:
             sm) {
            results.add(s.SaveModel2ViewModel());
        }
        return results;
    }

    public static int getCount(){
        try{
            return (int) SQLite.selectCountOf().from(SavedModel.class).count();
        }catch (Exception e){
            return 0;
        }
    }

    public static boolean isItemInDB(String id){
        return (int)SQLite.selectCountOf()
                .from(SavedModel.class)
                .where(SavedModel_Table.id.eq(id)).count() == 1;
    }

    public int saveToDB(){
        if (SavedModel.isItemInDB(this.id)){
            return 1;
        }
        this.save();
        return 0;
    }

    public void deleteFromDB(){
        SQLite.delete(SavedModel.class).
                where(SavedModel_Table.id.eq(this.id)).query();
    }
}
