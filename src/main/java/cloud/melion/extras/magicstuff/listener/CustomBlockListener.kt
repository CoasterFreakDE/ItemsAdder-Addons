package cloud.melion.extras.magicstuff.listener

import cloud.melion.extras.magicstuff.Magicstuff.Companion.INSTANCE
import dev.lone.itemsadder.api.ItemsAdder
import dev.lone.itemsadder.api.ItemsAdder.isCustomItem
import dev.lone.itemsadder.api.ItemsAdder.isFurniture
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import org.bukkit.*
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.util.Vector


class CustomBlockListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onEntityHit(event: EntityDamageByEntityEvent) {
        with(event) {
            if (isFurniture(entity)) {
                if (damager is Player) {
                    val player = damager as Player
                    if (player.gameMode == GameMode.CREATIVE)
                        return

                    val staff = player.inventory.itemInMainHand

                    if(isCustomItem(staff)) {
                        val name = ItemsAdder.getCustomItemName(staff)

                        if (name.contains("_staff")) {
                            val location = entity.location.clone()
                            entity.world.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, entity.location, 100)
                            damager.playSound(Sound.sound(Key.key("minecraft:entity.generic.explode"), Sound.Source.AMBIENT, 1f, 1f))
                            isCancelled = true
                            entity.teleport(entity.location.clone().add(Vector(0, -3, 0)))

                            val barriers = mutableListOf<Location>()

                            location.apply {
                                for (dx in blockX-1..blockX+1)
                                    for (dz in blockZ-1..blockZ+1)
                                        for (dy in blockY..blockY+2) {
                                            val block = location.world.getBlockAt(dx, dy, dz)

                                            if (block.type == Material.BARRIER) {
                                                block.setType(Material.AIR, false)
                                                barriers.add(block.location)
                                            }
                                        }
                            }

                            Bukkit.getScheduler().runTaskLater(INSTANCE, Runnable {
                                var success = entity.teleport(location)
                                var counter = 1
                                while(!success && counter < 15) {
                                    println("Teleport failed. Tried $counter times.")
                                    success = entity.teleport(location)
                                    counter++
                                }
                                damager.playSound(Sound.sound(Key.key("minecraft:block.anvil.land"), Sound.Source.AMBIENT, 1f, 0.1f))
                                for (loc in barriers) {
                                    loc.block.setType(Material.BARRIER, false)

                                    val entities = loc.getNearbyEntities(0.2, 1.0, 0.2)
                                    entities.forEach {
                                        if (it is LivingEntity) {
                                            it.damage(8.0, entity)
                                        }
                                    }
                                }
                            }, 100L)
                            return
                        }
                    }
                    isCancelled = true


                    val inventory = Bukkit.createInventory(
                        null,
                        54, ":offset_-270::blockade_1::offset_-1::blockade_2::offset_-1::blockade_3:")
                    player.openInventory(inventory)
                }
            }
        }
    }
}