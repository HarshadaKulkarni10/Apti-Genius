package com.example.apti_geniusadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apti_geniusadmin.Models.QuestionModel;
import com.example.apti_geniusadmin.databinding.ActivityAddQuestionBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.database.FirebaseDatabase;

public class AddQuestionActivity extends AppCompatActivity {

    ActivityAddQuestionBinding binding;
    int set;
    String categoryName;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        set = getIntent().getIntExtra("setNum",-1);
        categoryName = getIntent().getStringExtra("category");

        database = FirebaseDatabase.getInstance();

        if(set == -1){
            finish();
            return;
        }
        binding.btnUploadQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int correct = -1;

                for(int i = 0; i< binding.optionContainer.getChildCount();i++){

                    EditText answer = (EditText) binding.answerContainer.getChildAt(i);

                    if(answer.getText().toString().isEmpty()){
                        answer.setError("Required");
                        return;
                    }

                    RadioButton radioButton = (RadioButton) binding.optionContainer.getChildAt(i);

                    if (radioButton.isChecked()){

                        correct = i;
                        break;

                    }


                }

                if(correct == -1){
                    Toast.makeText(AddQuestionActivity.this,"Please mark the correct option",Toast.LENGTH_SHORT).show();
                    return;

                }

                QuestionModel model = new QuestionModel();
                model.setQuestion(binding.tvinputQuestion.getText().toString());
                model.setOptionA(((EditText)binding.answerContainer.getChildAt(0)).getText().toString());
                model.setOptionB(((EditText)binding.answerContainer.getChildAt(1)).getText().toString());
                model.setOptionC(((EditText)binding.answerContainer.getChildAt(2)).getText().toString());
                model.setOptionD(((EditText)binding.answerContainer.getChildAt(3)).getText().toString());
                model.setCorrectAnsw(((EditText)binding.answerContainer.getChildAt(correct)).getText().toString());

                model.setSetNum(set);

                database.getReference().child("Sets").child(categoryName).child("questions")
                        .push()
                        .setValue(model)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(AddQuestionActivity.this,"Question added successfully!",Toast.LENGTH_SHORT).show();

                            }
                        });


            }
        });

    }
}