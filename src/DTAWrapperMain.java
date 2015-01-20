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
		else if (args.length == 7 && args[0].equalsIgnoreCase("dta_remote"))
		{
			DTARemoteRunner runner = new DTARemoteRunner(args[2], args[3], args[1]);

			runner.setServerName(args[4]);
			runner.setDBName(args[5]);
			runner.setInputXML(args[6]);

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
		else if (args.length == 8 && args[0].equalsIgnoreCase("sqlcmd_remote"))
		{
			SqlCmdRemoteRunner sqlCmdRunner = new SqlCmdRemoteRunner(args[1], args[2]);
			sqlCmdRunner.setServerName(args[3]);
			sqlCmdRunner.setDbName(args[4]);
			sqlCmdRunner.setUserName(args[5]);
			sqlCmdRunner.setPassword(args[6]);
			sqlCmdRunner.setInputFilename(args[7]);

			sqlCmdRunner.run();
		}
		else if (args.length == 1 && args[0].equalsIgnoreCase("xmltest"))
		{
			InputXMLGeneratorTest.run();
		}
		else
		{
			System.out.println("USAGE: dta <server_name> <db_name> <input_file>");
			System.out.println("OR");
			System.out.println("USAGE: dta_remote <remote_ip> <local_PsExec_path> <remote_result_dir> <server_name> <db_name> <remote_input_file>");
			System.out.println("OR");
			System.out.println("USAGE: idx <server_name> <db_name> <user_name> <password>");
			System.out.println("OR");
			System.out.println("USAGE: sqlcmd <server_name> <db_name> <user_name> <password> <input_file>");
			System.out.println("OR");
			System.out.println("USAGE: sqlcmd_remote <remote_ip> <local_PsExec_path> <server_name> <db_name> <user_name> <password> <remote_input_file>");
			return;
		}
	}
}
