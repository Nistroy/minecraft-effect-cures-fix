package io.github.nistroy.effectcuresfix.mixin;

import com.google.common.base.Supplier;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Porting Lib (copyEffects, TAIL de setDetailsFrom) vide puis remplit les remèdes de l'effet en place ; chargés
 * depuis la sauvegarde, ils sont un ImmutableSet → UnsupportedOperationException. Avant lui, on les remplace par une
 * copie modifiable (même résultat que Porting-Lib#202). Priorité > 1000 : appliqué après le mixin de Porting Lib,
 * qui ajoute le champ.
 */
@Mixin(value = MobEffectInstance.class, priority = 1100)
abstract class MobEffectInstanceMixin {
	@Dynamic("ajouté par MobEffectInstanceMixin de porting_lib_entity")
	@Shadow(remap = false)
	private Supplier<Set<?>> porting_lib$cures;

	@Inject(method = "setDetailsFrom", at = @At("HEAD"))
	private void effectcuresfix$makeCuresMutable(MobEffectInstance source, CallbackInfo ci) {
		Set<?> cures = new HashSet<>(this.porting_lib$cures.get());
		this.porting_lib$cures = () -> cures;
	}
}
