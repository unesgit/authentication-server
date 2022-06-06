package ma.cam.auth.dto.result.authentication;

import lombok.Data;

@Data
public class ProfilResult {

	private Long identifiant;
	private String code;
	private String nom;
	private Long niveau;
	private Long groupe;
	private String application;
	private String pardefaut;

}
