package ir.haveen.mivanfinal.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ir.haveen.mivanfinal.R;
import ir.haveen.mivanfinal.databinding.FlodingPlaceItemBinding;
import ir.haveen.mivanfinal.model.db.DetailsItem;

public class FlodingPlaceItem extends RecyclerView.Adapter<FlodingPlaceItem.ViewHolder> {

    private List<DetailsItem> items;
    private FlodingPlaceItemBinding view;
    private DetailsItem item;
    private LatLng myLocation;
    private GoogleMap mMap;
    private Context context;

    public FlodingPlaceItem(List<DetailsItem> items) {
        this.items = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlodingPlaceItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.floding_place_item, parent, false);
        context = binding.getRoot().getContext();
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        item = items.get(position);
        holder.binding.title.setText(item.getName());
        holder.binding.card.setOnClickListener(v -> {
            if(myLocation != null){
            }else {
                Toast.makeText(context, "مکان شما در دسترس نیست", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void Onclick(GoogleMap mMap, LatLng myLocation) {
        this.mMap = mMap;
        this.myLocation = myLocation;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private FlodingPlaceItemBinding binding;

        ViewHolder(FlodingPlaceItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
