package com.example.apti_geniusadmin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apti_geniusadmin.Adapters.GrideAdapter;
import com.example.apti_geniusadmin.databinding.ActivityTestsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class TestsActivity extends AppCompatActivity {

    ActivityTestsBinding binding;
    FirebaseDatabase database;

    GrideAdapter adapter;

    int a = 1;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityTestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = FirebaseDatabase.getInstance();
        key = getIntent().getStringExtra("key");

        adapter = new GrideAdapter((getIntent().getIntExtra("tests",0)),
                getIntent().getStringExtra("category"),key,new GrideAdapter.GridListener(){

            @Override
            public void addSets() {

                database.getReference().child("categories").child(key).child("setNum").setValue(getIntent().getIntExtra("tests",0)+a++).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            adapter.sets++;
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(TestsActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        binding.gridView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}