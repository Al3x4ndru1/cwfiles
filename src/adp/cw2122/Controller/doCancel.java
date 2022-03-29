package adp.cw2122.Controller;

public class doCancel {

    //will print the "Ccanceling" and the number of thread and will call the cancel method form the doDownloads
    public doCancel(final int i) {
        System.out.println("Cancelling " + i);
        doDownloads.cancel(i);
    }
    public static void cancelall(){
        doDownloads.cancelall();
    }

}
