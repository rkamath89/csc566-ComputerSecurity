import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import gnu.getopt.Getopt;


public class DES {
	static String characters = "abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	static List<String> keyListAfterPc2 = new ArrayList<String>();
	static HashMap<Integer,List<String>> keyRounds = new HashMap<Integer,List<String>>();
	public final static boolean _DEBUG = false;
	public static String _cipherText = new String();
	static String _56BitKey = new String();
	//Use this to make key from 64 to 56 Bit
	public static int[]  PC1 = {
		57, 49, 41, 33, 25, 17, 9 ,
		1 , 58, 50, 42, 34, 26, 18,
		10, 2 , 59, 51, 43, 35, 27,
		19, 11, 3 , 60, 52, 44, 36,
		63, 55, 47, 39, 31, 23, 15,
		7 , 62, 54, 46, 38, 30, 22,
		14, 6 , 61, 53, 45, 37, 29,
		21, 13, 5 , 28, 20, 12, 4 };

	// Split the 56 Bit key into 2 Parts , 28 Bits each . Apply leftShit << rotations[i] for each round(16) on the key obtained previously
	public static int[] rotations = {
		1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
	};
	// Use PC2 after we get the Keys rotated in the previous step. Key will be 56 bit, use this box to make it 48 bit keys [16 such keys]
	public static int[] PC2 = {
		14, 17, 11, 24, 1 , 5 ,
		3 , 28, 15, 6 , 21, 10,
		23, 19, 12, 4 , 26, 8 ,
		16, 7 , 27, 20, 13, 2 ,
		41, 52, 31, 37, 47, 55,
		30, 40, 51, 45, 33, 48,
		44, 49, 39, 56, 34, 53,
		46, 42, 50, 36, 29, 32 };

	// Initial Permutation on the Original Message which is 64 Bit [ Basically rearranging the message]
	public static int[] IP = {
		58, 50, 42, 34, 26, 18, 10, 2
		, 60, 52, 44, 36, 28, 20, 12, 4
		, 62, 54, 46, 38, 30, 22, 14, 6
		, 64, 56, 48, 40, 32, 24, 16, 8
		, 57, 49, 41, 33, 25, 17, 9, 1
		, 59, 51, 43, 35, 27, 19, 11, 3
		, 61, 53, 45, 37, 29, 21, 13, 5
		, 63, 55, 47, 39, 31, 23, 15, 7
	};
	// Expanding the message that we dividen M = L & R || This  is used for the Right Side (R) to expand it from 32bits to 48 Bits
	public static int[] E = {	
		32, 1, 2, 3, 4, 5
		, 4, 5, 6, 7, 8, 9
		, 8, 9, 10, 11, 12, 13
		, 12, 13, 14, 15, 16, 17
		, 16, 17, 18, 19, 20, 21
		, 20, 21, 22, 23, 24, 25
		, 24, 25, 26, 27, 28, 29
		, 28, 29, 30, 31, 32, 1
	};
	// After getting the 48 bit xored value ( R(n) XOR KEY(n))
	// Group this 48 Bit number into 8 Groups of 6 Bits Each
	// Now for each of these groups 8 groups
	// Step1: take the 1st and last bit and find its decimalEquivalent and store it as Row value for S[] Mentioned Below
	// Step2: Take the remaining 4 bits and find decimal equivalent and store it as col value for S[] mentioned below
	// Using the row and column values obtained above find the entry in the S[row][col] and then convert that into corresponding DECIMAL
	// E.g : 011011 . Row = 01 -> 1 Col = 1101 -> 13
	// S[1][13] = 5; -> 0101
	// Repeat this for every group ( 8 Groups * 4 bits ) = 32 bit
	// NOW use the P Box permutation and scramble the 32 bits
	public static final byte[][]S1=
		{
		{14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7},
		{0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8},
		{4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0},
		{15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13}
		};

	public static final byte[][]S2= 
		{

		{15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10},
		{3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5},
		{0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15},
		{13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9}

		};

	public static final byte[][] S3=
		{
		{10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8},
		{13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1},
		{13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7},
		{1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12},
		};

	public static final byte[][] S4=
		{
		{7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15},
		{13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9},
		{10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4},
		{3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14}
		};
	public static final byte[][] S5=
		{

		{2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9},
		{14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6},
		{4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14},
		{11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3}
		};

	public static final byte[][] S6=
		{

		{12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11},
		{10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8},
		{9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6},
		{4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13}
		};

