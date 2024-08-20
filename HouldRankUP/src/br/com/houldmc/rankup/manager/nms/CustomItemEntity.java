package br.com.houldmc.rankup.manager.nms;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityItem;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.World;

public class CustomItemEntity extends EntityItem {

	public CustomItemEntity(World world, ItemStack itemStack) {
		super(world);
		setItemStack(itemStack);
	}

	public void inactiveTick() {
	}

	protected void burn(float i) {
	}

	protected void burn(int i) {
	}

	public boolean damageEntity(DamageSource damagesource, float f) {
		return false;
	}

	public void collide(Entity arg0) {

	}

	public void move(double d0, double d1, double d2) {

	}

	public ItemStack getItemStack() {
		return super.getItemStack();
	}

	public void setItemStack(ItemStack itemStack) {
		super.setItemStack(itemStack);
	}
}