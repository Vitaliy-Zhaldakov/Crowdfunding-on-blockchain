package com.blockchain.project.controllers;

import com.blockchain.project.models.Project;
import com.blockchain.project.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    private final ProjectService projectService;

    public MainController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @GetMapping("/home")
    public String home(Model model) {
        List<Project> activeProjects = projectService.getAllProjects();
        model.addAttribute("projects", activeProjects);
        return "index";
    }

    @GetMapping("/authIndex")
    public String authIndex(Model model) {
        List<Project> activeProjects = projectService.getAllProjects();
        model.addAttribute("projects", activeProjects);
    
        // Добавляем проверку на наличие сообщений
        if (!model.containsAttribute("success") && !model.containsAttribute("error")) {
            model.addAttribute("showDefaultMessage", true);
        }
        return "authIndex";
    }

    @GetMapping("/project/{id}/invest")
    public String showInvestPage(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project id: " + id));
        model.addAttribute("project", project);
        model.addAttribute("isProjectClosed",
                project.getCollected_amount() >= project.getTarget_amount() ||
                        project.getDateEnd().before(new Date()));
        return "invest";
    }

    @PostMapping("/project/{id}/invest")
    public String processInvestment(@PathVariable Long id,
                                    @RequestParam Integer amount,
                                    RedirectAttributes redirectAttributes) {
        try {
            projectService.investInProject(id, amount);
            redirectAttributes.addFlashAttribute("success", "Инвестиция успешно проведена! " +
                    "Хэш транзакции: 0xbf9dd3d5533243f01f038a1ef714c0a16903c5f23ce6ed8706fe805e00bb6734" /*+ projectService.txHash*/);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/authIndex2";
    }
}
