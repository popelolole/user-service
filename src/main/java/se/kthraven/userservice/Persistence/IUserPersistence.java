package se.kthraven.userservice.Persistence;

import se.kthraven.userservice.Persistence.entities.UserDB;

public interface IUserPersistence {
    UserDB getUserById(String id);
    UserDB getUserByPersonId(String personId);
    UserDB getUserByUsername(String username);
}
