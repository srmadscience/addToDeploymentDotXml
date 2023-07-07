package addtodeploymentdotxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientResponse;

public class AddToDeploymentDotXML {

	public static void main(String[] args) {

		try {

			
			if (args.length != 3) {
				msg("Usage: java -jar AddToDeploymentDotXML.jar hostname xmltag extrapartfilename");
				msg("E.G. : java -jar AddToDeploymentDotXML.jar localhost deployment /Users/drolfe/Desktop/EclipseWorkspace/xtra.txt");
				System.exit(0);
			}
			
			msg("Params: " + Arrays.toString(args));

			Client c = connectVoltDB(args[0]);
			String tag = args[1];
			File newbit = new File(args[2]);
			File deploymentFile = null;

			checkFile(newbit);

			ClientResponse cr = c.callProcedure("@SystemInformation", "OVERVIEW");
			while (cr.getResults()[0].advanceRow()) {
				if (cr.getResults()[0].getString("KEY").equals("DEPLOYMENT")) {
					deploymentFile = new File(cr.getResults()[0].getString("VALUE"));
					msg("Deployment file is " + deploymentFile.getAbsolutePath());
					break;
				}
			}

			checkFile(deploymentFile);

			boolean foundTag = false;

			StringBuilder newDeploymentFileContents = new StringBuilder();
			StringBuilder newBitContents = new StringBuilder();
			loadFile(newbit, newBitContents);

			BufferedReader deploymentFileReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(deploymentFile)));

			String line;
			while ((line = deploymentFileReader.readLine()) != null) {

				if (line.trim().equals("</" + tag + ">")) {
				    
					msg("NEW:" + newBitContents.toString());
					foundTag = true;
					newDeploymentFileContents.append(newBitContents.toString());
					
				}

				msg("OLD:" + line);
				newDeploymentFileContents.append(line).append("\n");
			}

			deploymentFileReader.close();

			msg(newDeploymentFileContents.toString());
			byte[] unusedParam = null;
			byte[] newConfig = newDeploymentFileContents.toString().getBytes();

			if (foundTag) {

				cr = c.callProcedure("@UpdateApplicationCatalog", unusedParam, newConfig);

				if (cr.getStatus() == ClientResponse.SUCCESS) {
					msg("Changed");
				} else {
					msg(cr.getStatusString());
				}

			} else {
				msg("tag not found");
			}

			c.close();
			
		} catch (

		Exception e) {
			msg(e.getMessage());
			System.exit(5);
		}
		
		

	}

	private static void loadFile(File existingFile, StringBuilder resultStringBuilder)
			throws FileNotFoundException, IOException {
		BufferedReader existingFileReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(existingFile)));

		String line;
		while ((line = existingFileReader.readLine()) != null) {
			resultStringBuilder.append(line).append("\n");
		}

		existingFileReader.close();
	}

	private static void checkFile(File theFile) {

		if (!theFile.exists()) {
			msg("File " + theFile + " not found");
			System.exit(1);
		}

		if (!theFile.isFile()) {
			msg("File " + theFile + " not a file");
			System.exit(2);
		}

		if (!theFile.canRead()) {
			msg("File " + theFile + " not readable");
			System.exit(3);
		}

		if (theFile.length() == 0) {
			msg("File " + theFile + " is zero length");
			System.exit(4);
		}
	}

	/**
	 * Connect to VoltDB using a comma delimited hostname list.
	 * 
	 * @param commaDelimitedHostnames
	 * @return
	 * @throws Exception
	 */
	private static Client connectVoltDB(String commaDelimitedHostnames) throws Exception {
		Client client = null;
		ClientConfig config = null;

		try {
			msg("Logging into VoltDB");

			config = new ClientConfig(); // "admin", "idontknow");
			config.setTopologyChangeAware(true);
			config.setReconnectOnConnectionLoss(true);

			client = ClientFactory.createClient(config);

			String[] hostnameArray = commaDelimitedHostnames.split(",");

			for (int i = 0; i < hostnameArray.length; i++) {
				msg("Connect to " + hostnameArray[i] + "...");
				try {
					client.createConnection(hostnameArray[i]);
				} catch (Exception e) {
					msg(e.getMessage());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("VoltDB connection failed.." + e.getMessage(), e);
		}

		return client;

	}

	/**
	 * Print a formatted message.
	 * 
	 * @param message
	 */
	public static void msg(String message) {

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		System.out.println(strDate + ":" + message);

	}

}
