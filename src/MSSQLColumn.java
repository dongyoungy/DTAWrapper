/**
 * Created by dyoon on 15. 1. 17..
 */
public class MSSQLColumn
{
	private int columnId;
	private int isDescendingKey;
	private int keyOrder;
	private int partitionOrder;

	public MSSQLColumn()
	{

	}

	public String toString()
	{
		return String.format("[id: %d, is_descending_key: %d, key_ordinal: %d, partition_ordinal: %d]", columnId,
				isDescendingKey, keyOrder, partitionOrder);
	}

	public int getColumnId()
	{
		return columnId;
	}

	public void setColumnId(int columnId)
	{
		this.columnId = columnId;
	}

	public int getIsDescendingKey()
	{
		return isDescendingKey;
	}

	public void setIsDescendingKey(int isDescendingKey)
	{
		this.isDescendingKey = isDescendingKey;
	}

	public int getKeyOrder()
	{
		return keyOrder;
	}

	public void setKeyOrder(int keyOrder)
	{
		this.keyOrder = keyOrder;
	}

	public int getPartitionOrder()
	{
		return partitionOrder;
	}

	public void setPartitionOrder(int partitionOrder)
	{
		this.partitionOrder = partitionOrder;
	}
}
