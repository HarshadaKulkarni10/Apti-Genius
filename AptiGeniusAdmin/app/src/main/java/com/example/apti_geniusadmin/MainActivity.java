package com.example.apti_geniusadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.apti_geniusadmin.Adapters.CategoryAdapter;
import com.example.apti_geniusadmin.Models.CategoryModel;
import com.example.apti_geniusadmin.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    EditText inputCategoryName;
    Button btnUploadCategory;
    Dialog dialog;
    ProgressDialog progressDialog;

    ArrayList<CategoryModel>list;
    CategoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        // Initialize Dialog for adding a new category
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_category_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        // Initialize ProgressDialog for showing progress when uploading
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait");

        // Find UI components within the dialog layout
        btnUploadCategory = dialog.findViewById(R.id.btnUploadCategory);
        inputCategoryName = dialog.findViewById(R.id.inputCategoryName);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        binding.recyCategory.setLayoutManager(layoutManager);

        adapter = new CategoryAdapter(this,list);
        binding.recyCategory.setAdapter(adapter);


        database.getReference().child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        /*CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                        list.add(model);*/
                        list.add(new CategoryModel(
                                dataSnapshot.child("categoryName").getValue().toString(),
                                dataSnapshot.getKey(),
                                Integer.parseInt(dataSnapshot.child("setNum").getValue().toString())


                        ));

                    }

                }else {
                    Toast.makeText(MainActivity.this,"Category not Exist",Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        // Show dialog when the add category button is clicked
        binding.addCategory.setOnClickListener(v -> dialog.show());

        // Handle category upload button click
        btnUploadCategory.setOnClickListener(v -> {
            String name = inputCategoryName.getText().toString();

            // Validate category name input
            if (name.isEmpty()) {
                inputCategoryName.setError("Enter Category Name");
            } else {
                progressDialog.show();
                uploadData(name);
            }
        });

        // Adjust padding for system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void uploadData(String categoryName) {
        // Create a new CategoryModel object with the category name
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setCategoryName(categoryName);
        categoryModel.setSetNum(0);

        // Generate a unique key for the category
        String key = database.getReference().child("categories").push().getKey();
        if (key != null) {
            // Save the category data to the Realtime Database
            database.getReference().child("categories").child(key).setValue(categoryModel)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MainActivity.this, "Category uploaded successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        dialog.dismiss();
                        resetFields();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this, "Failed to upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    });
        } else {
            Toast.makeText(MainActivity.this, "Failed to generate unique key.", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    // Method to reset input fields after successful upload
    private void resetFields() {
        inputCategoryName.setText("");
    }
}
