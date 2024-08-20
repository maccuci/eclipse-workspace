/*
 * API for spigot/bukkit, created by TxnyHe4rt
 */

package me.tony.commons.bukkit.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class TonyMCAPI {
	private Material material;
	private int amount;
	private short durability;
	private boolean useMeta;
	private boolean glow;
	private String displayName;
	private HashMap<Enchantment, Integer> enchantments;
	private ArrayList<String> lore;
	
	private Color color;
	private String skinOwner;
	private String skinUrl;

	private boolean hideAttributes;
	private boolean unbreakable;
	
	public TonyMCAPI() {
		material = Material.STONE;
		amount = 1;
		durability = 0;
		hideAttributes = false;
		unbreakable = false;
		useMeta = false;
		glow = false;
	}

	public TonyMCAPI type(Material material) {
		this.material = material;
		return this;
	}

	public TonyMCAPI amount(int amount) {
		if (amount > material.getMaxStackSize())
			amount = material.getMaxStackSize();
		if (amount <= 0)
			amount = 1;
		this.amount = amount;
		return this;
	}
	
	public TonyMCAPI durability(int durability) {
		return durability(durability, false);
	}
	
	public TonyMCAPI durability(int durability, boolean percent) {
		int maxDurability = Math.max(15, material.getMaxDurability());
		if (!percent && durability >= 0 && durability <= maxDurability) {
			this.durability = (short) durability;
		} else if (durability >= 1 && durability <= 100) {
			this.durability = (short) (maxDurability - ((maxDurability * durability) / 100));
		}
		return this;
	}

	public TonyMCAPI name(String text) {
		if (!useMeta) {
			useMeta = true;
		}
		this.displayName = text.replace("&", "§");
		return this;
	}

	public TonyMCAPI enchantment(Enchantment enchantment) {
		return enchantment(enchantment, 1);
	}

	public TonyMCAPI enchantment(Enchantment enchantment, Integer level) {
		if (enchantments == null) {
			enchantments = new HashMap<>();
		}
		enchantments.put(enchantment, level);
		return this;
	}

	public TonyMCAPI lore(String text) {
		if (!this.useMeta) {
			this.useMeta = true;
		}
		this.lore = new ArrayList<>(getLore(30, text));
		return this;
	}
	
	public TonyMCAPI lore(String ... lore) {
		return lore(Arrays.asList(lore));
	}
	
	public TonyMCAPI lore(List<String> text) {
		if (!this.useMeta) {
			this.useMeta = true;
		}
		if (this.lore == null) {
			this.lore = new ArrayList<>();
		}
		for (String str : text) {
			this.lore.add(str.replace("&", "§"));
		}
		return this;
	}

	public TonyMCAPI glow() {
		glow = true;		
		return this;
	}

	public TonyMCAPI color(Color color) {
		this.useMeta = true;
		this.color = color;
		return this;
	}
	
	public TonyMCAPI skin(String skin) {
		this.useMeta = true;
		this.skinOwner = skin;
		return this;		
	}
	
	public TonyMCAPI skinURL(String skinURL) {
		this.useMeta = true;
		this.skinUrl = skinURL;
		return this;
	}

	public TonyMCAPI hideAttributes() {
		this.useMeta = true;
		this.hideAttributes = true;
		return this;
	}
	
	public TonyMCAPI showAttributes() {
		this.useMeta = true;
		this.hideAttributes = false;
		return this;
	}
	
	public TonyMCAPI unbreakable() {
		this.unbreakable = true;
		return this;
	}
	
	public ItemStack build() {
		ItemStack stack = new ItemStack(material);
		stack.setAmount(amount);
		stack.setDurability(durability);
		
		if (enchantments != null && !enchantments.isEmpty()) {
			for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
				stack.addUnsafeEnchantment(entry.getKey(), entry.getValue());
			}
		}
		
		if (useMeta) {
			ItemMeta meta = stack.getItemMeta();
			
			if (displayName != null) {
				meta.setDisplayName(displayName.replace("&", "§"));
			}
			
			if (lore != null && !lore.isEmpty()) {
				meta.setLore(lore);
			}
			
			/** Colored Leather Armor */
			if (color != null) {
				if (meta instanceof LeatherArmorMeta) {
					((LeatherArmorMeta) meta).setColor(color);
				}
			}
			
			/** Skull Heads */
			if (meta instanceof SkullMeta) {
				SkullMeta skullMeta = (SkullMeta) meta;
				if (skinUrl != null) {
					GameProfile profile = new GameProfile(UUID.randomUUID(), null);
					profile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinUrl).getBytes(StandardCharsets.UTF_8))));
					try {
						Field field = skullMeta.getClass().getDeclaredField("profile");
						field.setAccessible(true);
						field.set(skullMeta, profile);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (skinOwner != null) {
					skullMeta.setOwner(skinOwner);					
				}
			}
			
			meta.spigot().setUnbreakable(unbreakable);
			
			/** Item Flags */
			if (hideAttributes) {
				meta.addItemFlags(ItemFlag.values());
			} else {
				meta.removeItemFlags(ItemFlag.values());
			}
			
			stack.setItemMeta(meta);
		}
		
		if (glow && (enchantments == null || enchantments.isEmpty())) {
			try {
				Constructor<?> caller = MinecraftReflection.getCraftItemStackClass()
						.getDeclaredConstructor(ItemStack.class);
				caller.setAccessible(true);
				ItemStack item = (ItemStack) caller.newInstance(stack);
				NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(item);
				compound.put(NbtFactory.ofList("ench"));
				return item;
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		material = Material.STONE;
		amount = 1;
		durability = 0;
		
		if (useMeta) {
			useMeta = false;
		}
		
		if (glow) {
			glow = false;
		}
		
		if (hideAttributes) {
			hideAttributes = false;
		}
		
		if (unbreakable) {
			unbreakable = false;
		}

		if (displayName != null) {
			displayName = null;
		}
		
		if (enchantments != null) {
			enchantments.clear();
			enchantments = null;
		}
		
		if (lore != null) {
			lore.clear();
			lore = null;
		}

		skinOwner = null;
		skinUrl = null;
		color = null;
		return stack;
	}

	public static ItemStack glow(ItemStack stack) {
		try {
			Constructor<?> caller = MinecraftReflection.getCraftItemStackClass()
					.getDeclaredConstructor(ItemStack.class);
			caller.setAccessible(true);
			ItemStack item = (ItemStack) caller.newInstance(stack);
			NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(item);
			compound.put(NbtFactory.ofList("ench"));
			return item;
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return stack;
	}
	
	public static TonyMCAPI fromStack(ItemStack stack) {
		TonyMCAPI builder =  new TonyMCAPI().type(stack.getType()).amount(stack.getAmount()).durability(stack.getDurability());
		
		if (stack.hasItemMeta()) {
			ItemMeta meta = stack.getItemMeta();
			
			if (meta.hasDisplayName())
				builder.name(meta.getDisplayName());
			
			if (meta.hasLore())
				builder.lore(meta.getLore());
			
			if (meta instanceof LeatherArmorMeta) {
				Color color = ((LeatherArmorMeta) meta).getColor();
				if (color != null)
					builder.color(color);				
			}
			
			if (meta instanceof SkullMeta) {
				SkullMeta sm = (SkullMeta) meta;
				if (sm.hasOwner())
					builder.skin(sm.getOwner());
			}
		}
		
		return builder;
	}
	
	public static List<String> getLore(int max, String text) {
		List<String> lore = new ArrayList<>();
		text = ChatColor.translateAlternateColorCodes('&', text);
		String[] split = text.split(" ");
		String color = "";
		text = "";
		for (int i = 0; i < split.length; i++) {
			if (ChatColor.stripColor(text).length() >= max || ChatColor.stripColor(text).endsWith(".")
					|| ChatColor.stripColor(text).endsWith("!")) {
				lore.add(text);
				if (text.endsWith(".") || text.endsWith("!"))
					lore.add("");
				text = color;
			}
			String toAdd = split[i];
			if (toAdd.contains("§"))
				color = ChatColor.getLastColors(toAdd.toLowerCase());
			if (toAdd.contains("\n")) {
				toAdd = toAdd.substring(0, toAdd.indexOf("\n"));
				split[i] = split[i].substring(toAdd.length() + 1);
				lore.add(text + (text.length() == 0 ? "" : " ") + toAdd);
				text = color;
				i--;
			} else {
				text += (ChatColor.stripColor(text).length() == 0 ? "" : " ") + toAdd;
			}
		}
		lore.add(text);
		return lore;
	}
	
	public static boolean checkItem(ItemStack item, String name) {
		if(item != null) {
			if(item.hasItemMeta()) {
				if(item.getItemMeta().hasDisplayName()) {
					if(item.getType() != Material.AIR) {
						if(item.getItemMeta().getDisplayName() == name) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isEquals(ItemStack item, ItemStack it) {
		if (it.getType() == item.getType() && it.getDurability() == item.getDurability()) {
			if (it.hasItemMeta() && item.hasItemMeta()) {
				if (it.getItemMeta().hasDisplayName() && item.getItemMeta().hasDisplayName()) {
					if (item.getItemMeta().getDisplayName().equals(it.getItemMeta().getDisplayName()))
						return true;
				} else if (!it.getItemMeta().hasDisplayName() && !item.getItemMeta().hasDisplayName())
					return true;
			} else if (!it.hasItemMeta() && !item.hasItemMeta())
				return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static List<ItemStack> getAllMinecraftItems() {
		List<ItemStack> items = new ArrayList<>();

		Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();

		while (recipeIterator.hasNext()) {
			Recipe recipe = recipeIterator.next();
			ItemStack result = recipe.getResult();
			result.setAmount(1);
			if (net.minecraft.server.v1_8_R3.Item.getById(result.getTypeId()) == null)
				continue;
			if (!containsItem(items, result.getType(), result.getDurability()))
				items.add(result);
		}

		for (Material m : Material.values()) {
			if (net.minecraft.server.v1_8_R3.Item.getById(m.getId()) == null)
				continue;
			ItemStack result = new ItemStack(m);
			if (!containsItem(items, result.getType(), result.getDurability()))
				items.add(result);
		}
		Collections.sort(items, new Comparator<ItemStack>() {
			@Override
			public int compare(ItemStack arg0, ItemStack arg1) {
				if (arg0.getTypeId() == arg1.getTypeId())
					return arg0.getDurability() - arg1.getDurability();
				return arg0.getTypeId() - arg1.getTypeId();
			}
		});

		return items;
	}
	
	public static boolean containsItem(List<ItemStack> list, Material material, int durability) {
		for (ItemStack item : list) {
			if (item.getType() == material && item.getDurability() == (short) durability)
				return true;
		}
		return false;
	}
	
	public static List<ItemStack> searchItemStacks(String searchName, List<ItemStack> itemList) {
		List<ItemStack> list = new ArrayList<>();
		for (ItemStack item : itemList) {
			if (item.getType().toString().toLowerCase().contains(searchName.toLowerCase()))
				list.add(item);
		}
		return list;
	}
	
	public static long fromString(String str) {
		String[] arrayOfString;
		Integer templateInteger;
		Integer integer;
		String[] timeString = str.split(",");
		Integer day = 0, hour = 0, minute = 0, second = 0;
		
		templateInteger = (arrayOfString = timeString).length;
		for(integer = 0; integer < templateInteger; integer++) {
			String string = arrayOfString[integer];
			if(string.contains("s") || string.contains("S")) {
				second = Integer.valueOf(string.replace("s", "").replace("S", "")).intValue();
			}
			if(string.contains("m") || string.contains("M")) {
				minute = Integer.valueOf(string.replace("m", "").replace("M", "")).intValue();
			}
			if(string.contains("h") || string.contains("H")) {
				hour = Integer.valueOf(string.replace("h", "").replace("H", "")).intValue();
			}
			if(string.contains("d") || string.contains("D")) {
				day = Integer.valueOf(string.replace("d", "").replace("D", "")).intValue();
			}
		}
		Long time = convert(day, hour, minute, second);
		return time;
	}
	
	public static String getMessage(long endOfBan) {
		String message = "";
		long current = System.currentTimeMillis();
		long difference = endOfBan - current;
		int time = (int)(difference / 1000L);
		if(time >= 31104000) {
			int years = time / 31104000;
			time %= 31104000;
			if(years == 1) {
				message = String.valueOf(message) + years + " ano ";
			} else {
				message = String.valueOf(message) + years + " anos ";
			}
		}
		if(time >= 2592000) {
			int months = time / 2592000;
			time %= 2592000;
			if(months == 1) {
				message = String.valueOf(message) + months + " mês ";
			} else {
				message = String.valueOf(message) + months + " meses ";
			}
		}
		if(time >= 86400) {
			int days = time / 86400;
			time %= 86400;
			if(days == 1) {
				message = String.valueOf(message) + days + " dia ";
			} else {
				message = String.valueOf(message) + days + " dias ";
			}
		}
		if(time >= 3600) {
			int hours = time / 3600;
			time %= 3600;
			if(hours == 1) {
				message = String.valueOf(message) + hours + " hora ";
			} else {
				message = String.valueOf(message) + hours + " horas ";
			}
		}
		if(time >= 60) {
			int min = time / 60;
			time %= 60;
			if(min == 1) {
				message = String.valueOf(message) + min + " minuto ";
			} else {
				message = String.valueOf(message) + min + " minutos ";
			}
		}
		if(time > 0) {
			if(time == 1) {
				message = String.valueOf(message) + time + " segundo";
			} else {
				message = String.valueOf(message) + time + " segundos";
			}
		}
		if(time <= 0) {
			message = "Expirado";
		}
		return message;
	}

	public static long convert(long days, long hours, long minutes, long seconds) {
		long x = 0L;
		long minute = minutes * 60L;
		long hour = hours * 3600L;
		long day = days * 86400L;
		x = minute + hour + day + seconds;
		long time = System.currentTimeMillis() + x * 1000L;
		return time;
	}

	public static String captalise(String string) {
		char[] arrayOfChar = string.toLowerCase().toCharArray();
		arrayOfChar[0] = Character.toUpperCase(arrayOfChar[0]);
		return new String(arrayOfChar);
	}

	public static String format(int time) {
		if (time >= 3600) {
			int hours = (time / 3600), minutes = (time % 3600) / 60, seconds = (time % 3600) % 60;
			return (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
					+ (seconds < 10 ? "0" : "") + seconds;
		} else {
			int minutes = (time / 60), seconds = (time % 60);
			return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
		}
	}

	public void setValue(String field, Class<?> clazz, Object instance, Object value) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Exception exception) {

		}
	}

	public Object getValue(String field, Class<?> clazz, Object instance) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception exception) {

		}
		return null;
	}

	public void setValue(String field, Object instance, Object value) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Exception exception) {

		}
	}

	public Object getValue(String field, Object instance) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception exception) {

		}
		return null;
	}
	
	public static Boolean getBoolean(int i) {
		if(i == 1)
			return true;
		return false;
	}
}
