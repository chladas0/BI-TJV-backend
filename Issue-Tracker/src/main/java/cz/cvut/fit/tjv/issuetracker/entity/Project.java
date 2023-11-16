package cz.cvut.fit.tjv.issuetracker.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Project{
    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(
        name = "project_user",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> contributors;

    public Project(String name, List<User> contributors, String description)
    {
        this.name = name;
        this.contributors = contributors;
        this.description = description;
        contributors.forEach(user -> user.getProjects().add(this));
    }
    public Project(){}
}