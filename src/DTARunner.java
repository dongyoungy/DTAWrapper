import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dyoon on 15. 1. 16..
 */
public class DTARunner
{
	private String dtaCommand = "dta";

	// needs to be set.
	private String serverName;
	private String DBName;
	private String inputFilename;

	// filled automatically
	private String sessionName;
	private String outputRecommendationName;
	private String outputSummaryName;
	private String outputReportName;

	// default exists
	private String PDSAddOption;
	private String PDSKeepOption;
	private String partitionStrategy;
	private String onlineOption;

	public DTARunner()
	{
		PDSAddOption = "IDX_IV";
		PDSKeepOption = "NONE";

		onlineOption = "OFF";
		partitionStrategy = "NONE";
	}

	public boolean run()
	{
		ProcessBuilder pb = getDTAProcessBuilder();

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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return true;
	}

	private ProcessBuilder getDTAProcessBuilder()
	{
		ArrayList<String> arguments = new ArrayList<String>();

		// dta command.
		arguments.add(dtaCommand);

		// server name.
		arguments.add("-S");
		arguments.add(serverName);

		// DB name
		arguments.add("-D");
		arguments.add(DBName);

		// use trusted connection
		arguments.add("-E");

		// input file
		arguments.add("-if");
		arguments.add(inputFilename);

		// session name
		arguments.add("-s");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String strDate = dateFormat.format(new Date());
		String sessionName = String.format("dta_%s", strDate);
		arguments.add(sessionName);

		// recommendation output
		arguments.add("-of");
		String recommendationName = String.format("rec_%s.sql", strDate);
		arguments.add(recommendationName);

		// summary output
		arguments.add("-ox");
		String summaryName = String.format("summary_%s.xml", strDate);
		arguments.add(summaryName);

		// report output
		arguments.add("-or");
		String reportName = String.format("report_%s.xml", strDate);
		arguments.add(reportName);

		// PDS add option
		arguments.add("-fa");
		arguments.add(PDSAddOption);

		// PDS keep option
		arguments.add("-fk");
		arguments.add(PDSKeepOption);

		// partition strategy
		arguments.add("-fp");
		arguments.add(partitionStrategy);

		ProcessBuilder pb = new ProcessBuilder(arguments);

		return pb;
	}

	public String getDtaCommand()
	{
		return dtaCommand;
	}

	public void setDtaCommand(String dtaCommand)
	{
		this.dtaCommand = dtaCommand;
	}

	public String getServerName()
	{
		return serverName;
	}

	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	public String getDBName()
	{
		return DBName;
	}

	public void setDBName(String DBName)
	{
		this.DBName = DBName;
	}

	public String getInputFilename()
	{
		return inputFilename;
	}

	public void setInputFilename(String inputFilename)
	{
		this.inputFilename = inputFilename;
	}

	public String getSessionName()
	{
		return sessionName;
	}

	public void setSessionName(String sessionName)
	{
		this.sessionName = sessionName;
	}

	public String getOutputRecommendationName()
	{
		return outputRecommendationName;
	}

	public void setOutputRecommendationName(String outputRecommendationName)
	{
		this.outputRecommendationName = outputRecommendationName;
	}

	public String getOutputSummaryName()
	{
		return outputSummaryName;
	}

	public void setOutputSummaryName(String outputSummaryName)
	{
		this.outputSummaryName = outputSummaryName;
	}

	public String getOutputReportName()
	{
		return outputReportName;
	}

	public void setOutputReportName(String outputReportName)
	{
		this.outputReportName = outputReportName;
	}

	public String getPDSAddOption()
	{
		return PDSAddOption;
	}

	public void setPDSAddOption(String PDSAddOption)
	{
		this.PDSAddOption = PDSAddOption;
	}

	public String getPDSKeepOption()
	{
		return PDSKeepOption;
	}

	public void setPDSKeepOption(String PDSKeepOption)
	{
		this.PDSKeepOption = PDSKeepOption;
	}

	public String getPartitionStrategy()
	{
		return partitionStrategy;
	}

	public void setPartitionStrategy(String partitionStrategy)
	{
		this.partitionStrategy = partitionStrategy;
	}

	public String getOnlineOption()
	{
		return onlineOption;
	}

	public void setOnlineOption(String onlineOption)
	{
		this.onlineOption = onlineOption;
	}
}
