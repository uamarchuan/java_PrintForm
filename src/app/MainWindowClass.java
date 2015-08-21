package app;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Method;
import java.sql.*;
import javax.swing.*;

public class MainWindowClass extends JFrame {
	
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	private JComboBox cbFormaName;
	private JLabel lblUserNameBar;

	/**
	 * Class constructor.
	 */	
	
	public MainWindowClass() {
		conn = sqliteConnection.dbConnector();
		initialize();
		FillFormList();
		GetUser();
	}

	/**
	 * Create the frame.
	 */
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(450, 300);
		this.setResizable(false);
		this.setTitle("Print Form - Main window");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w)/2;
		int y = (dim.height - h)/2;
		this.setLocation(x, y);
		this.setVisible(true);
				
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenuFile = new JMenu("File");
		menuBar.add(mnMenuFile);
		
		JMenuItem mntmOpenFile = new JMenuItem("Open File...");
		mnMenuFile.add(mntmOpenFile);
		
		JSeparator separator = new JSeparator();
		mnMenuFile.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		});
		
		JMenuItem mntmLogOut = new JMenuItem("Log Out");
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dispose();
				new LoginFrame();
			}
		});
		mnMenuFile.add(mntmLogOut);
		mnMenuFile.add(mntmExit);
		
		JMenu mnMenuEdit = new JMenu("Edit");
		menuBar.add(mnMenuEdit);
		
		JMenuItem mntmUserSettings = new JMenuItem("User Settings");
		mntmUserSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new UserSettingsMenu().setVisible(true);
			}
		});
		mnMenuEdit.add(mntmUserSettings);
		
		JMenu mnHelpHelp = new JMenu("Help");
		menuBar.add(mnHelpHelp);
		
		JMenuItem mntmCheckUpgrade = new JMenuItem("Check Upgrade");
		mnHelpHelp.add(mntmCheckUpgrade);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelpHelp.add(mntmAbout);
		
		menuBar.add(Box.createHorizontalGlue());
		
		lblUserNameBar = new JLabel();
		menuBar.add(lblUserNameBar);
		getContentPane().setLayout(null);
		
//		
		
		cbFormaName = new JComboBox();
		cbFormaName.setBounds(23, 114, 191, 24);
		getContentPane().add(cbFormaName);
		
		JButton btnNewButton = new JButton("Ok");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String item = (String)cbFormaName.getSelectedItem();
					String sql = "select class from forms where en_name=?";
					pst = conn.prepareStatement(sql);
					pst.setString(1, item);
					rs = pst.executeQuery();
					String output = rs.getString("class");
					
					VATMainWindow VatClass = new VATMainWindow();
					String strVatClass = VatClass.getClass().getSimpleName();
					ChangeVATMainWindow ChanVatClass = new ChangeVATMainWindow();
					String strChanVatClass = ChanVatClass.getClass().getSimpleName();
					
					if (output.equals(strVatClass)) {
						dispose();
						VatClass.setVisible(true);
					} else if (output.equals(strChanVatClass)) {
						dispose();
						ChanVatClass.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "Problem with associated class to forms in DB!");
					}
					

				} catch (Exception er) {
					JOptionPane.showMessageDialog(null, er);			
				} finally {
					try {
						rs.close();
						pst.close();
						conn.close();
					} catch (Exception err) {
						JOptionPane.showMessageDialog(null, err);
					}
				}
				
			}
		});
		btnNewButton.setBounds(238, 114, 200, 24);
		getContentPane().add(btnNewButton);
		
		JLabel lblSelectFormFor = new JLabel("Select form for continue working!");
		lblSelectFormFor.setBounds(96, 37, 241, 38);
		getContentPane().add(lblSelectFormFor);
	}
	
	private void FillFormList () {
		try {
			String sql = "select en_name from forms where status='1'";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				String name = rs.getString("en_name");
				cbFormaName.addItem(name);
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);			
		} finally {
			try {
				rs.close();
				pst.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
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
				JOptionPane.showMessageDialog(null, e);
			}
		}
	}
}
