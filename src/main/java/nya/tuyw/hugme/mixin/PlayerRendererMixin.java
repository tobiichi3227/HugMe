package nya.tuyw.hugme.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import nya.tuyw.hugme.events.RenderPlayerEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void hugme_onRenderPlayer(AbstractClientPlayer player, float entityYaw, float partialTick,
                                      PoseStack poseStack, MultiBufferSource bufferSource,
                                      int packedLight, CallbackInfo ci) {
        RenderPlayerEventHandler.onRenderPlayer(player, (PlayerRenderer)(Object)this, partialTick, poseStack, bufferSource, packedLight, ci);
    }
}
