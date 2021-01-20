package dev.jozefowicz.workshop.companydisc.web;

import com.amazonaws.util.IOUtils;
import dev.jozefowicz.workshop.companydisc.domain.FileRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/")
public class FileController {

    private final FileRepository fileRepository;

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @GetMapping
    public String listFiles(Model model) {
        // model.addAttribute("files", fileRepository.listAll());
        return "index";
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/files")
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        fileRepository.upload(file);
    }

    @GetMapping("/files/{fileId}/{fileName}")
    public byte[] download(String fileId, String fileName) throws IOException {
        final InputStream inputStream = fileRepository.download(fileId, fileName);
        return IOUtils.toByteArray(inputStream);
    }

}
