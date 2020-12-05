package br.com.onebr.controller;

import br.com.onebr.service.AnalyticsService;
import br.com.onebr.service.util.ApiOneBr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/analytics", produces = MediaType.APPLICATION_JSON_VALUE)
@ApiOneBr
@Slf4j
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/usersTotalCount")
    public ResponseEntity<AnalyticsRes> getUsersTotalCount(@RequestParam("path") String path) {
        return ResponseEntity.ok(analyticsService.getUsersTotalCount(path));
    }
}
