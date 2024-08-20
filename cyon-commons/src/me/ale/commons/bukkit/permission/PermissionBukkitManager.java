package me.ale.commons.bukkit.permission;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import me.ale.commons.bukkit.BukkitMain;

public class PermissionBukkitManager {
	
	public PermissionAttachment fetchPlayer(Player player) {
		if(player != null) {
			PermissionAttachment attachment = player.addAttachment(BukkitMain.getPlugin());
			
			return attachment;
		}
		
		return null;
	}
	
	public void addPermissionName(Player player, String permission) {
		PermissionAttachment attachment = fetchPlayer(player);
		attachment.setPermission(permission, true);
	}
	
	public void removePermission(Player player, String perms) {
	    for (PermissionAttachmentInfo PermissionInfo : player.getEffectivePermissions()) {
	      PermissionAttachment Attachment = PermissionInfo.getAttachment();
	      if (Attachment != null) {
	        Map<String, Boolean> Flags = Attachment.getPermissions();
	        for (String Perm : Flags.keySet()) {
	          if (Perm.equals(perms)) {
	            Attachment.setPermission(Perm, false);
	          }
	        }
	      }
	    }
	  }
	
	public Permissible getPermissions(Player player) {
	    PermissionAttachment perm = fetchPlayer(player);
	    return perm.getPermissible();
	  }
	
	 public void refreshPerms(Player player)
	  {
	    for (int x = 0; x < permissionAttachmentInfo(player).getPermission().length(); x++) {
	      removePermission(player, permissionAttachmentInfo(player).getPermission());
	    }
	    PermissionConfig pex = new PermissionConfig();
	    List<String> perms = new ArrayList<>();
	    for (String s : pex.getPermissions(player.getName())) {
	      if (!perms.contains(s)) {
	        perms.add(s);
	      }
	    }
	    for (int x = 0; x < perms.size(); x++) {
	      addPermissionName(player, (String)perms.get(x));
	    }
	  }
	
	public PermissionAttachmentInfo permissionAttachmentInfo(Player player	) {
	    Iterator<?> localIterator = player.getEffectivePermissions().iterator();
	    if (localIterator.hasNext())
	    {
	      PermissionAttachmentInfo s = (PermissionAttachmentInfo)localIterator.next();
	      return s;
	    }
	    return null;
	  }
	  
	  public Set<PermissionAttachmentInfo> getAllPermissons(Player player)
	  {
	    return player.getEffectivePermissions();
	  }
}
