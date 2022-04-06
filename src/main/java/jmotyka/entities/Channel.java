package jmotyka.entities;

import jmotyka.ClientHandler;

import java.io.Serializable;
import java.util.List;

public class Channel implements Serializable {

    private boolean isPrivate;
    private List<String> permittedUsers;
    private List<ClientHandler> usersInChannel;



}
