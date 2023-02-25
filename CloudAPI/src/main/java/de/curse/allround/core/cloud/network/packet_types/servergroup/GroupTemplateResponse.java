package de.curse.allround.core.cloud.network.packet_types.servergroup;

import de.curse.allround.core.cloud.network.packet.Packet;
import de.curse.allround.core.cloud.network.packet.PacketType;
import de.curse.allround.core.cloud.util.FileUtils;
import io.netty.buffer.UnpooledUnsafeDirectByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
@Getter
public class GroupTemplateResponse extends PacketType {

    private final UUID responseId;
    private final File dir;
    private final String group;
    public GroupTemplateResponse(@NotNull Packet packet) {
        super(packet);

        this.responseId = packet.getResponseId();
        this.group = packet.getData()[0];
        dir = Path.of("Storage","Templates","Servers",group).toFile();
        byte[] zipBytes = packet.getData()[1].getBytes();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zipBytes);
        try {
            FileUtils.unzip(dir,new ZipInputStream(byteArrayInputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Packet toPacket() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        try {
            FileUtils.zipFile(dir,group,zipOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return Packet.response(responseId,"GROUP_TEMPLATE_RESPONSE",group, byteArrayOutputStream.toString());
        }

        return Packet.response(responseId,"GROUP_TEMPLATE_RESPONSE",group, byteArrayOutputStream.toString());
    }
}
