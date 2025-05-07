package com.exp.server.service.simulation;


import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class ContactListenerImply implements ContactListener{
    
    public void beginContact(Contact contact){

    }
    public void endContact(Contact contact){

    }

    public void preSolve(Contact contact, Manifold oldManifold){

    }

    public void postSolve(Contact contact, Manifold impulse){
        
    }
    
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        throw new UnsupportedOperationException("Unimplemented method 'postSolve'");
    }
    
}
