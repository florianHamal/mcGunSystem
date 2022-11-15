

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.List;

public class WeaponHandler implements Listener {
    private ArrayList<Weapon> weapons;
    private Main plugin;
    public WeaponHandler(Main plugin) {
        this.plugin=plugin;
        weapons = new ArrayList<Weapon>();
        weapons.add(new Rifle("SE-14C",plugin, Material.PRISMARINE_SHARD,4,1.0,15,3,5));//SE-14C = Prismarine Shard
        weapons.add(new Rifle("DL-44",plugin, Material.SUGAR,15,2.0,9,1,20*3));//DL-44 = Sugar
        weapons.add(new Rifle("EE-3",plugin, Material.NETHER_STAR,4,2.0,18,1,20*3));//EE-3 = Nether Star
        weapons.add(new Rifle("A280-CFE",plugin, Material.NETHER_BRICK_ITEM,7,3.0,25,1,20*3));//A280-CFE = Nether Brick
        weapons.add(new Rifle("E-11",plugin, Material.STICK,7,2.0,9,1,20*3));//E-11 = Nether Quarz
        weapons.add(new Rifle("DH-17",plugin, Material.QUARTZ,10,2.0,15,1,20*3));//DH-17 =Prismarine Crystals
        weapons.add(new Rifle("CylerRifle",plugin, Material.BLAZE_ROD,50,20.0,1,1,20*5));//CylerRifle blazerod
        decreaseWeaponDelay();





    }

    @EventHandler
    public void handleWeaponShoot(PlayerInteractEvent event) {
        if(!(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)){
            Weapon weapon = checkWeaponMaterial(event.getItem().getType());
            if (weapon != null) {
                weapon.checkShooting(event.getPlayer(),weapon.getBurstSize());
            }
        }else {
            Weapon weapon = checkWeaponMaterial(event.getItem().getType());
            if (weapon != null) {
                weapon.refill(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void handleWeaponDamage(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Projectile))return;
        Projectile projectile = (Projectile) event.getDamager();
        if (!(projectile.getShooter() instanceof Player)) return;
        Player player = (Player) projectile.getShooter();
        Weapon weapon = checkWeaponMaterial((player.getItemInHand()).getType());
        if (weapon!=null)
            event.setDamage(weapon.getDamage());
    }

    public Weapon checkWeaponMaterial(Material material) {
        for (Weapon current : weapons) {
            if (current.getMaterial() == material) {
                return current;
            }
        }
        return null;
    }

    public void decreaseWeaponDelay(){

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player:Bukkit.getOnlinePlayers()){
                    Weapon weapon = checkWeaponMaterial(player.getItemInHand().getType());
                    if (weapon != null) {
                        if (weapon.getDelay(player)>0) {
                            weapon.setDelay(player, weapon.getDelay(player) - 1);
                            if (weapon.getWeaponState(player)!="-"){
                                sendMessage(player,ChatColor.RED+weapon.getWeaponState(player));
                            }else{
                                //sendMessage(player,"cooling down");
                            }
                        }else {
                            if (weapon.getWeaponState(player)!=null){
                                weapon.setWeaponState(player,"-");
                            }
                        }
                    }
                }
            }
        },0,1);
    }

    public static void sendMessage(Player p, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message.replace("&", "§") + "\"}"), (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

}
