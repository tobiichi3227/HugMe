package nya.tuyw.hugme;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelResource;
import nya.tuyw.hugme.command.HugCommandHandler;
import nya.tuyw.hugme.item.HugTicketItem;
import nya.tuyw.hugme.network.HugRenderPayload;
import org.slf4j.Logger;

import java.io.File;

public class HugMe implements ModInitializer {
    public static final String MODID = "hugme";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Item HUG_TICKET = Registry.register(
            BuiltInRegistries.ITEM,
            ResourceLocation.fromNamespaceAndPath(MODID, "hug_ticket"),
            new HugTicketItem(new Item.Properties().stacksTo(64))
    );

    public static MinecraftServer currentServer = null;

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> content.accept(HUG_TICKET));

        PayloadTypeRegistry.playS2C().register(HugRenderPayload.TYPE, HugRenderPayload.STREAM_CODEC);

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            currentServer = server;
            HugCommandHandler.register(server.getCommands().getDispatcher());
        });

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> currentServer = null);

        ServerTickEvents.END_SERVER_TICK.register(HugCommandHandler::onServerTickEnd);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.getPlayer();
            File playerDataFile = new File(server.getWorldPath(LevelResource.PLAYER_DATA_DIR).toFile(), player.getUUID() + ".dat");
            if (!playerDataFile.exists()) {
                ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(MODID, "hug_ticket")), 16);
                if (!player.getInventory().add(itemStack)) {
                    player.drop(itemStack, false);
                }
            }
        });
    }
}

