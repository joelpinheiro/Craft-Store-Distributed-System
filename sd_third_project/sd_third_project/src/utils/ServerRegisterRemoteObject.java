package utils;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import genclass.GenericIO;
import interfaces.Register;
import java.rmi.AlreadyBoundException;
/**
 *  This data type instantiates and registers a remote object that enables the registration of other remote objects
 *  located in the same or other processing nodes in the local registry service.
 *  Communication is based in Java RMI.
 */

public class ServerRegisterRemoteObject
{
  /**
   *  Main task.
   */
    
    

   public ServerRegisterRemoteObject()
   {
       String[] array = {""};
       ServerRegisterRemoteObject.main(array);
   }

    public static void main(String[] args) {
        /* get location of the registry service */
        
        String rmiRegHostName;
        int rmiRegPortNumb;
        
        GenericIO.writeString ("Nome do nó de processamento onde está localizado o serviço de registo? ");
        rmiRegHostName = GenericIO.readlnString ();
        rmiRegPortNumb = 22270;
        
        /* create and install the security manager */
        
        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
        GenericIO.writelnString ("Security manager was installed!");
        
        /* instantiate a registration remote object and generate a stub for it */
        
        RegisterRemoteObject regEngine = new RegisterRemoteObject (rmiRegHostName, rmiRegPortNumb);
        Register regEngineStub = null;
        int listeningPort = 22276;                            /* it should be set accordingly in each case */
        
        try
        { regEngineStub = (Register) UnicastRemoteObject.exportObject (regEngine, listeningPort);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RegisterRemoteObject stub generation exception: " + e.getMessage ());
        System.exit (1);
        }
        GenericIO.writelnString ("Stub was generated!");
        
        /* register it with the local registry service */
        
        String nameEntry = "RegisterHandler";
        Registry registry = null;
        
        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
        System.exit (1);
        }
        GenericIO.writelnString ("RMI registry was created!");
        
        try
        { registry.bind (nameEntry, regEngineStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RegisterRemoteObject remote exception on registration: " + e.getMessage ());
        System.exit (1);
        } catch (AlreadyBoundException ex) {
            GenericIO.writelnString ("RegisterRemoteObject remote exception on registration: " + ex.getMessage ());
            System.exit (1);
        }
        GenericIO.writelnString ("RegisterRemoteObject object was registered!");
    }
}
