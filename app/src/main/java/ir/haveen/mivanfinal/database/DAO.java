package ir.haveen.mivanfinal.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.databinding.ObservableList;

import java.util.List;

import ir.haveen.mivanfinal.model.db.DetailsItem;

@Dao
public interface DAO {


    @Query("select * from data where groupId = :groupId")
    public LiveData<List<DetailsItem>> getPlaces(int groupId); // return all pace by groupId

    @Query("select * from data where name = :name")
    public DetailsItem getPlace(String name); // get place by name

    @Update
    public void updatePlace(DetailsItem item); // update place

    @Insert
    public void insertAll(List<DetailsItem> items);
}
