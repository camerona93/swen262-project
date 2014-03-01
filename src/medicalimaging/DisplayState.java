/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

/**
 *
 * @author Kaleb
 */
public class DisplayState {
    // single vs fourup
    private String currState;
    
    public DisplayState(){
        this.currState = "single";
    }
    
    public DisplayState(String state){
       this.currState = state;
    }
    
    public String getCurrState(){
        return this.currState;
    }
    
    public void changeState(){
        if(this.currState == "single"){
            this.currState = "fourup";
        }
        else if(this.currState == "fourup"){
            this.currState = "single";
        }
    }
    
    public String toString(){
        if(this.currState == "single"){
            return "Single View";
        }
        else{
            return "Four by Four";
        }
    }
}
