package adp.cw2122.Controller;

import adp.cw2122.Model.doDownload;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class doDownloads {
    private static File[] downloads;
    private final List<JProgressBar> bars;
    private static Thread[] t;
    private static final ArrayList<ArrayList<Byte>> byteList = new ArrayList<>();

    //get the infromation form the Downloader and call the doDownloadersmethod
    public doDownloads(File[] downloads, List<JProgressBar> bars) {
        this.downloads = downloads;
        this.bars = bars;
        doDownloadsmethod();    }


    //will create a thread for each file and star it and join them
    private synchronized void doDownloadsmethod() {
        t = new Thread[downloads.length];
        for (int i = 0; i < downloads.length; i++) {
            ArrayList<Byte> bytes = new ArrayList<>(1024);
            byteList.add(bytes);
            doDownload r = new doDownload(bars.get(i), bytes, downloads[i]);
            t[i] = new Thread(r);
            t[i].start();
        }
        for (int i = 0; i < downloads.length; i++){
            try {
                t[i].join();
            } catch (InterruptedException e) {}
        }
    }

    //interrupt the thread and after rejoin them
    public static void cancel(int i) {
        t[i].interrupt();
        try {
            t[i].join();
        } catch (InterruptedException e) {}

    }

    //interrupt all the threads before closing the GUI application
    public static void cancelall() {
        for (int i = 0; i < downloads.length; i++) {
            t[i].interrupt();
            try {
                t[i].join();
            } catch (InterruptedException e) {
            }
        }
    }
}