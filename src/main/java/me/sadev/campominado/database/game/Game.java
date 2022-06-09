package me.sadev.campominado.database.game;

import org.bukkit.entity.Player;

import java.util.List;

public interface Game {

    void start();
    void stop();

    void removePlayer(Player player);
    void addPlayer(Player player);
    void setWinner(Player player);

    List<Player> getPlayers();
}
