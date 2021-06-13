package com.testorystudio.testorystudio.controllers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
@RequestMapping("/reports")
public class reportsController {

  @GetMapping("/{id}")
  public String getReport(@PathVariable("id") String id) {
    Path path = Paths.get("folder" + id + "/report/index.html");
    try {
      String content = Files.readString(path, Charset.forName("ISO-8859-1"));
      //String content = Files.readString(path, StandardCharsets.US_ASCII);
      String aTagsBefore = "class=\"dropdown-toggle\"";
      String aTageAfter = "class=\"dropdown-toggle\" onclick=\"event.preventDefault();\"";
      return content.replaceAll(aTagsBefore, aTageAfter);
    } catch (IOException ex) {
      return "HttpStatus.INTERNAL_SERVER_ERROR";
    }
  }

}
