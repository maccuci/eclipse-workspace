package me.tony.commons.api.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;

public class MenuInventory {
	
	private HashMap<Integer, MenuItem> slotItem;
	private int rows;
	private String title;
	private Inventory inv;
	private boolean onePerPlayer;

	public MenuInventory(String title, int rows) {
		this(title, rows, false);
	}

	public MenuInventory(String title, int rows, boolean onePerPlayer) {
		this.slotItem = new HashMap<>();
		this.rows = rows;
		this.title = title;
		this.onePerPlayer = onePerPlayer;
		if (!onePerPlayer) {
			this.inv = Bukkit.createInventory(new MenuHolder(this), rows * 9, "");
		}
	}

	public void addItem(MenuItem item) {
		setItem(item, firstEmpty());
	}

	public void addItem(ItemStack item) {
		setItem(item, firstEmpty());
	}

	public void setItem(ItemStack item, int slot) {
		setItem(new MenuItem(item), slot);
	}
	
	public void setItem(int slot, ItemStack item) {
		setItem(new MenuItem(item), slot);
	}

	public void setItem(MenuItem item, int slot) {
		this.slotItem.put(Integer.valueOf(slot), item);
		if (!this.onePerPlayer) {
			this.inv.setItem(slot, item.getStack());
		}
	}

	public int firstEmpty() {
		if (!this.onePerPlayer) {
			return this.inv.firstEmpty();
		}
		for (int i = 0; i < this.rows * 9; i++) {
			if (!this.slotItem.containsKey(Integer.valueOf(i))) {
				return i;
			}
		}
		return -1;
	}

	public boolean hasItem(int slot) {
		return this.slotItem.containsKey(Integer.valueOf(slot));
	}

	public MenuItem getItem(int slot) {
		return (MenuItem) this.slotItem.get(Integer.valueOf(slot));
	}

	public void open(Player p) {
		if (!this.onePerPlayer) {
			p.openInventory(this.inv);
		} else {
			if ((p.getOpenInventory() == null)
					|| (p.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST)
					|| (p.getOpenInventory().getTopInventory().getSize() != this.rows * 9)
					|| (p.getOpenInventory().getTopInventory().getHolder() == null)
					|| (!(p.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder))
					|| (!((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).isOnePerPlayer())) {
				createAndOpenInventory(p);
			} else {
				for (int i = 0; i < this.rows * 9; i++) {
					if (this.slotItem.containsKey(Integer.valueOf(i))) {
						p.getOpenInventory().getTopInventory().setItem(i,
								((MenuItem) this.slotItem.get(Integer.valueOf(i))).getStack());
					} else {
						p.getOpenInventory().getTopInventory().setItem(i, null);
					}
				}
				p.updateInventory();
			}
			((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).setMenu(this);
		}
		updateTitle(p);

		p = null;
	}

	public void updateTitle(Player p) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PacketPlayOutOpenWindow openWindow = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, 0, this.title,
				this.rows * 9, false);
		ep.playerConnection.sendPacket(openWindow);
		ep.updateInventory(ep.activeContainer);

		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = p.getInventory().getContents()).length;
		for (int i = 0; i < j; i++) {
			ItemStack item = arrayOfItemStack[i];
			p.getInventory().setItem(i, item);
			i++;
		}
		p.updateInventory();
		openWindow = null;
		ep = null;
	}

	public void createAndOpenInventory(Player p) {
		Inventory playerInventory = Bukkit.createInventory(new MenuHolder(this), this.rows * 9, "");
		for (Map.Entry<Integer, MenuItem> entry : this.slotItem.entrySet()) {
			playerInventory.setItem(((Integer) entry.getKey()).intValue(), ((MenuItem) entry.getValue()).getStack());
		}
		p.openInventory(playerInventory);

		p = null;
	}

	public void close(Player p) {
		if (this.onePerPlayer) {
			destroy(p);
			p = null;
		}
	}

	public void destroy(Player p) {
		if ((p.getOpenInventory().getTopInventory().getHolder() != null)
				&& ((p.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder))) {
			((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).destroy();
		}
	}

	public boolean isOnePerPlayer() {
		return this.onePerPlayer;
	}

	public Inventory getInventory() {
		return this.inv;
	}
}
