package com.example.springbootblogapplication.controllers;

import com.example.springbootblogapplication.models.Account;
import com.example.springbootblogapplication.models.Post;
import com.example.springbootblogapplication.services.AccountService;
import com.example.springbootblogapplication.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model, Authentication authentication) {
        // find post by id
        Optional<Post> optionalPost = this.postService.getById(id);

        // if post exists put it in model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            byte[] imageBytes = post.getImage();
            if (imageBytes != null) {
                // convert image bytes to Base64 encoded string
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                model.addAttribute("image", base64Image);
            }

            // check if the user is authorized to edit the post
            boolean canEdit = false;
            boolean canDelete = false;
            if (authentication != null) {
                String currentUserEmail = authentication.getName();
                boolean isPostOwner = post.getAccount().getEmail().equals(currentUserEmail);
                boolean isAdmin = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .anyMatch("ROLE_ADMIN"::equals);

                canEdit = isPostOwner || isAdmin;
                canDelete = isPostOwner || isAdmin;
            }

            model.addAttribute("canEdit", canEdit);
            model.addAttribute("canDelete", canDelete);

            return "post";
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{id}/download")
    public void downloadImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            byte[] imageBytes = post.getImage();
            if (imageBytes != null) {
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + post.getId() + ".jpg\"");
                response.getOutputStream().write(imageBytes);
                response.getOutputStream().flush();
            }
        }
    }

    @PostMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, @ModelAttribute("existingPost") Post existingPost,
                             BindingResult bindingResult, Model model, @RequestParam("image") MultipartFile imageFile) throws IOException {

        Post postToUpdate = postService.getById(id).orElse(null);

        if (postToUpdate == null) {
            return "redirect:/";
        }

        // check if the user is authorized to edit the post
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!postToUpdate.getAccount().getEmail().equals(authentication.getName())) {
            return "redirect:/";
        }

        // update the post fields
        postToUpdate.setTitle(existingPost.getTitle());
        postToUpdate.setContent(existingPost.getContent());
        if (imageFile != null && !imageFile.isEmpty()) {
            postToUpdate.setImage(imageFile.getBytes());
        }

        postService.save(postToUpdate);

        return "redirect:/posts/" + postToUpdate.getId();
    }

    @GetMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(Model model, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Optional<Account> optionalAccount = accountService.findOneByEmail(authUsername);
        if (optionalAccount.isPresent()) {
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "post_new";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(@ModelAttribute Post post, BindingResult bindingResult, Model model, @RequestParam("image") MultipartFile imageFile, Principal principal) throws IOException {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Optional<Account> optionalAccount = accountService.findOneByEmail(authUsername);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (!account.getEmail().equalsIgnoreCase(post.getAccount().getEmail())) {
                // Account email on the Post is not equal to the current logged-in account
                // Handle the error condition here
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                post.setImage(imageFile.getBytes());
            }

            post.setAccount(account);
            postService.save(post);
            return "redirect:/posts/" + post.getId();
        } else {
            return "redirect:/";
        }
    }


    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {

        // find post by id
        Optional<Post> optionalPost = postService.getById(id);
        // if post exist put it in model
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "404";
        }
    }

    @GetMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, Authentication authentication) {

        // find post by id
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            if (!post.getAccount().getEmail().equals(authentication.getName())) {
                return "redirect:/";
            }

            postService.delete(post);
            return "redirect:/";
        } else {
            return "404";
        }
    }

    //Search method
    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public String searchPosts(@RequestParam("q") String query, Model model) {
        List<Post> posts = postService.searchPosts(query);
        model.addAttribute("posts", posts);
        model.addAttribute("query", query);
        return "search-results";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "about-us";
    }

    @GetMapping("/contact")
    public String contactUs() {
        return "contact";
    }

}
