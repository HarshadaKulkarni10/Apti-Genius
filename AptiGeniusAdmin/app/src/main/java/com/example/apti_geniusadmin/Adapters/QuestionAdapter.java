package com.example.apti_geniusadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apti_geniusadmin.Models.QuestionModel;
import com.example.apti_geniusadmin.R;
import com.example.apti_geniusadmin.databinding.ItemQuestionBinding;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.viewHolder>{

    Context context;
    ArrayList<QuestionModel>list;

    public QuestionAdapter(Context context, ArrayList<QuestionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_question,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        QuestionModel model = list.get(position);

        holder.binding.tvQuestion.setText(model.getQuestion());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ItemQuestionBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemQuestionBinding.bind(itemView);


        }
    }
}
