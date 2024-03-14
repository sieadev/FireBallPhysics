package siea.dev.fireballphysics.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class PlayerInteractListener implements Listener {
    private final double speed;
    private final double coolDown;
    private final String coolDownMessage;
    private final HashMap<Player, Double> cooldowns = new HashMap<>();

    public PlayerInteractListener(double speed, double cooldown, String coolDownMessage) {
        this.speed = speed;
        this.coolDown = cooldown;
        this.coolDownMessage = coolDownMessage;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getItemInHand();
        if (event.getAction().name().contains("RIGHT_CLICK") && itemInHand.getType() == Material.FIREBALL) {
            if ((cooldowns.getOrDefault(player, (double) 0) < System.currentTimeMillis())){
                player.sendMessage(coolDownMessage);
                return;
            }
            cooldowns.put(player, (double) System.currentTimeMillis() - coolDown);
            if (player.getGameMode() != GameMode.CREATIVE){
                if (itemInHand.getAmount() > 1) {
                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                } else {
                    player.getInventory().removeItem(itemInHand);
                }
            }
            spawnFireball(player);
            event.setCancelled(true);
        }
    }

    private void spawnFireball(Player player) {
        Vector direction = player.getLocation().getDirection();
        direction.normalize();
        Fireball fireball = player.getWorld().spawn(player.getLocation().add(direction), Fireball.class);
        fireball.setVelocity(direction.multiply(speed));
    }
}
