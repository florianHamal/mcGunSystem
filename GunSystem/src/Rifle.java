import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

public class Rifle extends Weapon{
    public Rifle(String name,Main plugin, Material material,long shootTime,double damage,int magSize,int butstSize,long reloadTime){
        super(name,plugin,material,shootTime,damage,magSize,butstSize,reloadTime);
    }

    @Override
    public void shootEffects(Player player) {
        Arrow projectile =player.launchProjectile(Arrow.class);
        projectile.setVelocity(projectile.getVelocity().multiply(5));
    }

}
