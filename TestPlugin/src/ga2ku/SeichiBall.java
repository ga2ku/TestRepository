package Seichi.ga2ku;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SeichiBall extends BukkitRunnable{
		private final static double MAX_BALL_SIZE = 4D;
		private final static double MIN_BALL_SIZE = 1D;
		private final static double BALL_ENLARGE = 0.1D;
		private final static double BALL_SPEED = 0.1D;
		private final static double BALL_GRAVITY = 0.008D;
	
		private Player owner;
		private Location center;
		private double size;
		private boolean shootable = false;
		private Vector shootVelocity;

		public SeichiBall(Player owner){
			this.owner = owner;
			this.size = MIN_BALL_SIZE;
			this.center = owner.getLocation().add(0, 2, 0).add(owner.getLocation().getDirection().setY(0).multiply(3));
			
			runTaskTimer(Main.getInstance(), 0, 1);
		}
		
		
		public void enlarge(){
			if(size < MAX_BALL_SIZE){
				size += BALL_ENLARGE;
			}
		}
		public void shoot(){
			shootable = true;
			shootVelocity = owner.getLocation().getDirection().multiply(5 * BALL_SPEED);
			center.getWorld().playSound(center, Sound.ENTITY_BLAZE_HURT, size<2?2:3, 0);
			center.getWorld().spawnParticle(Particle.CLOUD, center, (int) (size * 10));
		}
		
		@Override
		public void run(){
			updateLocation();
			checkCollision();
			effect();
		}
		private void checkCollision(){
			if(center.getBlock().getType().isSolid() || center.getY() < 0){
				seichi();
				this.cancel();
			}
		}
		private void updateLocation(){
			if(shootable){
				shootVelocity.setY(shootVelocity.getY() - BALL_GRAVITY);
				center.add(shootVelocity);
			}else{
				Location target = owner.getLocation().add(0, 2, 0).add(owner.getLocation().getDirection().setY(0).multiply(3));
				center.add(target.toVector().subtract(center.toVector()).multiply(BALL_SPEED));
			}
		}
		private void effect(){
			for(int ya = 0; ya < 180; ya+=180/(size*3)){
				for(int xza = 0; xza < 360; xza+=360/(size*5)){
					double x = size * Math.cos(Math.toRadians(xza)) * Math.sin(Math.toRadians(ya));
					double y = size * Math.cos(Math.toRadians(ya));
					double z = size * Math.sin(Math.toRadians(xza)) * Math.sin(Math.toRadians(ya));
					
					if(Math.random() <= 0.9){
						center.getWorld().spawnParticle(Particle.SPELL_WITCH, center.clone().add(x, y, z), 0);
					}else if(Math.random() <= 0.005){
						center.getWorld().playSound(center, Sound.BLOCK_PORTAL_AMBIENT, 1, 2);
					}else{
						center.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, center.clone().add(x, y, z), 0);
					}
				}
			}
		}
		private void seichi(){
			for(int x = (int) (-size*2); x <= size*2; x++){
				for(int y = (int) (-size*2); y <= size*2; y++){
					for(int z = (int) (-size*2); z <= size*2; z++){
						center.clone().add(x, y, z).getBlock().setType(Material.AIR);
					}
				}
			}
			center.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, center, size<2?0:1);
			center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, size<2?1:2, size<2?1:0);
			
		}
	}