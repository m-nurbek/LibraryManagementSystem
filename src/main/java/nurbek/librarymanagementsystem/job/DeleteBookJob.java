package nurbek.librarymanagementsystem.job;

import org.quartz.*;

@PersistJobDataAfterExecution
public class DeleteBookJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        long id = dataMap.getLong("id");
        System.out.println("Deleting book with id: " + id);
    }
}