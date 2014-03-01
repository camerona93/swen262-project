/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author Cameron
 */
public class SystemController {
    
    protected ArrayList<Study> studies = new ArrayList<Study>();
    
    public static void main(String[] args) {
        
        Study study1 = new Study("test");
        
        try {
            String path = new File("").getAbsolutePath().concat("\\Studies\\study1.std");
            System.out.println(path);
            File file1 = new File(path);
            if ( ! file1.exists() ) {
                file1.createNewFile();
            }
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(study1);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        
        //String path = "";
        //StudyLoader loader = new LocalStudyLoader(path);
        //Study loadedStudy = loader.execute();
        //System.out.println(loadedStudy);
           
    }
}