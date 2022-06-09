package me.sadev.campominado.database.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Arena implements Game {

    final private Configuration config;

    // Arena inteira de jogo
    final private Location corner1;
    final private Location corner2;

    // Area que sai os bloco
    final private Location area1;
    final private Location area2;

    // Local de spawm
    final private Location spawn;

    final int delay;

    int round = 0;

    public Arena(Configuration config) {
        this.config = config;

        this.corner1 = new Location(Bukkit.getWorld(config.getString("arena.corner1.world")), config.getDouble("arena.corner1.x"), config.getDouble("arena.corner1.y"), config.getDouble("arena.corner1.z"));
        this.corner2 = new Location(Bukkit.getWorld(config.getString("arena.corner2.world")), config.getDouble("arena.corner2.x"), config.getDouble("arena.corner2.y"), config.getDouble("arena.corner2.z"));
        this.area1 = new Location(Bukkit.getWorld(config.getString("arena.corner1.world")), config.getDouble("arena.corner1.x"), config.getDouble("arena.corner1.y"), config.getDouble("arena.corner1.z"));
        this.area2 = new Location(Bukkit.getWorld(config.getString("arena.corner2.world")), config.getDouble("arena.corner2.x"), config.getDouble("arena.corner2.y"), config.getDouble("arena.corner2.z"));

        this.spawn = new Location(Bukkit.getWorld(config.getString("arena.spawn.world")), config.getDouble("arena.spawn.x"), config.getDouble("arena.spawn.y"), config.getDouble("arena.spawn.z"));
        this.delay = config.getInt("arena.delay");

        getBlocks(area1, area2).forEach(block -> block.setType(Material.GLASS));
    }

    public Location corner1() {
        return corner1;
    }

    public Location corner2() {
        return corner2;
    }

    public Location area1() {
        return area1;
    }

    public Location area2() {
        return area2;
    }

    public int round() {
        return round;
    }

    public Arena setRound(int round) {
        this.round = round;
        return this;
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void removePlayer(Player player) {

    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public void setWinner(Player player) {

    }

    @Override
    public List<Player> getPlayers() {
        return null;
    }

    private List<Block> getBlocks(Location pos1, Location pos2) {
        if(pos1.getWorld() != pos2.getWorld()) return null;
        World world = pos1.getWorld();
        List<Block> blocks = new ArrayList<>();
        int x1 = pos1.getBlockX();
        int y1 = pos1.getBlockY();
        int z1 = pos1.getBlockZ();

        int x2 = pos2.getBlockX();
        int y2 = pos2.getBlockY();
        int z2 = pos2.getBlockZ();

        int lowestX = Math.min(x1, x2);
        int lowestY = Math.min(y1, y2);
        int lowestZ = Math.min(z1, z2);

        int highestX = lowestX == x1 ? x2 : x1;
        int highestY = lowestX == y1 ? y2 : y1;
        int highestZ = lowestX == z1 ? z2 : z1;

        for(int x = lowestX; x <= highestX; x++)
            for(int y = lowestY; x <= highestY; y++)
                for(int z = lowestZ; x <= highestZ; z++)
                    blocks.add(world.getBlockAt(x, y, z));
        return blocks;
    }
}
