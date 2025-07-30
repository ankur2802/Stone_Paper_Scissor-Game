package com.example.spsgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ "round", "playerScore", "computerScore" })
public class GameController {

    @ModelAttribute("round")
    public Integer round() {
        return 1;
    }

    @ModelAttribute("playerScore")
    public Integer playerScore() {
        return 0;
    }

    @ModelAttribute("computerScore")
    public Integer computerScore() {
        return 0;
    }

    @GetMapping("/")
    public String showGame() {
        return "index";
    }

    @PostMapping("/play")
    public String play(@RequestParam String playerChoice,
                       @ModelAttribute("round") Integer round,
                       @ModelAttribute("playerScore") Integer playerScore,
                       @ModelAttribute("computerScore") Integer computerScore,
                       Model model) {

        String[] options = {"stone", "paper", "scissors"};
        String computerChoice = options[new Random().nextInt(3)];

        String result = getResult(playerChoice, computerChoice);

        if (result.equals("You Win!")) playerScore++;
        else if (result.equals("You Lose!")) computerScore++;

        model.addAttribute("playerChoice", playerChoice);
        model.addAttribute("computerChoice", computerChoice);
        model.addAttribute("result", result);
        model.addAttribute("playerScore", playerScore);
        model.addAttribute("computerScore", computerScore);
        model.addAttribute("round", round + 1);

        if (round == 5) {
            String winner;
            if (playerScore > computerScore) winner = " You are the Ultimate Winner!";
            else if (computerScore > playerScore) winner = " Computer Wins the Game!";
            else winner = " It's a Tie Game!";

            model.addAttribute("finalWinner", winner);
        }

        return "index";
    }

    @PostMapping("/restart")
    public String restartGame(SessionStatus status) {
        status.setComplete();
        return "redirect:/";
    }

    private String getResult(String player, String computer) {
        if (player.equals(computer)) return "It's a Draw!";
        if ((player.equals("stone") && computer.equals("scissors")) ||
            (player.equals("paper") && computer.equals("stone")) ||
            (player.equals("scissors") && computer.equals("paper"))) {
            return "You Win!";
        } else {
            return "You Lose!";
        }
    }
}
