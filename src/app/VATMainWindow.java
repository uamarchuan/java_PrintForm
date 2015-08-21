package app;

import java.awt.*;

import javax.swing.*;

import net.proteanit.sql.DbUtils;

import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.*;
import java.text.*;
import java.util.*;

import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class VATMainWindow extends JFrame {

	private JPanel contentPane;
	private JLabel lblUserNameBar;
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pst = null;
	private JTable tbVATlist;
	private JTextField tfSearch;
	private JTextField txtVATNumber;
	private JTextField txtContractDate;
	private JTextField txtContractName;
	private JTextField tLink;
	private JComboBox cbSeller;
	private JTextField txtOperator;
	private JPanel pnlList;
	private JPanel pnlCreate;
	private JTextField txtSelectVat;
	private Image imgPDF;
	private JScrollPane scrollPanePDF;
	
	
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
	
	private void UpdateTable () { 
		try {
			String sql = "select vat_number as 'VAT Number',vat_seller as 'Seller',vat_adddate as 'Date', vat_operator as 'Billing Clark' from tb_vat_header where vat_username=? ORDER BY vat_adddate DESC";
			pst = conn.prepareStatement(sql);
			pst.setString(1, LoginFrame.txt_username.getText().trim());
			rs = pst.executeQuery();
			tbVATlist.setModel(DbUtils.resultSetToTableModel(rs));
			
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
	
	private void Update_cbSeller () {
		try {
			String sql = "select sel_name from tb_seller";
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			cbSeller.removeAllItems();
			while (rs.next()) {
				String name = rs.getString("sel_name");
				cbSeller.addItem(name);
			}
			
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
	
	private void Update_txtOperator () {
		try {
			String sql = "select operator from user where login=?";
			pst = conn.prepareStatement(sql);
			pst.setString(1, LoginFrame.txt_username.getText().trim());
			rs = pst.executeQuery();
			String operator = rs.getString("operator");
			txtOperator.setText(operator);
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
	
	private void clean_fields_vat_create () {
		txtVATNumber.setText(null);
		txtContractDate.setText(null);
		txtContractName.setText(null);
		tLink.setText(null);
		Update_cbSeller();
		Update_txtOperator();
	}
	
	private void preViewPDF () {
		try{
			
	        String INPUTFILE = "example_2.pdf";
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
            ImageIcon imagePDFicon = new ImageIcon(imgPDF);
			scrollPanePDF.setViewportView(new JLabel(imagePDFicon));
		
        }
        catch (Exception err) {
            System.out.println(err);
        }
	}
	/**
	 * Create the frame.
	 */
	public VATMainWindow() {
		conn = sqliteConnection.dbConnector();
		initialize();
		GetUser();
		UpdateTable();
		clean_fields_vat_create();
	}
	
	private void initialize() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setTitle("Print Form - VAT Invoice");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = this.getSize().width;
		int h = this.getSize().height;
		int x = (dim.width - w)/2;
		int y = (dim.height - h)/2;
		this.setLocation(x, y);
		setVisible(true);
		
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
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 800, 30);
		toolBar.setFloatable(false);
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton lblBack = new JButton("Back");
		lblBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				new MainWindowClass().setVisible(true);
			}
		});
		Image img_back = new ImageIcon(this.getClass().getResource("/back.png")).getImage();
		lblBack.setIcon(new ImageIcon(img_back));
		toolBar.add(lblBack);
		
		//body
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 35, 785, 511);
		getContentPane().add(tabbedPane);
		
		pnlList = new JPanel();
		tabbedPane.addTab("VAT List", null, pnlList, null);
		pnlList.setLayout(null);
		
		JLabel lblSearch = new JLabel("Search");
		lblSearch.setBounds(629, 8, 50, 14);
		pnlList.add(lblSearch);
		
		tfSearch = new JTextField();
		tfSearch.setToolTipText("Search by column 'VAT Number'");
		tfSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				int charlen = tfSearch.getText().trim().length();
				if (charlen == 0) {
					UpdateTable();
				} else if (charlen > 0) {
					try {
						String sql = "select vat_number as 'VAT Number',vat_seller as 'Seller',vat_adddate as 'Date', vat_operator as 'Billing Clark' from tb_vat_header where vat_username=? and vat_number=?  ORDER BY vat_adddate DESC";
						pst = conn.prepareStatement(sql);
						pst.setString(1, LoginFrame.txt_username.getText().trim());
						pst.setString(2, tfSearch.getText().trim());
						rs = pst.executeQuery();
						tbVATlist.setModel(DbUtils.resultSetToTableModel(rs));
						
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
			}
		});
		tfSearch.setBounds(680, 5, 100, 20);
		pnlList.add(tfSearch);
		tfSearch.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 31, 780, 452);
		pnlList.add(scrollPane);
		
		tbVATlist = new JTable();
		tbVATlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					int row = tbVATlist.getSelectedRow();
					String table_val = (tbVATlist.getModel().getValueAt(row, 0).toString());
					txtSelectVat.setText(table_val);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);			
				}
			
			}
		});
		scrollPane.setViewportView(tbVATlist);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.setToolTipText("Select row for print!");
		btnPrint.setBounds(181, 4, 100, 23);
		pnlList.add(btnPrint);
		
		JButton btnRefreshTable = new JButton("Refresh");
		btnRefreshTable.setBounds(287, 4, 100, 23);
		pnlList.add(btnRefreshTable);
		
		JButton btnDeleteItem = new JButton("Delete Item");
		btnDeleteItem.setToolTipText("Select row for delete!");
		btnDeleteItem.setBounds(501, 4, 116, 23);
		pnlList.add(btnDeleteItem);
		
		JButton btnDeleteAll = new JButton("Delete All");
		btnDeleteAll.setBounds(393, 4, 102, 23);
		pnlList.add(btnDeleteAll);
		
		JLabel lblCurrent = new JLabel("Selected Doc.");
		lblCurrent.setFont(new Font("Tahoma", Font.ITALIC, 12));
		lblCurrent.setBounds(0, 7, 87, 14);
		pnlList.add(lblCurrent);
		
		txtSelectVat = new JTextField();
		txtSelectVat.setEditable(false);
		txtSelectVat.setToolTipText("Search by column 'VAT Number'");
		txtSelectVat.setColumns(10);
		txtSelectVat.setBounds(87, 6, 60, 20);
		pnlList.add(txtSelectVat);
		btnDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int action = JOptionPane.showConfirmDialog(null, "Do you realy want to delete all records?","Delete all",JOptionPane.YES_NO_OPTION);
				if (action == 0) {
					try {
						String sql = "delete from tb_vat_header where vat_username=?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, LoginFrame.txt_username.getText().trim());
						pst.execute();
					} catch (Exception err) {
						JOptionPane.showMessageDialog(null, err);			
					} finally {
						try {
							pst.close();
						} catch (Exception err) {
							
						}
					}
					
					try {
						String sql = "delete from tb_vat_body where vat_loginuser=?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, LoginFrame.txt_username.getText().trim());
						pst.execute();
					} catch (Exception err) {
						JOptionPane.showMessageDialog(null, err);			
					} finally {
						try {
							pst.close();
						} catch (Exception err) {
							
						}
					}
				}
				UpdateTable();

			}
		});
		btnDeleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int action = JOptionPane.showConfirmDialog(null, "Do you realy want to delete this item?","Delete item",JOptionPane.YES_NO_OPTION);
				if (action == 0) {
					try {
						String sql = "delete from tb_vat_header where vat_number=?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, txtSelectVat.getText().trim());
						pst.execute();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e);			
					} finally {
						try {
							rs.close();
							pst.close();
						} catch (Exception e) {
						}
					}
					
					try {
						String sql = "delete from tb_vat_body where vat_number=?";
						pst = conn.prepareStatement(sql);
						pst.setString(1, txtSelectVat.getText().trim());
						pst.execute();
						txtSelectVat.setText(null);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, e);			
					} finally {
						try {
							rs.close();
							pst.close();
						} catch (Exception e) {
							
						}
					}
					UpdateTable();
					}
			}
		});
		btnRefreshTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateTable();
			}
		});
		
		pnlCreate = new JPanel();
		tabbedPane.addTab("Create VAT", null, pnlCreate, null);
		pnlCreate.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Please fill in all fields to create VAT invoice!");
		lblNewLabel.setFont(new Font("Lucida Sans", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel.setBounds(245, 28, 411, 33);
		pnlCreate.add(lblNewLabel);
		
		txtVATNumber = new JTextField();
		txtVATNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c = arg0.getKeyChar();
				if (!(Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)) || txtVATNumber.getText().length()>=7) {
					getToolkit().beep();
					arg0.consume();
				}
			}
		});
		txtVATNumber.setBounds(452, 98, 150, 25);
		pnlCreate.add(txtVATNumber);
		txtVATNumber.setColumns(10);
		
		JLabel lblVatInvoiceNumber = new JLabel("VAT Invoice number");
		lblVatInvoiceNumber.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		lblVatInvoiceNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVatInvoiceNumber.setBounds(272, 100, 162, 15);
		pnlCreate.add(lblVatInvoiceNumber);
		
		cbSeller = new JComboBox();
		cbSeller.setBounds(452, 130, 150, 25);
		pnlCreate.add(cbSeller);
		
		txtContractDate = new JTextField();
		txtContractDate.setToolTipText("Date in format \"ddmmyyyy\"!");
		txtContractDate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isDigit(c) || (c==KeyEvent.VK_BACK_SPACE) || (c==KeyEvent.VK_DELETE)) || txtContractDate.getText().length()>=8) {
					getToolkit().beep();
					e.consume();
				}
			}
		});
		txtContractDate.setColumns(10);
		txtContractDate.setBounds(452, 167, 150, 25);
		pnlCreate.add(txtContractDate);
		
		txtContractName = new JTextField();
		txtContractName.setColumns(10);
		txtContractName.setBounds(452, 204, 150, 25);
		pnlCreate.add(txtContractName);
		
		txtOperator = new JTextField();
		txtOperator.setBounds(452, 241, 150, 25);
		pnlCreate.add(txtOperator);
		
		JLabel lblCompanyName = new JLabel("Company name");
		lblCompanyName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCompanyName.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		lblCompanyName.setBounds(272, 135, 162, 15);
		pnlCreate.add(lblCompanyName);
		
		JLabel lblDateOfCivil = new JLabel("Date of civil contract");
		lblDateOfCivil.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDateOfCivil.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		lblDateOfCivil.setBounds(272, 172, 162, 15);
		pnlCreate.add(lblDateOfCivil);
		
		JLabel lblNameOfCivil = new JLabel("Name of civil contract");
		lblNameOfCivil.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNameOfCivil.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		lblNameOfCivil.setBounds(272, 209, 162, 15);
		pnlCreate.add(lblNameOfCivil);
		
		JLabel lbOperator = new JLabel("Billing Clark name");
		lbOperator.setHorizontalAlignment(SwingConstants.RIGHT);
		lbOperator.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 12));
		lbOperator.setBounds(272, 246, 162, 15);
		pnlCreate.add(lbOperator);
		
		JButton btnAttachFile = new JButton("Choose");
		btnAttachFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Only new Excel file 'xlsx'","xlsx");
				chooser.addChoosableFileFilter(filter);
				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				String fileurl = file.getAbsolutePath();
				tLink.setText(fileurl);
			}
		});
		btnAttachFile.setBounds(452, 307, 150, 25);
		pnlCreate.add(btnAttachFile);
		
		tLink = new JTextField();
		tLink.setBounds(114, 307, 320, 25);
		pnlCreate.add(tLink);
		tLink.setColumns(10);
		
		JButton btnUploud = new JButton("Uploud");
		btnUploud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String sql = "INSERT INTO `tb_vat_header`(`vat_adddate`,`vat_number`,`vat_operator`,`vat_seller`,`vat_contract_number`,`vat_contract_date`,`vat_username`) VALUES (?,?,?,?,?,?,?);";
					
					pst = conn.prepareStatement(sql);
				    java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
				    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					pst.setString(1, dateFormat.format(sqlDate));
					pst.setString(2, txtVATNumber.getText().trim());
					pst.setString(3, txtOperator.getText().trim());
					pst.setString(4, cbSeller.getSelectedItem().toString());
					pst.setString(5, txtContractName.getText().trim());
					pst.setString(6, txtContractDate.getText().trim());
					pst.setString(7, LoginFrame.txt_username.getText().trim());
					pst.execute();
					

					
					