	public static final byte[][] S7=
		{
		{4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1},
		{13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6},
		{1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2},
		{6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12}

		};

	public static final byte[][] S8=
		{
		{13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7},
		{1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2},
		{7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8},
		{2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11}

		};
	// USE THIS AFTER DOING S to permute the 32bit Number obtained from concatenation of s1(b1),s2(b2),s3(b3).......s8(B8)
	public static int[] P = {
		16, 7, 20, 21,
		29, 12, 28, 17,
		1, 15, 23, 26,
		5, 18, 31, 10,
		2, 8, 24, 14,
		32, 27, 3, 9,
		19, 13, 30, 6,
		22, 11, 4, 25};

	// Use this for final permutation
	public static int[] FP = {
		40, 8, 48, 16, 56, 24, 64, 32
		, 39, 7, 47, 15, 55, 23, 63, 31
		, 38, 6, 46, 14, 54, 22, 62, 30
		, 37, 5, 45, 13, 53, 21, 61, 29
		, 36, 4, 44, 12, 52, 20, 60, 28
		, 35, 3, 43, 11, 51, 19, 59, 27
		, 34, 2, 42, 10, 50, 18, 58, 26
		, 33, 1, 41, 9, 49, 17, 57, 25
	};
	static String getRandomString(int len)
	{

		StringBuffer randomStr = new StringBuffer();
		Random rand = new Random(System.currentTimeMillis());
		for(int i=0;i<len;i++)
		{
			int randPos = rand.nextInt();
			int pos = Math.abs(randPos % (characters.length()-1));
			randomStr.append(characters.charAt(pos));
		}
		return randomStr.toString();
	}
	static int convertFromBinaryToDecimal(String binaryStr)
	{
		return Integer.parseInt(binaryStr,2);
	}
	static String convertFromDecimalToHex(int decimal)
	{
		return Integer.toString(decimal,16);
	}
	static String convertDecimalToBinary(String ch)
	{
		return Integer.toBinaryString(Integer.parseInt(ch));
	}
	public static String convertHexToString(String hex)
	{

		  StringBuilder sb = new StringBuilder();
		  StringBuilder temp = new StringBuilder();
		  
		  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
		  for( int i=0; i<hex.length()-1; i+=2 ){
			  
		      //grab the hex in pairs
		      String output = hex.substring(i, (i + 2));
		      //convert hex to decimal
		      int decimal = Integer.parseInt(output, 16);
		      //convert the decimal to character
		      sb.append((char)decimal);
			  
		      temp.append(decimal);
		  }
		  //System.out.println("Decimal : " + temp.toString());
		  
		  return sb.toString();
	  }
	static String convertFromBinaryToHex(String binaryStr)
	{
		StringBuffer hexStr = new StringBuffer();
		for(int i=0;i<binaryStr.length();i=i+4)
		{
			int decimal = Integer.parseInt(binaryStr.substring(i,i+4),2);
			 String val = Integer.toString(decimal,16);
			 hexStr.append(val);
		
		}
		return hexStr.toString();
	}
	static String decrypt(StringBuilder keyStr, StringBuilder inputFile,StringBuilder outputFile,String encryptedCipherFromChat) 
	{
		if(encryptedCipherFromChat.isEmpty())
		{
			try {
				PrintWriter writer = new PrintWriter(outputFile.toString(), "UTF-8");
				List<String> lines = Files.readAllLines(Paths.get(inputFile.toString()), Charset.defaultCharset());
				//String IVStr = lines.get(0);
				//lines.remove(0);
				String encryptedText;

				for (String line : lines) 
				{
					for(int startPos=0;startPos<line.length();startPos=startPos+16)
					{
						String decryptedPlainText = DES_decrypt(null,line.substring(startPos,startPos+16));
						decryptedPlainText = convertFromBinaryToHex(decryptedPlainText);
						if(decryptedPlainText.length() >= 4)
						{
							String val = decryptedPlainText.substring(decryptedPlainText.length()-4, decryptedPlainText.length()-2);
							if("30".equals(val)) // Means last 2 hexChar must indicate the Padding Length
							{
								int len = decryptedPlainText.length();
								String paddingLengthStr = decryptedPlainText.substring(len-2, len);
								int paddingLength =  Integer.parseInt(paddingLengthStr)-30;
								paddingLength = len - paddingLength*2;
								decryptedPlainText = decryptedPlainText.substring(0,paddingLength);

							}
						}
						decryptedPlainText = convertHexToString(decryptedPlainText);
						System.out.println("Decrypted Clear Text is : "+decryptedPlainText);
						//encryptedText = DES_decrypt(IVStr, line);
						writer.print(decryptedPlainText);
					}

				}
				writer.close();
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			return "";
		}
		else
		{
			generateRoundKeysAndPerformConsolidation(keyStr.toString());
			String line = encryptedCipherFromChat;
			String finalDexryptedString = new String();
			for(int startPos=0;startPos<line.length();startPos=startPos+16)
			{
				String decryptedPlainText = DES_decrypt(null,line.substring(startPos,startPos+16));
				decryptedPlainText = convertFromBinaryToHex(decryptedPlainText);
				if(decryptedPlainText.length() >= 4)
				{
					String val = decryptedPlainText.substring(decryptedPlainText.length()-4, decryptedPlainText.length()-2);
					if("30".equals(val)) // Means last 2 hexChar must indicate the Padding Length
					{
						int len = decryptedPlainText.length();
						String paddingLengthStr = decryptedPlainText.substring(len-2, len);
						int paddingLength =  Integer.parseInt(paddingLengthStr)-30;
						paddingLength = len - paddingLength*2;
						decryptedPlainText = decryptedPlainText.substring(0,paddingLength);
					}
				}
				finalDexryptedString += convertHexToString(decryptedPlainText);
			}
			return finalDexryptedString;
		}
	}

	/**
	 * TODO: You need to write the DES encryption here.
	 * @param line
	 */
	private static String DES_decrypt(String iVStr, String encryptedLineInHex) 
	{
		//System.out.println("CipherText in Hex to Decrypt is : "+encryptedLineInHex);
		String plainTextInBinary = getBinaryRepresentationOfHexString(encryptedLineInHex);
		String plainTextAfterIp = applyPCToString(IP, plainTextInBinary);
		List<String> splitPlainTextList = splitTheStringIntoTwoHalves(plainTextAfterIp);
		String leftHalf = splitPlainTextList.get(0);
		String rightHalf = splitPlainTextList.get(1);
		//Ln = Rn-1 
		//Rn = Ln-1 + f(Rn-1,Kn)
		String newLeftHalf = new String();
		String newRightHalf = new String();; 

		for(int i=15;i>=0;i--)
		{
			newLeftHalf = rightHalf;
			String roundKey = keyListAfterPc2.get(i);
			newRightHalf = xor_of_values(leftHalf,feitzelComputation(rightHalf,roundKey));
			rightHalf = newRightHalf;
			leftHalf = newLeftHalf;
		}

		String decryptedPlainText =new String();
		decryptedPlainText = newRightHalf+newLeftHalf;
		decryptedPlainText = applyPCToString(FP, decryptedPlainText);
		return decryptedPlainText;

	}

	static String rotateTheKeyLeft(String key,int rotation)
	{
		char []rotatedKey = new char[key.length()];
		char []OriginalKey = key.toCharArray();

		int keyLen = key.length();
		for(int i=0;i<keyLen;i++)
		{
			int finalPos = Math.abs((keyLen+i-rotation) % key.length());
			rotatedKey[finalPos] = OriginalKey[i];
		}
		return new String(rotatedKey);
	}
	static HashMap<Integer, List<String>>  generateRoundKeys(String _56BitBinaryKeyRep)
	{
		// Logic For the C0...Cn and D0....Dn
		keyRounds.clear();
		List<String> originalSplitKeyList = splitTheStringIntoTwoHalves(_56BitBinaryKeyRep);
		List<String> originalLeftAndRightKeyList = new ArrayList<String>(); // C0 and D0
		originalLeftAndRightKeyList.add(originalSplitKeyList.get(0));originalLeftAndRightKeyList.add(originalSplitKeyList.get(1));
		keyRounds.put(0, originalLeftAndRightKeyList);
		for(int i=1;i<17;i++)
		{
			int shiftBy = rotations[i-1];

			List<String> previousRoundKeys = keyRounds.get(i-1);
			String rotatedLeftKey = rotateTheKeyLeft(previousRoundKeys.get(0), shiftBy);
			String rotatedRightKey = rotateTheKeyLeft(previousRoundKeys.get(1), shiftBy);
			List<String> rotatedKeyList = new ArrayList<String>();
			rotatedKeyList.add(rotatedLeftKey);rotatedKeyList.add(rotatedRightKey);
			keyRounds.put(i, rotatedKeyList);

		}
		return keyRounds;
	}
	static String feitzelComputation(String rightHalfOfString,String roundKeyString)
	{
		String expandedRightHalf = convert_32to48(rightHalfOfString); // 48 Bits
		String roundKeyXorWithexpandedRightHalf = xor_of_values(expandedRightHalf, roundKeyString); // 48 Bits
		StringBuffer postSubstution32BitBlock = new StringBuffer();
		String block1 = roundKeyXorWithexpandedRightHalf.substring(0,6);
		String block2 = roundKeyXorWithexpandedRightHalf.substring(6,12);
		String block3 = roundKeyXorWithexpandedRightHalf.substring(12,18);
		String block4 = roundKeyXorWithexpandedRightHalf.substring(18,24);
		String block5 = roundKeyXorWithexpandedRightHalf.substring(24,30);
		String block6 = roundKeyXorWithexpandedRightHalf.substring(30,36);
		String block7 = roundKeyXorWithexpandedRightHalf.substring(36,42);
		String block8 = roundKeyXorWithexpandedRightHalf.substring(42,48);
		postSubstution32BitBlock = sBoxConversion(block1,S1);
		postSubstution32BitBlock.append(sBoxConversion(block2, S2));
		postSubstution32BitBlock.append(sBoxConversion(block3, S3));
		postSubstution32BitBlock.append(sBoxConversion(block4, S4));
		postSubstution32BitBlock.append(sBoxConversion(block5, S5));
		postSubstution32BitBlock.append(sBoxConversion(block6, S6));
		postSubstution32BitBlock.append(sBoxConversion(block7, S7));
		postSubstution32BitBlock.append(sBoxConversion(block8, S8));
		String final32BitStringAfterFietzel = applyPCToString(P, postSubstution32BitBlock.toString()); // 32 Bit String
		return final32BitStringAfterFietzel;
		// Put fietzel Here
	}
	static String encryptFileContents(String plainText,boolean decrypt)
	{

		String plainTextInHex = getHexRepresentationOfString(plainText);//"0123456789abcdef";
		//System.out.println("PlainText to encrypt is : "+plainText);
		//System.out.println("PlainText in HEX is : "+plainTextInHex);
		String plainTextInBinary = getBinaryRepresentationOfHexString(plainTextInHex);
		String plainTextAfterIp = applyPCToString(IP, plainTextInBinary);
		List<String> splitPlainTextList = splitTheStringIntoTwoHalves(plainTextAfterIp);
		String leftHalf = splitPlainTextList.get(0);
		String rightHalf = splitPlainTextList.get(1);
		//Ln = Rn-1 
		//Rn = Ln-1 + f(Rn-1,Kn)
		String newLeftHalf = new String();
		String newRightHalf = new String();; 
		for(int i=0;i<16;i++)
		{
			newLeftHalf = rightHalf;
			String roundKey = keyListAfterPc2.get(i);
			newRightHalf = xor_of_values(leftHalf,feitzelComputation(rightHalf,roundKey));
			rightHalf = newRightHalf;
			leftHalf = newLeftHalf;
		}

		String cipherText =new String();
		cipherText = newRightHalf+newLeftHalf;
		_cipherText = applyPCToString(FP, cipherText);
		return _cipherText;

	}

	public static StringBuffer sBoxConversion(String inp,byte[][] s_box)
	{
		StringBuffer sb=new StringBuffer();
		String output="";
		sb.append(inp.charAt(0));
		sb.append(inp.charAt(inp.length()-1));
		String row=sb.toString();
		int row_num=Integer.parseInt(row,2);
		String col=inp.substring(1,inp.length()-1);
		int col_num=Integer.parseInt(col,2);
		int new_val=s_box[row_num][col_num];
		String ch=String.valueOf(new_val);
		output=convertDecimalToBinary(ch);
		while(output.length()<4)
		{
			output="0"+output;
		}
		return new StringBuffer(output);
	}
	public static String convert_32to48(String input)
	{
		char temp_1[]=new char[E.length];
		for(int i=0;i<48;i++)
		{
			int index=E[i]-1;
			temp_1[i]=input.charAt(index);
		}
		return new String(temp_1);

	}
	public static String xor_of_values(String inp1,String inp2)
	{
		char temp_xor[]=new char[inp1.length()];
		for(int i=0;i<inp1.length();i++)
		{
			if(inp1.charAt(i)==inp2.charAt(i))
				temp_xor[i]='0';
			else
				temp_xor[i]='1';
		}
		return new String(temp_xor);
	}

	static String encrypt(StringBuilder keyStr, StringBuilder inputFile,StringBuilder outputFile,String lineFromChat) 
	{
		String encryptedPlainTextInHex = "";
		if(lineFromChat.isEmpty())
		{
			try 
			{
				PrintWriter writer = new PrintWriter(outputFile.toString(), "UTF-8");			
				List<String> allLines = Files.readAllLines(Paths.get(inputFile.toString()), Charset.defaultCharset());
				for (String line : allLines) 
				{
					int paddingReq = line.length() % 8;
					paddingReq = 8-paddingReq;
					if(paddingReq != 0 ) // Means we Need to pad the Line
					{
						StringBuffer paddedStr = new StringBuffer();
						for(int i=0;i<paddingReq-1;i++)
						{
							paddedStr.append('0');
						}
						paddedStr.append(paddingReq);
						line = line+paddedStr;
					}
					for(int startPos=0;startPos<line.length();startPos=startPos+8)
					{
						String encryptedPlainTextInBinary =  encryptFileContents(line.substring(startPos,startPos+8),false);
						encryptedPlainTextInHex = convertFromBinaryToHex(encryptedPlainTextInBinary);
						writer.print(encryptedPlainTextInHex);
					}

				}
				writer.close();
			} 
			catch (Exception e) 
			{
				System.out.println("Crashed During ENCRYPT");
				e.printStackTrace();
			}
			return encryptedPlainTextInHex;
		}
		else
		{		
			generateRoundKeysAndPerformConsolidation(keyStr.toString());
			String line = lineFromChat;
			int paddingReq = line.length() % 8;
			paddingReq = 8-paddingReq;
			if(paddingReq != 0 ) // Means we Need to pad the Line
			{
				StringBuffer paddedStr = new StringBuffer();
				for(int i=0;i<paddingReq-1;i++)
				{
					paddedStr.append('0');
				}
				paddedStr.append(paddingReq);
				line = line+paddedStr;
			}
			for(int startPos=0;startPos<line.length();startPos=startPos+8)
			{
				String encryptedPlainTextInBinary =  encryptFileContents(line.substring(startPos,startPos+8),false);
				encryptedPlainTextInHex += convertFromBinaryToHex(encryptedPlainTextInBinary);
			}
			return encryptedPlainTextInHex;
		}
	}
	/**
	 * TODO: You need to write the DES encryption here.
	 * @param line
	 */
	private static String DES_encrypt(String line) {

		return null;
	}

	static String getBinaryRepresentationOfHexString(String hexSting)
	{
		HashMap<String,Integer> hexToInt = new HashMap<String,Integer>();
		hexToInt.put("a",10);hexToInt.put("b",11);hexToInt.put("c",12);hexToInt.put("d",13);hexToInt.put("e",14);hexToInt.put("f",15);

		char[] hexChar = hexSting.toString().toCharArray();
		StringBuffer binaryKeyRep = new StringBuffer();
		for (int i = 0; i < hexChar.length; i++)
		{
			String ch = Character.toString(hexChar[i]).toLowerCase();
			if(hexToInt.containsKey(ch))
			{
				ch = hexToInt.get(ch).toString();
			}
			String binaryRep = Integer.toBinaryString(Integer.parseInt(ch));
			while(binaryRep.length() < 4)
			{
				binaryRep = "0" + binaryRep;
			}
			binaryKeyRep.append(binaryRep);
		}
		return binaryKeyRep.toString();
	}
	static String getHexRepresentationOfString(String keySting)
	{
		StringBuffer hexKey = new StringBuffer();
		char[] chars = keySting.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			String hexVal = convertFromDecimalToHex((int)chars[i]);
			hexKey.append(hexVal);
			//hexKey.append(Integer.toHexString((int) chars[i]));
		}
		return hexKey.toString();
	}
	static String applyPCToString(int []permutation,String OriginalKey)
	{
		char []oldKey = OriginalKey.toCharArray();
		char []newKey = new char[permutation.length];
		for(int i=0;i<permutation.length;i++)
		{
			int pos = permutation[i]-1;
			newKey[i] = oldKey[pos];
		}
		return new String(newKey);
	}
	static List<String> splitTheStringIntoTwoHalves(String key)
	{
		List<String> splitKey = new ArrayList<String>();
		int mid = key.length()/2;
		splitKey.add(key.substring(0, mid));
		splitKey.add(key.substring(mid,key.length()));
		return splitKey;
	}
	static String genDESkey()
	{	
		String keyString = getRandomString(8);	
		return getHexRepresentationOfString(keyString);//"133457799BBCDFF1";
	}
    static void generateRoundKeysAndPerformConsolidation(String hexKeyRep)
    {
    	String binaryKeyRep = getBinaryRepresentationOfHexString(hexKeyRep.toString());
		_56BitKey = applyPCToString(PC1, binaryKeyRep);
		generateRoundKeys(_56BitKey);
		consolidateRoundKeys();
		//System.out.println("Key : "+hexKeyRep);
		if(_DEBUG)
		{
			System.out.println("Hex Key :"+hexKeyRep.toString());
			System.out.println("Round Keys Are : ");
			Set<Integer> keys = keyRounds.keySet();
			for(Integer keyVal : keys)
			{
				System.out.println("Key for Round "+keyVal+" is "+keyRounds.get(keyVal));
			}
		}
    }
	static void consolidateRoundKeys()
	{
		keyListAfterPc2.clear();
		List<String> concanatedKeyString = new ArrayList<String>();
		Set<Integer> keySet = keyRounds.keySet();
		for(Integer key:keySet)
		{
			List<String> keyValue = keyRounds.get(key);
			StringBuffer concatenatedKey = new StringBuffer();
			concatenatedKey.append(keyValue.get(0)).append(keyValue.get(1));
			concanatedKeyString.add(concatenatedKey.toString());
		}
		for(int i=1;i<17;i++)
		{
			String finalKey = applyPCToString(PC2, concanatedKeyString.get(i));
			keyListAfterPc2.add(finalKey);
		}
	}
	/**
	 * This function Processes the Command Line Arguments.
	 * -p for the port number you are using
	 * -h for the host name of system
	 */
	private static void pcl(String[] args, StringBuilder inputFile,
			StringBuilder outputFile, StringBuilder keyString,
			StringBuilder encrypt,StringBuilder decrypt) {
		/*
		 * http://www.urbanophile.com/arenn/hacking/getopt/gnu.getopt.Getopt.html
		 */	
		Getopt g = new Getopt("Chat Program", args, "hke:d:i:o:");
		int c;
		String arg;
		while ((c = g.getopt()) != -1){
			switch(c){
			case 'o':
				arg = g.getOptarg();
				outputFile.append(arg);
				break;
			case 'i':
				arg = g.getOptarg();
				inputFile.append(arg);
				break;
			case 'e':
				arg = g.getOptarg();
				keyString.append(arg);
				encrypt.append("e");
				break;
			case 'd':
				arg = g.getOptarg();
				keyString.append(arg);
				decrypt.append("d");
				break;
			case 'k':
				System.out.println("Generated Key is : "+genDESkey());
				System.out.println();
				break;
			case 'h':
				callUseage(0);
			case '?':
				break; // getopt() already printed an error
				//
			default:
				break;
			}
		}

	}

