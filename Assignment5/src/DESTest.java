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
		List<String> splitKey = DES.splitTheStringIntoTwoHalves(_56BitBinaryKey);
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
		String _56BitBinaryKey = "11110000110011001010101011110101010101100110011110001111";
		HashMap<Integer,List<String>> roundKeys = DES.generateRoundKeys(_56BitBinaryKey);
		String leftHalf = roundKeys.get(0).get(0);
		String rightHalf = roundKeys.get(0).get(1);
		Assert.assertEquals("1111000011001100101010101111",leftHalf);
		Assert.assertEquals("0101010101100110011110001111",rightHalf);
		leftHalf = roundKeys.get(1).get(0);
		rightHalf = roundKeys.get(1).get(1);
		Assert.assertEquals("1110000110011001010101011111",leftHalf);
		Assert.assertEquals("1010101011001100111100011110",rightHalf);
		leftHalf = roundKeys.get(2).get(0);
		rightHalf = roundKeys.get(2).get(1);
		Assert.assertEquals("1100001100110010101010111111",leftHalf);
		Assert.assertEquals("0101010110011001111000111101",rightHalf);
		leftHalf = roundKeys.get(14).get(0);
		rightHalf = roundKeys.get(14).get(1);
		Assert.assertEquals("1111111000011001100101010101",leftHalf);
		Assert.assertEquals("1110101010101100110011110001",rightHalf);
		leftHalf = roundKeys.get(15).get(0);
		rightHalf = roundKeys.get(15).get(1);
		Assert.assertEquals("1111100001100110010101010111",leftHalf);
		Assert.assertEquals("1010101010110011001111000111",rightHalf);
		leftHalf = roundKeys.get(16).get(0);
		rightHalf = roundKeys.get(16).get(1);
		Assert.assertEquals("1111000011001100101010101111",leftHalf);
		Assert.assertEquals("0101010101100110011110001111",rightHalf);
		
	}
	@Test
	public void consolidateRoundKeysTest()
	{
		String _56BitBinaryKey = "11110000110011001010101011110101010101100110011110001111";
		HashMap<Integer,List<String>> roundKeys = DES.generateRoundKeys(_56BitBinaryKey);
		DES.consolidateRoundKeys();
		String key = DES.keyListAfterPc2.get(0);
		Assert.assertEquals("000110110000001011101111111111000111000001110010",key);
		key = DES.keyListAfterPc2.get(4); // Actually 5
		Assert.assertEquals("011111001110110000000111111010110101001110101000",key);
		key = DES.keyListAfterPc2.get(14); // Actually 15
		Assert.assertEquals("101111111001000110001101001111010011111100001010",key);
		key = DES.keyListAfterPc2.get(15); //  Actually 16
		Assert.assertEquals("110010110011110110001011000011100001011111110101",key);
		
	}
	public void IntitialPermuatationTest()
	{
		String plainTextInBinary="0000000100100011010001010110011110001001101010111100110111101111";
		String plainTextAfterIP = DES.applyPCToKey(DES.IP, plainTextInBinary);
		Assert.assertEquals("1100110000000000110011001111111111110000101010101111000010101010", plainTextAfterIP);
	}
}
