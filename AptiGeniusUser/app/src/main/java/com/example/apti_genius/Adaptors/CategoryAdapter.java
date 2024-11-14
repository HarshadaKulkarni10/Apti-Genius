package com.example.apti_genius.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apti_genius.Models.CategoryModel;
import com.example.apti_genius.R;
import com.example.apti_genius.TestsActivity;
import com.example.apti_genius.databinding.ItemCategoryBinding;
import com.example.apti_genius.Models.CategoryModel;
import com.example.apti_genius.R;
import com.example.apti_genius.TestsActivity;
import com.example.apti_genius.databinding.ItemCategoryBinding;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder>{

    Context context;
    ArrayList<CategoryModel>list;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

      CategoryModel model = list.get(position);

      holder.binding.categorName.setText(model.getCategoryName());

      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent = new Intent(context, TestsActivity.class);
              intent.putExtra("category",model.getCategoryName());
              intent.putExtra("tests",model.getSetNum());
              intent.putExtra("key",model.getKey());

              context.startActivity(intent);

          }
      });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ItemCategoryBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemCategoryBinding.bind(itemView);

        }
    }


}
