package com.blockchain.project.service;

import com.blockchain.project.ProjectRepository;
import com.blockchain.project.UserRepository;
import com.blockchain.project.models.Project;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private EthereumService ethereumService;
    public String txHash;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, EthereumService ethereumService) {
        this.projectRepository = projectRepository;
        this.ethereumService = ethereumService;
    }

    // Создание записи
    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    // Получение всех записей
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public void investInProject(Long id, Integer amount) throws Exception {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        RedirectAttributes redirectAttributes;

        if (project.getCollected_amount() >= project.getTarget_amount()) {
            throw new IllegalStateException("Проект уже собрал необходимую сумму");
        }

        if (project.getDateEnd().before(new Date())) {
            throw new IllegalStateException("Срок инвестирования в проект истек");
        }

        int newAmount = project.getCollected_amount() + amount;
        if (newAmount > project.getTarget_amount()) {
            throw new IllegalStateException("Сумма инвестиций превышает требуемую сумму");
        }

        // Валидация инвестиции
        validateInvestment(project, amount);

        // Конвертируем сумму в wei (1 ETH = 10^18 wei)
        BigInteger amountWei = BigInteger.valueOf(amount);

        // 1. Отправляем транзакцию в блокчейн
        this.txHash = ethereumService.investInCampaign(amountWei);

        // 2. Ждем подтверждения транзакции (опционально)
        TransactionReceipt receipt = ethereumService.waitForTransactionReceipt(txHash);
        if (!receipt.isStatusOK()) {
            throw new RuntimeException("Транзакция не была подтверждена в блокчейне");
        }

        // 3. Обновляем данные в БД только после успешной транзакции
        project.setCollected_amount(newAmount);

        // Если собрана нужная сумма - закрываем проект
        if (newAmount >= project.getTarget_amount()) {
            project.setActive(false);
        }
        projectRepository.save(project);
    }

    private void validateInvestment(Project project, Integer amount) {
        if (!project.getActive()) {
            throw new IllegalStateException("Проект не активен");
        }

        if (project.getDateEnd().before(new Date())) {
            throw new IllegalStateException("Срок инвестирования в проект истек");
        }

        int potentialAmount = project.getCollected_amount() + amount;
        if (potentialAmount > project.getTarget_amount()) {
            throw new IllegalStateException("Сумма инвестиций превышает требуемую сумму");
        }
    }

    // Поиск по имени
    public List<Project> getProjectByProjName(String projName) {
        return projectRepository.findByprojName(projName);
    }
}
