package net.sourceforge.stripes.util;

import org.testng.annotations.Test;
import org.testng.Assert;
import net.sourceforge.stripes.mock.MockHttpServletRequest;
import net.sourceforge.stripes.mock.MockHttpSession;
import net.sourceforge.stripes.StripesTestFixture;

import javax.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;

/**
 * Basic tests for the CryptoUtil
 *
 * @author Tim Fennell
 */
public class CryptoUtilTest {

    /** Manufactures a mock request. */
    protected HttpServletRequest getRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest("/foo", "/bar");
        request.setSession(new MockHttpSession(StripesTestFixture.getServletContext()));
        return request;
    }

    @Test(groups="fast")
    public void basicEncryptionTest() throws Exception {
        String input = "A Basic String to Encrypt";
        String encrypted = CryptoUtil.encrypt(input);
        String decrypted = CryptoUtil.decrypt(encrypted);

        Assert.assertFalse(input.equals(encrypted), "Encrypted string should be different!");
        Assert.assertTrue(input.equals(decrypted), "Decrypted string should match!");
    }

    @Test(groups="fast")
    public void encryptEmptyStringTest() throws Exception {
        String input = "";
        String encrypted = CryptoUtil.encrypt(input);
        String decrypted = CryptoUtil.decrypt(encrypted);

        Assert.assertFalse(input.equals(encrypted), "Encrypted string should be different!");
        Assert.assertTrue(input.equals(decrypted), "Decrypted string should match!");
    }

    @Test(groups="fast")
    public void encryptNullTest() throws Exception {
        String input = null;
        String encrypted = CryptoUtil.encrypt(input);

        Assert.assertNull(encrypted, "Encrypting null should give back null.");
    }

    @Test(groups="fast")
    public void decryptNullTest() throws Exception {
        String input = null;
        String decrypted = CryptoUtil.decrypt(input);

        Assert.assertNull(decrypted, "Decrypting null should give back null.");
    }
    
    @Test(groups = "fast")
    public void decryptBogusInputTest() throws Exception {
        String input = "_sipApTvfAXjncUGTRUf4OwZJBdz4Mbp2ZxqVyzkKio=";
        String decrypted = CryptoUtil.decrypt(input);
        Assert.assertNull(decrypted, "Decrypting a bogus input should give back null.");
    }

    @Test(groups="fast")
    public void replacementKeyTest() throws Exception {
        SecretKey oldKey = CryptoUtil.getSecretKey(); // cache the old key

        try {
            KeyGenerator gen = KeyGenerator.getInstance("AES");
            SecretKey key = gen.generateKey();
            CryptoUtil.setSecretKey(key);
            String input = "A string to be encrypted with a different algorigthm and key!";
            String output = CryptoUtil.encrypt(input);
            String result = CryptoUtil.decrypt(output);

            Assert.assertEquals(input, result);
        }
        finally {
            CryptoUtil.setSecretKey(oldKey);
        }
    }

}