package util;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 
 * @author jack
 * 
 */
public class PkCertFactory {

    private static X509Certificate umpCert;

    /**
     * 获取签名私钥
     * 
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String pkSuffix) throws Exception {
        String privateStr = ProFileUtil.getFileStr(pkSuffix);
        PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(
                Base64.decode(privateStr));
        PrivateKey pk;
        KeyFactory kf = KeyFactory.getInstance("RSA");
        pk = kf.generatePrivate(peks);
        return pk;
    }

    /**
     * 获取证书文件
     * 
     * @return
     */
    public static X509Certificate getCert(String platCertPath) {
        try {
            byte b[] = ProFileUtil.getFileByte(platCertPath);
            umpCert = getCert(b);
        } catch (Exception ex) {
            RuntimeException rex = new RuntimeException(ex.getMessage());
            rex.setStackTrace(ex.getStackTrace());
            throw rex;
        }
        return umpCert;
    }

    private static X509Certificate getCert(byte b[]) throws Exception {
        X509Certificate x509Certificate;
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        x509Certificate = (X509Certificate) cf.generateCertificate(bais);
        return x509Certificate;
    }
}
