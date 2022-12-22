package com.final_mad.datingapp.datingapp.Utils;

import java.io.Serializable;


public class User implements Serializable {
    private String user_id;
    private String phone_number;
    private String email;
    private String username;
    private boolean sports;
    private boolean travel;
    private boolean music;
    private boolean fishing;
    private String description;
    private String sex;
    private String preferSex;
    private String dateOfBirth;
    private double latitude;
    private double longitude;
    private String password;
    private String token;
    private String profileImage;
    private double distance;
    private boolean isAvailable;
    private boolean notShowAge;
    private boolean notShowDistance;
    private int maxDistance;
    private int minAge;
    private int maxAge;

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public boolean isNotShowAge() {
        return notShowAge;
    }

    public void setNotShowAge(boolean notShowAge) {
        this.notShowAge = notShowAge;
    }

    public boolean isNotShowDistance() {
        return notShowDistance;
    }

    public void setNotShowDistance(boolean notShowDistance) {
        this.notShowDistance = notShowDistance;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(String sex, String preferSex, String user_id, String phone_number, String email, String username, boolean sport, boolean travel, boolean music, boolean fish, String description, String dateOfBirth, String profileImageUrl, double latitude, double longtitude) {
        this.sex = sex;
        this.user_id = user_id;
        this.phone_number = phone_number;
        this.email = email;
        this.username = username;
        this.sports = sport;
        this.travel = travel;
        this.music = music;
        this.fishing = fish;
        this.description = description;
        this.preferSex = preferSex;
        this.dateOfBirth = dateOfBirth;
        this.latitude = latitude;
        this.longitude = longtitude;

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSports() {
        return sports;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public boolean isTravel() {
        return travel;
    }

    public void setTravel(boolean travel) {
        this.travel = travel;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isFishing() {
        return fishing;
    }

    public void setFishing(boolean fishing) {
        this.fishing = fishing;
    }

    public String getPreferSex() {
        return preferSex;
    }

    public void setPreferSex(String preferSex) {
        this.preferSex = preferSex;
    }

    // Added new attribute called date of birth.
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", sports=" + sports +
                ", travel=" + travel +
                ", music=" + music +
                ", fishing=" + fishing +
                ", description='" + description + '\'' +
                ", sex='" + sex + '\'' +
                ", preferSex='" + preferSex + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    }
}
