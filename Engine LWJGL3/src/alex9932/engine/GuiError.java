package alex9932.engine;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import alex9932.script.FileIO;

import javax.swing.ImageIcon;
import javax.swing.JTextPane;

public class GuiError extends JFrame {
	private static final long serialVersionUID = 2050240814470613218L;
	private Button button;

	public GuiError(Exception e) {
		super();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {}
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(1);
			}
		});
		getContentPane().setBackground(Color.DARK_GRAY);
		setBackground(Color.BLACK);
		this.setTitle("JFoxengine");
		this.setSize(550, 205);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		button = new Button("Close");
		button.setBounds(464, 145, 70, 22);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVisible(false);
		scrollPane.setBounds(10, 54, 524, 185);
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		textArea.setCaretColor(Color.WHITE);
		textArea.setSelectionColor(Color.LIGHT_GRAY);
		textArea.setForeground(Color.WHITE);
		textArea.setBackground(Color.BLACK);
		JLabel lblFosengineHasCrashed = new JLabel("Foxengine has crashed!");
		lblFosengineHasCrashed.setBounds(52, 11, 482, 32);
		lblFosengineHasCrashed.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblFosengineHasCrashed.setForeground(Color.WHITE);
		getContentPane().setLayout(null);
		
		this.getContentPane().add(button);
		this.getContentPane().add(scrollPane);
		this.getContentPane().add(lblFosengineHasCrashed);
		
		Button button_1 = new Button("Create log");
		button_1.setBounds(10, 145, 70, 22);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH mm ss");
				Date date = new Date();
				new File("./reports").mkdir();
				String string = "~~~ C R A S H   R E P O R T ~~~\n";
				string = string + "----SYSTEM----\n";
				string = string + "OS name: " + System.getProperty("os.name") + "\n";
				string = string + "OS version: " + System.getProperty("os.version") + "\n";
				string = string + "OS arch: " + System.getProperty("os.arch") + "\n";
				string = string + "Java version: " + System.getProperty("java.version") + "\n";
				string = string + "Java home dir: " + System.getProperty("java.home") + "\n";
				string = string + "Classpath: " + System.getProperty("java.class.path") + "\n\n";
				string = string + "----STACKTRACE----\n";
				string = string + textArea.getText() + "\n";
				string = string + "~~~ E N D   O F   R E P O R T ~~~";
				try {
					FileIO.write("./reports/report " + dateFormat.format(date) + ".txt", string);
				} catch (Exception e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Report " + "./reports/report " + dateFormat.format(date) + ".txt successfully created!");
			}
		});
		button_1.setForeground(Color.WHITE);
		button_1.setBackground(Color.BLACK);
		getContentPane().add(button_1);
		
		Button button_2 = new Button("More...");
		button_2.setBounds(388, 145, 70, 22);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setSize(550, 305);
				button.setBounds(464, 245, 70, 22);
				button_1.setBounds(10, 245, 70, 22);
				button_2.setBounds(388, 545, 70, 22);
				scrollPane.setVisible(true);
			}
		});
		button_2.setForeground(Color.WHITE);
		button_2.setBackground(Color.BLACK);
		getContentPane().add(button_2);
		
		JLabel label = new JLabel("");
		label.setBounds(10, 11, 38, 32);
		label.setIcon(new ImageIcon(GuiError.class.getResource("/alex9932/engine/util/crash.png")));
		getContentPane().add(label);
		
		JTextPane txtpn = new JTextPane();
		txtpn.setText("This is Foxengine has crashed. To help development process, pleace\r\nsubmit bug or save report and email it manually (button More...).\r\nMany thanks in advance and sorry for inconvenence.");
		txtpn.setEditable(false);
		txtpn.setForeground(Color.WHITE);
		txtpn.setBackground(Color.BLACK);
		txtpn.setBounds(10, 54, 524, 85);
		getContentPane().add(txtpn);

		StackTraceElement[] elems = e.getStackTrace();
		textArea.append(e + "\n");
		for (int i = 0; i < elems.length; i++) {
			textArea.append("    " + elems[i] + "\n");
		}
		textArea.append("  " + elems.length + " more...");
		
		this.setVisible(true);
	}
}