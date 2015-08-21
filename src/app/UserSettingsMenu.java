package app;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UserSettingsMenu extends JFrame {

	private JPanel contentPane;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JTextField txtName;
	private JTextField txtSurName;
	private JTextField txtOperator;
	private JTextField txtPosition;
	private JLabel lblPassword;
	private JLabel lblName;
	private JLabel lblSurname;
	private JLabel lblOperatorName;
	private JLabel lblPositionName;
	private JTextField txtEmail;
	private JLabel lblEmail;
	
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	private JLabel lblRole;
	private JTextField txtRole;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserSettingsMenu frame = new UserSettingsMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void getUserinfo () {
		try {
			String sql = "select * from user where login=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, LoginFrame.txt_username.getText().trim());
			rs = pst.executeQuery();
			txtUserName.setText(LoginFrame.txt_username.getText().trim());
			txtPassword.setText(rs.getString("password"));
			txtName.setText(rs.getString("name"));
			txtSurName.setText(rs.getString("surname"));
			txtOperator.setText(rs.getString("operator"));
			txtPosition.setText(rs.getString("position"));
			txtEmail.setText(rs.getString("email"));
			txtRole.setText(rs.getString("role"));
		} catch (Exception err) {
			JOptionPane.showMessageDialog(null, err);			
		} finally {
			try {
				rs.close();
				pst.close();
			} catch (Exception err) {
				
			}
		}
	}

	/**
	 * Create the frame.
	 */
	public UserSettingsMenu() {
		conn = sqliteConnection.dbConnector();
		initialize();
		getUserinfo();
	}
	
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(300, 360);
		this.setResizable(false);
		this.setTitle("Print Form - User Settings");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w)/2;
		int y = (dim.height - h)/2;
		this.setLocation(x, y);
		getContentPane().setLayout(null);
		
		JButton btnSave = new JButton("Save / Update");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String sql = "update user set password=?,name=?,surname=?,operator=?,position=?,email=? where login=?";
					
					pst = conn.prepareStatement(sql);
					pst.setString(1, txtPassword.getText().trim());
					pst.setString(2, txtName.getText().trim());
					pst.setString(3, txtSurName.getText().trim());
					pst.setString(4, txtOperator.getText().trim());
					pst.setString(5, txtPosition.getText().trim());
					pst.setString(6, txtEmail.getText().trim());
					pst.setString(7, LoginFrame.txt_username.getText().trim());
					pst.execute();
					dispose();
				} catch (Exception err) {
					JOptionPane.showMessageDialog(null, err);			
				} finally {
					try {
						rs.close();
						pst.close();
					} catch (Exception err) {
						
					}
				}
			}
		});
		btnSave.setBounds(145, 298, 139, 23);
		getContentPane().add(btnSave);
		
		JLabel lblNewLabel = new JLabel("Own user settings");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(65, 12, 133, 14);
		getContentPane().add(lblNewLabel);
		
		txtUserName = new JTextField();
		txtUserName.setEditable(false);
		txtUserName.setBounds(135, 50, 86, 20);
		getContentPane().add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblUserName = new JLabel("UserName");
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUserName.setBounds(25, 53, 100, 14);
		getContentPane().add(lblUserName);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(135, 80, 86, 20);
		getContentPane().add(txtPassword);
		
		txtName = new JTextField();
		txtName.setColumns(10);
		txtName.setBounds(135, 111, 86, 20);
		getContentPane().add(txtName);
		
		txtSurName = new JTextField();
		txtSurName.setColumns(10);
		txtSurName.setBounds(135, 142, 86, 20);
		getContentPane().add(txtSurName);
		
		txtOperator = new JTextField();
		txtOperator.setColumns(10);
		txtOperator.setBounds(135, 173, 149, 20);
		getContentPane().add(txtOperator);
		
		txtPosition = new JTextField();
		txtPosition.setColumns(10);
		txtPosition.setBounds(135, 204, 149, 20);
		getContentPane().add(txtPosition);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPassword.setBounds(25, 86, 100, 14);
		getContentPane().add(lblPassword);
		
		lblName = new JLabel("Name");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblName.setBounds(25, 117, 100, 14);
		getContentPane().add(lblName);
		
		lblSurname = new JLabel("Surname");
		lblSurname.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSurname.setBounds(25, 148, 100, 14);
		getContentPane().add(lblSurname);
		
		lblOperatorName = new JLabel("Operator name");
		lblOperatorName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblOperatorName.setBounds(25, 179, 100, 14);
		getContentPane().add(lblOperatorName);
		
		lblPositionName = new JLabel("Position name");
		lblPositionName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPositionName.setBounds(25, 210, 100, 14);
		getContentPane().add(lblPositionName);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(135, 235, 149, 20);
		getContentPane().add(txtEmail);
		
		lblEmail = new JLabel("E-mail");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblEmail.setBounds(25, 241, 100, 14);
		getContentPane().add(lblEmail);
		
		lblRole = new JLabel("Role");
		lblRole.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRole.setBounds(25, 272, 100, 14);
		getContentPane().add(lblRole);
		
		txtRole = new JTextField();
		txtRole.setEditable(false);
		txtRole.setText((String) null);
		txtRole.setColumns(10);
		txtRole.setBounds(135, 266, 86, 20);
		getContentPane().add(txtRole);
	}
}
