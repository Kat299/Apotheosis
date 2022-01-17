package shadows.apotheosis.ench.asm;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.enchantment.Enchantment;
import shadows.apotheosis.Apotheosis;
import shadows.apotheosis.ench.EnchModule;

/**
 * Methods injected by Javascript Coremods.
 * @author Shadows
 *
 */
public class EnchHooks {

	/**
	 * Replaces the call to {@link Enchantment#getMaxLevel()} in various classes.
	 * Injected by coremods/ench/max_level_redirects.js
	 */
	public static int getMaxLevel(Enchantment ench) {
		if (!Apotheosis.enableEnch) return ench.getMaxLevel();
		return EnchModule.getEnchInfo(ench).getMaxLevel();
	}

	/**
	 * Replaces the call to {@link Enchantment#isTreasureOnly()} in various classes.
	 * Injected by coremods/ench/treasure_redirects.js
	 */
	public static boolean isTreasureOnly(Enchantment ench) {
		if (!Apotheosis.enableEnch) return ench.isTreasureOnly();
		return EnchModule.getEnchInfo(ench).isTreasure();
	}

	/**
	 * Replaces the call to {@link Enchantment#isDiscoverable()} in various classes.
	 * Injected by coremods/ench/discoverable_redirects.js
	 */
	public static boolean isDiscoverable(Enchantment ench) {
		if (!Apotheosis.enableEnch) return ench.isDiscoverable();
		return EnchModule.getEnchInfo(ench).isDiscoverable();
	}
	
	/**
	 * Replaces the call to {@link Enchantment#isTradeable()} in various classes.
	 * Injected by coremods/ench/tradeable_redirects.js
	 */
	public static boolean isTradeable(Enchantment ench) {
		if (!Apotheosis.enableEnch) return ench.isTradeable();
		return EnchModule.getEnchInfo(ench).isTradeable();
	}

	/**
	 * Override for protection calculations.  Removes the hard cap of total level 20.  Effectiveness is reduced past 20.
	 * Called from {@link CombatRules#getDamageAfterMagicAbsorb(float, float)}
	 * Injected by apothasm/combat-rules.js
	 */
	public static float getDamageAfterMagicAbsorb(float damage, float enchantModifiers) {
		float clamped = Mth.clamp(enchantModifiers, 0, 20);
		float remaining = Mth.clamp(enchantModifiers - 20, 0, 44);
		float factor = 1 - clamped / 25;
		if (remaining > 0) {
			factor -= 0.2F * remaining / 60;
		}
		return damage * factor;
	}

	/**
	 * Calculates the delay for catching a fish.  Ensures that the value never returns <= 0, so that it doesn't get infinitely locked.
	 * Called at the end of {@link FishingBobberEntity#catchingFish(BlockPos)}
	 * Injected by coremods/fishing_hook.js
	 */
	public static int getTicksCaughtDelay(FishingHook bobber) {
		int lowBound = Math.max(1, 100 - bobber.lureSpeed * 10);
		int highBound = Math.max(lowBound, 600 - bobber.lureSpeed * 60);
		return Mth.nextInt(bobber.random, lowBound, highBound);
	}

}