package ir.haveen.mivanfinal.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

import ir.haveen.mivanfinal.R;
import ir.haveen.mivanfinal.databinding.FlodingPlaceItemBinding;
import ir.haveen.mivanfinal.model.db.DetailsItem;

public class FlodingPlaceItem extends RecyclerView.Adapter<FlodingPlaceItem.ViewHolder> {

    private List<DetailsItem> items;

    public FlodingPlaceItem(List<DetailsItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlodingPlaceItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.floding_place_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoldingCell cell = holder.binding.foldingCell;

        //fold item
        holder.binding.fold.setOnClickListener((v -> cell.fold(false)));
        //unfold item
        holder.binding.unfold.setOnClickListener((v -> cell.unfold(false)));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private FlodingPlaceItemBinding binding;

        ViewHolder(FlodingPlaceItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
