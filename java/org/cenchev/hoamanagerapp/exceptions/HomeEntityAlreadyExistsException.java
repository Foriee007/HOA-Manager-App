package org.cenchev.hoamanagerapp.exceptions;

public class HomeEntityAlreadyExistsException extends RuntimeException{
    private static final long currentId = 1L;


    public HomeEntityAlreadyExistsException(String s) {
        super(s);
    }
    public HomeEntityAlreadyExistsException(){
        super();
    }

}
