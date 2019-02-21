package ir.haveen.mivanfinal.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ir.haveen.mivanfinal.R;
import ir.haveen.mivanfinal.databinding.FlodingPlaceItemBinding;
import ir.haveen.mivanfinal.databinding.ItemPlaceBinding;
import ir.haveen.mivanfinal.model.db.DetailsItem;
import ir.haveen.mivanfinal.model.view.PlaceViewModel;

public class PlaceItem extends RecyclerView.Adapter<PlaceItem.ViewHolder> {

    private List<DetailsItem> items;

    public PlaceItem(List<DetailsItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemPlaceBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.item_place, parent, false);
            return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.placeTitle.setText(items.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemPlaceBinding binding;
        ViewHolder(ItemPlaceBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
