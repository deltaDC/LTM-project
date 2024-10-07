package com.n19.ltmproject.server.service;

import com.n19.ltmproject.server.model.Player;
import com.n19.ltmproject.server.model.Reward;

import java.util.List;

public class RewardManager {

    private List<Reward> rewards;

    public RewardManager(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public List<Reward> getEligibleRewards(Player player) {
        return rewards.stream()
                .filter(reward -> reward.isEligible(player))
                .toList();
    }
}
