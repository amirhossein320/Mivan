package ir.haveen.mivanfinal.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ir.haveen.mivanfinal.R;
import ir.haveen.mivanfinal.databinding.ItemPlaceBinding;
import ir.haveen.mivanfinal.model.db.DetailsItem;

public class PlaceItem extends RecyclerView.Adapter<PlaceItem.ViewHolder> {

    private List<DetailsItem> items;
    private Context context;
    private ItemClickListener itemClickListener;
    private String resImage;

    public PlaceItem(List<DetailsItem> items, ItemClickListener itemClickListener, String resImage) {
        this.items = items;
        this.itemClickListener = itemClickListener;
        this.resImage = resImage;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemPlaceBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_place, parent, false);
        context = parent.getContext();
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DetailsItem item = items.get(position);
        holder.binding.placeTitle.setText(item.getName());
        holder.binding.imageView10.setImageResource(context.getResources()
                .getIdentifier(resImage, "mipmap", context.getPackageName()));
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
            itemView.getRoot().setOnClickListener(v -> {
                itemClickListener.onItemClick(itemView.getRoot(), getAdapterPosition());
            });
        }
    }
}
