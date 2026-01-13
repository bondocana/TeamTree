package com.example.teamtree.controller;

import com.example.teamtree.model.Position;
import com.example.teamtree.service.PositionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/positions")
public class PositionViewController {

    private final PositionService positionService;

    public PositionViewController(PositionService positionService) {
        this.positionService = positionService;
    }

    // List positions
    @GetMapping
    public String listPositions(Model model) {
        model.addAttribute("positions", positionService.getAll());
        model.addAttribute("pageTitle", "Positions");
        return "positions";
    }

    // Show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("position", new Position());
        model.addAttribute("pageTitle", "Add Position");
        return "position_form";
    }

    // Save
    @PostMapping("/save")
    public String savePosition(@ModelAttribute Position position) {
        positionService.save(position);
        return "redirect:/positions";
    }
}
