package com.example.minor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyItemsAdapter extends RecyclerView.Adapter<MyItemsAdapter.MyItemsViewHolder> {

    private List<MyItem> myItemsList;
    private OnItemClickListener listener;

    public MyItemsAdapter(List<MyItem> myItemsList) {
        this.myItemsList = myItemsList;
    }

    public interface OnItemClickListener {
        void onItemClick(MyItem myItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class MyItemsViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage;
        public TextView itemName;
        public TextView itemDeadline;

        public MyItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.pt_img);
            itemName = itemView.findViewById(R.id.pt_item_name);
            itemDeadline = itemView.findViewById(R.id.pt_rent_item_deadline);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            MyItem clickedItem = myItemsList.get(position);
                            listener.onItemClick(clickedItem);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_item, parent, false);
        return new MyItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemsViewHolder holder, int position) {
        MyItem currentItem = myItemsList.get(position);

        holder.itemName.setText(currentItem.getName());
        holder.itemDeadline.setText(currentItem.getDeadline());

        Glide.with(holder.itemView.getContext()).load(currentItem.getImageUrl()).into(holder.itemImage);

    }

    @Override
    public int getItemCount() {
        return myItemsList.size();
    }
}
