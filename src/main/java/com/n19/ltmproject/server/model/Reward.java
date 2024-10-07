package com.n19.ltmproject.server.model;

public class Reward {

    private int id;
    private String name;
    private String description;
    private int pointsThreshold;
    private int winsThreshold;

    public Reward() {}

    public Reward(String name, String description, int pointsThreshold, int winsThreshold) {
        this.name = name;
        this.description = description;
        this.pointsThreshold = pointsThreshold;
        this.winsThreshold = winsThreshold;
    }

    public boolean isEligible(Player player) {
        return player.getTotalPoints() >= pointsThreshold || player.getWins() >= winsThreshold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPointsThreshold() {
        return pointsThreshold;
    }

    public void setPointsThreshold(int pointsThreshold) {
        this.pointsThreshold = pointsThreshold;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", pointsThreshold=" + pointsThreshold +
                '}';
    }
}
