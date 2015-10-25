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

}
