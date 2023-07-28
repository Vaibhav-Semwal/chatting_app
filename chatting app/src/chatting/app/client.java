package chatting.app;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.util.*;
import java.text.*; 

public class client extends JFrame implements ActionListener{
	static JTextField typeBox; 
	JButton send; 
	static JPanel greypane;
	static Box vertical = Box.createVerticalBox();
	static DataOutputStream dout;
	private void textbox() { 
		typeBox = new JTextField();
		typeBox.setForeground(new Color(138, 138, 138));
		typeBox.setBounds(5,620,320,40);
		typeBox.setFont(new Font("Osward", Font.PLAIN, 18));
		add(typeBox);
		
		send = new JButton("Send");
		send.setBounds(330,620,100,40); 
		send.setBackground(new Color(191, 126, 29));		send.setForeground(Color.WHITE);
		send.addActionListener(this);
		add(send);
		
	}
	
	private void UI_icons() { 
		ImageIcon back1 = new ImageIcon(ClassLoader.getSystemResource("icons/back.png"));
		Image back2 = back1.getImage().getScaledInstance(20, 30, Image.SCALE_SMOOTH);
		ImageIcon back3 = new ImageIcon(back2);
		JLabel back = new JLabel(back3);	
		back.setBounds(-20, -15, 100, 100);
		add(back);
		back.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mouse) { 
				System.exit(0);
			}
		});
		
		ImageIcon phone1 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
		Image phone2 = phone1.getImage().getScaledInstance(25, 30, Image.SCALE_SMOOTH);
		ImageIcon phone3 = new ImageIcon(phone2);
		JLabel phone = new JLabel(phone3);
		phone.setBounds(260, -12, 100, 100);
		add(phone);
		
		ImageIcon video1 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
		Image video2 = video1.getImage().getScaledInstance(25, 30, Image.SCALE_SMOOTH);
		ImageIcon video3 = new ImageIcon(video2);
		JLabel video = new JLabel(video3);
		video.setBounds(310, -12, 100, 100);
		add(video);

		ImageIcon menu1 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
		Image menu2 = menu1.getImage().getScaledInstance(15, 30, Image.SCALE_SMOOTH);
		ImageIcon menu3 = new ImageIcon(menu2);
		JLabel menu = new JLabel(menu3);
		menu.setBounds(360, -15, 100, 100);
		add(menu);
	}
	
	private void userIcons() { 
		ImageIcon profile1 = new ImageIcon(ClassLoader.getSystemResource("icons/profile.png"));
		Image profile2 = profile1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon profile3 = new ImageIcon(profile2);
		JLabel profile = new JLabel(profile3);	
		profile.setBounds(40, -13, 100, 100);
		add(profile);
		
		JLabel name = new JLabel("Person - 2");
		name.setFont(new Font("Osward", Font.BOLD, 20));
		name.setForeground(Color.white);
		name.setBounds(120, 10, 200, 30);
		add(name);
		
		JLabel status = new JLabel("Online");
		status.setFont(new Font("Osward", Font.PLAIN, 16));
		status.setForeground(Color.white);
		status.setBounds(120, 32, 200, 30);
		add(status);
	}
	
	client() { 
		setLayout(null);
		setTitle("Chatting App");
		
		UI_icons(); 

		userIcons();
		
		textbox();
		
		greypane = new JPanel(); 
		greypane.setBackground(new Color(230,230,230));
		greypane.setBounds(5,75,440,540);
		add(greypane);
		
		JPanel greenpane = new JPanel(); 
		greenpane.setBackground(new Color(191, 126, 29));
		greenpane.setBounds(0,0,450,70);
		add(greenpane);
		
		getContentPane().setBackground(Color.WHITE);
		setSize(460,700);
		setVisible(true);
		setLocation(750,50);
	}
	
	public void actionPerformed(ActionEvent ae) { 
		try {
			String output = typeBox.getText();
			JPanel your_msg = formatLabel(output);
			
			greypane.setLayout(new BorderLayout());
			
			JPanel right = new JPanel(new BorderLayout());
			right.add(your_msg, BorderLayout.LINE_END);
			right.setBackground(new Color(230,230,230));
			vertical.add(right);
			vertical.add(Box.createVerticalStrut(15));
			greypane.add(vertical, BorderLayout.PAGE_START);
			
			dout.writeUTF(output);
			typeBox.setText("");
		
			greypane.repaint();
			greypane.invalidate();
			greypane.validate();				
		} catch (IOException e) {	
			e.printStackTrace();
		}
		
	}
	
	public static JPanel formatLabel(String input) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel , BoxLayout.Y_AXIS));
		
		JLabel output = new JLabel("<html><p style=\"width: 150px\">"+ input +"</p></html>");
		output.setFont(new Font("Tahoma", Font.PLAIN, 18));
		output.setBackground(new Color(255, 191, 94));
		output.setOpaque(true);
		output.setBorder(new EmptyBorder(10,10,10,25));
		panel.add(output);
		
		Calendar cal = Calendar.getInstance(); 
		SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
		
		JLabel time = new JLabel();
		time.setBackground(new Color(230,230,230));
		time.setText(sdf.format(cal.getTime())); 
		panel.add(time);
		
		return panel; 	
	}
	public static void main(String[] args) {
		new client(); 
		
		try (Socket s = new Socket("127.0.0.1", 6001)) {
			DataInputStream din = new DataInputStream(s.getInputStream());
			dout = new DataOutputStream(s.getOutputStream());
			while (true) { 	
				greypane.setLayout(new BorderLayout());
				String msg = din.readUTF();
				JPanel other_msg = formatLabel(msg);
					
				JPanel left = new JPanel(new BorderLayout());
				left.add(other_msg, BorderLayout.LINE_START);
				left.setBackground(new Color(230,230,230));
				vertical.add(left);
				
				vertical.add(Box.createVerticalStrut(15));
				greypane.add(vertical, BorderLayout.PAGE_START); 
				greypane.validate();
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
