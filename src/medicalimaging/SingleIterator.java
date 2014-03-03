/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Zach
 */
public class SingleIterator implements StudyIterator {
    private final Study study;
    private final List<Image> images;
    private int index;
    
    /**
     * Create iterator object at index = 0
     * @param study
     */
    public SingleIterator(Study study){
        this.study = study;
        this.images = new ArrayList<>();
        ArrayList<MedicalImage> studyImages = study.getImages();
        for (MedicalImage i: studyImages){
            images.add(i.getImage());
        }
        this.index = 0;
    }
    /**
     * Create iterator object with given index
     * @param study
     * @param index
     */
    public SingleIterator(Study study, int index){
        this.study = study;
        this.images = new ArrayList<>();
        ArrayList<MedicalImage> studyImages = study.getImages();
        for (MedicalImage i: studyImages){
            images.add(i.getImage());
        }
        this.index = index;
    }
    
    @Override
    public boolean next(){
        if (index == images.size()-1){
            return false;
        }
        index++;
        return true;
    }
    
    @Override
    public boolean prev(){
        if (index == 0){
            return false;
        }
        index--;
        return true;
    }
    
    @Override
    public Image[] getImages(){
        Image[] imageSet = new Image[4];
        imageSet[0] = getOrNull(index);
        imageSet[1] = getOrNull(index+1);
        imageSet[2] = getOrNull(index+2);
        imageSet[3] = getOrNull(index+3);
        return imageSet;
    }
    
    @Override
    public int getIndex(){
        return index;
    }
    
    private Image getOrNull(int i){
        if (i >= images.size()){
            return null;
        }
        return images.get(i);
    }
                
                
}
