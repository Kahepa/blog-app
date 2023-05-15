package com.example.springbootblogapplication.controllers;

import com.example.springbootblogapplication.models.Post;
import com.example.springbootblogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Base64;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.getAll();
        for (Post post : posts) {
            byte[] imageBytes = post.getImage();
            if (imageBytes != null) {
                // convert image bytes to Base64 encoded string
                String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
                post.setBase64Image(base64Image);
            }
        }

        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/posts/user/{accountId}")
    public String getPostsByAccountId(@PathVariable("accountId") Long accountId, Model model) {
        List<Post> posts = postService.getAllByAccountId(accountId);
        model.addAttribute("posts", posts);
        return "home";
    }

}
