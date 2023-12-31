package cz.cvut.fit.tjv.issuetracker.service;

import cz.cvut.fit.tjv.issuetracker.entity.Task;
import cz.cvut.fit.tjv.issuetracker.entity.User;
import cz.cvut.fit.tjv.issuetracker.entity.Project;
import cz.cvut.fit.tjv.issuetracker.exception.EntityStateException;
import cz.cvut.fit.tjv.issuetracker.exception.IllegalDataException;
import cz.cvut.fit.tjv.issuetracker.dto.ProjectCreateDTO;
import cz.cvut.fit.tjv.issuetracker.dto.ProjectDTO;
import cz.cvut.fit.tjv.issuetracker.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProjectService extends CrudService<Integer,Project, ProjectDTO, ProjectCreateDTO>{
    private final UserService userService;

    public ProjectService(ProjectRepository ProjectRepository, UserService userService) {
        super(ProjectRepository);
        this.userService = userService;
    }

    @Override
    public ProjectDTO create(ProjectCreateDTO Project) throws IllegalDataException {
        List<User> users = userService.findByIDs(Project.getContributorsIds());

        if(users.size() != Project.getContributorsIds().size()) {
            throw new IllegalDataException("Some contributors not found");
        }

        return toDTO(repository.save(toEntity(Project)));
    }

    @Override
    public ProjectDTO toDTO(Project Project)
    {
        return new ProjectDTO(Project.getId(), Project.getName(),
                Project.getContributors().stream().map(User::getId).collect(Collectors.toList()),
                Project.getDescription(), Project.getTasks().stream().map(Task::getId).collect(Collectors.toList()));
    }

    @Override
    protected Project toEntity(ProjectCreateDTO projectCreateDTO) {
        return new Project(projectCreateDTO.getName(), userService.findByIDs(projectCreateDTO.getContributorsIds()), projectCreateDTO.getDescription());
    }

    @Override
    protected Project updateEntity(Project existingProject, ProjectCreateDTO e) throws EntityStateException {

        List<User> users = userService.findByIDs(e.getContributorsIds());

        if(users.size() != e.getContributorsIds().size())
            throw new EntityStateException("Some authors not found");

        existingProject.setName(e.getName());
        existingProject.setContributors(userService.findByIDs(e.getContributorsIds()));
        existingProject.setDescription(e.getDescription());

        return existingProject;
    }
}
