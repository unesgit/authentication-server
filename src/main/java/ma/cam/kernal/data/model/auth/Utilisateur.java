package ma.cam.kernal.data.model.auth;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

	@Id
    @SequenceGenerator(name = "my_seq_user", sequenceName = "SEQ_UTILISATEUR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_user")
	@Column(name="identifiant")
	private Long id;
    @Column(name="login")
    private String username;
    @Column(name="password")
    @JsonIgnore
    private String motPasse;
    
   
    
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "UTILISATEUR_ROLE", joinColumns = {
            @JoinColumn(name = "ID_UTILISATEUR") }, inverseJoinColumns = {
            @JoinColumn(name = "ID_ROLE") })
    private Set<Role> roles;


	public Long getI() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getMotPasse() {
		return motPasse;
	}


	public void setMotPasse(String motPasse) {
		this.motPasse = motPasse;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
    
    
}
