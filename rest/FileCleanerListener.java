package gr.deddie.pfr.rest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.FileCleaningTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class FileCleanerListener implements ServletContextListener {

	private static Logger logger = LogManager.getLogger(FileCleanerListener	.class);
	
    private static final String TRACKER_KEY = "fileCleaningTracker";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	logger.info("Starting FileCleaningTracker ...");
        FileCleaningTracker tracker = new FileCleaningTracker();
        sce.getServletContext().setAttribute(TRACKER_KEY, tracker);
    	logger.info("FileCleaningTracker started succesfully");        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        FileCleaningTracker tracker = (FileCleaningTracker) sce.getServletContext().getAttribute(TRACKER_KEY);
        if (tracker != null) {
        	logger.info("Stopping FileCleaningTracker ...");        	
            tracker.exitWhenFinished(); // Shuts down background cleanup thread
        	logger.info("FileCleaningTracker stopped succesfully");               
        }
    }
}
