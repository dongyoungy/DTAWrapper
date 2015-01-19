/**
 * Created by dyoon on 15. 1. 16..
 */
public class DTAWrapperMain
{
	public static void main(String[] args)
	{
		if (args.length == 4 && args[0].equalsIgnoreCase("dta"))
		{
			DTARunner runner = new DTARunner();
			runner.setServerName(args[1]);
			runner.setDBName(args[2]);
			runner.setInputFilename(args[3]);

			runner.run();
		}
		else if (args.length == 5 && args[0].equalsIgnoreCase("idx"))
		{
			IndexBuilder builder = new IndexBuilder();
			builder.setServerName(args[1]);
			builder.setDbName(args[2]);
			builder.setUserName(args[3]);
			builder.setPassword(args[4]);

			builder.run();
		}
		else if (args.length == 6 && args[0].equalsIgnoreCase("sqlcmd"))
		{
			SqlCmdRunner sqlCmdRunner = new SqlCmdRunner();
			sqlCmdRunner.setServerName(args[1]);
			sqlCmdRunner.setDbName(args[2]);
			sqlCmdRunner.setUserName(args[3]);
			sqlCmdRunner.setPassword(args[4]);
			sqlCmdRunner.setInputFilename(args[5]);

			sqlCmdRunner.run();
		}
		else
		{
			System.out.println("USAGE: dta <server_name> <db_name> <input_file>");
			System.out.println("OR");
			System.out.println("USAGE: idx <server_name> <db_name> <user_name> <password>");
			System.out.println("OR");
			System.out.println("USAGE: sqlcmd <server_name> <db_name> <user_name> <password> <input_file>");
			return;
		}
	}
}
