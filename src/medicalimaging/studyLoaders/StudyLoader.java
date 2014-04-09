/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package medicalimaging.studyLoaders;

import medicalimaging.model.Study;

/**
 * Outlines methods for all study handlers.  All study handlers must be able to
 * to load(execute), save and copy a study.
 * @author ericlee
 */
public interface StudyLoader {
    public Study execute();
    public void save(Study saveStudy);
    public boolean copyStudy(Study copyStudy, String copyPath);
}
