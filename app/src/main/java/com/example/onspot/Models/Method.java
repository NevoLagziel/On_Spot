package com.example.onspot.Models;


import com.example.onspot.Enums.StainType;

import java.util.HashMap;
import java.util.Map;

public class Method {

    private String mId;
    private StainType stainType;

    String supplies = "";

    String recommended = "";

    String description = "";
    String userName = "";

    float rating = 0;

    int numberOfVotes = 0;

    boolean full = false;

    float ratingSum = 0;


    private Map<String, Boolean> saves = new HashMap<>();

    private Map<String, Float> stars = new HashMap<>();


    public Method() {
    }

    public StainType getStainType() {
        return stainType;
    }

    public Method setStainType(StainType stainType) {
        this.stainType = stainType;
        return this;
    }

    public String getSupplies() {
        return supplies;
    }

    public Method setSupplies(String supplies) {
        this.supplies = supplies;
        return this;
    }

    public String getRecommended() {
        return recommended;
    }

    public Method setRecommended(String recommended) {
        this.recommended = recommended;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Method setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Method setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public Method setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
        return this;
    }

    public boolean isFull() {
        return full;
    }

    public Method setFull(boolean full) {
        this.full = full;
        return this;
    }

    public void addVote(float vote){
        rating = (ratingSum + vote)/(numberOfVotes + 1);
        numberOfVotes++;
    }

    public String getDescription() {
        return description;
    }

    public Method setDescription(String description) {
        this.description = description;
        return this;
    }

    public float getRatingSum() {
        return ratingSum;
    }

    public Method setRatingSum(float ratingSum) {
        this.ratingSum = ratingSum;
        return this;
    }

    public Map<String, Boolean> getSaves() {
        return saves;
    }

    public Method setSaves(Map<String, Boolean> saves) {
        this.saves = saves;
        return this;
    }

    public Map<String, Float> getStars() {
        return stars;
    }

    public Method setStars(Map<String, Float> stars) {
        this.stars = stars;
        return this;
    }

    public String getmId() {
        return mId;
    }

    public Method setmId(String mId) {
        this.mId = mId;
        return this;
    }

    @Override
    public String toString() {
        return "Method{" +
                "mId='" + mId + '\'' +
                ", stainType=" + stainType +
                ", supplies='" + supplies + '\'' +
                ", recommended='" + recommended + '\'' +
                ", userName='" + userName + '\'' +
                ", rating=" + rating +
                ", numberOfVotes=" + numberOfVotes +
                ", full=" + full +
                ", ratingSum=" + ratingSum +
                ", description='" + description + '\'' +
                ", saves=" + saves +
                ", stars=" + stars +
                '}';
    }
}
