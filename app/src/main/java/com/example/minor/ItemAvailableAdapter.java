package com.example.minor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAvailableAdapter extends RecyclerView.Adapter<ItemAvailableAdapter.ViewHolder> {

    private static List<ItemAvailable> itemList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(ItemAvailable item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemModel;
        TextView itemCity;
        TextView itemDeadline;
        TextView itemCost;
        Button viewDetailsButton;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.pt_img);
            itemName = itemView.findViewById(R.id.pt_name);
            itemModel = itemView.findViewById(R.id.pt_model);
            itemCity = itemView.findViewById(R.id.pt_city);
            itemDeadline = itemView.findViewById(R.id.pt_deadline);
            itemCost = itemView.findViewById(R.id.pt_cost);
            viewDetailsButton = itemView.findViewById(R.id.htbutton);

            viewDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            ItemAvailable item = itemList.get(position);
                            listener.onItemClick(item);
                        }
                    }
                }
            });
        }
    }

    public ItemAvailableAdapter(List<ItemAvailable> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemAvailable item = itemList.get(position);
        holder.itemName.setText(item.getName());
        holder.itemCity.setText(item.getCity());
        holder.itemModel.setText(item.getModelYear());
        holder.itemDeadline.setText(item.getDeadline());
        holder.itemCost.setText(item.getRentCost());


        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

