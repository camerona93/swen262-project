/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging;

/**
 *
 * @author ericlee
 */
public abstract class ReconStudyLoader implements StudyLoader{
    protected String studyName;
    protected int[][][] render;
    
    public ReconStudyLoader(String _studyName, int[][][] _render) {
        studyName = _studyName;
        render = _render;
    }
    
    @Override
    public void save(Study saveStudy) {
       
    }

    @Override
    public boolean copyStudy(Study copyStudy, String copyPath) {
        return true;
    }
}
