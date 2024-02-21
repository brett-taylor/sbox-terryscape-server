package content.interfaces;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.terryscape.Config;
import com.terryscape.game.chat.PlayerChatComponent;
import com.terryscape.game.interfaces.InterfaceActionHandler;
import com.terryscape.game.interfaces.InterfaceManager;
import com.terryscape.net.Client;

import java.nio.ByteBuffer;
import java.util.Set;

@Singleton
public class WelcomeScreenInterfaceActionHandler implements InterfaceActionHandler {

    private final InterfaceManager interfaceManager;

    @Inject
    public WelcomeScreenInterfaceActionHandler(InterfaceManager interfaceManager) {
        this.interfaceManager = interfaceManager;
    }

    @Override
    public Set<String> getInterfaceId() {
        return Set.of("welcome_screen");
    }

    @Override
    public void handleAction(Client client, String interfaceId, String interfaceAction, ByteBuffer packet) {
        var player = client.getPlayer().orElseThrow();
        var playerChat = player.getEntity().getComponentOrThrow(PlayerChatComponent.class);

        if (!interfaceAction.equals("close")) {
            return;
        }

        interfaceManager.closeInterface(client, interfaceId);
        playerChat.sendGameMessage("Welcome to %s, %s.".formatted(Config.NAME, player.getUsername()));
        playerChat.sendGameMessage("Say ::help to see commands.");
    }

}
