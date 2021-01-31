package dev.jozefowicz.workshop.companydisc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.jozefowicz.workshop.companydisc.domain.FileRepository;
import dev.jozefowicz.workshop.companydisc.domain.TweetDTO;
import dev.jozefowicz.workshop.companydisc.domain.TweetRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class IndexController {

    private final FileRepository fileRepository;
    private final TweetRepository tweetRepository;

    public IndexController(FileRepository fileRepository, TweetRepository tweetRepository) {
        this.fileRepository = fileRepository;
        this.tweetRepository = tweetRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("files", fileRepository.listAll());
        model.addAttribute("tweets", tweetRepository.findAll());
        model.addAttribute("tweet", new TweetDTO());
        return "index";
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/files")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        fileRepository.upload(file);
        return "redirect:/";
    }

    @GetMapping("/files/{fileId}/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable String fileId, @PathVariable String fileName) throws IOException {
        final InputStream inputStream = fileRepository.download(fileId, fileName);
        Resource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"").body(resource);
    }

    @PostMapping("/tweets")
    public String greetingSubmit(@ModelAttribute TweetDTO tweet) throws JsonProcessingException {
        tweet.setId(UUID.randomUUID().toString());
        tweetRepository.persist(tweet);
        return "redirect:/";
    }

}
