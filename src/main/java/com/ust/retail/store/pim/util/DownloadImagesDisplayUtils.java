package com.ust.retail.store.pim.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadImagesDisplayUtils {
    private String localFilePath;
    private URL fileUrl;

    public boolean downloadImage(String fileName,String url) {
        try {
            localFilePath = "./files/" + fileName + ".jpg";
            fileUrl = new URL(url);
            ReadableByteChannel readableByteChannel = Channels.newChannel(fileUrl.openStream());
            FileChannel fileChannel = new FileOutputStream(localFilePath).getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileChannel.close();
            return true;
        } catch (IOException e) {
            e.getStackTrace();
            return false;
        }
    }
}
