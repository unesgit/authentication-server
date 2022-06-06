package ma.cam.kernal.data.model.auth;

import javax.persistence.*;

@Entity
@Table(name = "ws_role")
public class Role {

	 @Id
	 @SequenceGenerator(name = "my_seq_role", sequenceName = "SEQ_ROLE", allocationSize = 1)
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_role")
     private long id;

    @Column
    private String name;

    @Column
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
