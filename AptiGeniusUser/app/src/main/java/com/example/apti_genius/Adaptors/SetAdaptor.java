package com.example.apti_genius.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apti_genius.Models.SetModel;
import com.example.apti_genius.QuestionActivity;
import com.example.apti_genius.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import com.example.apti_genius.databinding.ItemTestsBinding;


public class SetAdaptor extends RecyclerView.Adapter<SetAdaptor.viewHolder>{

    Context context;
    ArrayList<SetModel> list;

    public SetAdaptor(Context context, ArrayList<SetModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_tests,parent,false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        final SetModel model = list.get(position);

        //holder.binding.testName.setText(model.getTestName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("test", model.getTestName());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {

        ItemTestsBinding binding;

        public viewHolder(@NotNull View itemView){
            super(itemView);

            binding = ItemTestsBinding.bind(itemView);
        }
    }

}
