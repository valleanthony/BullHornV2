package com.example.springboot_401;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class TweetController {

    @Autowired
    TweetRepo tweetRepo;

    @Autowired
    UserService userService;

    @Autowired
    CloudnairyConfig cloudc;


    @RequestMapping("/")
    public String listtweet(Model model){
        model.addAttribute("tweets",tweetRepo.findAll());
        return "list";
    }

    // To test new index page
    @RequestMapping("/index2")
    public String listTweetTwo(Model model){
        model.addAttribute("tweets",tweetRepo.findAll());
        model.addAttribute("userId",userService.getCurrentUser().getId());
        return "index2";
    }



    @GetMapping("/add")
    public String tweetForm(Model model){
        model.addAttribute("tweet",new Tweet());
        return "form";
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

    @PostMapping("/processtweet") // <-- not sure if i need this part
    public String processCarForm(@ModelAttribute Tweet tweet, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));

            tweet.setPicture(uploadResult.get("url").toString());
            tweet.setUserId(userService.getCurrentUser().getId());
            tweetRepo.save(tweet);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/";
        }
        return "redirect:/";
    }







}
