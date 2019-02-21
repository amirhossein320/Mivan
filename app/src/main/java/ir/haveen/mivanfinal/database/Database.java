package ir.haveen.mivanfinal.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import ir.haveen.mivanfinal.model.db.DetailsItem;
import ir.haveen.mivanfinal.sqlAsset.AssetSQLiteOpenHelperFactory;

@android.arch.persistence.room.Database(entities = {DetailsItem.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    public abstract DAO dao();

    private static Database INSTANCE;

    public static Database getINSTANCE(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.
                    databaseBuilder(context.getApplicationContext(),
                            Database.class, "data.db")
                    .openHelperFactory(new AssetSQLiteOpenHelperFactory())
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
