package com.example.apti_genius;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apti_genius.Adaptors.GrideAdapter;
import com.example.apti_genius.databinding.ActivityTestsBinding;
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

        adapter = new GrideAdapter(getIntent().getIntExtra("tests",0),getIntent().getStringExtra("category"));

        binding.gridView.setAdapter(adapter);

    }
}
