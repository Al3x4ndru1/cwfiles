package adp.cw2122.View;

import adp.cw2122.Controller.doCancel;
import adp.cw2122.Controller.doDownloads;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Downloader extends JFrame implements WindowListener {

  private  final File[] downloads;
  private final List<JProgressBar> bars;
  private Thread win;


  public Downloader(final File[] downloads) {

    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    final JPanel progPanel = new JPanel(new GridLayout(0,1));
    this.downloads = downloads;
    this.bars = new ArrayList<>(downloads.length);
    for (int i = 0; i < downloads.length; i++) {
      final JProgressBar bar = new JProgressBar();
      bar.setMaximum((int) downloads[i].length());
      this.bars.add(bar);
      final JPanel border = new JPanel(new BorderLayout());
      border.setBorder(BorderFactory.createTitledBorder(downloads[i].getPath()));
      border.add(bar);
      final JButton cancelButton = new JButton("X");
      final int j = i;
      cancelButton.addActionListener((ev)-> new Thread(()-> new doCancel(j)).start()); //create a thread for doCancel cancel the j thread
      border.add(cancelButton, BorderLayout.EAST);
      progPanel.add(border);
      win = new Thread(()-> addWindowListener(this));
      win.start();
    }

    final JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(progPanel);

    final JButton goButton = new JButton("Start");
    goButton.addActionListener((ev)-> new Thread(()-> new doDownloads(downloads, bars)).start()); //create a thread for doDownloads and pass the downloads and bars to the class
    mainPanel.add(goButton, BorderLayout.SOUTH);

    add(mainPanel);

    pack();
    setVisible(true);
  }

//this will show the Image
  public synchronized static void showImage(final BufferedImage image, final String name) {
    final JFrame iw = new JFrame(name);
    iw.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    final ImagePanel ip = new ImagePanel();
    ip.setImage(image);
    iw.add(ip);
    iw.pack();
    iw.setVisible(true);
  }

  @Override
  public void windowOpened(WindowEvent windowEvent) {
  }

  @Override
  public void windowClosing(WindowEvent windowEvent) {
    doCancel.cancelall();
    win.interrupt();
  }

  @Override
  public void windowClosed(WindowEvent windowEvent) {
  }

  @Override
  public void windowIconified(WindowEvent windowEvent) {
  }

  @Override
  public void windowDeiconified(WindowEvent windowEvent) {
  }

  @Override
  public void windowActivated(WindowEvent windowEvent) {
  }

  @Override
  public void windowDeactivated(WindowEvent windowEvent) {
  }

  //the Blueprint of how to show the image
  private static class ImagePanel extends JComponent {
    private BufferedImage image;
    public void setImage(final BufferedImage image) {
      this.image = image;
      setPreferredSize(new Dimension(this.image.getWidth(), this.image.getHeight()));
      repaint();
    }
    @Override
    public void paintComponent(final Graphics g) {
      g.drawImage(this.image, 0, 0, this);
    }
  }

  //is called by the main and start in the SwinUtilities.invokeLater thread, the GUI thread
  public synchronized static void launch() {
    final String[] names = new String[] { "images/pyramids.jpg", "images/pagodas.jpg", "images/bug.jpg" };
    final File[] downloads = new File[names.length];
    for (int i = 0; i < downloads.length; i++) {
      downloads[i] = new File(names[i]);
    }
    new Downloader(downloads);
  }

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(()->launch());
  }
}