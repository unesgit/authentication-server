package ma.cam.auth.dto.result.authentication;

import lombok.Data;

@Data
public class ApplicationResult {

	private String code;
	private String libelle;
	private String titre;
	private String router;
	private String parametre;

}
