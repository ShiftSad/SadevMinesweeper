package me.sadev.campominado;

import me.sadev.campominado.database.Database;
import org.bukkit.plugin.java.JavaPlugin;
import org.tinylog.Logger;

public final class CampoMinado extends JavaPlugin {

    public Database database;

    @Override
    public void onEnable() {
        long time = System.currentTimeMillis();
        Logger.info("[SadevCampoMinado] Iniciando o plugin.");

        saveDefaultConfig();

        // Iniciar database
        database = new Database(this);


    }
}
