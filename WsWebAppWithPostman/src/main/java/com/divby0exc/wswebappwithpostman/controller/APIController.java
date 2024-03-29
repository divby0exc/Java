package com.divby0exc.wswebappwithpostman.controller;

import com.divby0exc.wswebappwithpostman.handler.ServerWebSocketHandler;
import com.divby0exc.wswebappwithpostman.model.DTOChannel;
import com.divby0exc.wswebappwithpostman.model.Message;
import com.divby0exc.wswebappwithpostman.service.ChannelServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



 /**Studerande väljer hur deltagare av borttagna kanaler hanteras. Det är ok att låta de vara kvar efter att annonsen är borttagen.**/
@RestController
public class APIController {
    @Autowired
    ChannelServiceImpl cs;

    final Logger logger = LoggerFactory.getLogger(APIController.class);

 /**[GET] /channels/ ← Hämtar en lista över annonserade kanaler**/
    @GetMapping("/channels")
    public ResponseEntity<List<DTOChannel>> getAllChannels() {
        List<DTOChannel> channels = new ArrayList<>();
        logger.atInfo().setMessage("Created new channel array").addArgument(channels).log();
        if (cs.getAll().isEmpty()) {
            logger.atDebug().setMessage("DB was empty of channels").addArgument(cs.getAll().isEmpty()).log();
            ResponseEntity.status(204)
                    .build();
        }
        channels.addAll(cs.getAll());
        logger.atInfo().setMessage("Added all channels from DB to array").addArgument(channels).log();
        return
                ResponseEntity
                        .status(200)
                        .body(channels);
    }
    /**[POST] - /channels/ ← skapar en ny kanal som annonseras i den permanenta chatt-kanalen.**/
    @PostMapping("/channels")
    public ResponseEntity addNewChannel(@RequestBody DTOChannel newChannel) throws IOException {
        cs.save(newChannel);

        logger.atInfo().log("Fetched id: {}",newChannel.getId());
        logger.atInfo().log("Fetched title: {}", newChannel.getTitle());
        logger.atInfo().log("Fetched username: {}", newChannel.getUsername());
        logger.atInfo().log("Added new channel to db");
        Message msg = new Message();
        msg.setFrom("[Server announcement]");
        msg.setContent("New channel was registered with title: " + newChannel.getTitle());
        System.out.println(msg.getFrom());
        System.out.println(msg.getContent());

        ServerWebSocketHandler.broadcast(msg.getContent());
        ServerWebSocketHandler.broadcast(msg.getFrom());
        logger.atInfo().log("Sended channel dto to announcements");

        return
                ResponseEntity
                        .status(200)
                        .build();
    }
    /**[DELETE] /channels/{id} ← tar bort en annonserad kanal**/
    @DeleteMapping("/channels/{channelId}")
    public ResponseEntity deleteChannel(@PathVariable Long channelId) {
        boolean exists = cs.exists(channelId);
        logger.atDebug().setMessage("Checking if channel exists in DB").addArgument(exists).log();
        Assert.isTrue(exists, "Deleted channel with ID: " + channelId);
        cs.deleteDTOChannelById(channelId);
        logger.atInfo().setMessage("Deleted channel from DB").log();
        return
                ResponseEntity
                        .status(410)
                        .build();
    }
}