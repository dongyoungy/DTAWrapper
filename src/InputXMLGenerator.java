import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

/**
 * Created by dyoon on 15. 1. 20..
 */
public class InputXMLGenerator
{
	private String serverName;
	private String dbName;

	public InputXMLGenerator(String serverName, String dbName)
	{
		this.serverName = serverName;
		this.dbName = dbName;
	}

	public boolean generateInputXML(List<WeightedQuery> queries, String xmlPath)
	{
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.newDocument();
			Element DTAXML = doc.createElement("DTAXML");
			doc.appendChild(DTAXML);

			Element DTAInput = doc.createElement("DTAInput");
			DTAXML.appendChild(DTAInput);

			Element Server = doc.createElement("Server");
			DTAInput.appendChild(Server);

			Element serverName = doc.createElement("Name");
			serverName.appendChild(doc.createTextNode(this.serverName));
			Server.appendChild(serverName);

			Element Database = doc.createElement("Database");
			Server.appendChild(Database);

			Element databaseName = doc.createElement("Name");
			databaseName.appendChild(doc.createTextNode(this.dbName));
			Database.appendChild(databaseName);

			Element Workload = doc.createElement("Workload");
			DTAInput.appendChild(Workload);

			for (WeightedQuery q : queries)
			{
				String query = q.getQuery();
				double weight = q.getWeight();

				Element EventString = doc.createElement("EventString");
				EventString.setAttribute("Weight", Double.toString(weight));
				EventString.appendChild(doc.createTextNode(query));

				Workload.appendChild(EventString);
			}

			// write the content into xml file
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlPath));

			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			t.transform(source, result);

			System.out.println("Input XML file has been created.");
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}
