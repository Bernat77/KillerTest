/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package killerproject;

/**
 *
 * @author berna
 */
public abstract class VisibleObject implements Renderizable {
    
    KillerGame kg;
    
    int x;
    int y;
    
    int HEIGHT;
    int WIDTH;

    public KillerGame getKg() {
        return kg;
    }

    public void setKg(KillerGame kg) {
        this.kg = kg;
    }
    
    

}
