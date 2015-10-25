import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class DESTest {

	@Test
	public void TestBinaryRepresentationOfHexString() 
	{
		String binaryRep = DES.getBinaryRepresentationOfHexString("ab");
		String expectedBinaryRep = "1010"+"1011";
		Assert.assertEquals(expectedBinaryRep,binaryRep);
	}
	
	@Test
	public void testKeyGen() 
	{
	   String randomKey = "vjdXgVqX";
	   String expectedHexString = "766a645867567158";
	   String fetchedHexString = DES.getHexRepresentationOfString(randomKey);
	   Assert.assertEquals(expectedHexString,fetchedHexString);
	   Assert.assertEquals(16,fetchedHexString.length());
	}
	@Test
	public void testKeySplitLogic()
	{
		String _56BitBinaryKey = "00000000110111111111000110111011010100010011010010001111";
		String leftHalf = "0000000011011111111100011011";
		String rightHalf = "1011010100010011010010001111";
		List<String> splitKey = DES.splitTheKeyIntoTwoHalves(_56BitBinaryKey);
		Assert.assertEquals(leftHalf, splitKey.get(0));
		Assert.assertEquals(rightHalf, splitKey.get(1));
	}
	@Test
	public void testRotationOfKey()
	{
		String roatedKey = DES.rotateTheKeyLeft("111000",3);
		Assert.assertEquals("000111",roatedKey);
		roatedKey = DES.rotateTheKeyLeft("0101110",2);
		Assert.assertEquals("0111001",roatedKey);
		
	}
	@Test
	public void generateRoundKeysTest()
	{
		String _56BitBinaryKey = "00000000110111111111000110111011010100010011010010001111";
		HashMap<Integer,List<String>> roundKeys = DES.generateRoundKeys(_56BitBinaryKey);
		
	}
}
