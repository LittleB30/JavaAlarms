/**
 * This is where all the GUI development can be found.
 */

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class AlarmsGUI extends JFrame{
	private String[] selection;
	private JPanel mainPanel;
	private JPanel dialogPanel1;
	private JPanel dialogPanel11;
	private JPanel dialogPanel12;
	private JPanel panel1;
	private JButton newAlarm;
	private JButton newAlarm2;
	private JButton deleteAlarm;
	private JButton reset;
	private JDialog newAlarmPrompt;
	/*private JComboBox<String> day1;
	private JComboBox<String> day2;
	private JComboBox<String> day3;*/
	private JComboBox<String> day4;
	private JComboBox<String> month;
	private JComboBox<String> minute;
	private JComboBox<String> hour;
	private JComboBox<String> amPm;
	private MyListener buttonListener = new MyListener();
	private JLabel description = null;
	private JDialog alarmTriggerd = null;
	private JPanel alarmPanel = null;
	private JLabel alarmDescription = null;
	private JTextField labelField = new JTextField(30);
	private JButton snooze = null;
	private JButton ok = null;
	
	public void createAndShowGUI() {
        fillComboBoxes();
        setTitle("Alarms");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);
        labelField.setText("An alarm has gone off");
        createGUI();
    }

	private void createGUI() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(4,0));
			panel1 = new JPanel();
			panel1.setLayout(new GridLayout(0,2));
				newAlarm = new JButton("New Alarm");
				newAlarm.setName("New Alarm");
				newAlarm.addActionListener(buttonListener);
			panel1.add(newAlarm);
				deleteAlarm = new JButton("Delete");
				deleteAlarm.setName("Delete Alarm");
				deleteAlarm.addActionListener(buttonListener);
			panel1.add(deleteAlarm);
		mainPanel.add(panel1);
		
		add(mainPanel);
	}
	
	public class MyListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton)e.getSource();
			String sourceName = source.getName();
			int j = 0;
			switch (sourceName) {
				case "New Alarm":
					newAlarm();
					break;
				case "Delete Alarm":
					try {
						Alarms.deleteAlarm();
					} catch (ParserConfigurationException e1) {
						e1.printStackTrace();
					} catch (SAXException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				case "Create New Alarm":
					selection = new String[]{(String) month.getSelectedItem(), (String) day4.getSelectedItem(), 
											 (String) hour.getSelectedItem(),  (String) minute.getSelectedItem(),
											 (String) amPm.getSelectedItem(), (String)labelField.getText()};
					for (String i: selection) {
						if (i.equals("Month") || i.equals("Day") || i.equals("Hour") || i.equals("Minute")) {
							JOptionPane.showMessageDialog(source.getParent(), "Pick a non-string value", "Invalid Input", JOptionPane.ERROR_MESSAGE);
							j++;
							break;
						}
					}
					if (j == 1) {
						break;
					}
					Alarms.createNewAlarm(selection);
					break;
				case "Reset":
					reset();
					break;
				case "snooze":
					alarmTriggerd.dispose();
					break;
				case "ok":
					alarmTriggerd.dispose();
					break;
			}
		}
	}

	private void newAlarm() {
		newAlarmPrompt = new JDialog(this, "Create New Alarm");
		newAlarmPrompt.setVisible(true);
		newAlarmPrompt.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		newAlarmPrompt.setSize(400, 225);
		newAlarmPrompt.setVisible(true);
			dialogPanel1 = new JPanel(new GridLayout(2,0));
				dialogPanel11 = new JPanel(new GridLayout(0,2));
					newAlarm2 = new JButton("Create");
					newAlarm2.setName("Create New Alarm");
					newAlarm2.addActionListener(buttonListener);
				dialogPanel11.add(newAlarm2);
					reset = new JButton("Reset");
					reset.setName("Reset");
					reset.addActionListener(buttonListener);
				dialogPanel11.add(reset);
			dialogPanel1.add(dialogPanel11);
				dialogPanel12 = new JPanel();
				description = new JLabel("Message for alarm: ");
				dialogPanel12.add(description);
				dialogPanel12.add(labelField);
				dialogPanel12.add(month);
				dialogPanel12.add(day4);
				dialogPanel12.add(hour);
				dialogPanel12.add(minute);
				dialogPanel12.add(amPm);
			dialogPanel1.add(dialogPanel12);
		newAlarmPrompt.add(dialogPanel1);
	}
	
	private void reset() {
		
	}
	
	protected void runTimerTask() {
		String alarmLabel = labelField.getText();
		alarmDescription = new JLabel(alarmLabel);
		alarmPanel = new JPanel();
		snooze = new JButton("Snooze");
		ok = new JButton("Ok");
		snooze.setName("snooze");
		ok.setName("ok");
		snooze.addActionListener(buttonListener);
		ok.addActionListener(buttonListener);
		alarmTriggerd = new JDialog(this,"Alarm");
		alarmTriggerd.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		alarmTriggerd.setSize(300,300);
		alarmTriggerd.setVisible(true);
		alarmPanel.add(alarmDescription);
		alarmPanel.add(snooze);
		alarmPanel.add(ok);
		alarmTriggerd.add(alarmPanel);
	}
	
	private void fillComboBoxes() {
		/*String[] dayArr1 = new String[29];
		String[] dayArr2 = new String[30];
		String[] dayArr3 = new String[31];*/
		String[] dayArr4 = new String[32];
		String[] minuteArr = new String[61];
		String[] hourArr = new String[13];
		/*dayArr1[0] = "Day";
		dayArr2[0] = "Day";
		dayArr3[0] = "Day";*/
		dayArr4[0] = "Day";
		minuteArr[0] = "Minute";
		hourArr[0] = "Hour";
		
		month = new JComboBox<String>(new String[]{"Month","Jan","Feb","Mar","Apr","May","Jun",
												   "Jul","Aug","Sep","Oct","Nov","Dec"});
		amPm = new JComboBox<String>(new String[]{"am","pm"});
		
		for (Integer i = new Integer(1); i <= 60; i++) {
			/*if (i <= 28) {
				dayArr1[i] = i.toString();
			}
			if (i <= 29) {
				dayArr2[i] = i.toString();
			}
			if (i <= 30) {
				dayArr3[i] = i.toString();
			}*/
			if (i <= 31) {
				dayArr4[i] = i.toString();
			}
			if (i <= 60) {
				minuteArr[i] = i.toString();
			}
			if (i <= 12) {
				hourArr[i] = i.toString();
			}
		}
		/*day1 = new JComboBox<String>(dayArr1);
		day2 = new JComboBox<String>(dayArr2);
		day3 = new JComboBox<String>(dayArr3);*/
		day4 = new JComboBox<String>(dayArr4);
		minute = new JComboBox<String>(minuteArr);
		hour = new JComboBox<String>(hourArr);
	}
}
