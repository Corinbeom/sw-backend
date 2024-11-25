package com.sw.controller;


import com.sw.service.FootballService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/football")
public class FootballController {
    @Autowired
    private FootballService footballService;

    @GetMapping(value = "/competitions", produces = "application/json")
    public String getCompetitions() {
        return footballService.getCompetitions();
    }

    @GetMapping(value = "/competitions/{competitionId}/matches", produces = "application/json")
    public String getCompetitionMatches(@PathVariable String competitionId) {
        return footballService.getCompetitionMatches(competitionId);
    }
}