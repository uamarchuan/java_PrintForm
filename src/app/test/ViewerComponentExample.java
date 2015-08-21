package app.test;

import java.awt.BorderLayout;
import org.icepdf.ri.common.SwingController; 
import org.icepdf.ri.common.SwingViewBuilder;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ViewerComponentExample { 
	public static void main(String[] args) { 
		// build a component controller 
		SwingController controller = new SwingController(); 
		SwingViewBuilder factory = new SwingViewBuilder(controller); 
		controller.setIsEmbeddedComponent(true); // set the viewController embeddable flag. 
		DocumentViewController viewController = controller.getDocumentViewController(); 
		JPanel viewerComponentPanel = factory.buildViewerPanel(); // add copy keyboard command 
		ComponentKeyBinding.install(controller, viewerComponentPanel); // add interactive mouse link annotation support via callback 
		controller.getDocumentViewController().setAnnotationCallback( 
				new org.icepdf.ri.common.MyAnnotationCallback( 
						controller.getDocumentViewController())); // build a containing JFrame for display 
		JFrame applicationFrame = new JFrame(); 
		applicationFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); 
		applicationFrame.getContentPane().setLayout(new BorderLayout()); 
		applicationFrame.getContentPane().add(viewerComponentPanel, BorderLayout.CENTER); 
		applicationFrame.getContentPane().add(factory.buildCompleteMenuBar(), BorderLayout.NORTH); 
		// Now that the GUI is all in place, we can try opening a PDF 
		// hard set the page view to single page which effectively give a single 
		// page view. This should be done after openDocument as it has code that 
		// can change the view mode if specified by the file. 
		controller.setPageViewMode(DocumentViewControllerImpl.ONE_PAGE_VIEW, false); // show the component 
		applicationFrame.pack(); 
		applicationFrame.setVisible(true); 
	}
}
