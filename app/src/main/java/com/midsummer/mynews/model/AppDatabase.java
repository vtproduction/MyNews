package com.midsummer.mynews.model;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by nienb on 9/2/16.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";

    public static final int VERSION = 1;
}
