import java.util.ArrayList;

/**
 * Created by dyoon on 15. 1. 19..
 */
public class MSSQLIndexedView
{
	private ArrayList<String> columns;
	private ArrayList<String> aliases;
	private ArrayList<Integer> indexedColumns;
	private ArrayList<Integer> isDescending;

	private String from;
	private String where;
	private String groupBy;

	private String viewName;
	private String indexName;
	private String schemaName;

	private int objectId;
	private int indexId;
	private int indexType;

	private int isDisabled;
	private int isUnique;
	private int isPrimaryKey;
	private int isUniqueConstraint;

	public MSSQLIndexedView()
	{
		columns = new ArrayList<String>();
		aliases = new ArrayList<String>();
		indexedColumns = new ArrayList<Integer>();
		isDescending = new ArrayList<Integer>();

		from = "";
		where = "";
		groupBy = "";
	}

	public void print()
	{
		System.out.println(String.format("INDEXED VIEW: [INDEX: %s.%s ON %s] (type: %d, is_disabled: %d)", schemaName, indexName, viewName, indexType, isDisabled));
		System.out.println("Columns:");
		for (int i = 0; i < columns.size(); ++i)
		{
			System.out.print("\t" + columns.get(i));
			if (aliases.get(i) != "")
			{
				System.out.println(" AS " + aliases.get(i));
			}
			else
			{
				System.out.println();
			}
		}
		System.out.println("Indexed Columns:");
		for (int i = 0; i < indexedColumns.size(); ++i)
		{
			int index = indexedColumns.get(i).intValue();
			System.out.println("\t" + columns.get(index) + " (is_descending: " + isDescending.get(i) + ")");
		}
		System.out.println("From:");
		if (from != "")
		{
			System.out.println("\t" + from);
		}
		System.out.println("Where:");
		if (where != "" )
		{
			System.out.println("\t" + where);
		}
		System.out.println("Group-by/having:\n\t" + groupBy);
	}

	public void addColumn(String column, String alias)
	{
		columns.add(column);
		aliases.add(alias);
	}

	public void addIndexedColumn(String column, int isDescending)
	{
		for (int i = 0; i < columns.size(); ++i)
		{
			String col = columns.get(i);
			String alias = aliases.get(i);

			if (alias != "")
			{
				if (alias.trim().compareTo(column.trim()) == 0)
				{
					indexedColumns.add(i);
					break;
				}
			}
			else
			{
				if (col.contains(column))
				{
					indexedColumns.add(i);
					break;
				}
			}
		}
		this.isDescending.add(isDescending);
	}

	public int getObjectId()
	{
		return objectId;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}

	public int getIndexId()
	{
		return indexId;
	}

	public void setIndexId(int indexId)
	{
		this.indexId = indexId;
	}

	public int getIndexType()
	{
		return indexType;
	}

	public void setIndexType(int indexType)
	{
		this.indexType = indexType;
	}

	public int getIsUnique()
	{
		return isUnique;
	}

	public void setIsUnique(int isUnique)
	{
		this.isUnique = isUnique;
	}

	public int getIsPrimaryKey()
	{
		return isPrimaryKey;
	}

	public void setIsPrimaryKey(int isPrimaryKey)
	{
		this.isPrimaryKey = isPrimaryKey;
	}

	public int getIsUniqueConstraint()
	{
		return isUniqueConstraint;
	}

	public void setIsUniqueConstraint(int isUniqueConstraint)
	{
		this.isUniqueConstraint = isUniqueConstraint;
	}

	public int getIsDisabled()
	{
		return isDisabled;
	}

	public void setIsDisabled(int isDisabled)
	{
		this.isDisabled = isDisabled;
	}

	public String getSchemaName()
	{
		return schemaName;
	}

	public void setSchemaName(String schemaName)
	{
		this.schemaName = schemaName;
	}

	public String getViewName()
	{
		return viewName;
	}

	public void setViewName(String viewName)
	{
		this.viewName = viewName;
	}

	public String getIndexName()
	{
		return indexName;
	}

	public void setIndexName(String indexName)
	{
		this.indexName = indexName;
	}

	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getWhere()
	{
		return where;
	}

	public void setWhere(String where)
	{
		this.where = where;
	}

	public String getGroupBy()
	{
		return groupBy;
	}

	public void setGroupBy(String groupBy)
	{
		this.groupBy = groupBy;
	}
}
