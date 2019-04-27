package jackson.test.demo;

import java.io.IOException;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;

public class ReadJSON extends MakeJSON{
	
	public ReadJSON(String metamodelToRegister) {
		super(metamodelToRegister);
	}

	public void readJSON(String jsonPath) {
		Resource resource = resourceSet.createResource(URI.createFileURI(jsonPath));

		try {
			resource.load(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TreeIterator<EObject> eAllContents = resource.getAllContents();
		while (eAllContents.hasNext()) {
			EObject next = eAllContents.next();
			EClass eClass = next.eClass();
			System.out.println(eClass.getName());
			if (eClass instanceof EClass) {
				// CLASS ATTRIBUTES
				for (EAttribute attribute : eClass.getEAllAttributes()) {
					System.out.println("\t "+attribute.getName() +": "+ next.eGet(eClass.getEStructuralFeature(attribute.getName())).toString());
				}
				// CLASS REFERENCES
				for (EReference reference : eClass.getEReferences()) {
					EClass eContainingClass = reference.getEContainingClass();
					System.out.println("\t Reference: "+eContainingClass.getName());
					for (EAttribute attribute : eContainingClass.getEAllAttributes()) {
						System.out.println("\t\t "+attribute.getName() +": "+ next.eGet(eClass.getEStructuralFeature(attribute.getName())).toString());
					}
				 }
			}
			
		}
	}

}
