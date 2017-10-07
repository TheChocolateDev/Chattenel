package me.callmefilms.CNL;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CNLFront extends JavaPlugin{
	
	Channel global = new Channel("global");
	Channel local = new Channel("local");
	Channel teamOne = new Channel("team1");
	Channel teamTwo = new Channel("teamTwo");
	
	List<Channel> channels = new ArrayList<Channel>();
	
	public void onEnable() {
		
		this.channels.add(global);
		this.channels.add(local);
		this.channels.add(teamOne);
		this.channels.add(teamTwo);
		
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ChatRunner(), this);
		
		Bukkit.getServer().getPluginCommand("channel").setExecutor(new Commands());
		
	}
	
	public Channel getPlayerChannel(Player player) {
		for(int i = 0; i < channels.size(); i++) {
			for(int j = 0; j < channels.get(i).getPlayers().size(); j++) {
				if(channels.get(i).getPlayers().get(j).getUniqueId().toString() == player.getUniqueId().toString()) {
					return channels.get(i);
				}
			}
		}
		return global;
	}
	
	public Channel getChannel(String name) {
		for(int i = 0; i < channels.size(); i++) {
			if(channels.get(i).getName().equalsIgnoreCase(name)) {
				return channels.get(i);
			}
		}
		return null;
	}
	
	public class JoinListener implements Listener {
		
		@EventHandler
		public void onPlayerJoinEvent(PlayerJoinEvent event) {
			Player player = event.getPlayer();
			for(int i = 0; i < channels.size(); i++) {
				for(int j = 0; j < channels.get(i).getPlayers().size(); j++) {
					if(channels.get(i).getPlayers().get(j).getUniqueId().toString() == player.getUniqueId().toString()) {
						channels.get(i).removePlayer(player);
					}
				}
			}
			global.addPlayer(player);
		}
		
	}
	
	public class ChatRunner implements Listener {
		
		@EventHandler
		public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
			getPlayerChannel(event.getPlayer()).runMessage(event.getPlayer(), event.getMessage());
		}
		
	}
	
	public class Commands implements CommandExecutor {
		
		@Override
		public boolean onCommand(CommandSender sndr, Command cmd, String label, String[] args) {
			if(!(sndr instanceof Player)) {
				sndr.sendMessage("This command can not be executed by console personnel. Please try again in-game.");
			} else {
				Player player = (Player) sndr;
				player.sendMessage("Error message 1");
				switch(cmd.getName()) {
				case "channel":
					player.sendMessage("Error message 2");
					if(args.length < 1) {
//						INSERT HELP MENU COMMAND
					} else {
						player.sendMessage("Error message 3");
						if(getChannel(args[0]) == null) {
							player.sendMessage("Invalid channel name");
						} else {
							
							player.sendMessage("Checking channel for player list.");
							
							Channel playerChannel = getPlayerChannel(player);
							
							Channel argedChan = getChannel(args[0]);
							
							if(argedChan.getPlayers().contains(player)) {
								player.sendMessage("You are alread in " + argedChan.getName());
							} else {
								playerChannel.removePlayer(player);
								if(playerChannel.getPlayers().contains(player)) {
									player.sendMessage("Player leave failed.");
								} else {
									player.sendMessage("Player leave successful.");
								}
								argedChan.addPlayer(player);
								if(argedChan.getPlayers().contains(player)) {
									player.sendMessage("Player add successful.");
								} else {
									player.sendMessage("Player add failed.");
								}
							}
							
//							player.sendMessage("Error message 444444444");
//							Channel playerChannel = getPlayerChannel(player);
//							if(getChannel(args[0]).getPlayers().contains(player)) {
//								player.sendMessage("You are already in " + getChannel(args[0]).getName());
//							} else {
//								playerChannel.removePlayer(player);
//								if(playerChannel.getPlayers().contains(player)) {
//									player.sendMessage("Oops! You're still in the " + playerChannel.getName() + " channel!");
//								} else {
//									player.sendMessage("You've left the " + playerChannel.getName() + " channel.");
//								}
//								Channel newChannel = getChannel(args[0]);
//								newChannel.addPlayer(player);
//								if(newChannel.getPlayers().contains(player)) {
//									player.sendMessage("You've successfully switched to the " + newChannel.getName() + " channel.");
//								} else {
//									player.sendMessage("Uh oh, it looks like you haven't been able to successfully switch to the " + newChannel.getName() + " channel.");
//								}
//							}
						}
					}
				}
			}
			return true;
		}
		
	}
	
}
