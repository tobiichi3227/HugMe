package nya.tuyw.hugme;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import nya.tuyw.hugme.animation.AnimationManager;
import nya.tuyw.hugme.network.HugRenderClientHandler;
import nya.tuyw.hugme.network.HugRenderPayload;

public class HugMeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AnimationManager.setup();
        ClientPlayNetworking.registerGlobalReceiver(HugRenderPayload.TYPE, HugRenderClientHandler::handleHugRender);
    }
}
