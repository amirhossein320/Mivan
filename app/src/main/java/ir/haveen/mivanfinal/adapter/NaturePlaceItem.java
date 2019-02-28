package ir.haveen.mivanfinal.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.haveen.mivanfinal.R;
import ir.haveen.mivanfinal.databinding.FlodingPlaceItemBinding;
import ir.haveen.mivanfinal.model.db.DetailsItem;

public class NaturePlaceItem extends RecyclerView.Adapter<NaturePlaceItem.ViewHolder> {

    private List<DetailsItem> items;
    private DetailsItem item;
    private Context context;
    private ItemClickListener itemClickListener;

    public NaturePlaceItem(List<DetailsItem> items, ItemClickListener itemClickListener) {
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FlodingPlaceItemBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.floding_place_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        item = items.get(position);
        holder.binding.title.setText(item.getName());
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
            itemView.getRoot().setOnClickListener(v ->
                    itemClickListener.onItemClick(itemView.getRoot(), getAdapterPosition())
            );
            itemView.btnPath.setOnClickListener(v ->
                    itemClickListener.natureDetailsClickListener(itemView.btnPath, getAdapterPosition())
            );
        }
    }
}
