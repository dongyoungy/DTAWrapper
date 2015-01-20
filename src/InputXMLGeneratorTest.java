import java.util.ArrayList;

/**
 * Created by dyoon on 15. 1. 20..
 */
public class InputXMLGeneratorTest
{
	public static void run()
	{
		InputXMLGenerator generator = new InputXMLGenerator("DY-WINVM", "AdventureWorks");

		String query1 = "SELECT *\n" +
				"FROM Production.Product\n" +
				"ORDER BY Name ASC;";
		double weight1 = 2.0;
		WeightedQuery q1 = new WeightedQuery(query1, weight1);

		String query2 = "SELECT ProductModelID, AVG(ListPrice) AS 'Average List Price'\n" +
				"FROM Production.Product\n" +
				"WHERE ListPrice > $1000\n" +
				"GROUP BY ProductModelID\n" +
				"ORDER BY ProductModelID;";
		double weight2 = 2.5;
		WeightedQuery q2 = new WeightedQuery(query2, weight2);

		ArrayList<WeightedQuery> queries = new ArrayList<WeightedQuery>();
		queries.add(q1);
		queries.add(q2);

		generator.generateInputXML(queries, "test_input.xml");
	}
}
