package com.example.football.service;

import com.example.football.models.entity.Player;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface PlayerService {
    boolean areImported();

    String readPlayersFileContent() throws IOException;

    String importPlayers() throws FileNotFoundException, JAXBException;

    String exportBestPlayers();

    Player findByEmail(String email);

    List<Player> getTheBestPlayers(LocalDate endDate, LocalDate startDate);
}
