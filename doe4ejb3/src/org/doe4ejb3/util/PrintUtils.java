package org.doe4ejb3.util;

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

/*
 * Copied from this tutorial:
 * 
 * http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-Printing...
 * 
 * And also from a post on the forums at java.swing.com. My apologies that do not have
 * a link to that post, by my hat goes off to the poster because he/she figured out the 
 * sticky problem of paging properly when printing a Swing component.
 */
public class PrintUtils implements Printable {
  private Component componentToBePrinted;

  public static void printComponent(Component c) {
    new PrintUtils(c).print();
  }
  
  public PrintUtils(Component componentToBePrinted) {
    this.componentToBePrinted = componentToBePrinted;
  }
  
  public void print() {
    PrinterJob printJob = PrinterJob.getPrinterJob();
    printJob.setPrintable(this);
    if (printJob.printDialog())
      try {
        printJob.print();
      } catch(PrinterException pe) {
        System.out.println("Error printing: " + pe);
      }
  }

  public int print(Graphics g, PageFormat pf, int pageIndex) {
   int response = NO_SUCH_PAGE;

    Graphics2D g2 = (Graphics2D) g;

    //  for faster printing, turn off double buffering
    disableDoubleBuffering(componentToBePrinted);

    Dimension d = componentToBePrinted.getSize(); //get size of document
    double panelWidth = d.width; //width in pixels
    double panelHeight = d.height; //height in pixels

    double pageHeight = pf.getImageableHeight(); //height of printer page
    double pageWidth = pf.getImageableWidth(); //width of printer page

    double scale = pageWidth / panelWidth;
    int totalNumPages = (int) Math.ceil(scale * panelHeight / pageHeight);

    //  make sure not print empty pages
    if (pageIndex >= totalNumPages) {
      response = NO_SUCH_PAGE;
    }
    else {

      //  shift Graphic to line up with beginning of print-imageable region
      g2.translate(pf.getImageableX(), pf.getImageableY());

      //  shift Graphic to line up with beginning of next page to print
      g2.translate(0f, -pageIndex * pageHeight);

      //  scale the page so the width fits...
      g2.scale(scale, scale);

      Color bg = componentToBePrinted.getBackground();
      try {
          componentToBePrinted.setBackground(Color.WHITE);
          componentToBePrinted.print(g2); //repaint the page for printing
      } finally {
          componentToBePrinted.setBackground(bg);
      }

      enableDoubleBuffering(componentToBePrinted);
      response = Printable.PAGE_EXISTS;
    }
    return response;
  }

  /** The speed and quality of printing suffers dramatically if
   *  any of the containers have double buffering turned on.
   *  So this turns if off globally.
   *  @see enableDoubleBuffering
   */
  public static void disableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(false);
  }

  /** Re-enables double buffering globally. */
  
  public static void enableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(true);
  }
}
