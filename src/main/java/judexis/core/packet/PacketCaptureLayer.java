package judexis.core.packet;

import com.comphenix.protocol.events.PacketEvent;

public interface PacketCaptureLayer {
    void start();
    void stop();
    void route(PacketEvent event);
}