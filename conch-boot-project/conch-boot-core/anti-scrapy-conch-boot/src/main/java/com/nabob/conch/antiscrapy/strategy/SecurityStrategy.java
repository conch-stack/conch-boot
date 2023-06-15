package com.nabob.conch.antiscrapy.strategy;

public interface SecurityStrategy {

    boolean checkSecurity(String target);

    boolean checkSecurity(String target1, String target2);
}
