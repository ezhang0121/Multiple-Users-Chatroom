package chatroom;

import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.text.*;
import java.io.IOException;

public class Terminal extends JFrame {
	private static final long serialVersionUID = 1L;
	public int port0 = 6004;
	public int port1 = 6000;
	public int port2 = 6002;

	private PicsJWindow picWindow;

	private JPanel p1 = new JPanel();
	private JPanel p2 = new JPanel();

	private JLabel jlbIP = new JLabel("My IP Address:  ");

	private JLabel jlbChatName = new JLabel("  Chat Name");
	private JTextField jtfChatName = new JTextField();

	private JScrollPane jsp1;
	private JScrollPane jsp2;

	private JTextPane jtp1 = new JTextPane();
	private JTextPane jtp2 = new JTextPane();
	StyledDocument doc = jtp1.getStyledDocument();

	private JButton jbtJoin = new JButton("Join");
	private JButton jbtSend = new JButton("Send");
	private JButton jbtEmoji;

	private DataOutputStream toServer;
	private DataOutputStream toServer1;
	private DataOutputStream toServer2;

	private DataInputStream fromServer;
	private String host = "192.168.1.138";

	Vector<Socket> clients = new Vector<Socket>(); //
	Vector<Socket> servers = new Vector<Socket>(); // one client connected to 2
													// server sockets.

	public static void main(String[] args) {

		new Terminal();
	}

	public void insertSendPic(ImageIcon imgIc) {
		// jtp2.setCaretPosition(docChat.getLength()); // 设置插入位置
		jtp2.insertIcon(imgIc); // 插入图片
		System.out.print(imgIc.toString());

	}

	public JButton getPicBtn() {
		// return b_pic;
		return jbtJoin;
	}

