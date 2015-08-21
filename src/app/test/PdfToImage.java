package app.test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JButton;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import javax.swing.JScrollPane;



public class PdfToImage extends JFrame {

	private JPanel contentPane;
	private ImageIcon imagePDFicon = null;
	private Image imgPDF;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PdfToImage frame = new PdfToImage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PdfToImage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 443);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 0, 410, 395);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("New tab", null, panel, null);
		panel.setLayout(null);
		
		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setBounds(12, 12, 277, 344);
		panel.add(jScrollPane1);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					
			        String INPUTFILE = "example.pdf";
			        File file = new File(INPUTFILE);
			        RandomAccessFile raf = new RandomAccessFile(file, "r");
			        FileChannel channel = raf.getChannel();
			        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			        PDFFile pdffile = new PDFFile(buf);

		            // draw the first page to an image
		            int num = pdffile.getNumPages();
		            for (int i = 0; i <= num; i++) {
		            	System.out.println(i);
				        // draw the first page to an image
				        PDFPage page = pdffile.getPage(i);
				        
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
				        imgPDF=img;
		            }
					imagePDFicon = new ImageIcon(imgPDF);
					jScrollPane1.setViewportView(new JLabel(imagePDFicon));
		        }
		        catch (Exception err) {
		            System.out.println(err);
		        }
			}
		});
		btnNewButton.setBounds(304, 12, 73, 25);
		panel.add(btnNewButton);
	}
}
