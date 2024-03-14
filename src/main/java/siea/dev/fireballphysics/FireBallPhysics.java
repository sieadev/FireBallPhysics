package siea.dev.fireballphysics;

import org.bukkit.plugin.java.JavaPlugin;
import siea.dev.fireballphysics.listeners.FireballImpactListener;
import siea.dev.fireballphysics.listeners.PlayerInteractListener;

public final class FireBallPhysics extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        double speed = getConfig().getDouble("speed");
        double radius = getConfig().getDouble("radius");
        double multiplicatorVertical = getConfig().getDouble("multiplicatorVertical");
        double multiplicatorHorizontal = getConfig().getDouble("multiplicatorHorizontal");
        double strength = getConfig().getDouble("strength");
        double coolDown = getConfig().getDouble("coolDown");
        String coolDownMessage = getConfig().getString("coolDownMessage");
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(speed,coolDown,coolDownMessage), this);
        getServer().getPluginManager().registerEvents(new FireballImpactListener(radius,strength,multiplicatorVertical,multiplicatorHorizontal, this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
