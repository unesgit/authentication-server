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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

@Entity
public class Menu {

	@Id
    @SequenceGenerator(name = "my_seq_menu", sequenceName = "SEQ_MENU", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_menu")
    private long id;
    @Column
    private String designation;
    @Column
    private String icon;
    @Column
    private String route;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_MENU_PARENT")
    private Set<Menu> sousMenu;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "MENU_ROLES", joinColumns = {
            @JoinColumn(name = "ID_MENU") }, inverseJoinColumns = {
            @JoinColumn(name = "ID_ROLE") })
    private Set<Role> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Menu> getSousMenu() {
		return sousMenu;
	}

	public void setSousMenu(Set<Menu> sousMenu) {
		this.sousMenu = sousMenu;
	}


}
