package ir.haveen.mivanfinal;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import ir.haveen.mivanfinal.adapter.FlodingPlaceItem;

public class BindingAdapter {

    @android.databinding.BindingAdapter(value = {"adapter", "layout_manager"})
    public static void setAdapter(RecyclerView recyclerView, List list, RecyclerView.LayoutManager layoutManager){
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new FlodingPlaceItem(list));
    }
}
