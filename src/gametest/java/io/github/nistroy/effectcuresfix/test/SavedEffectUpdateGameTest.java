package io.github.nistroy.effectcuresfix.test;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pig;

/**
 * Porting Lib stocke les remèdes (cures) d'un effet chargé depuis la sauvegarde dans un ensemble immuable, puis le
 * vide quand l'effet expiré laisse place à son effet caché : crash. Le test passe par une sauvegarde/rechargement,
 * comme un joueur qui se reconnecte ou un mob dont le tronçon est rechargé.
 */
public class SavedEffectUpdateGameTest implements FabricGameTest {
	@GameTest(template = EMPTY_STRUCTURE)
	public void savedEffectFallsBackToHiddenEffect(GameTestHelper helper) {
		MobEffectInstance stacked = new MobEffectInstance(MobEffects.REGENERATION, 400, 0);
		stacked.update(new MobEffectInstance(MobEffects.REGENERATION, 1, 1));
		MobEffectInstance saved = reload(helper, stacked);
		Pig pig = helper.spawn(EntityType.PIG, BlockPos.ZERO.above());

		saved.tick(pig, () -> {});

		helper.assertTrue(saved.getAmplifier() == 0, "à expiration, l'effet caché (niveau I) doit reprendre");
		helper.succeed();
	}

	private static MobEffectInstance reload(GameTestHelper helper, MobEffectInstance effect) {
		MobEffectInstance loaded = MobEffectInstance.load((CompoundTag) effect.save());
		helper.assertTrue(loaded != null, "rechargement de l'effet impossible");
		return loaded;
	}
}
