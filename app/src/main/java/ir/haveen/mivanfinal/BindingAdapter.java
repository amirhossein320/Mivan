package ir.haveen.mivanfinal;

import android.support.v7.widget.RecyclerView;

import java.util.List;

public class BindingAdapter {

    @android.databinding.BindingAdapter(value = {"adapter", "layout_manager"})
    public static void setAdapter(RecyclerView recyclerView, List list, RecyclerView.LayoutManager layoutManager){
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(new NaturePlaceItem(list, 1));
    }
}
