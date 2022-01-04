import service.ServiceInterface;
import service.Service;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main extends Service {
    public Main() {

    }
    private static Service service;

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        service = new Service();
        ServiceInterface serviceInterface = (ServiceInterface) UnicastRemoteObject.exportObject(service, 0);
        Registry registry = LocateRegistry.createRegistry(2);
        registry.bind("ServiceInterface", serviceInterface);
        System.out.println("Serverul a fost pornit. Asteptare taskuri...");
        service.startChecker();
        try {
            Thread.sleep(120000);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
