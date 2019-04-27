package jackson.test.demo;


public class TestMakeJSON {
	
	
	public static void main(String[] args) {
		String comandiMetamodelPath = "models/Company.ecore";
		String comandiModelPath = "models/CompanyModel.xmi";
		String pathToSaveComandi = "models/Company.json";
		
		
		WriteJSON writeJSON = new WriteJSON(comandiMetamodelPath);
		writeJSON.writeJSON(comandiModelPath, pathToSaveComandi);
		
		
		ReadJSON readJSON = new ReadJSON(comandiMetamodelPath);
		readJSON.readJSON(pathToSaveComandi);
	}

}
