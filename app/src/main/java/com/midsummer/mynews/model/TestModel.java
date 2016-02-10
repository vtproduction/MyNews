package com.midsummer.mynews.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

/**
 * Created by nienb on 10/2/16.
 */
@Table(database = AppDatabase.class)
@Parcel(analyze = {TestModel.class})
public class TestModel extends BaseModel {
    @Column
    @PrimaryKey
    String id;

    @Column
    String name;

    public TestModel() {
    }

    public TestModel(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
