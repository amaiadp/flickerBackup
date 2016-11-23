package flickrBackup.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Nagusia {

	private static Nagusia instantzia = new Nagusia();
	private ArrayList<Argazkia> igotzekoa;
	
	
	
	static final String[] EXTENSIONS = new String[]{
	        "jpg", "gif", "png", "bmp"// and other formats you need
	    };
	
	 public static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {

	        @Override
	        public boolean accept(final File dir, final String name) {
	            for (final String ext : EXTENSIONS) {
	                if (name.endsWith("." + ext)) {
	                    return (true);
	                }
	            }
	            return (false);
	        }
	    };
	
	
	
	private Nagusia(){
		
	}
	
	public static Nagusia getInstantzia(){
		return instantzia;
	}
	
	public ArrayList<Argazkia> igotzekoArgazkiakLortu(File pF){
		igotzekoa= new ArrayList<Argazkia>();
		for (File f : pF.listFiles(IMAGE_FILTER)){
			igotzekoa.add(new Argazkia(f));
		}
		return igotzekoa;
	}
}
