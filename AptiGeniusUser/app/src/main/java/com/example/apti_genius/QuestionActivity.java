package com.example.apti_genius;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.apti_genius.Models.QuestionModel;
import com.example.apti_genius.databinding.ActivityQuestionBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    ActivityQuestionBinding binding;
    FirebaseDatabase database;
    private ArrayList<QuestionModel>list;
   // QuesionAdapter adapter;

    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer tvTimer;
    String categoryName;
    private int setNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseDatabase.getInstance();

        categoryName = getIntent().getStringExtra("categoryName");
        setNum = getIntent().getIntExtra("setNum",1);

        list = new ArrayList<>();

        resetTimer();
        tvTimer.start();

        database.getReference().child("Sets").child(categoryName).child("questions")
                .orderByChild("setNum").equalTo(setNum).addValueEventListener(new ValueEventListener() {

                   /* @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Clear the list to avoid duplicate questions when data changes
                        list.clear();

                        // Check if the snapshot contains data
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                                if (model != null) {
                                    list.add(model);
                                }
                            }

                            // Debug log to check if data was added successfully
                            Log.d("QuestionActivity", "Questions loaded: " + list.size());

                            if (!list.isEmpty()) {
                                // Set up click listeners for options if questions exist
                                for (int i = 0; i < 4; i++) {
                                    binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkAnsw((Button) view);
                                        }
                                    });
                                }

                                // Display the first question and options
                                playAnimation(binding.tvQuestion, 0, list.get(position).getTvQuestion());

                                // Set up the "Next" button to load the next question
                                binding.btnNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        binding.btnNext.setEnabled(false);
                                        binding.btnNext.setAlpha(0.3f);

                                        enableOption(true);
                                        position++;

                                        if (position == list.size()) {
                                            // Move to ScoreActivity when all questions are done
                                            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                                            intent.putExtra("score", score);
                                            intent.putExtra("totalQuestions", list.size());
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }

                                        count = 0;
                                        playAnimation(binding.tvQuestion, 0, list.get(position).getTvQuestion());
                                    }
                                });

                            } else {
                                // Show a message if no questions were retrieved
                                Toast.makeText(QuestionActivity.this, "No questions available in this category.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // Handle the case where the snapshot doesn't exist
                            Toast.makeText(QuestionActivity.this, "Questions not found for the selected category.", Toast.LENGTH_SHORT).show();
                        }
                    }*/

                    @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()){


                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                /*QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                                list.add(model);*/
                                Log.d("QuestionActivity", "SnapShot loaded: " + snapshot.getChildren().toString());
                                list.add(new QuestionModel(
                                        dataSnapshot.child("question").getValue().toString(),
                                        dataSnapshot.child("optionA").getValue().toString(),
                                        dataSnapshot.child("optionB").getValue().toString(),
                                        dataSnapshot.child("optionC").getValue().toString(),
                                        dataSnapshot.child("optionD").getValue().toString(),
                                        dataSnapshot.child("correctAnsw").getValue().toString()

                                ));

                            }
                            if(list.size() > 0){

                                for(int i = 0; i<4; i++){

                                    binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkAnsw((Button)view);
                                        }
                                    });
                                }

                                playAnimation(binding.question,0,list.get(position).getQuestion());

                                binding.btnNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        binding.btnNext.setEnabled(false);
                                        binding.btnNext.setAlpha(0.3f);

                                        enableOption(true);
                                        position++;

                                        if(position == list.size()){

                                            Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
                                            intent.putExtra("correctAnsw",score);
                                            intent.putExtra("totalQuestions",list.size());
                                            startActivity(intent);
                                            finish();

                                            return;

                                        }
                                        count= 0;
                                        playAnimation(binding.question,0,list.get(position).getQuestion());

                                    }
                                });

                            }
                            else {
                                Toast.makeText(QuestionActivity.this,"Questions Not Exists",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(QuestionActivity.this,"Questions Not Exists",Toast.LENGTH_SHORT).show();

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(QuestionActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();




                    }
                });


    }

    private void resetTimer() {
        tvTimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                binding.tvTimer.setText(String.valueOf(l/1000));


            }

            @Override
            public void onFinish() {

                Dialog dialog = new Dialog(QuestionActivity.this);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.timeout_dialog);
                dialog.findViewById(R.id.btnTryAgain).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(QuestionActivity.this, TestsActivity.class);
                        startActivity(intent);
                        finish();


                    }
                });

                dialog.show();

            }
        };

    }

    private void enableOption(boolean enable) {

        for(int i = 0; i<4;i++){

            binding.optionContainer.getChildAt(i).setEnabled(enable);

            if(enable){

                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);


            }
        }

    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

                if(value == 0 && count < 4){

                    String option = "";
                    if(count == 0)
                    {
                        option = list.get(position).getOptionA();
                    }
                    else if(count == 1){
                        option = list.get(position).getOptionB();
                    }
                    else if (count == 2)
                    {
                        option = list.get(position).getOptionC();
                    }
                    else if(count == 3){
                        option = list.get(position).getOptionD();
                    }
                    playAnimation(binding.optionContainer.getChildAt(count),0,option);
                    count++;
                }

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                if(value == 0)
                {
                    try {
                        ((TextView)view).setText(data);
                        ((TextView)view).setText(data);
                        binding.tvTotalQuesions.setText(position+1+"/"+list.size());
                    }catch (Exception e){

                        ((Button)view).setText(data);
                    }

                    view.setTag(data);
                    playAnimation(view,1,data);
                }
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
    }

    private void checkAnsw(Button selectedOption) {


        enableOption(false);

        binding.btnNext.setEnabled(true);
        binding.btnNext.setAlpha(1);

        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnsw())){

            score++;
            selectedOption.setBackgroundResource(R.drawable.right_answ);

        }
        else {
            selectedOption.setBackgroundResource(R.drawable.wrong_answ);
            Button correctOpt = (Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectAnsw());
            correctOpt.setBackgroundResource(R.drawable.right_answ);
        }



    }
}