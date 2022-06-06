package ma.cam.auth.jwt.signature;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Data
public class JwtPrivateKeyProvider {

	@Value("${jwt.token.key-store-path}")
	private String keyStorePath;

	@Value("${jwt.token.key-store-alias}")
	private String keyAlias;

	@Value("${jwt.token.key-store-password}")
	private String keyStorePass;

	private Key key;

	/**
	 * 
	 */
	@PostConstruct
	private void loadPrivateKey() {
		try {
			try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile(keyStorePath))) {
				log.debug("loading jwt private key");
				KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
				ks.load(fis, keyStorePass.toCharArray());
				key = ks.getKey(keyAlias, keyStorePass.toCharArray());
				log.debug("jwt private key is successfully loaded");
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

}
