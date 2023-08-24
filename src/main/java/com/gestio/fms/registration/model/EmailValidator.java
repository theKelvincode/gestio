package com.gestio.fms.registration.model;

import java.util.function.Predicate;

public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
        // regex to vaidate email
        return true;
    }
}
