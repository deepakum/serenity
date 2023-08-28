/*
 ******************************************************************
 *                Confidentiality Information:                    
 *                                                                
 * This module is the confidential and proprietary information of 
 * Wipro Limited; it is not to be copied, reproduced, or         
 * transmitted in any form, by any means, in whole or in part,    
 * nor is it to be used for any purpose other than that for which 
 * it is expressly provided without the written permission of     
 * Wipro Limited.                                                
 *
 ******************************************************************
 *                                                                
 * PROGRAM DESCRIPTION:     
 * Batch Program to process System Recovery Transaction records.
 * 
 *                                                                
 *****************************************************************
 *                                                                
 * CHANGE HISTORY:                                                
 *                                                                
 * Date:        by:     		Reason:                                     
 * DD-MM-YYYY   IN      		Reason text.                   
 * 06-01-2020	JO40031926		Initial Version.      
 *                                                                
 *****************************************************************/

package Utility.ABT;

import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author JO40031926
 *
@BatchJob (modules = {"demo"},
 *      softParameters = { @BatchJobSoftParameter (name = paramOne, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramTwo, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramThree, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramFour, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramFive, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramSix, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramSeven, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramEight, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramNine, required = false, type = string)
 *            , @BatchJobSoftParameter (name = paramTen, required = false, type = string)})
 */
public class CmGenerateChecksumBatch{
	private static Logger logger = LoggerFactory.getLogger(CmGenerateChecksumBatch.class);

		private String paramOne = CmConstants.EMPTY_STRING;
		private String paramTwo = CmConstants.EMPTY_STRING;
		private String paramThree = CmConstants.EMPTY_STRING;
		private String paramFour = CmConstants.EMPTY_STRING;
		private String paramFive = CmConstants.EMPTY_STRING;
		private String paramSix = CmConstants.EMPTY_STRING;
		private String paramSeven = CmConstants.EMPTY_STRING;
		private String paramEight = CmConstants.EMPTY_STRING;
		private String paramNine = CmConstants.EMPTY_STRING;
		private String paramTen = CmConstants.EMPTY_STRING;
		
		public static void checkSHA512(String outputPath,String filename) throws Exception{
		      
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-512");
		         String checkSumData = "";
		      String inputFile="";
		      StringBuffer checkSumDataFromInputFile = new StringBuffer();
		      if (filename.indexOf(".") > 0)
				{
		    	  filename = filename.substring(0, filename.lastIndexOf("."));
				}
		      File inXmlFile=new File(outputPath + File.separator + filename.concat(".xml"));
		        try {
		        	byte mdBytes[] = md.digest(Files.readAllBytes(Paths.get(inXmlFile.getPath())));
					for (int i=0;i<mdBytes.length;i++) 
					{
						checkSumDataFromInputFile.append(Integer.toString((mdBytes[i]&0xff)+0x100,16).substring(1));              
					}
		        
					checkSumData="SHA512("+filename.concat(".xml")+")= "+checkSumDataFromInputFile.toString(); 
		        
				File checksumFile = new File(outputPath + File.separator
						+ filename.concat(CmConstants.CSEXTENSTION) );
				
				PrintWriter out = new PrintWriter(checksumFile);
				out.print(checkSumData);
				out.close();
		        }
		        catch(IOException e)
		        {
		        	e.printStackTrace();
		        	System.out.println("IO Exception");
		        }
		  }
		
		public static String checkSHAABT(String outputPath, String fileType)throws Exception
		{
			String filename = Arrays.stream(new File(outputPath).listFiles()).filter(file->file.getName().contains(fileType))
							.findFirst().get().getName();
			logger.info("---------------ABT CHecksum Started------------");
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-384");
			String checkSumData = "";
			String inputFile = "";
			File file = new File(outputPath + File.separator + filename);
			md = MessageDigest.getInstance("SHA-384");
			Path path = Paths.get(file.getAbsolutePath());
			byte mdBytes[] = md.digest(Files.readAllBytes(path));
			StringBuffer hexString = new StringBuffer();
			String checksumOfFile = "";
			for (int i=0;i<mdBytes.length;i++) 
			{
				hexString.append(Integer.toString((mdBytes[i]&0xff)+0x100,16).substring(1));		    	
			}
			
			checksumOfFile = hexString.toString();
			logger.info("--------------checkSUm-----------"+checksumOfFile);
//			File checksumFile = new File(outputPath + File.separator + filename+".CS");
//			PrintWriter out = new PrintWriter(checksumFile);
//			out.print(checksumOfFile);
//			out.close();
			return checksumOfFile;
		}
		
		//Checksum Generation Logic for EPS2 and ERP2
		public void generateCheckSumFile(String filePath, String fileName)
		{
			try 
			{
				SHA3.DigestSHA3 md = new SHA3.Digest512();
				String checkSumData = "";
	                 
		        byte[] digest = md.digest(Files.readAllBytes(Paths.get(filePath+File.separator+fileName)));
		            checkSumData = Hex.toHexString(digest); 
		        File checksumFile = new File(fileName+".CS");
	            
		            PrintWriter out = new PrintWriter(filePath + File.separator + checksumFile);
		            out.print(checkSumData);
		            out.close();
		            logger.info("Check Sum file created successfully ");
				}
				catch(Exception e)
				{
		            logger.info("Error occured while generating Check Sum: "+e.getMessage());
				}
		}

	public static void main(String[] args) throws Exception {
		checkSHAABT("C:\\Users\\anilkodam\\IdeaProjects\\CCRS\\src\\test\\java\\Utility\\ABT","LTA-ABT-CCRS-WOSF-D-20230428");
	}
	}
