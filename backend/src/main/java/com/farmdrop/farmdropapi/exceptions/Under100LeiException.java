package com.farmdrop.farmdropapi.exceptions;

//runtimeexception - unchecked
//                 - caught at UI level
//                 - Order throughs  |  GUI catches
public class Under100LeiException extends RuntimeException {
    public Under100LeiException(String message) {
        super(message);
    }
}
