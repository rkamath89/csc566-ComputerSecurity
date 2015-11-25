import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gnu.getopt.Getopt;


public class RSA {

	static final String MIN_KEY_SIZE = "1024";
	static BigInteger ONE = BigInteger.ONE;
	static BigInteger e  = new BigInteger("65537"); //fixed value
	public static void main(String[] args){
		StringBuilder bitSizeStr = new StringBuilder();
		StringBuilder generateKey = new StringBuilder();
		StringBuilder nStr = new StringBuilder();
		StringBuilder decryptionKey = new StringBuilder();
		StringBuilder encryptionKey = new StringBuilder();
		StringBuilder message = new StringBuilder();
		
		pcl(args, generateKey,bitSizeStr, nStr, decryptionKey, encryptionKey,message);
		if(!bitSizeStr.toString().isEmpty())
		{
			//This means you want to create a new key
			genRSAkey(bitSizeStr);
		}
		
		if(!encryptionKey.toString().equalsIgnoreCase("")){
			RSAencrypt(message, nStr, encryptionKey,false);
		}
		
		if(!decryptionKey.toString().equalsIgnoreCase("")){
			RSAdecrypt(message, nStr, decryptionKey,false);
		}
		
		
	}



	static String RSAencrypt(StringBuilder message, StringBuilder nStr, StringBuilder encryptionString,boolean isFromChat) 
	{
		BigInteger plaintext = new BigInteger(message.toString(),16);
		BigInteger e = new BigInteger(encryptionString.toString(),16);
		BigInteger n = new BigInteger(nStr.toString(),16);
		BigInteger cipherText = plaintext.modPow(e,n);
		if(!isFromChat)
			System.out.println("Cipher Text is : "+cipherText.toString(16));
		return cipherText.toString(16);
	}

	static String RSAdecrypt(StringBuilder message, StringBuilder nStr,StringBuilder decryptionString,boolean isFromChat)
	{
		BigInteger cipherText = new BigInteger(message.toString(),16);
		BigInteger d = new BigInteger(decryptionString.toString(),16);
		BigInteger n = new BigInteger(nStr.toString(),16);
		BigInteger plainText = cipherText.modPow(d,n);
		if(!isFromChat)
			System.out.println("Plain Text is : "+plainText.toString(16));
		return plainText.toString(16);
		
	}
	
	static List<String> genRSAkey(StringBuilder bitSizeStr) 
	{
		Random rnd = new SecureRandom();
		rnd.setSeed(System.currentTimeMillis());
		
		// Step 1 Generate two Primes
		BigInteger p = BigInteger.probablePrime(Integer.valueOf(bitSizeStr.toString()), rnd);
		System.out.println("p Length :: "+p.bitLength());
		BigInteger q = BigInteger.probablePrime(Integer.valueOf(bitSizeStr.toString()), rnd);
		System.out.println("q Length :: "+q.bitLength());
		//Step 2 Calculate N = p x q
		BigInteger n = p.multiply(q);
		// Step 3 : Compute phi(n) = (p-1) x (q-1)
		BigInteger p1 = p.subtract(ONE);
		BigInteger q1 = q.subtract(ONE);
		BigInteger phi = p1.multiply(q1);
		//Step 4 : get a value for e , I am using a constant defined above
		
		//step 5 : compute d = e(inverse) mod phi(n)
		BigInteger d = e.modInverse(phi);
		
		//For Printing 
		String eInHex = e.toString(16);
		String dInHex = d.toString(16);
		String nInHex = n.toString(16);
		BigInteger temp = new BigInteger(dInHex,16);
		System.out.println("Your Computed Public Key(e,n) is : "+eInHex+" , "+nInHex);
		System.out.println("Your Computed Secret Key(d,n) is : "+dInHex+" , "+nInHex);
		List<String> rsaKeys = new ArrayList<String>();
		rsaKeys.add(eInHex);
		rsaKeys.add(dInHex);
		rsaKeys.add(nInHex);
		return rsaKeys;
	}


	/**
	 * This function Processes the Command Line Arguments.
	 */
	private static void pcl(String[] args, StringBuilder generateKey,StringBuilder bitSizeStr,
							StringBuilder nStr, StringBuilder decryptionKey, StringBuilder encryptionKey,
							StringBuilder m) {
		/*
		 * http://www.urbanophile.com/arenn/hacking/getopt/gnu.getopt.Getopt.html
		*/	
		Getopt g = new Getopt("Chat Program", args, "hke:d:b:n:i:");
		int c;
		String arg;
		while ((c = g.getopt()) != -1){
		     switch(c){
		     	  case 'i':
		        	  arg = g.getOptarg();
		        	  m.append(arg);
		        	  break;
		          case 'e':
		        	  arg = g.getOptarg();
		        	  encryptionKey.append(arg);
		        	  break;
		     	  case 'n':
		        	  arg = g.getOptarg();
		        	  nStr.append(arg);
		        	  break;
		     	  case 'd':
		        	  arg = g.getOptarg();
		        	  decryptionKey.append(arg);
		        	  break;
		          case 'k':
		        	  generateKey.append("Y"); // I am using this to know if i need to Generate a key
		        	  bitSizeStr.append(MIN_KEY_SIZE);// This is Default
		        	  break;
		     	  case 'b':
		        	  arg = g.getOptarg();
		        	  bitSizeStr.setLength(0); // Clear it if i set a default value as per -k
		        	  bitSizeStr.append(arg);
		        	  break;
		          case 'h':
		        	  callUsage(0);
		          case '?':
		            break; // getopt() already printed an error
		          default:
		              break;
		       }
		   }
	}
	
	private static void callUsage(int exitStatus) {

		System.out.println("java RSA -h : Help Options");
		System.out.println("java RSA -k -b <bit size>: Generate a public/private key pair, and print these, on standard output, one per line,encoded in hex,size of the key should be <bit size> bits.");
		System.out.println("java RSA -e <public key> -i <plaintext value> : encrypt the integer <plaintext value> (encoded in hex) using <public key> and print the result (in hex) to standard output.");
		System.out.println("java RSA -d <private key> -i <ciphertext value>: decrypt the <ciphertext value> using <private key> and print the result to standard output (encoded in hex)");

	}


}
