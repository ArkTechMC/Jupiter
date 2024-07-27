package fi.dy.masa.malilib.mixin;

import fi.dy.masa.malilib.event.RenderEventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen {
    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Inject(method = "drawMouseoverTooltip", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V"))
    private void onRenderTooltip(DrawContext drawContext, int x, int y, CallbackInfo ci) {
        if (this.focusedSlot != null && this.focusedSlot.hasStack()) {
            ((RenderEventHandler) RenderEventHandler.getInstance()).onRenderTooltipLast(drawContext, this.focusedSlot.getStack(), x, y);
        }
    }
}
