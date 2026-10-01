package io.github.nistroy.effectcuresfix.test.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.List;
import net.fabricmc.fabric.impl.gametest.FabricGameTestModInitializer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Le module gametest de Porting Lib (dépendance obligatoire de son module core) déclare ses propres tests, qui
 * échouent hors de son projet (« argument type mismatch »). Seuls les tests de ce dépôt sont lancés.
 */
@Mixin(value = FabricGameTestModInitializer.class, remap = false)
abstract class GameTestEntrypointsMixin {
	@ModifyExpressionValue(method = "onInitialize", at = @At(value = "INVOKE",
			target = "Lnet/fabricmc/loader/api/FabricLoader;getEntrypointContainers(Ljava/lang/String;Ljava/lang/Class;)Ljava/util/List;"))
	private List<EntrypointContainer<Object>> effectcuresfix$keepOwnTests(List<EntrypointContainer<Object>> containers) {
		return containers.stream()
				.filter(container -> !container.getProvider().getMetadata().getId().equals("porting_lib_gametest"))
				.toList();
	}
}
