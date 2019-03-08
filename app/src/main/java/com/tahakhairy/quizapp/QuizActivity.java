package com.tahakhairy.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 30500;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";

    private TextView questionTextView;
    private TextView scoreTextView;
    private TextView questionCountTextView;
    private TextView countdownTextView;
    private TextView difficultyTextView;
    private TextView categoryTextView;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private Button confirmNextButton;

    private ArrayList<Question> questionList;

    private ColorStateList defaultColorTextRb;
    private ColorStateList defaultColorTextCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private int questionCounter;
    private int totalQuestions;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.question_text_view);
        scoreTextView = findViewById(R.id.score_text_view);
        questionCountTextView = findViewById(R.id.question_count_text_view);
        countdownTextView = findViewById(R.id.countdown_text_view);
        difficultyTextView = findViewById(R.id.difficulty_text_view);
        categoryTextView = findViewById(R.id.category_text_view);
        radioGroup = findViewById(R.id.radio_group);
        radioButton1 = findViewById(R.id.radio_button);
        radioButton2 = findViewById(R.id.radio_button2);
        radioButton3 = findViewById(R.id.radio_button3);
        confirmNextButton = findViewById(R.id.confirm_next_button);

        defaultColorTextRb = radioButton1.getTextColors();
        defaultColorTextCd = countdownTextView.getTextColors();

        Intent intent = getIntent();
        int categoryId = intent.getIntExtra(StartingScreenActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(StartingScreenActivity.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(StartingScreenActivity.EXTRA_DIFFICULTY);

        categoryTextView.setText("Category: " + categoryName);
        difficultyTextView.setText("Difficulty: " + difficulty);

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryId, difficulty);

            totalQuestions = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            if (questionList == null) {
                finish();
            }
            totalQuestions = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        confirmNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (radioButton1.isChecked() || radioButton2.isChecked() || radioButton3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        radioButton1.setTextColor(defaultColorTextRb);
        radioButton2.setTextColor(defaultColorTextRb);
        radioButton3.setTextColor(defaultColorTextRb);

        radioGroup.clearCheck();

        if (questionCounter < totalQuestions) {
            currentQuestion = questionList.get(questionCounter);
            questionTextView.setText(currentQuestion.getQuestion());
            radioButton1.setText(currentQuestion.getOption1());
            radioButton2.setText(currentQuestion.getOption2());
            radioButton3.setText(currentQuestion.getOption3());

            questionCounter++;
            questionCountTextView.setText("Question: " + questionCounter + "/" + totalQuestions);
            answered = false;
            confirmNextButton.setText(R.string.confirm_button);

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();

        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countdownTextView.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            countdownTextView.setTextColor(Color.RED);
        } else {
            countdownTextView.setTextColor(defaultColorTextCd);
        }
    }

    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();

        RadioButton selectedRadioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNum = radioGroup.indexOfChild(selectedRadioButton) + 1;

        if (answerNum == currentQuestion.getAnswerNum()) {
            score++;
            scoreTextView.setText("Score: " + score);
        }

        showSolution();
    }


    private void showSolution() {
        radioButton1.setTextColor(Color.RED);
        radioButton2.setTextColor(Color.RED);
        radioButton3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNum()) {
            case 1:
                radioButton1.setTextColor(Color.GREEN);
                questionTextView.setText(R.string.first_answer);
                break;
            case 2:
                radioButton2.setTextColor(Color.GREEN);
                questionTextView.setText(R.string.second_answer);
                break;
            case 3:
                radioButton3.setTextColor(Color.GREEN);
                questionTextView.setText(R.string.third_answer);
                break;
        }

        if (questionCounter < totalQuestions) {
            confirmNextButton.setText(R.string.next_button);
        } else {
            confirmNextButton.setText(R.string.finish_button);
        }
    }


    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();

        }

        backPressedTime = System.currentTimeMillis();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}
