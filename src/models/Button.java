/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 *
 * @author Hao
 */
public abstract class Button {

    public Button() {
    }

    public Button(boolean illuminated) {
        this.illuminated = illuminated;
    }
    
    protected boolean illuminated;

    public boolean isIlluminated() {
        return illuminated;
    }

    public void setIlluminated(boolean illuminated) {
        this.illuminated = illuminated;
    }
    protected abstract void turnOff();
    protected abstract void turnOn();
    protected abstract void pressed();
}
