package ma.cam.auth.dto.result.authentication;

import java.util.Date;

import lombok.Data;

@Data
public class InfoUserResult {
	private Long identifiant;
	private String nom;
	private String prenom;
	private Long idEntite;
	private String codeEntite;
	private String nomEntite;
	private Long typeEntite;
	private Long idEmploi;
	private String nomEmploi;
	private Date dateExpirationMotPasse;

}
