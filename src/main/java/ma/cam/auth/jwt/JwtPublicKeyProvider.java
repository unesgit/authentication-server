package ma.cam.auth.jwt;

import java.io.FileInputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import lombok.Data;

@Component
@Data
public class JwtPublicKeyProvider {

	@Value("${jwt.token.public-key-path}")
	private String publicKeyPath;

	private PublicKey publicKey;

	@PostConstruct
	private void loadPublicKey() {
		try {
			try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(publicKeyPath))) {
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				Certificate cert = cf.generateCertificate(fis);
				publicKey = cert.getPublicKey();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}
