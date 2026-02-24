package judexis.core.gui;

import java.util.UUID;

public interface GuiControlSuite {
    void openMainConsole(UUID viewerId);
    void openForensic(UUID viewerId, UUID targetId);
    void back(UUID viewerId);
}
