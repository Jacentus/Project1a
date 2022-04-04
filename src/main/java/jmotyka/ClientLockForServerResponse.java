package jmotyka;

import lombok.Getter;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientLockForServerResponse {

    @Getter
    private final Lock serverResponseLock = new ReentrantLock();
    @Getter
    private final Condition responseHandled = serverResponseLock.newCondition();

}
