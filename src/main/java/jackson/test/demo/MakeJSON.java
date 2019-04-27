package jackson.test.demo;

import java.io.File;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.emfjson.jackson.module.EMFModule;
import org.emfjson.jackson.resource.JsonResourceFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public abstract class MakeJSON {
	
	protected ResourceSet resourceSet = new ResourceSetImpl();

	public MakeJSON(String metamodelToRegister) {
		registerMetamodel(metamodelToRegister);
		
		resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		                .getExtensionToFactoryMap()
		                .put("json", new JsonResourceFactory(makeMapper()));
	}
	
	private ObjectMapper makeMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.enable(SerializationFeature.FAIL_ON_SELF_REFERENCES);
		
		
		EMFModule module = new EMFModule();
		module.configure(EMFModule.Feature.OPTION_SERIALIZE_DEFAULT_VALUE, true); //Mi fa vedere i valori di default nel JSON (Nel caso in cui non sia stato settato un valore)
		module.configure(EMFModule.Feature.OPTION_SERIALIZE_TYPE, true);
		
//		module.setTypeInfo(new EcoreTypeInfo("type", new ValueWriter<EClass, String>() {
//			public String writeValue(EClass value, SerializerProvider context) {
//				return value.getInstanceTypeName();
//			}
//		}));
//		
//		module.setReferenceSerializer(new JsonSerializer<EObject>() {
//			  @Override
//			  public void serialize(EObject v, JsonGenerator g, SerializerProvider s)
//			  throws IOException {
////			    g.writeString(((JsonResource) v.eResource()).getID(v));
//			    g.writeObject(v);
//			  }
//			});
		
		
		mapper.registerModule(module);
		
		return mapper;
	}
	
	
//	private void packageRegistering() {
//		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
//		resourceSet.getPackageRegistry().put(MappacomandiPackage.eINSTANCE.getNsURI(), MappacomandiPackage.eINSTANCE);
//		resourceSet.getPackageRegistry().put(StatoentiPackage.eINSTANCE.getNsURI(), StatoentiPackage.eINSTANCE);
//		resourceSet.getPackageRegistry().put(ErmesMMPackage.eINSTANCE.getNsURI(), ErmesMMPackage.eINSTANCE);
//	}
	
	public static Resource registerMetamodel(String path) {
		File fileName = new File(path);
		URI uri = URI.createFileURI(fileName.getAbsolutePath());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		ResourceSet rs = new ResourceSetImpl();
		// enable extended metadata
		final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(rs.getPackageRegistry());
		rs.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
		Resource r = rs.getResource(uri, true);
		List<EObject> eObject = r.getContents();
		for (EObject eObject2 : eObject) {
			if (eObject2 instanceof EPackage) {
				EPackage p = (EPackage) eObject2;
				registerSubPackage(p);
			}
		}
//		System.out.println(path + " registered!");
		return r;
	}

	private static void registerSubPackage(EPackage p) {
		EPackage.Registry.INSTANCE.put(p.getNsURI(), p);
		for (EPackage pack : p.getESubpackages()) {
			registerSubPackage(pack);
		}
	}
	
	protected Resource loadModel(String modelPath) {
		URI uri = URI.createFileURI(modelPath);
		Resource resource = resourceSet.getResource(uri, true);
		return resource;
	}

}