//					pnlCreate.setVisible(false);
//					pnlList.setVisible(true);
//					pnlList.repaint();
//					pnlList.revalidate();
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e);			
				} finally {
					try {
						rs.close();
						pst.close();					
					} catch (Exception e) {
						
					}
				}
				
				try	{
					//		FileInputStream file = new FileInputStream(new File("/media/files/projects/java/PrintForm/invoice.xlsx"));
							FileInputStream file = new FileInputStream(new File(tLink.getText()));
							//Create Workbook instance holding reference to .xlsx file
							XSSFWorkbook workbook = new XSSFWorkbook(file);
							//Get first/desired sheet from the workbook
							XSSFSheet sheet = workbook.getSheetAt(0);

							int rowNum = sheet.getLastRowNum() + 1;
							
					        for(int i = 10; i <rowNum; i++){
					            XSSFRow row = sheet.getRow(i);
					            	            
					            String vat_invoice = row.getCell(1).getRawValue().trim();
					            String vat_item = row.getCell(3).getRawValue().trim();
					            String vat_created_by = row.getCell(5).getStringCellValue().trim();
					            String vat_bil_type = row.getCell(6).getStringCellValue().trim();
					            String vat_net_value = null;
				                switch (row.getCell(7).getCellType())
				                {
				                    case Cell.CELL_TYPE_NUMERIC:
				                    	 vat_net_value = String.valueOf(row.getCell(7).getRawValue()).trim();
				                        break;
				                    case Cell.CELL_TYPE_STRING:
				                    	 vat_net_value = row.getCell(7).getStringCellValue().toString().trim();
				                    	 vat_net_value = vat_net_value.replace(".", "");
				                    	 vat_net_value = vat_net_value.replace(",", ".");
				                        break;
				                }
					            String vat_curr = row.getCell(8).getStringCellValue().trim();
					            String vat_sorg = row.getCell(9).getStringCellValue().trim();
					            String vat_ch = row.getCell(10).getRawValue().trim();
					            String vat_payer = row.getCell(14).getRawValue().trim();
					            String vat_payer_name = row.getCell(15).getStringCellValue().trim();
					            String vat_ship_to = row.getCell(16).getRawValue().trim();
					            String vat_material = row.getCell(17).getRawValue().trim();
					            String vat_description = row.getCell(18).getStringCellValue().trim();
					            String vat_qty = row.getCell(22).getRawValue().trim();
					            String vat_su = row.getCell(23).getStringCellValue().trim();
					            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
					            String vat_bill_date = sdf.format(row.getCell(27).getDateCellValue());
				            
					            
								String sql = "INSERT INTO `tb_vat_body`(`vat_number`,`vat_invoice`,`vat_item`,`vat_created_by`,`vat_bil_type`,`vat_net_value`,`vat_curr`,`vat_sorg`,"
										+ "`vat_ch`,`vat_payer`,`vat_payer_name`,`vat_ship_to`,`vat_material`,`vat_description`,`vat_qty`,`vat_su`,`vat_bill_date`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
								
								pst = conn.prepareStatement(sql);
								pst.setString(1, txtVATNumber.getText().trim());
								pst.setString(2, vat_invoice);
								pst.setString(3, vat_item);
								pst.setString(4, vat_created_by);
								pst.setString(5, vat_bil_type);
								pst.setString(6, vat_net_value);
								pst.setString(7, vat_curr);
								pst.setString(8, vat_sorg);
								pst.setString(9, vat_ch);
								pst.setString(10, vat_payer);
								pst.setString(11, vat_payer_name);
								pst.setString(12, vat_ship_to);
								pst.setString(13, vat_material);
								pst.setString(14, vat_description);
								pst.setString(15, vat_qty);
								pst.setString(16, vat_su);
								pst.setString(17, vat_bill_date);
								
								pst.execute();
					        }
					        
							file.close();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, e);	
						} finally {
							try {
								pst.close();
//								conn.close();
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, e);	
							}
						}			
				
				
				clean_fields_vat_create();
				UpdateTable();
			}

		});
		btnUploud.setBounds(547, 394, 200, 50);
		pnlCreate.add(btnUploud);
		
		JPanel pnlPreview = new JPanel();
		tabbedPane.addTab("Print Preview", null, pnlPreview, null);
		pnlPreview.setLayout(null);
		
		scrollPanePDF = new JScrollPane();
		scrollPanePDF.setBounds(2, 2, 682, 482);
		pnlPreview.add(scrollPanePDF);
		
		JButton btnRefreshPDFPrev = new JButton("Refresh");
		btnRefreshPDFPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preViewPDF();
			}
		});
		btnRefreshPDFPrev.setBounds(686, 63, 90, 23);
		pnlPreview.add(btnRefreshPDFPrev);
		
		JButton btnPrintPDF = new JButton("Print");
		btnPrintPDF.setBounds(686, 113, 90, 23);
		pnlPreview.add(btnPrintPDF);
		
		JButton btnSaveAsPDF = new JButton("Save As");
		btnSaveAsPDF.setBounds(686, 143, 90, 23);
		pnlPreview.add(btnSaveAsPDF);
		
		JButton btnCreateVAT = new JButton("Create");
		btnCreateVAT.setBounds(686, 12, 90, 40);
		pnlPreview.add(btnCreateVAT);
		
		JPanel pnlXML = new JPanel();
		tabbedPane.addTab("XML", null, pnlXML, null);
		pnlXML.setLayout(null);
	}
}