	public Terminal() {
		setLayout(new BorderLayout(5, 5));
		add(jlbIP, BorderLayout.NORTH);
		jlbIP.setOpaque(true);
		jlbIP.setBackground(new Color(233, 131, 0));
		add(p1, BorderLayout.CENTER);
		add(p2, BorderLayout.EAST);
		p1.setLayout(new GridLayout(2, 1, 5, 20));

		picWindow = new PicsJWindow(this);

		addStylesToDocument(doc);

		p1.setMinimumSize(new Dimension(600, 600));
		p1.setPreferredSize(new Dimension(600, 600));
		p1.setMaximumSize(new Dimension(600, 600));

		jsp1 = new JScrollPane(jtp1,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		jsp2 = new JScrollPane(jtp2,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		p1.add(jsp1);
		// p1.add(jlbGap);
		p1.add(jsp2);

		jtp1.setMinimumSize(new Dimension(600, 200));
		jtp1.setPreferredSize(new Dimension(600, 200));
		jtp1.setMaximumSize(new Dimension(600, 200));
		p1.setBackground(new Color(233, 131, 0));

		jtp2.setMinimumSize(new Dimension(600, 200));
		jtp2.setPreferredSize(new Dimension(600, 200));
		jtp2.setMaximumSize(new Dimension(600, 200));

		/*
		 * Enumeration<NetworkInterface> e =
		 * NetworkInterface.getNetworkInterfaces(); while(e.hasMoreElements()){
		 * System.out.println(e.nextElement()); jlbIP.setText("My IP Address: "
		 * + e.nextElement); }
		 */

		jlbIP.setMinimumSize(new Dimension(600, 20));
		jlbIP.setPreferredSize(new Dimension(600, 20));
		jlbIP.setMaximumSize(new Dimension(600, 20));

		jlbChatName.setMinimumSize(new Dimension(200, 35));
		jlbChatName.setPreferredSize(new Dimension(200, 35));
		jlbChatName.setMaximumSize(new Dimension(200, 35));
		jlbChatName.setAlignmentX(Component.CENTER_ALIGNMENT);

		p2.setLayout(new BorderLayout());
		p2.setBackground(new Color(233, 131, 0));

		jtfChatName.setMinimumSize(new Dimension(150, 35));
		jtfChatName.setPreferredSize(new Dimension(150, 35));
		jtfChatName.setMaximumSize(new Dimension(150, 35));
		jtfChatName.setAlignmentX(Component.CENTER_ALIGNMENT);
		jtfChatName.setText(Integer.toString(port0));

		jbtJoin.setMinimumSize(new Dimension(100, 35));
		jbtJoin.setPreferredSize(new Dimension(100, 35));
		jbtJoin.setMaximumSize(new Dimension(100, 35));
		jbtJoin.setAlignmentX(Component.CENTER_ALIGNMENT);
		jbtSend.setAlignmentX(Component.CENTER_ALIGNMENT);

		jbtSend.setMinimumSize(new Dimension(100, 35));
		jbtSend.setPreferredSize(new Dimension(100, 35));
		jbtSend.setMaximumSize(new Dimension(100, 35));

		jbtEmoji = new JButton(new ImageIcon(
				"C:/Users/SHUXU/chatroom/emoji.png"));
		jbtEmoji.setMaximumSize(new Dimension(100, 35));
		jbtEmoji.setAlignmentX(Component.CENTER_ALIGNMENT);

		// press Join to connect to server
		jbtJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					Socket socket = new Socket(host, port1);
					// output stream to send message to server
					toServer1 = new DataOutputStream(socket.getOutputStream());

					socket = new Socket(host, port2);
					toServer2 = new DataOutputStream(socket.getOutputStream());

				}

				catch (IOException ex) {
					System.out.println(ex);
				}
			}

		});

		// press Send to send jtp2 text
		jbtSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Date today = Calendar.getInstance().getTime();
					String reportDate = df.format(today);
					String s = jtfChatName.getText() + ":  " + reportDate
							+ "\n" + jtp2.getText() + "\n"; // string to send

					// display it locally
					try {
						doc.insertString(doc.getLength(), s,
								doc.getStyle("red"));
					} catch (BadLocationException ble) {
						System.err
								.println("Couldn't insert initial text into text pane.");
					}

					toServer1.writeUTF(s);
					toServer1.flush();
					toServer2.writeUTF(s);
					toServer2.flush();
				}

				catch (IOException ex) {
					System.out.println(ex);
				}

				jtp2.setText("");

			}

		});
		jbtEmoji.addActionListener(new ActionListener() {
			int count = 0;

			public void actionPerformed(ActionEvent e) {
				count++;
				if (count % 2 == 1) {
					picWindow.setVisible(true);
				} else {
					picWindow.setVisible(false);
				}
			}
		});

		Box box1 = Box.createVerticalBox();
		box1.add(jlbChatName);
		box1.add(jtfChatName);
		// box1.add(jlbPhoto);
		box1.setPreferredSize(new Dimension(200, 300));

		Box box2 = Box.createVerticalBox();
		box2.add(jbtJoin);
		box2.add(jbtEmoji);
		box2.add(jbtSend);
		box2.setPreferredSize(new Dimension(200, 150));

		p2.add(box1, BorderLayout.NORTH);
		p2.add(box2, BorderLayout.SOUTH);

		this.setBackground(new Color(233, 131, 0));
		setTitle("Chat Room");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		try {
			// as a server, listen to 2 clients
			// port of this terminal is 8000
			ServerSocket serverSocket = new ServerSocket(port0);

			// keep listening
			while (true) {

				Socket socket = serverSocket.accept(); // listen to client
				InetAddress ip = socket.getInetAddress();

				jlbIP.setText("My IP Address: " + ip.getHostAddress());

				HandleAClient task = new HandleAClient(socket);
				clients.add(socket);
				new Thread(task).start();
			}

		}// try

		catch (IOException ex) {
			jtp1.setText(ex.toString() + '\n');
		}

	}

	// add style
	protected void addStylesToDocument(StyledDocument doc) {
		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);
		StyleConstants.setFontSize(s, 16);

		s = doc.addStyle("red", regular);
		StyleConstants.setForeground(s, Color.red);
		StyleConstants.setFontSize(s, 16);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = doc.addStyle("blue", regular);
		StyleConstants.setForeground(s, Color.blue);
		StyleConstants.setFontSize(s, 16);

		s = doc.addStyle("large", regular);
		StyleConstants.setFontSize(s, 16);

	}

	class HandleAClient implements Runnable {
		private Socket socket;
		private DataInputStream fromClient;

		// private DataOutputStream toClient;

		public HandleAClient(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				fromClient = new DataInputStream(socket.getInputStream());
				while (true) {
					String buffer = fromClient.readUTF(); // read and display
															// only, display
															// buffer.

					try {
						doc.insertString(doc.getLength(), buffer,
								doc.getStyle("blue"));
					} catch (BadLocationException ble) {
						System.err
								.println("Couldn't insert initial text into text pane.");
					}

				}
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}

	}

}

