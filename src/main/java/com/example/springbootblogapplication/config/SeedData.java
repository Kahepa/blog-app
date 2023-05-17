package com.example.springbootblogapplication.config;

import com.example.springbootblogapplication.models.Account;
import com.example.springbootblogapplication.models.Authority;
import com.example.springbootblogapplication.models.Post;
import com.example.springbootblogapplication.repositories.AuthorityRepository;
import com.example.springbootblogapplication.services.AccountService;
import com.example.springbootblogapplication.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = postService.getAll();

        if (posts.size() == 0) {

            Authority user = new Authority();
            user.setName("ROLE_USER");
            authorityRepository.save(user);

            Authority admin = new Authority();
            admin.setName("ROLE_ADMIN");
            authorityRepository.save(admin);

            Account account1 = new Account();
            Account account2 = new Account();

            account1.setFirstName("Michael");
            account1.setLastName("Jackson");
            account1.setEmail("user.user@domain.com");
            account1.setPassword("password");
            Set<Authority> authorities1 = new HashSet<>();
            authorityRepository.findById("ROLE_USER").ifPresent(authorities1::add);
            account1.setAuthorities(authorities1);


            account2.setFirstName("Rush");
            account2.setLastName("Kahepa");
            account2.setEmail("admin.admin@domain.com");
            account2.setPassword("password");

            Set<Authority> authorities2 = new HashSet<>();
            authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities2::add);
            authorityRepository.findById("ROLE_USER").ifPresent(authorities2::add);
            account2.setAuthorities(authorities2);

            accountService.save(account1);
            accountService.save(account2);

            Post post1 = new Post();
            post1.setTitle("The Importance of Regular Exercise");
            post1.setContent("Regular exercise is essential for maintaining good physical and mental health. It helps to strengthen our muscles, improve cardiovascular health, and boost our immune system. Exercise also plays a vital role in managing weight, reducing the risk of chronic diseases such as diabetes and heart disease, and improving mood and overall well-being. Incorporating exercise into our daily routine, whether it's through brisk walking, jogging, cycling, or any other physical activity, can have long-term benefits for our health. Remember, consistency is key, so make sure to find activities that you enjoy and make exercise a regular part of your life.");
            post1.setAccount(account1);

            Post post2 = new Post();
            post2.setTitle("Tips for Effective Time Management");
            post2.setContent("Time management is crucial for productivity and achieving our goals. Here are some tips to help you manage your time effectively:\n" +
                    "\n" +
                    "Prioritize tasks: Identify the most important and urgent tasks and tackle them first. This helps you stay focused and avoid procrastination.\n" +
                    "Set goals and deadlines: Establish clear goals and set deadlines for each task. This provides a sense of direction and helps you stay motivated.\n" +
                    "Plan and schedule: Use a planner or digital tools to plan your day, week, or month. Schedule specific time blocks for different activities and allocate sufficient time for each task.");
            post2.setAccount(account2);

            postService.save(post1);
            postService.save(post2);
        }
    }

}
