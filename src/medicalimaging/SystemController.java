/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Cameron
 */
public class SystemController {
    
    protected static final String STUDY_PATH = new File("").getAbsolutePath().concat("\\Studies\\");
    protected static ArrayList<Study> studies = new ArrayList<>();
    
    public static void main(String[] args) {
        loadStudies();
        Study newStudy = new Study("study1.std");
        studies.add(newStudy);
        saveStudies();
    }
    
    /* Loading serialized studies from disk. */
    private static void loadStudies() {

    }
    
    private static void saveStudies() {
        for (Study s : studies) {
            serializeStudy(s);
        }
    }
    
    private static Study deserializeStudy(String studyName) {
        Study s = null;
        
        try(
            InputStream file = new FileInputStream(STUDY_PATH.concat(studyName));
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            ){
                s = (Study)input.readObject();
            }
            catch(ClassNotFoundException ex){
            }
            catch(IOException ex){
            }
        
        return s;
    }
    
    private static void serializeStudy(Study study){ 
        try (
            OutputStream file = new FileOutputStream(STUDY_PATH.concat(study.name));
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            ){
                output.writeObject(study);
            }  
        catch(IOException ex){
        }
    }
}
