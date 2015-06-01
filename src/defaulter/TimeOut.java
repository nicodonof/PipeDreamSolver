package defaulter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeOut {
	public static class MyJob implements Callable<String> {

        public String call() throws Exception {
            // Do something
            return "result";
        }

    }

    public static void main(String[] args) {

        Future<String> control = Executors.newSingleThreadExecutor().submit(new MyJob());

        try {

            String result = control.get(5, TimeUnit.SECONDS);

        } catch (TimeoutException ex) {

            // 5 seconds expired, we cancel the job !!!
            control.cancel(true);

        }
        catch (InterruptedException ex) {

        } catch (ExecutionException ex) {

        }

    }
}
