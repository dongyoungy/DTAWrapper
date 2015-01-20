import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TGroupBy;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TWhereClause;
import gudusoft.gsqlparser.stmt.TCreateViewSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by dyoon on 15. 1. 17..
 */
public class IndexBuilder
{
	private String url = "jdbc:sqlserver://";
	private String serverName;
	private String dbName;
	private String userName;
	private String password;

	private ArrayList<MSSQLIndex> indexList;
	private ArrayList<MSSQLIndexedView> indexedViewList;

	public void run()
	{
		indexList = new ArrayList<MSSQLIndex>();
		indexedViewList = new ArrayList<MSSQLIndexedView>();

		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			Connection conn = DriverManager.getConnection(getConnectionUrl(), userName, password);
			if (conn != null)
			{
				System.out.println("Connection successful");
			}
			Statement stmt = conn.createStatement();
			String sql = "SELECT t.name as table_name, s.name as schema_name, ind.* " +
					"FROM sys.indexes ind INNER JOIN sys.tables t ON ind.object_id = t.object_id " +
					"INNER JOIN sys.schemas s ON t.schema_id = s.schema_id " +
					"WHERE t.is_ms_shipped = 0 AND ind.type > 0 and ind.is_primary_key = 0 and ind.is_unique_constraint = 0;";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				MSSQLIndex newIndex = new MSSQLIndex();

				newIndex.setSchemaName(rs.getString("schema_name"));
				newIndex.setTableName(rs.getString("table_name"));
				newIndex.setObjectId(rs.getInt("object_id"));
				newIndex.setIndexId(rs.getInt("index_id"));
				newIndex.setIndexName(rs.getString("name"));
				newIndex.setIndexType(rs.getInt("type"));
				newIndex.setIsPrimaryKey(rs.getInt("is_primary_key"));
				newIndex.setIsDisabled(rs.getInt("is_disabled"));
				newIndex.setIsUnique(rs.getInt("is_unique"));
				newIndex.setIsUniqueConstraint((rs.getInt("is_unique_constraint")));

				String sqlGetKeyColumns = "SELECT * FROM sys.index_columns WHERE object_id = ? and index_id = ? and is_included_column = 0 " +
						"ORDER BY key_ordinal";
				PreparedStatement keyColumnStatement = conn.prepareStatement(sqlGetKeyColumns);
				keyColumnStatement.setInt(1, newIndex.getObjectId());
				keyColumnStatement.setInt(2, newIndex.getIndexId());
				ResultSet keyColumns = keyColumnStatement.executeQuery();
				while (keyColumns.next())
				{
					MSSQLColumn newColumn = new MSSQLColumn();
					newColumn.setColumnId(keyColumns.getInt("column_id"));
					newColumn.setIsDescendingKey(keyColumns.getInt("is_descending_key"));
					newColumn.setKeyOrder(keyColumns.getInt("key_ordinal"));
					newColumn.setPartitionOrder(keyColumns.getInt("partition_ordinal"));

					newIndex.addKeyColumn(newColumn);
				}
				keyColumns.close();

				String sqlGetIncludeColumns = "SELECT * FROM sys.index_columns WHERE object_id = ? and index_id = ? and is_included_column = 1";
				PreparedStatement includeColumnStatement = conn.prepareStatement(sqlGetIncludeColumns);
				includeColumnStatement.setInt(1, newIndex.getObjectId());
				includeColumnStatement.setInt(2, newIndex.getIndexId());
				ResultSet includeColumns = includeColumnStatement.executeQuery();
				while (includeColumns.next())
				{
					MSSQLColumn newColumn = new MSSQLColumn();
					newColumn.setColumnId(includeColumns.getInt("column_id"));
					newColumn.setIsDescendingKey(includeColumns.getInt("is_descending_key"));
					newColumn.setKeyOrder(includeColumns.getInt("key_ordinal"));
					newColumn.setPartitionOrder(includeColumns.getInt("partition_ordinal"));

					newIndex.addIncludeColumn(newColumn);
				}
				includeColumns.close();

				indexList.add(newIndex);
			}
			rs.close();

			stmt = conn.createStatement();
			String sqlGetIndexedViews = "SELECT o.object_id, o.name as view_name, i.*, s.name as schema_name " +
					"FROM sys.objects o " +
					"INNER JOIN sys.indexes i ON o.object_id = i.object_id " +
					"INNER JOIN sys.views v ON v.object_id = i.object_id " +
					"INNER JOIN sys.schemas s ON s.schema_id = v.schema_id " +
					"WHERE o.type = 'V'";
			ResultSet indexedViews = stmt.executeQuery(sqlGetIndexedViews);

