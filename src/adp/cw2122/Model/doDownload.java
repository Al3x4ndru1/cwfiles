package adp.cw2122.Model;

import adp.cw2122.Controller.doDownloads;
import adp.cw2122.View.Downloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static adp.cw2122.View.Downloader.showImage;

public class doDownload extends Component implements Runnable{

    private final JProgressBar bar;
    private final ArrayList<Byte> bytes;
    private final File downloads;
    protected static Downloader frame;

    public doDownload(JProgressBar bar, ArrayList<Byte> bytes,File downloads,Downloader frame) {
        this.downloads=downloads;
        this.bar=bar;
        this.bytes=bytes;
        frame=frame;

    }

    //show the image
    public synchronized static void show(File downloads ,byte[] bytess){
        try {
            final BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytess));
            showImage(image, downloads.getPath());
        } catch (IOException e) {
           JOptionPane.showMessageDialog(frame,"Don t have any data","Error message",JOptionPane.ERROR_MESSAGE);
        }
    }

//update the bar progress in its own therad
    private synchronized void updateProgress() {
        bar.setValue(bytes.size());
    }

//run method will download each file and it will print an error message dialog if is unable to find the file
//will show the image by calling the show method

    @Override
    public void run() {
        System.out.println("Doing: " + this.downloads);

        try {
            final InputStream is = Network.getInputStreamForDownload(this.downloads);

            while (true) {
                final int read = is.read();
                if (read < 0 || Thread.interrupted()) {
                    break;
                } else {
                    bytes.add((byte) read);
                    SwingUtilities.invokeLater(()-> updateProgress());
                    if ((bytes.size() % 1000) == 0) {
                        System.out.println(bytes.size() + " bytes read");
                    }
                }
            }

        } catch (final IOException e) {
            new Thread(() -> JOptionPane.showMessageDialog(frame,"Unable to find the file","Error message",JOptionPane.ERROR_MESSAGE)).start();
        }
        //this will be executed at the end
        finally {
            byte[] byteraw = new byte[bytes.size()];
            for (int j = 0; j < bytes.size(); j++) {
                byteraw[j] = bytes.get(j);
            }
            SwingUtilities.invokeLater(()->show(downloads,byteraw));
        }
        System.out.println(this.downloads + " DONE");
    }
}