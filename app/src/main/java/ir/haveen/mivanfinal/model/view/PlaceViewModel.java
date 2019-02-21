package ir.haveen.mivanfinal.model.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ir.haveen.mivanfinal.database.DAO;
import ir.haveen.mivanfinal.database.Database;
import ir.haveen.mivanfinal.model.db.DetailsItem;

public class PlaceViewModel extends AndroidViewModel {

    private DAO dao;
    private ExecutorService service;

    public PlaceViewModel(@NonNull Application application) {
        super(application);
        dao = Database.getINSTANCE(application).dao();
        service = Executors.newSingleThreadExecutor();
    }

    //get places by groupId
    public LiveData<List<DetailsItem>> getPlaces(int id) {
        return dao.getPlaces(id);
    }
}
