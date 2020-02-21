package xyz.potomac_foods.OCUConsole;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;

    public Server(int port){
        // TODO Get the IP address that I got the connection, confirm it's from the main server(192.168.1.100), and do nothing if it's not.
        try {
            server = new ServerSocket(port);
            System.out.println("Server Started");
            System.out.println("Waiting for Client");
            while(true) {
                socket = server.accept();
                //System.out.println("Receiving Data");
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String msg;
                StringBuilder completeXML= new StringBuilder();
                //String line = reader.readLine();
                //System.out.println(line);
                while ((msg = reader.readLine()) != null) {
                    completeXML.append(msg);
                }

                //System.out.println(completeXML);
                //String stepOne = completeXML.toString();
                //String[] stepTwo = stepOne.replace("<?xml version=\"1.0\"?>", ",").split(",");
                //String[] stepThree = Arrays.copyOfRange(stepTwo, 1, stepTwo.length);;
                //System.out.println(Arrays.toString(stepThree));

                //String latestOrder = stepThree[stepThree.length-1];
                //System.out.println(latestOrder);
                //System.out.println"Length: " + fixedXML.length + "\n" + latestOrder);

                System.out.println("\nYour current order (current order at the bottom, past order at the top):");

                Document doc = convertStringToXMLDocument(completeXML.toString());

                NodeList orderHeaderNL = doc.getElementsByTagName("OrderHeader");
                Node orderHeaderN = orderHeaderNL.item(0);
                Element orderHeaderE = (Element) orderHeaderN;
                String orderState = orderHeaderE.getElementsByTagName("OrderState").item(0).getTextContent();
                //System.out.println(completeXML.toString());

                if(orderState.equals("Open")) {

                    NodeList items = doc.getElementsByTagName("Item");

                    for (int i = 0; i < items.getLength(); i++) {
                        Node nNode = items.item(i);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            System.out.println("You ordered " +
                                    eElement.getElementsByTagName("Quantity").item(0).getTextContent() + " " +
                                    eElement.getElementsByTagName("Name").item(0).getTextContent() + " at " +
                                    eElement.getElementsByTagName("Price").item(0).getTextContent()
                            );

                            try {
                                NodeList condis = eElement.getElementsByTagName("Condiments");
                                for(int x = 0; x < condis.getLength(); x++){
                                    Node nCondi = condis.item(x);
                                    if(nCondi.getNodeType() == Node.ELEMENT_NODE) {
                                        Element nElement = (Element) nCondi;
                                        for(int z = 0; z < nElement.getElementsByTagName("Description").getLength(); z++){
                                            System.out.println(" - " + nElement.getElementsByTagName("Description").item(z).getTextContent());
                                        }
                                    }
                                }

                            } catch (Exception ignored){

                            }
                        }
                    }
                } else {
                    String subTotal = orderHeaderE.getElementsByTagName("Subtotal").item(0).getTextContent();
                    String total = orderHeaderE.getElementsByTagName("Total").item(0).getTextContent();
                    String tax = orderHeaderE.getElementsByTagName("Tax").item(0).getTextContent();
                    String discount = orderHeaderE.getElementsByTagName("Discount").item(0).getTextContent();
                    System.out.println("Your subtotal: " + subTotal);
                    System.out.println("Your tax: " + tax);
                    System.out.println("Your discount: " + discount);
                    System.out.println("Your total: " + total);
                }

                completeXML.delete(0, completeXML.length());
            }

        }catch (IOException e){
            System.out.println("ERROR:" + e.getMessage() + e.getStackTrace());
        }
    }

    private static Document convertStringToXMLDocument(String xmlString){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder;

        try{
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
