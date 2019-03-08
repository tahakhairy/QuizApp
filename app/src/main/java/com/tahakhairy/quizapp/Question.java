package com.tahakhairy.quizapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

    private int mId;
    private String mQuestion;
    private String mOption1;
    private String mOption2;
    private String mOption3;
    private int mAnswerNum;
    private String mDifficulty;
    private int mCategoryId;

    public Question() {

    }

    public Question(String mQuestion, String mOption1, String mOption2, String mOption3,
                    int mAnswerNum, String mDifficulty, int mCategoryId) {
        this.mQuestion = mQuestion;
        this.mOption1 = mOption1;
        this.mOption2 = mOption2;
        this.mOption3 = mOption3;
        this.mAnswerNum = mAnswerNum;
        this.mDifficulty = mDifficulty;
        this.mCategoryId = mCategoryId;
    }

    protected Question(Parcel in) {
        mId = in.readInt();
        mQuestion = in.readString();
        mOption1 = in.readString();
        mOption2 = in.readString();
        mOption3 = in.readString();
        mAnswerNum = in.readInt();
        mDifficulty = in.readString();
        mCategoryId = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mQuestion);
        dest.writeString(mOption1);
        dest.writeString(mOption2);
        dest.writeString(mOption3);
        dest.writeInt(mAnswerNum);
        dest.writeString(mDifficulty);
        dest.writeInt(mCategoryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getOption1() {
        return mOption1;
    }

    public void setOption1(String mOption1) {
        this.mOption1 = mOption1;
    }

    public String getOption2() {
        return mOption2;
    }

    public void setOption2(String mOption2) {
        this.mOption2 = mOption2;
    }

    public String getOption3() {
        return mOption3;
    }

    public void setOption3(String mOption3) {
        this.mOption3 = mOption3;
    }

    public int getAnswerNum() {
        return mAnswerNum;
    }

    public void setAnswerNum(int mAnswerNum) {
        this.mAnswerNum = mAnswerNum;
    }

    public String getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(String mDifficulty) {
        this.mDifficulty = mDifficulty;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public static String[] getAllDifficultyLevels() {
        return new String[]{
                DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD
        };
    }
}
