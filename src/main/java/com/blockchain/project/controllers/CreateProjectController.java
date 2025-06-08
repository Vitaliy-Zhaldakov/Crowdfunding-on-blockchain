package com.blockchain.project.controllers;

import com.blockchain.project.blockchain.BCinstance;
import com.blockchain.project.models.Project;
import com.blockchain.project.service.EthereumService;
import com.blockchain.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;
import java.util.Date;

@Controller
public class CreateProjectController {
    private BCinstance bcInstance;
    private final ProjectService projectService;
    private final EthereumService ethereumService;

    @Autowired
    public CreateProjectController(ProjectService projectService, EthereumService ethereumService) {
        this.projectService = projectService;
        this.ethereumService = ethereumService;
    }

    @GetMapping("/create")
    public String create() {
        return "createProj";
    }

    @PostMapping("/createProj")
    public String createProject(
            @RequestParam String projName,
            @RequestParam Integer summa,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd,
            @RequestParam String file,
            @RequestParam String description,
            RedirectAttributes redirectAttributes
    ) {
        try {
            BigInteger goalAmount = BigInteger.valueOf(summa);
            String txHash = ethereumService.createCampaignOnBlockchain(goalAmount);

            bcInstance = new BCinstance();
            bcInstance.bc.new_transaction(projName, summa, dateEnd, description);
            System.out.println("Transaction Hash:" + txHash);

            Project project = new Project(projName, summa, description, 0, dateEnd, true, txHash);
            projectService.addProject(project);

            // Добавляем сообщение об успехе с хэшем транзакции
            redirectAttributes.addFlashAttribute("success",
                    "Проект успешно создан! Хэш транзакции: " + txHash);

            return "redirect:/authIndex";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при создании проекта: " + e.getMessage());
            return "redirect:/create";
        }
    }
}