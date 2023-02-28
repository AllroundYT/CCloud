package de.curse.allround.core.cloud.util;

import de.curse.allround.core.cloud.network.packet.Packet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {


    @Contract(pure = true)
    public static void copy(@NotNull File fileToCopy, File destination) throws IOException {
        if (fileToCopy.isDirectory()){

            if (!destination.isDirectory() && !destination.mkdirs()) throw new IOException("Could not create destination directory. "+destination);

            File[] children = fileToCopy.listFiles();
            if (children == null) return;
            for (File child : children) {
                File newFile = new File(destination, child.getName());
                copy(child,newFile);
            }
            return;
        }
        if (!destination.exists() && !destination.createNewFile()) throw new IOException("Destination file does not exist. "+destination);

        try (FileInputStream fileInputStream = new FileInputStream(fileToCopy);
        FileOutputStream fileOutputStream = new FileOutputStream(destination)){
            fileOutputStream.write(fileInputStream.readAllBytes());
            fileOutputStream.flush();
        }
    }
    public static void zipFile(@NotNull File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()){
            return;
        }
        if (fileToZip.isDirectory()){
            if (fileName.endsWith("/")){
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            }else {
                zipOut.putNextEntry(new ZipEntry(fileName+"/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            if (children == null) return;
            for (File childFile : children) {
                zipFile(childFile,fileName+"/"+childFile.getName(),zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0){
            zipOut.write(bytes,0,length);
        }
        fis.close();
    }

    public static boolean delete(@NotNull File file){
        if (file.isDirectory()){
            File[] children = file.listFiles();
            if (children == null) return false;
            for (File child : children) {
                if (!delete(child)) return false;
            }
            return true;
        }
        return file.delete();
    }

    @Contract(pure = true)
    public static void unzip(@NotNull File destination, ZipInputStream zipIn) throws IOException {
        if (!destination.exists()) {
            if(!destination.mkdirs()) return;
        }

        byte[] buffer = new byte[1024];
        ZipEntry zipEntry = zipIn.getNextEntry();
        while (zipEntry != null){
            File newFile = newFile(destination,zipEntry);
            if (zipEntry.isDirectory()){
                if (!newFile.isDirectory() && !newFile.mkdirs()){
                    throw new IOException("Failed to create directory "+newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()){
                    throw new IOException("Failed to create directory "+parent);
                }
                try(FileOutputStream fos = new FileOutputStream(newFile);){
                    int len;
                    while ((len = zipIn.read(buffer)) > 0){
                        fos.write(buffer,0,len);
                    }
                }
            }
            zipEntry = zipIn.getNextEntry();
        }

        zipIn.closeEntry();
        zipIn.close();
    }

    public static @NotNull File newFile(File destinationDir, @NotNull ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir,zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath+File.separator)){
            throw new IOException("Entry is outside of the target dir: "+zipEntry.getName());
        }
        return destFile;
    }
}
