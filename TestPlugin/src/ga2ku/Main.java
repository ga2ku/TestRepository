package Seichi.ga2ku;

import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	private final String PLAYER_TAG = "#player";
	private final String MESSAGE_CHARGE = ChatColor.YELLOW + "*#playerÇÕóÕÇÇΩÇﬂÇƒÇ¢ÇÈ*\n" + ChatColor.WHITE + "<#player> ÇﬁÇ®ÇßÇßÇßÇ®Ç®Ç®ÅI";
	private final String MESSAGE_SHOOT = ChatColor.YELLOW + "*#playerÇÕóÕÇï˙èoÇµÇΩ!*\n" + ChatColor.WHITE + "<#player> Ç∆ÇËÇ·ÇüÇüÇüÇüÇüÇ†Ç†Ç†Ç†Ç†ÅI";
	
	private final Material summonItem = Material.DIAMOND_PICKAXE;
	
	private HashMap<Player, SeichiBall> ballOwners = new HashMap<>();
	
	private static Main instance;
	
	@Override
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		Main.instance = this;
	}
	public static Main getInstance(){
		return instance;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(!e.getPlayer().getItemInHand().getType().equals(summonItem)){
			return;
		}
		Player player = e.getPlayer();
		
		switch (e.getAction()) {
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK:
				if(hasBall(player)){
					getBall(player).enlarge();
				}else{
					summonBall(player);
				}
				
				break;
			case LEFT_CLICK_AIR:
			case LEFT_CLICK_BLOCK:
				if(hasBall(player)){
					shootBall(player);
				}
				break;
			default:
				break;
		}
	}
	
	private void summonBall(Player summoner){
		SeichiBall ball = new SeichiBall(summoner);
		ballOwners.put(summoner, ball);
		Bukkit.broadcastMessage(MESSAGE_CHARGE.replaceAll(PLAYER_TAG, summoner.getName()));
	}
	private void shootBall(Player shooter){
		getBall(shooter).shoot();
		Bukkit.broadcastMessage(MESSAGE_SHOOT.replaceAll(PLAYER_TAG, shooter.getName()));
		ballOwners.remove(shooter);
	}
	private SeichiBall getBall(Player player){
		return ballOwners.get(player);
	}
	private boolean hasBall(Player player){
		return ballOwners.containsKey(player);
	}
}