	private static void callUseage(int exitStatus) {

		System.out.println("-h : Help Options");
		System.out.println("-k : Generate DES key and Encode in HEX and Display");
		System.out.println("-e : Encrypt the file [e.g: java DES -e <64 bit key in hex> -i <input file> -o <output file>");
		System.out.println("-d : Decrypt the file [e.g:java DES -d <64 bit key in hex> -i <input file> -o <output file>");

	}
	public static void main(String[] args) 
	{

		StringBuilder inputFile = new StringBuilder();
		StringBuilder outputFile = new StringBuilder();
		StringBuilder keyStr = new StringBuilder();
		StringBuilder encrypt = new StringBuilder();
		StringBuilder decrypt = new StringBuilder();
		pcl(args, inputFile, outputFile, keyStr, encrypt,decrypt);
		if(keyStr.toString() != "" && encrypt.toString().equals("e"))
		{
			generateRoundKeysAndPerformConsolidation(keyStr.toString());
			//System.out.println("----ENCRYPTION-----");
			String encryptedPlainText = encrypt(keyStr, inputFile, outputFile,"");
			//System.out.println("Encrypted Plain Text in HEX is : "+encryptedPlainText);
			//System.out.println("----ENCRYPTION COMPLETED-----");
		} 
		if(keyStr.toString() != "" && decrypt.toString().equals("d"))
		{
			//System.out.println("----DECRYPTION-----");
			generateRoundKeysAndPerformConsolidation(keyStr.toString());
			decrypt(keyStr, inputFile, outputFile,"");
			//System.out.println("----DECRYPTION COMPLETED-----");
			
		}

	}

}