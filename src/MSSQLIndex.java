import java.util.ArrayList;

/**
 * Created by dyoon on 15. 1. 17..
 */
public class MSSQLIndex
{
	private static final int TYPE_HEAP = 0;
	private static final int TYPE_CLUSTERED = 1;
	private static final int TYPE_NONCLUSTERED = 2;
	private static final int TYPE_XML = 3;
	private static final int TYPE_SPATIAL = 4;
	private static final int TYPE_CLUSTERED_COLUMNSTORE = 5;
	private static final int TYPE_NONCLUSTERED_COLUMNSTORE = 6;
	private static final int TYPE_NONCLUSTERED_HASH = 7;

	private int objectId;
	private String indexName;
	private String schemaName;
	private String tableName;
	private int indexType;
	private int indexId;

	private int isUnique;
	private int isPrimaryKey;
	private int isUniqueConstraint;
	private int isDisabled;

	private ArrayList<MSSQLColumn> keyColumns;
	private ArrayList<MSSQLColumn> includeColumns;

	public MSSQLIndex()
	{
		keyColumns = new ArrayList<MSSQLColumn>();
		includeColumns = new ArrayList<MSSQLColumn>();
	}

	public void print()
	{
		System.out.println(String.format("INDEX: %s ON %s.%s, type: %d, is_disabled: %d", indexName, schemaName, tableName, indexType, isDisabled));
		if (keyColumns.size() > 0)
		{
			System.out.println("\tKey Columns:");
			for (MSSQLColumn column : keyColumns)
			{
				System.out.println("\t\t" + column.toString());
			}
		}
		if (includeColumns.size() > 0)
		{
			System.out.println("\tInclude Columns:");
			for (MSSQLColumn column : includeColumns)
			{
				System.out.println("\t\t" + column.toString());
			}
		}
	}

	public void addKeyColumn(MSSQLColumn column)
	{
		keyColumns.add(column);
	}

	public void addIncludeColumn(MSSQLColumn column)
	{
		includeColumns.add(column);
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

	public String getIndexName()
	{
		return indexName;
	}

	public void setIndexName(String indexName)
	{
		this.indexName = indexName;
	}

	public String getSchemaName()
	{
		return schemaName;
	}

	public void setSchemaName(String schemaName)
	{
		this.schemaName = schemaName;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
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
}
