/**
 * This is where all the logic behind making and managing alarms can be found.
 */


import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Alarms extends TimerTask{
	public static final String xmlFilePath = System.getProperty("user.dir") + "\\src\\Alarm.xml";
	private static TransformerFactory transformerFactory;
	private static DOMSource domSource;
	private static StreamResult streamResult;
	private static Transformer transformer;
	private static DocumentBuilderFactory documentFactory;
	private static DocumentBuilder documentBuilder;
	private static Document document;
	private static AlarmsGUI gui;
	private static Timer timer;
	private static Date date;
	private static Calendar calendar;
	private static Alarms alarm;
	private static Element root;
	private static boolean reading = false;
	private static boolean createOnRead = true;
	
	@Override
	public void run() {
		gui.runTimerTask();
		try {
			deleteAlarm();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void createNewAlarm(String[] selection){
		int monthInt = 0;
		int j = 0;
		date = new Date();
		timer = new Timer();
		alarm = new Alarms();
		calendar = Calendar.getInstance();
		
		if (!reading) {
			//hour
			if (selection[2].equals("12") && selection[4].equals("am")) {
				selection[2] = new Integer(new Integer(selection[2]) - 12).toString();
				j++;
			}
			//ampm
			if (selection[4].equals("pm") && j == 0 && !(selection[2].equals("12"))) {
				selection[2] = new Integer(new Integer(selection[2]) + 12).toString();
			}
			//month
			switch (selection[0]) {
			case "Jan":
				monthInt = 0; break;
			case "Feb":
				monthInt = 1; break;
			case "Mar":
				monthInt = 2; break;
			case "Apr":
				monthInt = 3; break;
			case "May":
				monthInt = 4; break;
			case "Jun":
				monthInt = 5; break;
			case "Jul":
				monthInt = 6; break;
			case "Aug":
				monthInt = 7; break;
			case "Sep":
				monthInt = 8; break;
			case "Oct":
				monthInt = 9; break;
			case "Nov":
				monthInt = 10; break;
			case "Dec":
				monthInt = 11; break;
			}
			
			calendar.set(calendar.get(Calendar.YEAR), monthInt, new Integer(selection[1]), 
						 new Integer(selection[2]), new Integer(selection[3]));
			date = calendar.getTime();
		
			try {
				writeAlarm(selection);
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			calendar.set(calendar.get(Calendar.YEAR), monthInt, new Integer(selection[1]), 
					 new Integer(selection[2]), new Integer(selection[3]));
			date = calendar.getTime();
		}

		timer.schedule(alarm, date);
	}
	
	protected static void deleteAlarm() throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile = new File(xmlFilePath);
		documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("alarm");

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
			}
		}
	}
	
	private static int readAlarms(String[] selection) throws SAXException, IOException, ParserConfigurationException{
		reading = true;
		String[] temp = new String[6];
		ArrayList<String> monthArr = new ArrayList<String>();
		monthArr.add("Jan");monthArr.add("Feb");monthArr.add("Mar");monthArr.add("Apr");monthArr.add("May");monthArr.add("Jun");monthArr.add("Jul");monthArr.add("Aug");
		monthArr.add("Sep");monthArr.add("Oct");monthArr.add("Nov");monthArr.add("Dec");
		File fXmlFile = new File(xmlFilePath);
		documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("alarm");

		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				temp[0] = eElement.getElementsByTagName("month").item(0).getTextContent();
				temp[1] = eElement.getElementsByTagName("day").item(0).getTextContent();
				temp[2] = eElement.getElementsByTagName("hour").item(0).getTextContent();
				temp[3] = eElement.getElementsByTagName("minute").item(0).getTextContent();
				temp[5] = eElement.getElementsByTagName("message").item(0).getTextContent();
				if (createOnRead) {
					createNewAlarm(temp);
				} /*else {
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					cal1.set(calendar.get(Calendar.YEAR), monthArr.indexOf(temp[0]), new Integer(temp[1]), new Integer(temp[2]), new Integer(temp[3]));
					cal2.set(calendar.get(Calendar.YEAR), monthArr.indexOf(selection[0]), new Integer(selection[1]), new Integer(selection[2]), new Integer(selection[3]));
					if (cal2.before(cal1)) {
						reading = false;
						return i;
					}
				}*/
			}
		}
		reading = false;
		return -1;
	}
	
	public static void writeAlarm(String[] selection) throws TransformerException, ParserConfigurationException, SAXException, IOException {
            documentFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.parse(new File(xmlFilePath));
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            streamResult = new StreamResult(new File(xmlFilePath));
            root = document.getDocumentElement();
            createOnRead = false;
            
            int index = readAlarms(selection);
            
            Element root2 = document.createElement("alarm");
            root.appendChild(root2);
            Element month = document.createElement("month");
            month.appendChild(document.createTextNode(selection[0]));
            root2.appendChild(month);
            Element day = document.createElement("day");
            day.appendChild(document.createTextNode(selection[1]));
            root2.appendChild(day);
            Element hour = document.createElement("hour");
            hour.appendChild(document.createTextNode(selection[2]));
            root2.appendChild(hour);
            Element minute = document.createElement("minute");
            minute.appendChild(document.createTextNode(selection[3]));
            root2.appendChild(minute);
            Element message = document.createElement("message");
            message.appendChild(document.createTextNode(selection[5]));
            root2.appendChild(message);
            if (index == -1) {
            	domSource = new DOMSource(document);
            } /*else {
            	Node node = document.getFirstChild();
        		for (int i = 0; i < index; i++) {
            		node = document.importNode(node.getNextSibling(), false);
            	}
            	document.insertBefore(root2, node);
            	domSource = new DOMSource(document);
            }*/
            
            transformer.transform(domSource, streamResult);
            createOnRead = true;
	}
	
	public static void createXml() {
		try {
            documentFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentFactory.newDocumentBuilder();
			document = documentBuilder.newDocument();
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            streamResult = new StreamResult(new File(xmlFilePath));
            root = document.createElement("alarms");
            document.appendChild(root);

            domSource = new DOMSource(document);
            transformer.transform(domSource, streamResult);
            
            
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				gui = new AlarmsGUI();
				
				gui.createAndShowGUI();
				try {
					readAlarms(null);
				} catch (SAXException e) {
					createXml();
				} catch (IOException e) {
					createXml();
				} catch (ParserConfigurationException e) {
					createXml();
				}
			}
		});
	}
}
