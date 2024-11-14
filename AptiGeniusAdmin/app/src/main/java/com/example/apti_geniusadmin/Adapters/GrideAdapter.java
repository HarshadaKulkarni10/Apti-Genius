package com.example.apti_geniusadmin.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apti_geniusadmin.QuestionActivity;
import com.example.apti_geniusadmin.R;

public class GrideAdapter extends BaseAdapter {

    public int sets = 0;
    private String category;
    private String key;

    private GridListener listener;

    public GrideAdapter(int sets, String category, String key, GridListener listener) {
        this.sets = sets;
        this.category = category;
        this.key = key;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return sets+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;

        if(view == null){
            view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tests,viewGroup,false);
        }else{

            view1 = view;
        }

        if(i==0){
            ((TextView)view1.findViewById(R.id.tvsetName)).setText("+");
        }
        else {
            ((TextView)view1.findViewById(R.id.tvsetName)).setText(String.valueOf(i));
        }

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(i ==  0){
                    listener.addSets();

                }
                else {

                    Intent intent = new Intent(viewGroup.getContext(), QuestionActivity.class);
                    intent.putExtra("setNum",1);
                    intent.putExtra("categoryName",category);
                    viewGroup.getContext().startActivity(intent);

                }


            }
        });

        return view1;
    }

    public interface GridListener{

        public void addSets();

    }
}