			while (indexedViews.next())
			{
				MSSQLIndexedView newView = new MSSQLIndexedView();

				newView.setObjectId(indexedViews.getInt("object_id"));
				newView.setIndexId(indexedViews.getInt("index_id"));
				newView.setIndexName(indexedViews.getString("name"));
				newView.setIndexType(indexedViews.getInt("type"));
				newView.setViewName(indexedViews.getString("view_name"));
				newView.setSchemaName(indexedViews.getString("schema_name"));
				newView.setIsDisabled(indexedViews.getInt("is_disabled"));
				newView.setIsPrimaryKey(indexedViews.getInt("is_primary_key"));
				newView.setIsDisabled(indexedViews.getInt("is_disabled"));
				newView.setIsUnique(indexedViews.getInt("is_unique"));
				newView.setIsUniqueConstraint((indexedViews.getInt("is_unique_constraint")));

				String sqlGetViewDefinition = "SELECT definition from sys.sql_modules WHERE object_id = ?";
				PreparedStatement viewDefinitionStmt = conn.prepareStatement(sqlGetViewDefinition);
				viewDefinitionStmt.setInt(1, newView.getObjectId());

				ResultSet viewDefinition = viewDefinitionStmt.executeQuery();
				if (viewDefinition.next())
				{
					String definition = viewDefinition.getString("definition");

					TGSqlParser parser = new TGSqlParser(EDbVendor.dbvmssql);

					parser.setSqltext(definition);
					int ret = parser.parse();
					if (ret == 0)
					{
						if (parser.sqlstatements.size() != 1)
						{
							continue;
						}
						else
						{
							TCustomSqlStatement customSqlStatement = parser.sqlstatements.get(0);
							if (customSqlStatement instanceof TCreateViewSqlStatement)
							{
								TCreateViewSqlStatement viewStatement = (TCreateViewSqlStatement)customSqlStatement;

								TSelectSqlStatement selectSqlStatement = viewStatement.getSubquery();

								// get select columns
								for (int i = 0; i < selectSqlStatement.getResultColumnList().size(); ++i)
								{
									TResultColumn column = selectSqlStatement.getResultColumnList().getResultColumn(i);

									String columnExpr = column.getExpr().toString();
									String alias = "";
									if (column.getAliasClause() != null)
									{
										alias = column.getAliasClause().toString();
									}

									newView.addColumn(columnExpr, alias);
								}

								// get from clause
								newView.setFrom(selectSqlStatement.joins.toString());

								// get where clause
								TWhereClause where = selectSqlStatement.getWhereClause();
								if (where != null)
								{
									newView.setWhere(where.toString());
								}

								// get group-by & having clause
								TGroupBy groupBy = selectSqlStatement.getGroupByClause();
								if (groupBy != null)
								{
									newView.setGroupBy(groupBy.toString());
								}

								// get indexed columns
								String sqlGetIndexedColumns = "SELECT c.name, i.is_descending_key FROM sys.index_columns i " +
										"INNER JOIN sys.columns c ON c.object_id = i.object_id AND c.column_id = i.column_id " +
										"WHERE i.object_id = ? AND i.index_id = ? ORDER BY key_ordinal";

								PreparedStatement getIndexedColumnStatement = conn.prepareStatement(sqlGetIndexedColumns);
								getIndexedColumnStatement.setInt(1, newView.getObjectId());
								getIndexedColumnStatement.setInt(2, newView.getIndexId());

								ResultSet indexedColumns = getIndexedColumnStatement.executeQuery();

								while (indexedColumns.next())
								{
									String indexName = indexedColumns.getString("name");
									int isDescending = indexedColumns.getInt("is_descending_key");
									newView.addIndexedColumn(indexName, isDescending);
								}

								indexedColumns.close();
							}
							else
							{
								continue;
							}
						}
					}
				}
				viewDefinition.close();

				indexedViewList.add(newView);
			}
			indexedViews.close();

			conn.close();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		for (MSSQLIndex index : indexList)
		{
			index.print();
			System.out.println();
		}
		for (MSSQLIndexedView view : indexedViewList)
		{
			view.print();
			System.out.println();
		}
	}

	private String getConnectionUrl()
	{
		return url + serverName + ":1433;databaseName=" + dbName;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public String getDbName()
	{
		return dbName;
	}

	public void setDbName(String dbName)
	{
		this.dbName = dbName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
