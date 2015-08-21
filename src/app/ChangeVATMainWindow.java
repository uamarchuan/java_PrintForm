package app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class ChangeVATMainWindow extends JFrame {

	private JPanel contentPane;
	private JLabel lblUserNameBar;
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeVATMainWindow frame = new ChangeVATMainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	private void GetUser () {
		try {
			String sql = "select name,surname from user where login=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, LoginFrame.txt_username.getText().trim());
			rs = pst.executeQuery();
			String name = rs.getString("name");
			String surname = rs.getString("surname");
			lblUserNameBar.setText("User: "+name+" "+surname);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);			
		} finally {
			try {
				rs.close();
				pst.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	
	/**
	 * Create the frame.
	 */
	public ChangeVATMainWindow() {
		conn = sqliteConnection.dbConnector();
		initialize();
		GetUser();
	}
	
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setTitle("Print Form - Change VAT Invoice");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w)/2;
		int y = (dim.height - h)/2;
		this.setLocation(x, y);
		
		//
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmOpenFile = new JMenuItem("Open File...");
		mnNewMenu.add(mntmOpenFile);
		
		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		});
		
		JMenuItem mntmLogOut = new JMenuItem("Log Out");
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

			}
		});
		mnNewMenu.add(mntmLogOut);
		mnNewMenu.add(mntmExit);
		
		JMenu mnNewMenu_1 = new JMenu("Edit");
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmUserSettings = new JMenuItem("User Settings");
		mntmUserSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new UserSettingsMenu().setVisible(true);
			}
		});
		mnNewMenu_1.add(mntmUserSettings);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmCheckUpgrade = new JMenuItem("Check Upgrade");
		mnHelp.add(mntmCheckUpgrade);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		menuBar.add(Box.createHorizontalGlue());
		
		lblUserNameBar = new JLabel();
		menuBar.add(lblUserNameBar);
		getContentPane().setLayout(null);
		
		//
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 800, 30);
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton lblBack = new JButton("Back");
		lblBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				//new MainWindowClass().setVisible(true);
			}
		});
		Image img_back = new ImageIcon(this.getClass().getResource("/back.png")).getImage();
		lblBack.setIcon(new ImageIcon(img_back));
		toolBar.add(lblBack);
		
		//body
	}

}