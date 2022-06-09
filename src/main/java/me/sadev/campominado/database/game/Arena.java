package me.sadev.campominado.database.game;

import me.sadev.campominado.CampoMinado;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class Arena implements Game {

    final private Configuration config;
    final private CampoMinado plugin;

    final private List<Player> players = new ArrayList<>();

    // Arena inteira de jogo
    final private Location corner1;
    final private Location corner2;

    // Murinho la
    final private Location Muro1_1;
    final private Location Muro1_2;

    // Murinho la
    final private Location Muro2_1;
    final private Location Muro2_2;

    // Area que sai os bloco
    final private Location area1;
    final private Location area2;

    // Local de spawm
    final private Location spawn;

    final int delay;

    private Player winner = null;

    int round = 0;

    public Arena(Configuration config, CampoMinado plugin) {
        this.config = config;
        this.plugin = plugin;

        // Pegar as locations da config
        this.corner1 = new Location(Bukkit.getWorld(config.getString("arena.corner1.world")), config.getDouble("arena.corner1.x"), config.getDouble("arena.corner1.y"), config.getDouble("arena.corner1.z"));
        this.corner2 = new Location(Bukkit.getWorld(config.getString("arena.corner2.world")), config.getDouble("arena.corner2.x"), config.getDouble("arena.corner2.y"), config.getDouble("arena.corner2.z"));
        this.area1 = new Location(Bukkit.getWorld(config.getString("arena.corner1.world")), config.getDouble("arena.corner1.x"), config.getDouble("arena.corner1.y"), config.getDouble("arena.corner1.z"));
        this.area2 = new Location(Bukkit.getWorld(config.getString("arena.corner2.world")), config.getDouble("arena.corner2.x"), config.getDouble("arena.corner2.y"), config.getDouble("arena.corner2.z"));
        this.Muro1_1 = new Location(Bukkit.getWorld(config.getString("arena.Muro1_1.world")), config.getDouble("arena.Muro1_1.x"), config.getDouble("arena.Muro1_1.y"), config.getDouble("arena.Muro1_1.z"));
        this.Muro1_2 = new Location(Bukkit.getWorld(config.getString("arena.Muro1_2.world")), config.getDouble("arena.Muro1_2.x"), config.getDouble("arena.Muro1_2.y"), config.getDouble("arena.Muro1_2.z"));
        this.Muro2_1 = new Location(Bukkit.getWorld(config.getString("arena.Muro2_1.world")), config.getDouble("arena.Muro2_1.x"), config.getDouble("arena.Muro2_1.y"), config.getDouble("arena.Muro2_1.z"));
        this.Muro2_2 = new Location(Bukkit.getWorld(config.getString("arena.Muro2_2.world")), config.getDouble("arena.Muro2_2.x"), config.getDouble("arena.Muro2_2.y"), config.getDouble("arena.Muro2_2.z"));

        this.spawn = new Location(Bukkit.getWorld(config.getString("arena.spawn.world")), config.getDouble("arena.spawn.x"), config.getDouble("arena.spawn.y"), config.getDouble("arena.spawn.z"));
        this.delay = config.getInt("arena.delay");

        // Colocar os blocos de vidro.
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

    public Location Muro1_1() {
        return Muro1_1;
    }

    public Location Muro1_2() {
        return Muro1_2;
    }

    public Location Muro2_1() {
        return Muro2_1;
    }

    public Location Muro2_2() {
        return Muro2_2;
    }

    @Override
    public void start() {
        // Manda as mensanges de iniciar
        List<?> list = config.getList("mensagems.StartMSG");
        list.forEach(msg -> players.forEach(p -> p.sendMessage(ChatColor.translateAlternateColorCodes('&' , msg.toString()))));


        Bukkit.getScheduler().runTaskTimer(plugin, () -> {


        }, 0, 20L * 20);
    }

    @Override
    public void stop() {
        // Desconectar todos os jogadores
        players.forEach(this::removePlayer);

        // Reseta a arena
        getBlocks(area1, area2).forEach(block -> block.setType(Material.GLASS));

        // Da as recompensas
        if (winner == null) return;
        List<?> list = config.getList("Recompensas.CampoMinado.Comando");
        list.forEach(value -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format(value.toString(), winner)));
    }

    @Override
    public void removePlayer(Player player) {
        player.sendMessage(String.format(config.getString("mensagems.player.eliminado"), player.getDisplayName()));
        Bukkit.getServer().dispatchCommand(player, "/spawn");
        players.remove(player);
    }

    @Override
    public void addPlayer(Player player) {
        player.sendMessage(String.format(config.getString("mensagems.player.entrou"), player.getDisplayName()));
        player.teleport(spawn);
        players.add(player);
    }

    @Override
    public void setWinner(Player player) {
        winner = player;
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public void newLevel() {
        round++;

        List<Block> blocks = getBlocks(area1, area2);
        SplittableRandom random = new SplittableRandom();

        // Colocar os blocos de vidro.
        blocks.forEach(block -> {
            if (random.nextInt(0, 101) <= round) return;
            block.setType(Material.GLASS);
        });
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
