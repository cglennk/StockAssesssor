package NB;

/** Class Book contains the elements: title, author, ISBN, and yearPublished.
 * @Author Colin Knappert
 * @Version 1.3 */


import java.io.*;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.*;
import java.util.Calendar;
import java.util.StringTokenizer;

public class NB {

	/**
	 * Retrieves file from Internet and assess the stock history of the DOW top 30        (1)
	 *
	 * No input is required, only a valid Internet connection is necessary.   [2]
	 * The last five days for which the markets were open are assessed and given values
	 * which depend upon whether the stock rose or fell in volume and whether
	 * the price rose or fell. 
	 *
	 * @param  None.          (3)
	 * @return Prints values for each companies stock determined over the last 5 days.
	 */
	static void constructData() {
		try{
			
			String dowArray[] = {"MMM", "AA", "MO", "AXP", "AIG", "T", "BA", "CAT", "C", "KO", "DD", "XOM", "GE", "GM", "HPQ", "HD", "HON", "IBM", "INTC", "JNJ", "JPM", "MCD", "MRK", "MSFT", "PFE", "PG", "UTX", "VZ", "WMT", "DIS"}; 

	        int dowValues[] = new int[30];
	        
			for(int y = 0; y < 30; y++)
			{
				//date	parameter	
				Calendar	c2	=	Calendar.getInstance();	
				Calendar	c1	=	Calendar.getInstance();	
				c1.set(2012, 00, 01);
				String symbol = dowArray[y];
				//change	the	value	of	c1,	and	c2	
				String	sDate	=	String.format("&a=%1$tm&b=%1$te&c=%1$tY&d=%2$tm&e=%2$te&f=%2$tY",	c1,c2);	
				//System.out.println("string" + sDate);
				String	strURL	=	"http://ichart.finance.yahoo.com/table.csv?s="	+	symbol	+	sDate	+	"&g=d&ignore=.csv";	
				//System.out.println(strURL);
				String fileName = symbol + ".csv";
				DataInputStream in = null;	
				URL	remoteFile = new URL(strURL);	
				URLConnection fileStream = remoteFile.openConnection();	
				//System.out.println(fileStream);
				in = new DataInputStream(new BufferedInputStream(fileStream.getInputStream()));	
				//	Open	the	input	streams	for	the	remote	?le		
				FileOutputStream fOut = new	FileOutputStream(fileName);	
				//	Open	the	output	streams	for	saving	this	?le	on	disk	
				DataOutputStream out = new	DataOutputStream(new	BufferedOutputStream(fOut));	
				//	Read	the	remote	on	save	save	the	?le	
				int	data;	
				while((data=in.read())!=-1){ out.write(data);	}
				out.close();

				if(in	!=	null)	in.close();	
				if(out	!=	null)	out.close();	
				if(fOut	!=	null)	fOut.close();
		
			
				String[][] stockArray = new String[7][7];     
		        File file = new File(fileName);     
		        
		        BufferedReader fileReader  = new BufferedReader(new FileReader(file));     
		        String line = null;  
		        int daysToExamine = 6;
		        int row = 0;     
		        int col = 0;     
		        line = fileReader.readLine();
		        //read each line of text file  
				for(int x = 0; x < daysToExamine; x++)
		        {     
			        line = fileReader.readLine();
		            StringTokenizer st = new StringTokenizer(line,",");  
		            //System.out.println(line);
		            while (st.hasMoreTokens())     
		            {     
		            	//get next token and store it in the array     
		            	stockArray[row][col] = st.nextToken();     
		            	col++;     
		            }    
		            row++;     
		            col = 0;
		        }     
		        //close the file     
		        fileReader.close(); 
		        file.delete();
		        
		        int todaysTradeRate = 0;
		        int tradeRate = 0;
				int today = 0;
				int yesterday = 1;
				final int close = 4;
				final int volume = 5;
				//System.out.println(stockArray[today][close]);
				for(int x = 0; x < daysToExamine - 1; x++)
				{
					if(Double.parseDouble(stockArray[today][close]) > Double.parseDouble(stockArray[yesterday][close])) 
					{
						if(Double.parseDouble(stockArray[today][volume]) > Double.parseDouble(stockArray[yesterday][volume])) todaysTradeRate++;
					}
					else
					{
						todaysTradeRate--;
						if(Double.parseDouble(stockArray[today][volume]) > Double.parseDouble(stockArray[yesterday][volume])) todaysTradeRate--;
					}
					//System.out.println("Trade rate from " + today + " days ago is: " + todaysTradeRate);
					tradeRate = tradeRate + todaysTradeRate;
					todaysTradeRate = 0;
					today++;
					yesterday++;
				}
				dowValues[y] = tradeRate;
				//System.out.println("Trade rate for " + dowArray[y] + " over five days is: " + tradeRate);
			}
			for(int t = 0; t < 30; t++)
			{
				System.out.println("Trade rate for " + dowArray[t] + " over five days is: " + dowValues[t]);
			}	
		}
		catch(MalformedURLException	e){	
			System.out.println("Please	check	the	URL:" + e.toString());	
		}	
		catch	(ConnectException	e){		
			System.out.println(":failed!	Connection	Error!");	
		}
		catch	(Exception	e){	
			e.printStackTrace();		
		}
	}
        
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		constructData();
	}

}
