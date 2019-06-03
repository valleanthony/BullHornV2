package com.example.springboot_401;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Controller
public class TweetController {

    @Autowired
    TweetRepo tweetRepo;

    @Autowired
    UserService userService;

    @Autowired
    CloudnairyConfig cloudc;


//    @RequestMapping("/")
//    public String listtweet(Model model){
//        model.addAttribute("user", userService.getCurrentUser());
//        model.addAttribute("tweets",tweetRepo.findAll());
//        return "list";
//    }

    // To test new index page
    @RequestMapping("/")
    public String listTweetTwo(Model model){
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("tweets",tweetRepo.findAll());
        model.addAttribute("userId",userService.getCurrentUser().getId());
        return "main";
    }



    @GetMapping("/add")
    public String tweetForm(Model model){
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("tweet",new Tweet());
        return "form";
    }



    @RequestMapping("/detail/{id}")
    public String showTweet(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("tweet",tweetRepo.findById(id).get());
        return "mytweet";
    }

    @RequestMapping("/update/{id}")
    public String updateTwee(@PathVariable("id") long id,Model model ){
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("tweet",tweetRepo.findById(id).get());
        return "form";
    }

    @RequestMapping("/delete/{id}")
    public String delTweet(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userService.getCurrentUser());
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

@RequestMapping("/mypost")// <-- Call back all posts made by user.
    public String profilePage(Model model){
    long creatorID= userService.getCurrentUser().getId();
    ArrayList<Tweet> results =(ArrayList<Tweet>)
            tweetRepo.findByUserId(creatorID);


        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("results",results);


        return "mypost";
}





}
