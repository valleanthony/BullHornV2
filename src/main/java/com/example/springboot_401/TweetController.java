package com.example.springboot_401;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class TweetController {

    @Autowired
    TweetRepo tweetRepo;


    @RequestMapping("/")
    public String listtweet(Model model){
        model.addAttribute("tweets",tweetRepo.findAll());
        return "list";
    }


    @GetMapping("/add")
    public String tweetForm(Model model){
        model.addAttribute("tweet",new Tweet());
        return "form";
    }

    @PostMapping("/process")
    public String processForm(@Valid Tweet tweet, BindingResult result){
        if (result.hasErrors()){
            return "form";
        }
        tweetRepo.save(tweet);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showTweet(@PathVariable("id") long id, Model model){
        model.addAttribute("tweet",tweetRepo.findById(id).get());
        return "mytweet";
    }

    @RequestMapping("/update/{id}")
    public String updateTwee(@PathVariable("id") long id, Model model){
        model.addAttribute("tweet",tweetRepo.findById(id).get());
        return "form";
    }

    @RequestMapping("/delete/{id}")
    public String delTweet(@PathVariable("id") long id){
        tweetRepo.deleteById(id);
        return "redirect:/";
    }







}
