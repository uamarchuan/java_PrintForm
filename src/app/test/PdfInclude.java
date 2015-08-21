package app.test;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.*;
public class PdfInclude extends JFrame {

    private static String INPUTFILE = "example.pdf"; //Specifying the file location.
    private JFrame frame;
    private JPanel panelPDF;
    
   public static void main(String[] args) {
        new PdfInclude();
    }
   
	public PdfInclude() {

		initialize();
		pdfRead();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setSize(600, 700);
//		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(false);
		frame.setTitle("PDF");
//		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
//        frame.pack();

	}
	
	public void pdfRead() {
        try { 
        String INPUTFILE = "example_2.pdf";
        File file = new File(INPUTFILE);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);

        // draw the first page to an image
        PDFPage page = pdffile.getPage(0);
        
        //get the width and height for the doc at the default zoom 
        Rectangle rect = new Rectangle(0,0,
                (int)page.getBBox().getWidth(),
                (int)page.getBBox().getHeight());
        
        //generate the image
        Image img = page.getImage(
                rect.width, rect.height, //width & height
                rect, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true  // block until drawing is done
                );


        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
        
        frame.pack();
        frame.setSize(600, 700);
        }
        catch (Exception e) {
            System.out.println(e);
        }
	}
}
