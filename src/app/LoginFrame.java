package app;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class LoginFrame extends JFrame {

	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	
	private JFrame frame;
	public static JTextField txt_username;
	private JPasswordField txt_password;

	/**
	 * Launch the application.
	 */
	public LoginFrame() {
		conn = sqliteConnection.dbConnector();
		initialize();
		frame.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(450, 300);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Print Form - Authorisation Form");
		frame.setLocationRelativeTo(null);
		
		JLabel lblLogo = new JLabel("");
		Image img_logo = new ImageIcon(this.getClass().getResource("/NestleLogo.jpg")).getImage();
		lblLogo.setIcon(new ImageIcon(img_logo));
		lblLogo.setBounds(42, 87, 147, 67);
		frame.getContentPane().add(lblLogo);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Login", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 255)));
		panel.setBounds(224, 77, 201, 110);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		txt_username = new JTextField();
		txt_username.setBounds(90, 23, 95, 20);
		panel.add(txt_username);
		txt_username.setColumns(10);
		
		txt_password = new JPasswordField();
		txt_password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					//
					//
					//
					//
				}
			}
		});
		txt_password.setBounds(90, 48, 95, 20);
		panel.add(txt_password);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(6, 23, 86, 14);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(6, 48, 86, 14);
		panel.add(lblNewLabel_1);
		
		JButton btnLogin = new JButton("Login");
		Image img_ok = new ImageIcon(this.getClass().getResource("/ok.png")).getImage();
		btnLogin.setIcon(new ImageIcon(img_ok));
		btnLogin.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				String sql = "select login,password from user where login=? and password=?";
				try{
					pst = conn.prepareStatement(sql);
					pst.setString(1, txt_username.getText().trim());
					pst.setString(2, txt_password.getText().trim());
					rs = pst.executeQuery();
					int count = 0;
					while (rs.next()) {
						count = count + 1;
					}
					if (count == 1) {
						frame.dispose();
						new MainWindowClass().setVisible(true);
					} else if (count > 1) {
						JOptionPane.showMessageDialog(null, "Dublicate user, access denied!");
					} else {
						JOptionPane.showMessageDialog(null, "Username or password is incorrect!");
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);
				} finally {
					try {
						rs.close();
						pst.close();
						conn.close();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e);
					}
				}
			}
		});
		btnLogin.setBounds(90, 79, 95, 23);
		panel.add(btnLogin);
	}

}
