import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;

/**
 * Created by dyoon on 15. 1. 19..
 */
public class SqlCmdRemoteRunner
{
	private static final String command = "sqlcmd";

	private String psexecPath;
	private String remoteIP;
	private String serverName;
	private String dbName;
	private String userName;
	private String password;
	private String inputFilename;

	public SqlCmdRemoteRunner(String remoteIP, String psexecPath)
	{
		this.remoteIP = remoteIP;
		this.psexecPath = psexecPath;
	}

	public boolean run()
	{
		ProcessBuilder pb = getSqlCmdProcessBuilder();

		pb.redirectErrorStream(true);

		try
		{
			Process p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = br.readLine()) != null)
			{
				System.out.println(line);
			}
			p.waitFor();
			System.out.println("sqlcmd has run successfully.");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	private ProcessBuilder getSqlCmdProcessBuilder()
	{
		ArrayList<String> arguments = new ArrayList<String>();

		arguments.add(psexecPath);
		arguments.add("\\\\" + remoteIP);

		// 'sqlcmd' command
		arguments.add(command);

		// server name
		arguments.add("-S");
		arguments.add(serverName);

		// db name
		arguments.add("-d");
		arguments.add(dbName);

		// username
		arguments.add("-U");
		arguments.add(userName);

		// password
		arguments.add("-P");
		arguments.add(password);

		// input file
		arguments.add("-i");
		arguments.add(inputFilename);

		ProcessBuilder pb = new ProcessBuilder(arguments);

		return pb;
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

	public String getInputFilename()
	{
		return inputFilename;
	}

	public void setInputFilename(String inputFilename)
	{
		this.inputFilename = inputFilename;
	}
}
