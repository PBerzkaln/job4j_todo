package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tasks")
@ThreadSafe
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    /**
     * Страница со списком всех заданий.
     * В таблице отображаем имя, дату создания и состояние (выполнено или нет).
     * На странице со списком добавить кнопку "Добавить задание".
     *
     * @param model
     * @return
     */
    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getAllNew(Model model) {
        model.addAttribute("tasks", taskService.findByIsDone(false));
        return "tasks/list";
    }

    @GetMapping("/old")
    public String getAllOld(Model model) {
        model.addAttribute("tasks", taskService.findByIsDone(true));
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        task.setUser(user);
        var savedTask = taskService.save(task);
        if (savedTask.isEmpty()) {
            model.addAttribute("message",
                    String.format("%s%s", "Не удалось сохранить задание, вероятно оно уже существует.",
                            "Перейдите на страницу создания задания и попробуйте снова."));
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    /**
     * На странице с подробным описанием добавить кнопки: Выполнено, Отредактировать, Удалить.
     * Если нажали на кнопку выполнить, то задание переводиться в состояние выполнено.
     * Кнопка редактировать переводит пользователя на отдельную страницу для редактирования.
     * Кнопка удалить, удаляет задание и переходит на список всех заданий.
     */
    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @PostMapping("/execute/{id}")
    public String setTaskStatusDone(@PathVariable int id, Model model) {
        var updateRsl = taskService.setIsDone(id);
        if (!updateRsl) {
            model.addAttribute("message",
                    "Не удалось установить заданию статус \"исполнено\"");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @PostMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var deleteRsl = taskService.deleteById(id);
        if (!deleteRsl) {
            model.addAttribute("message", "Не удалось удалить задание");
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/update/{id}")
    public String getEditPage(Model model, @PathVariable int id) {
        var taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание не найдено");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, Model model) {
        try {
            var isUpdated = taskService.update(task);
            if (!isUpdated) {
                model.addAttribute("message", "Не удалось отредактировать задание");
                return "errors/404";
            }
            return "redirect:/tasks";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }
}