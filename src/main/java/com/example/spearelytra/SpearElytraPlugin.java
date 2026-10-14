package com.example.spearelytra;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SpearElytraPlugin extends JavaPlugin implements Listener {
    private final Map<UUID, Long> lastAttack = new ConcurrentHashMap<>();
    private static final long COOLDOWN_MS = 500;
    private static final double RANGE = 4.5;
    private static final double DAMAGE = 8.0;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("SpearElytra enabled.");
    }

    @EventHandler
    public void onSwing(PlayerAnimationEvent event) {
        Player attacker = event.getPlayer();
        if (!attacker.isGliding() || !isSpear(attacker.getInventory().getItemInMainHand())) return;

        long now = System.currentTimeMillis();
        long previous = lastAttack.getOrDefault(attacker.getUniqueId(), 0L);
        if (now - previous < COOLDOWN_MS) return;

        Vector direction = attacker.getEyeLocation().getDirection().normalize();
        RayTraceResult hit = attacker.getWorld().rayTraceEntities(
                attacker.getEyeLocation(),
                direction,
                RANGE,
                0.35,
                entity -> entity instanceof Player
                        && entity != attacker
                        && ((Player) entity).isGliding()
                        && !((Player) entity).isDead()
        );

        if (hit == null || !(hit.getHitEntity() instanceof Player target)) return;
        if (attacker.getWorld() != target.getWorld()) return;

        lastAttack.put(attacker.getUniqueId(), now);
        // Use the normal Bukkit damage pipeline so armor, protection, and other plugins apply.
        target.damage(DAMAGE, attacker);
        attacker.swingMainHand();
    }

    private boolean isSpear(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        // Supports server builds where the spear material is named SPEAR.
        // TRIDENT is accepted as a fallback for older APIs/builds.
        String name = item.getType().name();
        return name.equals("SPEAR") || item.getType() == Material.TRIDENT;
    }
}
