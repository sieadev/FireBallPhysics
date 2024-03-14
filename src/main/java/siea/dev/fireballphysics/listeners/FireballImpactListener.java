package siea.dev.fireballphysics.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class FireballImpactListener implements Listener {
    private final double multiplicatorVertical;
    private final double multiplicatorHorizontal;
    private final double radius;
    private final Plugin plugin;
    private final double strength;

    public FireballImpactListener(double radius,double strength, double multiplicatorVertical, double multiplicatorHorizontal, Plugin plugin) {
        this.radius = radius;
        this.multiplicatorVertical = multiplicatorVertical;
        this.multiplicatorHorizontal = multiplicatorHorizontal;
        this.plugin = plugin;
        this.strength = strength;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Fireball) {
            event.blockList().removeIf(block -> !block.getType().name().toLowerCase().contains("wool"));
            Fireball fireball = (Fireball) event.getEntity();
            fireball.remove();
            Location impactLocation = fireball.getLocation();
            World world = impactLocation.getWorld();
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Entity entity : world.getNearbyEntities(impactLocation, radius, radius, radius)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            double distance = impactLocation.distance(player.getLocation());
                            double knockbackStrength = strength - (distance / radius);
                            Vector knockbackVelocity = player.getLocation().toVector().subtract(impactLocation.toVector()).normalize().multiply(knockbackStrength);
                            knockbackVelocity.setY(knockbackVelocity.getY() / multiplicatorHorizontal);
                            knockbackVelocity.setX(knockbackVelocity.getX() * multiplicatorVertical);
                            knockbackVelocity.setY(knockbackVelocity.getY() * multiplicatorVertical);
                            player.setVelocity(knockbackVelocity);
                        }
                    }
                }
            }.runTaskLater(plugin, 1); // Adjust the delay for explosion (10 ticks = 0.5 seconds)
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Fireball){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL){
            event.setCancelled(true);
        }
    }
}
