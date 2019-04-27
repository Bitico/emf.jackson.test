package jackson.test.demo;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

public class WriteJSON extends MakeJSON{
	
	public WriteJSON(String metamodelToRegister) {
		super(metamodelToRegister);
	}

	public void writeJSON(String jsonPath, String pathToSave) {
		
		String jsonPathAbsolute = new File(jsonPath).getAbsolutePath();
		
		Resource resource = loadModel(jsonPathAbsolute);
		Resource resourceToSave = resourceSet.createResource(URI.createFileURI(pathToSave));
		
		resourceToSave.getContents().addAll(resource.getContents());
		try {
			resourceToSave.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
