package org.simpleyaml.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.file.YamlFile;

/**
 * YAML is a human-readable data serialization language.<br>
 * This class shows you how to use this API to use these files to save your data.
 * @author Carlos L�zaro Costa
 */
public final class YamlTest {
	
	public static void main(String[] args) {
		
		// Create new YAML file with relative path
		YamlFile yamlFile = new YamlFile("test.yml");
		
		// Load the YAML file if is already created or create new one otherwise
		try {
			if (yamlFile.exists()) {
				System.out.println("File already exists, loading configurations...\n");
				yamlFile.load(); // Loads the entire file
			}
			else {
				System.out.println("New file has been created: " + yamlFile.getFilePath() + "\n");
				yamlFile.createNewFile(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// You can manage hierarchies by separating the sections with a dot at path
		// Let's put some values to the file
		
		yamlFile.set("test.number", 5);
		yamlFile.set("test.string", "Hello world");
		yamlFile.set("test.boolean", true);
		
		// More additions, e.g. adding entire lists
		
		List<String> list = Arrays.asList("Each word will be in a separated entry.".split("[\\s]+"));
		yamlFile.set("test.list", list);
		
		// You can move between sections with a ConfigurationSection
		ConfigurationSection section = yamlFile.createSection("newSection");
		section.set("subSection.pi", Math.PI);
		
		section = yamlFile.getConfigurationSection("test");
		
		// To remove a section or a value set it to null
		yamlFile.set("test.number", null);
		
		// You can check if a value is already present at the selected path inside the file
		System.out.println("There is a value at " + section.getName() + ".number?: "
				+ yamlFile.isSet(section.getName() + ".number"));
		
		// And you can check if a path is a ConfigurationSection or a simple value
		System.out.println("Is " + section.getCurrentPath() + " a ConfigurationSection?: "
				+ yamlFile.isConfigurationSection(section.getCurrentPath()) + "\n");
		
		// Now we'll get some objects from the file
		// We can iterate over sections with getKeys(deep) and getValues(deep) methods

		for (Entry<String, Object> entry : section.getValues(false).entrySet()) // false is not recursive
			System.out.println(entry.getKey() + ": " + entry.getValue());
		
		double pi = yamlFile.getDouble("newSection.subSection.pi");
		System.out.println(pi);
		
		// You can use get methods with default values if the path is unknown
		String value = yamlFile.getString("randomSection.noValue"); // returns null
		System.out.println(value);
		
		String defValue = yamlFile.getString("randomSection.noValue", "Default");
		System.out.println(defValue);
		
		// Finally, save changes!
		try {
			yamlFile.save();
			// If your file has comments inside you have to save it with yamlFile.saveWithComments()
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Now, you can restart this test and see how the file is loaded due to it's already created
		
		// You can delete the generated file uncommenting next line and catching the I/O Exception
		// yamlFile.deleteFile();
	}

}
