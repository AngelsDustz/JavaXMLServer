package utils;

import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueHandler implements Runnable {
    private XMLHelper               xmlHelper;
    private ConcurrentLinkedQueue   queue;
    private DatabaseHelper          databaseHelper;
    private boolean                 canRun = false;

    public QueueHandler(ConcurrentLinkedQueue queue) {
        this.queue          = queue;
        this.xmlHelper      = new XMLHelper();
        this.databaseHelper = new DatabaseHelper();
    }

    @Override
    public void run() {
        while (this.canRun) {
            if (this.queue.size() > 0) {
                int loops   = this.queue.size();
                String data = (String) this.queue.poll(); //Force cast into string.

                try {
                    this.xmlHelper.setData(data); // Give the data to the XML Helper.
                    this.databaseHelper.insert(this.xmlHelper); // Give the XML Helper to the Database Helper.
                } catch (NullPointerException npe) {
                    //
                }
            } else {
                // Nothing to do, sleep.
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }
}
