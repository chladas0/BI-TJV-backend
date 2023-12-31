package cz.cvut.fit.tjv.issuetracker.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User
{
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private int id;

    @ManyToMany(mappedBy = "contributors", cascade = CascadeType.ALL)
    private List<Project> projects;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    public User(){}
    public User(String userName, String password){
        this.username = userName;
        this.password = password;
    }
}
