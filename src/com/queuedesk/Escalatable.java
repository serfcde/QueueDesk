package com.queuedesk;

//interface is also used for abstraction that defines a contract that implementing classes must follow
//only bugs and access tickets are escalatable
public interface Escalatable {
    int MAX_LEVEL =3;
    void escalate();
    boolean isEscalated();
    int escalationLevel();

    default String escalationBadge(){
        return isEscalated()? "Escalated L"+escalationLevel():"";
    }
}
