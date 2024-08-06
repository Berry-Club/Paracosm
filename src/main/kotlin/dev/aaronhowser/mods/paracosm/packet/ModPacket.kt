package dev.aaronhowser.mods.paracosm.packet

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

interface ModPacket : CustomPacketPayload {

    fun receiveMessage(context: IPayloadContext)

}