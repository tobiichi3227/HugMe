package nya.tuyw.hugme.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record HugRenderPayload(String sender, String receiver, boolean isdone, int animationEnum) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<HugRenderPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("hugme", "render_hug"));

    public static final StreamCodec<FriendlyByteBuf, HugRenderPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            HugRenderPayload::sender,
            ByteBufCodecs.STRING_UTF8,
            HugRenderPayload::receiver,
            ByteBufCodecs.BOOL,
            HugRenderPayload::isdone,
            ByteBufCodecs.INT,
            HugRenderPayload::animationEnum,

            HugRenderPayload::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}