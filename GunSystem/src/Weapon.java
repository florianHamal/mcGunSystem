import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Weapon {
    private Main plugin;
    private String name;
    private Material material;
    private long shootTime;
    private double damage;
    private ArrayList<String> shootWeapon;
    private int magSize,burstSize;
    private long reloadTime;


    public Weapon(String name,Main plugin, Material material, long shootTime, double damage, int magSize, int burstSize,long reloadRime){
        this.plugin = plugin;
        this.material = material;
        this.shootTime = shootTime;
        this.damage = damage;
        this.magSize = magSize;
        this.burstSize = burstSize;
        this.reloadTime = reloadRime;
        this.name = name;
        shootWeapon = new ArrayList<>();
    }
    public void checkShooting(Player player,int currBurstSize) {
        if (getAmo(player) > 0) {
            if (getDelay(player) <= 0) {
                shootEffects(player);
                decreaseAmo(player);
                updateStatus(player);
                setDelay(player, (int) shootTime);
            }
            System.out.println("loop"+currBurstSize);
            if (currBurstSize>0){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    checkShooting(player,currBurstSize-1);
                }
            }, shootTime);

            }
        }


    }



        /*
        if (!shootWeapon.contains(player.getName())) {
            if (getAmo(player) > 0) {
                shootWeapon.add(player.getName());
                shootEffects(player);
                decreaseAmo(player);
                player.sendMessage(getAmo(player)+ "/" + magSize);
            } else {
                player.sendMessage("Waffe ist leer");
            }
            currBurst--;
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new ShootCycle(currBurst,player), shootTime);
        }
         */

    public abstract void shootEffects(Player player);

    public void decreaseAmo(Player player){
        System.out.println("bevore"+getAmo(player));
        setAmo(player,getAmo(player)-1);
        System.out.println("after"+getAmo(player));
        updateStatus(player);
    }

    public Material getMaterial() {
        return material;
    }

    public Main getPlugin() {
        return plugin;
    }

    public void setPlugin(Main plugin) {
        this.plugin = plugin;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public long getShootTime() {
        return shootTime;
    }

    public void setShootTime(long shootTime) {
        this.shootTime = shootTime;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public ArrayList<String> getShootWeapon() {
        return shootWeapon;
    }

    public void setShootWeapon(ArrayList<String> shootWeapon) {
        this.shootWeapon = shootWeapon;
    }
    public void refill(Player player){
        if (!shootWeapon.contains(player)) {
            shootWeapon.add(player.getName());
            setAmo(player, magSize);
            setDelay(player, (int) reloadTime);
            updateStatus(player);
            setWeaponState(player, "reloading");
        }
    }
/*
    public class ShootCycle implements Runnable{
        private int currBurst = 0;
        private Player player;
        public ShootCycle(int currBurst,Player player){
            this.player = player;
            this.currBurst = currBurst;
        }
        public ShootCycle(Player player){
            this.player = player;
        }
        @Override
        public void run() {
            shootWeapon.remove(player.getName());
            if(currBurst>0){
                checkShooting(player,currBurst);
            }
        }
    }
*/
    public int getMagSize() {
        return magSize;
    }

    public void setMagSize(int magSize) {
        this.magSize = magSize;
    }

    public int getBurstSize() {
        return burstSize;
    }

    public void setBurstSize(int burstSize) {
        this.burstSize = burstSize;
    }
    public void setStatus(Player player){
        if (player.getItemInHand().getType()==material){
            ItemMeta itemMeta =player.getItemInHand().getItemMeta();
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.LIGHT_PURPLE +""+magSize+"/"+magSize);
            lore.add(String.valueOf(magSize));
            lore.add("0");
            lore.add("-");
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(ChatColor.GOLD + name);
            player.getItemInHand().setItemMeta(itemMeta);

            player.setExp(1);
            player.setLevel(magSize);
        }
    }
    public void updateStatus(Player player){
        if (player.getItemInHand().getType()==material){
            ItemMeta itemMeta =player.getItemInHand().getItemMeta();
            List<String> lore = itemMeta.getLore();
            lore.set(0,ChatColor.LIGHT_PURPLE +lore.get(1).toString()+"/"+magSize);
            itemMeta.setLore(lore);
            player.getItemInHand().setItemMeta(itemMeta);

            float xp = 1/magSize*Integer.parseInt(lore.get(1));
            player.setExp(xp);
            player.setLevel(Integer.parseInt(lore.get(1)));


        }
    }



    public int getAmo(Player player){
        if ((player.getItemInHand().getType())==material){
            try {
                return Integer.parseInt(player.getItemInHand().getItemMeta().getLore().get(1));
            }catch (Exception e){
                setStatus(player);
                System.out.println("test2");
            }
        }
        return 0;
    }
    public int getDelay(Player player){
        if ((player.getItemInHand().getType())==material){
            try {
                return Integer.parseInt(player.getItemInHand().getItemMeta().getLore().get(2));
            }catch (Exception e){
                setStatus(player);
            }
        }
        return 0;
    }

    public void setAmo(Player player,int value){
        if ((player.getItemInHand().getType())==material){
            ItemMeta itemMeta = player.getItemInHand().getItemMeta();
            List<String> lore = itemMeta.getLore();
            lore.set(1,String.valueOf(value));
            itemMeta.setLore(lore);
            player.getItemInHand().setItemMeta(itemMeta);
        }
    }
    public void setDelay(Player player,int value){
        if ((player.getItemInHand().getType())==material){
            ItemMeta itemMeta = player.getItemInHand().getItemMeta();
            List<String> lore = itemMeta.getLore();
            lore.set(2,String.valueOf(value));
            itemMeta.setLore(lore);
            player.getItemInHand().setItemMeta(itemMeta);
        }
    }
    public String getWeaponState(Player player){
        if ((player.getItemInHand().getType())==material){
            try {
                return player.getItemInHand().getItemMeta().getLore().get(3);
            }catch (Exception e){
                return null;
            }
        }
        return null;
    }

    public void setWeaponState(Player player,String value){
        if ((player.getItemInHand().getType())==material){
            ItemMeta itemMeta = player.getItemInHand().getItemMeta();
            List<String> lore = itemMeta.getLore();
            lore.set(3,value);
            itemMeta.setLore(lore);
            player.getItemInHand().setItemMeta(itemMeta);
        }
    }
}
