package settingdust.calypsos_afflatus.forge

import net.minecraftforge.network.NetworkRegistry
import settingdust.calypsos_afflatus.CalypsosAfflatus
import settingdust.calypsos_afflatus.forge.item.nightvision_goggles.C2SSwitchModePacket

@Suppress("INACCESSIBLE_TYPE")
object CalypsosAfflatusNetworking {
    const val PROTOCOL_VERSION = "1"
    val CHANNEL = NetworkRegistry.newSimpleChannel(
        CalypsosAfflatus.id("main"),
        { PROTOCOL_VERSION },
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    )
    private var index = 0

    init {
        CHANNEL.registerMessage(
            index++,
            C2SSwitchModePacket::class.java,
            { packet, buf -> packet.encode(buf) },
            { C2SSwitchModePacket.decode(it) },
            { packet, context -> C2SSwitchModePacket.handle(packet, context) })
    }
}