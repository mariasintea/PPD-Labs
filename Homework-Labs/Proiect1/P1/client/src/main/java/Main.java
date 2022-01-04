import service.ServiceInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws RemoteException, NotBoundException, ExecutionException, InterruptedException {
        try{
            Random random = new Random();

            System.setProperty("java.rmi.server.hostname", "192.168.100.9");
            Registry registry = LocateRegistry.getRegistry(2);
            ServiceInterface serviceInterface = (ServiceInterface) registry.lookup("ServiceInterface");

            while (true) {
                Random rand = new Random();
                int spectacol = rand.nextInt(2) + 1;
                int nr_bilete = rand.nextInt(10) + 1;

                List<Integer> locuri = new ArrayList<>();
                for (int i = 0; i < nr_bilete; i++) {
                    int loc = rand.nextInt(100) + 1;
                    int sem = 0;
                    for (Integer loc2 : locuri)
                        if (loc == loc2)
                        {
                            sem = 1;
                            break;
                        }
                    if (sem == 0)
                        locuri.add(loc);
                }
                boolean result = serviceInterface.saveVanzare(spectacol,nr_bilete,locuri);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (result == false) System.out.println("Vanzarea nu a fost salvata!");
                else System.out.println("Vanzarea a fost salvata!");
            }
        }
        catch (IOException e){
            System.out.println("Serverul s-a oprit!");
        }
    }
}
