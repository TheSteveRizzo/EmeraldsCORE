package com.steve_rizzo.emeraldscore.features.villagersave;

import com.steve_rizzo.emeraldscore.Main;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class VillagerSaverListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Villager))
            return;
        Villager villagerEnt = (Villager) event.getEntity();
        EntityDamageEvent damageCauseEvent = villagerEnt.getLastDamageCause();
        EntityDamageEvent.DamageCause cause = damageCauseEvent.getCause();
        if (!isValidDamageCause(cause))
            return;
        Entity killerEntity = getKillerEntity(damageCauseEvent, cause);
        if (killerEntity == null)
            return;
        if (!killerEntity.isValid())
            return;
        if (!isZombieVariant(killerEntity))
            return;
        if (isVillagerWorldBlacklisted(villagerEnt))
            return;
        villagerEnt.zombify();
    }

    private Entity getKillerEntity(EntityDamageEvent damageCauseEvent, EntityDamageEvent.DamageCause cause) {
        switch (cause) {
            case PROJECTILE:
                return (Entity) ((Projectile) ((EntityDamageByEntityEvent) damageCauseEvent).getDamager()).getShooter();
            case ENTITY_ATTACK:
                return ((EntityDamageByEntityEvent) damageCauseEvent).getDamager();
            case ENTITY_EXPLOSION:
                return ((TNTPrimed) ((EntityDamageByEntityEvent) damageCauseEvent).getDamager()).getSource();
        }
        return null;
    }

    private boolean isValidDamageCause(EntityDamageEvent.DamageCause cause) {
        return (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK || cause == EntityDamageEvent.DamageCause.PROJECTILE || cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
    }

    private boolean isZombieVariant(Entity entity) {
        if (entity.getType().equals(EntityType.ZOMBIE) ||
                (entity.getType().equals(EntityType.ZOMBIE_VILLAGER)) ||
                (entity.getType().equals(EntityType.DROWNED)) ||
                (entity.getType().equals(EntityType.HUSK)) ||
                (entity.getType().equals(EntityType.HUSK)) ||
                (entity.getType().equals(EntityType.ZOMBIFIED_PIGLIN))) return true;
        return false;
    }

    private boolean isVillagerWorldBlacklisted(Villager villagerEnt) {
        return Main.WorldBlackList.contains(villagerEnt.getWorld().getName());
    }
}