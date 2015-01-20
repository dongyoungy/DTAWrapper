/**
 * Created by dyoon on 15. 1. 20..
 */
public class WeightedQuery
{
	private String query;
	private double weight;

	public WeightedQuery(String query, double weight)
	{
		this.query = query;
		this.weight = weight;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public double getWeight()
	{
		return weight;
	}

	public void setWeight(double weight)
	{
		this.weight = weight;
	}
}
